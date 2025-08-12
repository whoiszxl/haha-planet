package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.cache.caffeine.util.CaffeineUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.common.utils.HahaJsonUtil;
import com.whoiszxl.constants.PlanetCacheConstants;
import com.whoiszxl.model.cache.PlanetCategoryListCache;
import com.whoiszxl.model.cache.PlanetListCache;
import com.whoiszxl.model.resp.PlanetCategoryResp;
import com.whoiszxl.model.resp.PlanetResp;
import com.whoiszxl.planet.mapper.PlanetCategoryMapper;
import com.whoiszxl.planet.mapper.PlanetMapper;
import com.whoiszxl.planet.model.entity.PlanetCategoryDO;
import com.whoiszxl.planet.model.entity.PlanetDO;
import com.whoiszxl.service.PlanetCachedService;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 星球缓存服务实现类
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanetCachedServiceImpl implements PlanetCachedService {

    private final CaffeineUtil caffeineUtil;
    private final RedissonUtil redissonUtil;
    private final RedissonClient redissonClient;
    private final PlanetCategoryMapper planetCategoryMapper;
    private final PlanetMapper planetMapper;

    // 本地缓存更新锁
    private final ReentrantLock localCategoryListCacheUpdateLock = new ReentrantLock();
    private final ReentrantLock localPlanetListCacheUpdateLock = new ReentrantLock();
    
    // 有效分类ID集合（本地缓存）
    private volatile Set<Long> validCategoryIds = new HashSet<>();

    /**
     * 应用启动时初始化有效分类ID集合
     */
    @PostConstruct
    public void initValidCategoryIds() {
        try {
            log.info("[星球缓存] 开始初始化有效分类ID集合");
            
            // 从数据库获取所有有效分类ID
            List<Long> categoryIds = planetCategoryMapper.lambdaQuery()
                    .eq(PlanetCategoryDO::getStatus, 1)
                    .select(PlanetCategoryDO::getId)
                    .list()
                    .stream()
                    .map(PlanetCategoryDO::getId)
                    .collect(Collectors.toList());
            
            // 添加全部分类（categoryId=-1L）
            categoryIds.add(-1L);
            
            // 更新本地缓存
            this.validCategoryIds = new HashSet<>(categoryIds);
            
            log.info("[星球缓存] 有效分类ID集合初始化完成，共 {} 个分类: {}", 
                    categoryIds.size(), categoryIds);
                    
        } catch (Exception e) {
            log.error("[星球缓存] 初始化有效分类ID集合失败", e);
            throw new RuntimeException("有效分类ID集合初始化失败", e);
        }
    }

    /**
     * 检查分类ID是否有效
     */
    private boolean isValidCategoryId(Long categoryId) {
        return validCategoryIds.contains(categoryId);
    }

    @Override
    public PlanetCategoryListCache getCachedPlanetCategoryList(Long version) {
        // 1. 从本地缓存获取分类列表数据
        PlanetCategoryListCache categoryListCache = caffeineUtil.get(
                PlanetCacheConstants.LOCAL_CACHE_PLANET_CATEGORY_LIST, 
                "categories"
        );

        // 2. 如果本地缓存不为空
        if (categoryListCache != null) {
            // 2.1 版本号为空则走无版本号流程，直接将本地缓存返回，存在缓存不一致的问题
            if (version == null) {
                log.info("[星球缓存] 获取分类列表命中本地缓存");
                return categoryListCache;
            }

            // 2.2 版本号存在则判断传入版本号是否小于等于缓存内的版本号，如果小于等于则说明本地缓存为最新
            if (version <= categoryListCache.getVersion()) {
                log.info("[星球缓存] 获取分类列表命中本地缓存，版本号: {}", version);
                return categoryListCache;
            }
        }

        // 3. 本地缓存不存在，则去分布式缓存获取
        return getPlanetCategoryListByDistributedCache();
    }

    @Override
    public PlanetListCache getCachedPlanetList(Long categoryId, Integer page, Integer pageSize, Integer sortType, Long version) {
        // 构建缓存键
        String cacheKey = buildPlanetListCacheKey(categoryId, page, pageSize, sortType);
        
        // 1. 从本地缓存获取星球列表数据
        PlanetListCache planetListCache = caffeineUtil.get(
                PlanetCacheConstants.LOCAL_CACHE_PLANET_LIST, 
                cacheKey
        );

        // 2. 如果本地缓存不为空
        if (planetListCache != null) {
            // 2.1 版本号为空则走无版本号流程，直接将本地缓存返回，存在缓存不一致的问题
            if (version == null) {
                log.info("[星球缓存] 获取星球列表命中本地缓存，分类ID: {}", categoryId);
                return planetListCache;
            }

            // 2.2 版本号存在则判断传入版本号是否小于等于缓存内的版本号，如果小于等于则说明本地缓存为最新
            if (version <= planetListCache.getVersion()) {
                log.info("[星球缓存] 获取星球列表命中本地缓存，分类ID: {}，版本号: {}", categoryId, version);
                return planetListCache;
            }
        }

        // 3. 本地缓存不存在，则去分布式缓存获取
        return getPlanetListByDistributedCache(categoryId, page, pageSize, sortType);
    }

    /**
     * 从分布式缓存获取分类列表
     */
    private PlanetCategoryListCache getPlanetCategoryListByDistributedCache() {
        log.info("[星球缓存] 从分布式缓存中获取分类列表");
        
        // 1. 从分布式缓存Redis中获取分类列表数据
        PlanetCategoryListCache categoryListCache = redissonUtil.get(PlanetCacheConstants.CACHE_PLANET_CATEGORY_LIST);

        // 2. 分布式缓存中不存在列表，需要从数据库中读取，并更新缓存
        if (categoryListCache == null) {
            categoryListCache = loadPlanetCategoryListFromDb();
        }

        // 3. 如果分布式缓存中存在列表，或者从数据库读出了列表，那么就需要更新本地缓存
        if (!categoryListCache.isLater()) {
            // 3.1 此处使用ReentrantLock加锁进行更新，防止并发更新下产生安全问题
            boolean isLockSuccess = localCategoryListCacheUpdateLock.tryLock();
            if (isLockSuccess) {
                try {
                    caffeineUtil.put(PlanetCacheConstants.LOCAL_CACHE_PLANET_CATEGORY_LIST, "categories", categoryListCache);
                    log.info("[星球缓存] 更新了本地分类列表缓存");
                } finally {
                    localCategoryListCacheUpdateLock.unlock();
                }
            }
        }

        return categoryListCache;
    }

    /**
     * 从分布式缓存获取星球列表
     */
    private PlanetListCache getPlanetListByDistributedCache(Long categoryId, Integer page, Integer pageSize, Integer sortType) {
        log.info("[星球缓存] 从分布式缓存中获取星球列表，分类ID: {}", categoryId);
        
        String cacheKey = buildPlanetListCacheKey(categoryId, page, pageSize, sortType);
        String planetListKey = PlanetCacheConstants.CACHE_PLANET_LIST_PREFIX + cacheKey;

        // 1. 从分布式缓存Redis中获取星球列表数据
        PlanetListCache planetListCache = redissonUtil.get(planetListKey);

        // 2. 分布式缓存中不存在列表，需要从数据库中读取，并更新缓存
        if (planetListCache == null) {
            // 2.1 检查分类ID是否有效（简单高效的本地验证）
            if (!isValidCategoryId(categoryId)) {
                // 分类ID无效，直接返回不存在（防止缓存穿透）
                log.info("[星球缓存] 分类ID无效: {}", categoryId);
                planetListCache = new PlanetListCache().notExist();
            } else {
                // 分类ID有效，从数据库加载数据
                log.info("[星球缓存] 分类ID有效，从数据库加载: categoryId={}", categoryId);
                planetListCache = loadPlanetListFromDb(categoryId, page, pageSize, sortType);
            }
        }

        // 3. 如果分布式缓存中存在列表，或者从数据库读出了列表，那么就需要更新本地缓存
        if (!planetListCache.isLater()) {
            // 3.1 此处使用ReentrantLock加锁进行更新，防止并发更新下产生安全问题
            boolean isLockSuccess = localPlanetListCacheUpdateLock.tryLock();
            if (isLockSuccess) {
                try {
                    caffeineUtil.put(PlanetCacheConstants.LOCAL_CACHE_PLANET_LIST, cacheKey, planetListCache);
                    log.info("[星球缓存] 更新了本地星球列表缓存，分类ID: {}", categoryId);
                } finally {
                    localPlanetListCacheUpdateLock.unlock();
                }
            }
        }

        return planetListCache;
    }

    /**
     * 从数据库加载分类列表
     */
    private PlanetCategoryListCache loadPlanetCategoryListFromDb() {
        log.info("[星球缓存] 从数据库获取分类列表");

        // 1. 从数据库中获取列表数据需要加分布式锁，防止多个微服务同时去数据库中获取数据更新缓存造成数据错误的问题
        RLock lock = redissonUtil.getLock(PlanetCacheConstants.LOCK_GET_PLANET_CATEGORY_LIST_FROM_DB);
        try {
            boolean lockFlag = lock.tryLock(10, 20, TimeUnit.SECONDS);
            if (!lockFlag) {
                return new PlanetCategoryListCache().tryLater();
            }

            // 2. 从数据库中获取分类列表
            LambdaQueryWrapper<PlanetCategoryDO> queryWrapper = Wrappers.<PlanetCategoryDO>lambdaQuery()
                    .eq(PlanetCategoryDO::getParentId, 0L)
                    .eq(PlanetCategoryDO::getStatus, 1)
                    .orderByAsc(PlanetCategoryDO::getSort);
            
            List<PlanetCategoryDO> categoryList = planetCategoryMapper.selectList(queryWrapper);
            
            PlanetCategoryListCache categoryListCache;
            if (categoryList.isEmpty()) {
                // 3. 如果数据库中不存在数据列表，则返回无记录对象
                categoryListCache = new PlanetCategoryListCache().notExist();
            } else {
                // 4. 如果数据库中存在记录，则返回有记录对象，并设置版本号，版本号为当前时间的时间戳
                List<PlanetCategoryResp> categoryRespList = HahaBeanUtil.copyToList(categoryList, PlanetCategoryResp.class);
                categoryListCache = new PlanetCategoryListCache()
                        .setTotal(categoryList.size())
                        .setCategoryList(categoryRespList)
                        .setVersion(System.currentTimeMillis())
                        .setExist(true);
            }

            // 5. 将数据库中获取的数据列表回写到Redis，并设置失效时间为5分钟，具体失效时间根据业务而定
            redissonUtil.set(PlanetCacheConstants.CACHE_PLANET_CATEGORY_LIST, categoryListCache, Duration.ofMinutes(5));
            log.info("[星球缓存] 从数据库获取分类列表，更新分布式缓存成功");
            return categoryListCache;
        } catch (Exception e) {
            log.error("[星球缓存] 从数据库获取分类列表，更新分布式缓存失败", e);
            // 6. 如果抛出异常，则返回一个稍后再试的缓存对象
            return new PlanetCategoryListCache().tryLater();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从数据库加载星球列表
     */
    private PlanetListCache loadPlanetListFromDb(Long categoryId, Integer page, Integer pageSize, Integer sortType) {
        log.info("[星球缓存] 从数据库获取星球列表，分类ID: {}", categoryId);

        String cacheKey = buildPlanetListCacheKey(categoryId, page, pageSize, sortType);
        String lockKey = PlanetCacheConstants.LOCK_GET_PLANET_LIST_FROM_DB_PREFIX + cacheKey;

        // 1. 从数据库中获取列表数据需要加分布式锁，防止多个微服务同时去数据库中获取数据更新缓存造成数据错误的问题
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean lockFlag = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!lockFlag) {
                return new PlanetListCache().tryLater();
            }

            // 2. 构建查询条件
            LambdaQueryWrapper<PlanetDO> queryWrapper = Wrappers.<PlanetDO>lambdaQuery()
                    .eq(PlanetDO::getStatus, 1);

            if(categoryId != -1L) {
                queryWrapper.eq(PlanetDO::getCategoryId, categoryId);
            }

            // 3. 根据排序类型设置排序规则
            switch (sortType) {
                case 2 -> queryWrapper.orderByDesc(PlanetDO::getHotScore);
                case 1 -> queryWrapper.orderByDesc(PlanetDO::getCreatedAt);
                case 3 -> queryWrapper.orderByDesc(PlanetDO::getMemberCount);
                default -> queryWrapper.orderByDesc(PlanetDO::getCreatedAt);
            }

            // 4. 分页查询
            Page<PlanetDO> planetPage = new Page<>(page, pageSize);
            planetPage = planetMapper.selectPage(planetPage, queryWrapper);

            PlanetListCache planetListCache;
            if (planetPage.getRecords().isEmpty()) {
                // 5. 如果数据库中不存在数据列表，则返回无记录对象
                planetListCache = new PlanetListCache().notExist();
            } else {
                // 6. 如果数据库中存在记录，则返回有记录对象，并设置版本号，版本号为当前时间的时间戳
                List<PlanetResp> planetRespList = HahaBeanUtil.copyToList(planetPage.getRecords(), PlanetResp.class);
                planetListCache = new PlanetListCache()
                        .setTotal(planetPage.getTotal())
                        .setPlanetList(planetRespList)
                        .setCategoryId(categoryId)
                        .setPage(page)
                        .setPageSize(pageSize)
                        .setSortType(sortType)
                        .setVersion(System.currentTimeMillis())
                        .setExist(true);
            }

            // 7. 将数据库中获取的数据列表回写到Redis，并设置失效时间为5分钟，具体失效时间根据业务而定
            String planetListKey = PlanetCacheConstants.CACHE_PLANET_LIST_PREFIX + cacheKey;
            redissonUtil.set(planetListKey, planetListCache, Duration.ofMinutes(5));
            
            log.info("[星球缓存] 从数据库获取星球列表，更新分布式缓存成功，分类ID: {}", categoryId);
            return planetListCache;
        } catch (Exception e) {
            log.error("[星球缓存] 从数据库获取星球列表，更新分布式缓存失败，分类ID: {}", categoryId, e);
            // 8. 如果抛出异常，则返回一个稍后再试的缓存对象
            return new PlanetListCache().tryLater();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 构建星球列表缓存键
     */
    private String buildPlanetListCacheKey(Long categoryId, Integer page, Integer pageSize, Integer sortType) {
        return String.format("%d_%d_%d_%s", categoryId, page, pageSize, sortType);
    }
}
