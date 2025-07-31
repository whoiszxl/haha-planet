package com.whoiszxl.cache.jetcache.manager;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.MultiLevelCache;
import com.alicp.jetcache.template.QuickConfig;
import com.whoiszxl.cache.jetcache.properties.JetCacheProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * JetCache 缓存管理器
 *
 * @author whoiszxl
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JetCacheManager {

    private final JetCacheProperties jetCacheProperties;
    private final ConcurrentMap<String, Cache<Object, Object>> cacheMap = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private CacheManager cacheManager;

    /**
     * 获取缓存实例
     *
     * @param cacheName 缓存名称
     * @return 缓存实例
     */
    public Cache<Object, Object> getCache(String cacheName) {
        return cacheMap.computeIfAbsent(cacheName, this::createCache);
    }

    /**
     * 获取缓存实例（指定区域）
     *
     * @param area      缓存区域
     * @param cacheName 缓存名称
     * @return 缓存实例
     */
    public Cache<Object, Object> getCache(String area, String cacheName) {
        String fullName = area + ":" + cacheName;
        return cacheMap.computeIfAbsent(fullName, name -> createCache(area, cacheName));
    }

    /**
     * 创建缓存实例
     *
     * @param cacheName 缓存名称
     * @return 缓存实例
     */
    private Cache<Object, Object> createCache(String cacheName) {
        if (cacheManager == null) {
            log.warn("[HaHa JetCache] CacheManager 未初始化，无法创建缓存: {}", cacheName);
            return null;
        }

        QuickConfig quickConfig = QuickConfig.newBuilder(cacheName)
                .expire(jetCacheProperties.getLocal().getDefaultExpire())
                .cacheType(com.alicp.jetcache.CacheType.BOTH)
                .localExpire(jetCacheProperties.getLocal().getDefaultExpire())
                .build();

        return cacheManager.getOrCreateCache(quickConfig);
    }

    /**
     * 创建缓存实例（指定区域）
     *
     * @param area      缓存区域
     * @param cacheName 缓存名称
     * @return 缓存实例
     */
    private Cache<Object, Object> createCache(String area, String cacheName) {
        if (cacheManager == null) {
            log.warn("[HaHa JetCache] CacheManager 未初始化，无法创建缓存: {}:{}", area, cacheName);
            return null;
        }

        JetCacheProperties.CacheAreaConfig areaConfig = jetCacheProperties.getAreas().get(area);
        Duration expire = areaConfig != null && areaConfig.getLocal() != null
                ? areaConfig.getLocal().getDefaultExpire()
                : jetCacheProperties.getLocal().getDefaultExpire();

        QuickConfig quickConfig = QuickConfig.newBuilder(cacheName)
                .area(area)
                .expire(expire)
                .cacheType(com.alicp.jetcache.CacheType.BOTH)
                .localExpire(expire)
                .build();

        return cacheManager.getOrCreateCache(quickConfig);
    }

    /**
     * 移除缓存实例
     *
     * @param cacheName 缓存名称
     */
    public void removeCache(String cacheName) {
        Cache<Object, Object> cache = cacheMap.remove(cacheName);
        if (cache != null) {
            cache.close();
            log.info("[HaHa JetCache] 移除缓存: {}", cacheName);
        }
    }

    /**
     * 清空所有缓存
     */
    public void clearAllCaches() {
        cacheMap.values().forEach(cache -> {
            try {
                cache.removeAll();
            } catch (Exception e) {
                log.error("[HaHa JetCache] 清空缓存失败", e);
            }
        });
        log.info("[HaHa JetCache] 清空所有缓存完成");
    }

    /**
     * 获取缓存统计信息
     *
     * @param cacheName 缓存名称
     * @return 统计信息
     */
    public String getCacheStats(String cacheName) {
        Cache<Object, Object> cache = cacheMap.get(cacheName);
        if (cache != null && cache instanceof MultiLevelCache) {
            MultiLevelCache multiLevelCache = (MultiLevelCache) cache;
            return String.format("Local: %s, Remote: %s",
                    multiLevelCache.getLocalCache().stat(),
                    multiLevelCache.getRemoteCache().stat());
        }
        return cache != null ? cache.stat().toString() : "Cache not found";
    }
}