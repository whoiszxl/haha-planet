package com.whoiszxl.service.impl;

import com.whoiszxl.cache.caffeine.util.CaffeineUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.constants.PostCacheConstants;
import com.whoiszxl.service.PostCacheUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

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

    @Override
    public void clearPostListCache(Long planetId) {
        if (planetId == null) {
            log.warn("[帖子缓存更新] 清除帖子列表缓存失败，星球ID为空");
            return;
        }

        try {
            // 1. 清除本地缓存中该星球的所有帖子列表缓存
            clearLocalPostListCache(planetId);
            
            // 2. 清除分布式缓存中该星球的所有帖子列表缓存
            clearDistributedPostListCache(planetId);
            
            log.info("[帖子缓存更新] 清除星球[{}]的帖子列表缓存成功", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 清除星球[{}]的帖子列表缓存失败", planetId, e);
        }
    }

    @Override
    public void updatePostCacheVersion(Long planetId) {
        if (planetId == null) {
            log.warn("[帖子缓存更新] 更新帖子缓存版本号失败，星球ID为空");
            return;
        }

        try {
            // 清除缓存，下次访问时会重新加载并生成新的版本号
            clearPostListCache(planetId);
            log.info("[帖子缓存更新] 更新星球[{}]的帖子缓存版本号成功", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 更新星球[{}]的帖子缓存版本号失败", planetId, e);
        }
    }

    @Override
    public void warmUpPostListCache(Long planetId) {
        if (planetId == null) {
            log.warn("[帖子缓存更新] 预热帖子列表缓存失败，星球ID为空");
            return;
        }

        try {
            // TODO: 实现缓存预热逻辑，可以预加载热门排序的第一页数据
            log.info("[帖子缓存更新] 预热星球[{}]的帖子列表缓存", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 预热星球[{}]的帖子列表缓存失败", planetId, e);
        }
    }

    /**
     * 清除本地缓存中指定星球的所有帖子列表缓存
     */
    private void clearLocalPostListCache(Long planetId) {
        try {
            // 构建缓存键模式，匹配该星球的所有分页和排序组合
            // 缓存键格式：planetId_page_pageSize_sortType
            String keyPattern = planetId + "_";
            
            // 获取本地缓存中所有的帖子列表缓存键
            // 注意：Caffeine没有直接的模式匹配删除方法，这里需要遍历所有可能的组合
            // 常见的分页参数组合
            int[] pageSizes = {10, 20, 50};
            int[] sortTypes = {1, 2, 3};
            
            for (int pageSize : pageSizes) {
                for (int sortType : sortTypes) {
                    // 清除前几页的缓存（通常用户只会浏览前几页）
                    for (int page = 1; page <= 5; page++) {
                        String cacheKey = String.format("%d_%d_%d_%d", planetId, page, pageSize, sortType);
                        caffeineUtil.evict(PostCacheConstants.LOCAL_CACHE_POST_LIST, cacheKey);
                    }
                }
            }
            
            log.debug("[帖子缓存更新] 清除本地缓存中星球[{}]的帖子列表缓存成功", planetId);
        } catch (Exception e) {
            log.error("[帖子缓存更新] 清除本地缓存中星球[{}]的帖子列表缓存失败", planetId, e);
        }
    }

    /**
     * 清除分布式缓存中指定星球的所有帖子列表缓存
     */
    private void clearDistributedPostListCache(Long planetId) {
        try {
            // 构建缓存键模式，匹配该星球的所有帖子列表缓存
            String keyPattern = PostCacheConstants.CACHE_POST_LIST_PREFIX + planetId + "_*";
            
            // 获取所有匹配的缓存键
            Collection<String> keys = redissonUtil.keys(keyPattern);
            
            // 批量删除缓存
            if (keys != null && !keys.isEmpty()) {
                // 使用批量删除提高性能
                String[] keyArray = keys.toArray(new String[0]);
                long deletedCount = redissonUtil.delete(keyArray);
                log.debug("[帖子缓存更新] 清除分布式缓存中星球[{}]的帖子列表缓存成功，删除{}个缓存键", planetId, deletedCount);
            } else {
                log.debug("[帖子缓存更新] 分布式缓存中星球[{}]没有帖子列表缓存需要清除", planetId);
            }
        } catch (Exception e) {
            log.error("[帖子缓存更新] 清除分布式缓存中星球[{}]的帖子列表缓存失败", planetId, e);
        }
    }
}