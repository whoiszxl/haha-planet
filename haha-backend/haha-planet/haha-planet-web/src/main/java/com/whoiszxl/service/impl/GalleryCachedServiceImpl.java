package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whoiszxl.cache.caffeine.util.CaffeineUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.constants.GalleryCacheConstants;
import com.whoiszxl.model.cache.GalleryListCache;
import com.whoiszxl.model.resp.GalleryResp;
import com.whoiszxl.planet.mapper.PlanetGalleryMapper;
import com.whoiszxl.planet.model.entity.PlanetGalleryDO;
import com.whoiszxl.service.GalleryCachedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 画廊缓存服务实现类
 *
 * @author whoiszxl
 */
@Slf4j
@Service("galleryCachedServiceImpl")
@RequiredArgsConstructor
public class GalleryCachedServiceImpl implements GalleryCachedService {

    private final CaffeineUtil caffeineUtil;
    private final RedissonUtil redissonUtil;
    private final RedissonClient redissonClient;
    private final PlanetGalleryMapper planetGalleryMapper;

    // 本地缓存更新锁
    private final ReentrantLock localGalleryListCacheUpdateLock = new ReentrantLock();
    
    // 布隆过滤器（画廊分类）
    private RBloomFilter<String> galleryCategoryBloomFilter;

    @Override
    public GalleryListCache getCachedGalleryList(Long version) {
        // 构建缓存键
        String cacheKey = buildGalleryListCacheKey();
        
        // 1. 从本地缓存获取画廊列表数据
        GalleryListCache galleryListCache = caffeineUtil.get(
                GalleryCacheConstants.LOCAL_CACHE_GALLERY_LIST, 
                cacheKey
        );

        // 2. 如果本地缓存不为空
        if (galleryListCache != null) {
            // 2.1 版本号为空则走无版本号流程，直接将本地缓存返回，存在缓存不一致的问题
            if (version == null) {
                log.info("[画廊缓存] 获取画廊列表命中本地缓存");
                return galleryListCache;
            }

            // 2.2 版本号存在则判断传入版本号是否小于等于缓存内的版本号，如果小于等于则说明本地缓存为最新
            if (version <= galleryListCache.getVersion()) {
                log.info("[画廊缓存] 获取画廊列表命中本地缓存，版本号: {}", version);
                return galleryListCache;
            }
        }

        // 3. 本地缓存不存在，则去分布式缓存获取
        return getGalleryListByDistributedCache();
    }

    /**
     * 从分布式缓存获取画廊列表
     */
    private GalleryListCache getGalleryListByDistributedCache() {
        String cacheKey = buildGalleryListCacheKey();
        String galleryListKey = GalleryCacheConstants.CACHE_GALLERY_LIST_PREFIX + cacheKey;
        
        // 1. 从分布式缓存获取画廊列表
        GalleryListCache galleryListCache = redissonUtil.get(galleryListKey);
        
        // 2. 如果分布式缓存中不存在，则从数据库加载
        if (galleryListCache == null) {
            log.info("[画廊缓存] 分布式缓存不存在，从数据库加载画廊列表");
            galleryListCache = loadGalleryListFromDb();
        }

        // 3. 如果分布式缓存中存在列表，或者从数据库读出了列表，那么就需要更新本地缓存
        if (!galleryListCache.isLater()) {
            // 3.1 此处使用ReentrantLock加锁进行更新，防止并发更新下产生安全问题
            boolean isLockSuccess = localGalleryListCacheUpdateLock.tryLock();
            if (isLockSuccess) {
                try {
                    caffeineUtil.put(GalleryCacheConstants.LOCAL_CACHE_GALLERY_LIST, cacheKey, galleryListCache);
                    log.info("[画廊缓存] 更新了本地画廊列表缓存");
                } finally {
                    localGalleryListCacheUpdateLock.unlock();
                }
            }
        }

        return galleryListCache;
    }

    /**
     * 从数据库加载画廊列表
     */
    private GalleryListCache loadGalleryListFromDb() {
        log.info("[画廊缓存] 从数据库获取画廊列表");

        String cacheKey = buildGalleryListCacheKey();
        String lockKey = GalleryCacheConstants.LOCK_GET_GALLERY_LIST_FROM_DB_PREFIX + cacheKey;

        // 1. 从数据库中获取列表数据需要加分布式锁，防止多个微服务同时去数据库中获取数据更新缓存造成数据错误的问题
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean lockFlag = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!lockFlag) {
                return new GalleryListCache().tryLater();
            }

            // 2. 构建查询条件
            LambdaQueryWrapper<PlanetGalleryDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(PlanetGalleryDO::getCategory)
                       .orderByAsc(PlanetGalleryDO::getSortOrder)
                       .orderByDesc(PlanetGalleryDO::getCreatedAt);

            // 3. 查询画廊列表
            List<PlanetGalleryDO> galleryDOList = planetGalleryMapper.selectList(queryWrapper);
            
            // 4. 转换为响应对象
            List<GalleryResp> galleryRespList = galleryDOList.stream()
                    .map(this::convertToGalleryResp)
                    .toList();

            // 5. 按分类分组
            Map<String, List<GalleryResp>> galleryMap = galleryRespList.stream()
                    .collect(Collectors.groupingBy(GalleryResp::getCategory));

            // 6. 构建缓存对象
            GalleryListCache galleryListCache = new GalleryListCache()
                    .exist()
                    .setGalleryMap(galleryMap)
                    .setTotal((long) galleryRespList.size())
                    .setVersion(System.currentTimeMillis());

            // 7. 更新分布式缓存
            String galleryListKey = GalleryCacheConstants.CACHE_GALLERY_LIST_PREFIX + cacheKey;
            Duration expireTime = galleryListCache.isExist() ? Duration.ofMinutes(30) : Duration.ofMinutes(5);
            redissonUtil.set(galleryListKey, galleryListCache, expireTime);
            
            log.info("[画廊缓存] 从数据库获取画廊列表，更新分布式缓存成功，总数: {}, 分类数: {}", 
                    galleryListCache.getTotal(), galleryMap.size());
            return galleryListCache;
        } catch (Exception e) {
            log.error("[画廊缓存] 从数据库获取画廊列表，更新分布式缓存失败", e);
            // 8. 如果抛出异常，则返回一个稍后再试的缓存对象
            return new GalleryListCache().tryLater();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 转换为画廊响应对象
     */
    private GalleryResp convertToGalleryResp(PlanetGalleryDO galleryDO) {
        GalleryResp galleryResp = new GalleryResp();
        BeanUtils.copyProperties(galleryDO, galleryResp);
        return galleryResp;
    }

    /**
     * 构建画廊列表缓存键
     */
    private String buildGalleryListCacheKey() {
        return "all";
    }
}