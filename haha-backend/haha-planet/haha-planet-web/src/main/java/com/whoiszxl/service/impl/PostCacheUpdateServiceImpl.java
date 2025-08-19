package com.whoiszxl.service.impl;

import com.whoiszxl.cache.caffeine.util.CaffeineUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.constants.PostCacheConstants;
import com.whoiszxl.service.PostCacheUpdateService;
import com.whoiszxl.service.PostCachedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 帖子缓存更新服务实现类
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostCacheUpdateServiceImpl implements PostCacheUpdateService {

    private final CaffeineUtil caffeineUtil;
    private final RedissonUtil redissonUtil;
    private final PostCachedService postCachedService;

    @Override
    public void clearPostListCache(Long planetId) {
        log.info("[帖子缓存更新] 开始清除星球帖子列表缓存，星球ID: {}", planetId);
        
        try {
            // 清除本地缓存
            clearLocalPostListCache(planetId);
            
            // 清除分布式缓存
            clearDistributedPostListCache(planetId);
            
            log.info("[帖子缓存更新] 成功清除星球帖子列表缓存，星球ID: {}", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 清除星球帖子列表缓存失败，星球ID: {}", planetId, e);
        }
    }

    @Override
    public void updatePostCacheVersion(Long planetId) {
        log.info("[帖子缓存更新] 开始更新帖子缓存版本号，星球ID: {}", planetId);
        
        try {
            // 生成新的版本号
            long newVersion = System.currentTimeMillis();
            
            // 更新版本号到Redis
            String versionKey = "cache:post:version:" + planetId;
            redissonUtil.set(versionKey, newVersion, Duration.ofHours(24));
            
            // 清除相关缓存，强制下次查询时重新加载
            clearPostListCache(planetId);
            
            log.info("[帖子缓存更新] 成功更新帖子缓存版本号，星球ID: {}, 新版本号: {}", planetId, newVersion);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 更新帖子缓存版本号失败，星球ID: {}", planetId, e);
        }
    }

    @Override
    public void warmUpPostListCache(Long planetId) {
        log.info("[帖子缓存更新] 开始预热星球帖子列表缓存，星球ID: {}", planetId);
        
        try {
            // 预热常用的分页和排序组合
            List<Integer> commonPages = Arrays.asList(1, 2);
            List<Integer> commonPageSizes = Arrays.asList(20, 50);
            List<Integer> commonSortTypes = Arrays.asList(1, 2, 3, 4);
            
            for (Integer page : commonPages) {
                for (Integer pageSize : commonPageSizes) {
                    for (Integer sortType : commonSortTypes) {
                        try {
                            // 异步预热缓存
                            postCachedService.getCachedPostList(planetId, page, pageSize, sortType, null);
                            Thread.sleep(10); // 避免过于频繁的数据库访问
                        } catch (Exception e) {
                            log.warn("[帖子缓存更新] 预热缓存失败，星球ID: {}, 页码: {}, 页大小: {}, 排序: {}", 
                                    planetId, page, pageSize, sortType, e);
                        }
                    }
                }
            }
            
            log.info("[帖子缓存更新] 完成预热星球帖子列表缓存，星球ID: {}", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 预热星球帖子列表缓存失败，星球ID: {}", planetId, e);
        }
    }

    /**
     * 清除本地缓存
     */
    private void clearLocalPostListCache(Long planetId) {
        try {
            // 清除所有可能的本地缓存键
            List<Integer> pages = Arrays.asList(1, 2, 3, 4, 5);
            List<Integer> pageSizes = Arrays.asList(10, 20, 50, 100);
            List<Integer> sortTypes = Arrays.asList(1, 2, 3, 4);
            
            for (Integer page : pages) {
                for (Integer pageSize : pageSizes) {
                    for (Integer sortType : sortTypes) {
                        String cacheKey = buildPostListCacheKey(planetId, page, pageSize, sortType);
                        caffeineUtil.evict(PostCacheConstants.LOCAL_CACHE_POST_LIST, cacheKey);
                    }
                }
            }
            
            log.debug("[帖子缓存更新] 成功清除本地缓存，星球ID: {}", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 清除本地缓存失败，星球ID: {}", planetId, e);
        }
    }

    /**
     * 清除分布式缓存
     */
    private void clearDistributedPostListCache(Long planetId) {
        try {
            // 使用模糊匹配获取所有相关的Redis键，然后批量删除
            String pattern = PostCacheConstants.CACHE_POST_LIST_PREFIX + planetId + "_*";
            Collection<String> keys = redissonUtil.keys(pattern);
            
            if (!keys.isEmpty()) {
                String[] keyArray = keys.toArray(new String[0]);
                long deletedCount = redissonUtil.delete(keyArray);
                log.debug("[帖子缓存更新] 成功清除分布式缓存，星球ID: {}, 删除键数量: {}", planetId, deletedCount);
            } else {
                log.debug("[帖子缓存更新] 没有找到需要清除的分布式缓存，星球ID: {}", planetId);
            }
        } catch (Exception e) {
            log.error("[帖子缓存更新] 清除分布式缓存失败，星球ID: {}", planetId, e);
        }
    }

    /**
     * 构建帖子列表缓存键
     */
    private String buildPostListCacheKey(Long planetId, Integer page, Integer pageSize, Integer sortType) {
        return String.format("%d_%d_%d_%d", planetId, page, pageSize, sortType);
    }
}
