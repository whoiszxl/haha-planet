package com.whoiszxl.cache.caffeine.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.whoiszxl.cache.caffeine.manager.CaffeineCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Caffeine缓存工具类
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CaffeineUtil {

    private final CacheManager cacheManager;

    /**
     * 获取缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            org.springframework.cache.Cache.ValueWrapper wrapper = cache.get(key);
            return wrapper != null ? (T) wrapper.get() : null;
        }
        return null;
    }

    /**
     * 获取缓存值，如果不存在则通过loader加载
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key, Function<Object, T> loader) {
        T value = get(cacheName, key);
        if (value == null) {
            value = loader.apply(key);
            if (value != null) {
                put(cacheName, key, value);
            }
        }
        return value;
    }

    /**
     * 设置缓存值
     */
    public void put(String cacheName, Object key, Object value) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    /**
     * 如果不存在则设置缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T putIfAbsent(String cacheName, Object key, T value) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            org.springframework.cache.Cache.ValueWrapper existing = cache.putIfAbsent(key, value);
            return existing != null ? (T) existing.get() : value;
        }
        return value;
    }

    /**
     * 删除缓存
     */
    public void evict(String cacheName, Object key) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    /**
     * 清空缓存
     */
    public void clear(String cacheName) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 检查缓存是否存在
     */
    public boolean exists(String cacheName, Object key) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        return cache != null && cache.get(key) != null;
    }

    /**
     * 获取缓存大小
     */
    public long size(String cacheName) {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            return nativeCache != null ? nativeCache.estimatedSize() : 0;
        }
        return 0;
    }

    /**
     * 获取缓存统计信息
     */
    public CacheStats getStats(String cacheName) {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            return nativeCache != null ? nativeCache.stats() : null;
        }
        return null;
    }

    /**
     * 获取所有缓存统计信息
     */
    public Map<String, CacheStats> getAllStats() {
        Map<String, CacheStats> statsMap = new ConcurrentHashMap<>();
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Collection<String> cacheNames = caffeineCacheManager.getCacheNames();
            for (String cacheName : cacheNames) {
                CacheStats stats = getStats(cacheName);
                if (stats != null) {
                    statsMap.put(cacheName, stats);
                }
            }
        }
        return statsMap;
    }

    /**
     * 批量获取缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> Map<Object, T> getAll(String cacheName, Iterable<?> keys) {
        Map<Object, T> result = new ConcurrentHashMap<>();
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            if (nativeCache != null) {
                Map<Object, Object> allPresent = nativeCache.getAllPresent(keys);
                allPresent.forEach((k, v) -> result.put(k, (T) v));
            }
        }
        return result;
    }

    /**
     * 批量设置缓存值
     */
    public void putAll(String cacheName, Map<?, ?> map) {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            if (nativeCache != null) {
                nativeCache.putAll(map);
            }
        }
    }

    /**
     * 批量删除缓存
     */
    public void evictAll(String cacheName, Iterable<?> keys) {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            if (nativeCache != null) {
                nativeCache.invalidateAll(keys);
            }
        }
    }

    /**
     * 刷新缓存
     */
    public void refresh(String cacheName, Object key) {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            if (nativeCache instanceof com.github.benmanes.caffeine.cache.LoadingCache<Object, Object> loadingCache) {
                loadingCache.refresh(key);
            }
        }
    }

    /**
     * 清理过期缓存
     */
    public void cleanUp(String cacheName) {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Cache<Object, Object> nativeCache = caffeineCacheManager.getNativeCache(cacheName);
            if (nativeCache != null) {
                nativeCache.cleanUp();
            }
        }
    }

    /**
     * 清理所有缓存的过期数据
     */
    public void cleanUpAll() {
        if (cacheManager instanceof CaffeineCacheManager caffeineCacheManager) {
            Collection<String> cacheNames = caffeineCacheManager.getCacheNames();
            cacheNames.forEach(this::cleanUp);
        }
    }
}