package com.whoiszxl.cache.jetcache.util;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.whoiszxl.cache.jetcache.manager.JetCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * JetCache 工具类
 *
 * @author whoiszxl
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JetCacheUtil {

    private final JetCacheManager jetCacheManager;

    /**
     * 获取缓存值
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param <T>       值类型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                CacheGetResult<Object> result = cache.GET(key);
                return result.isSuccess() ? (T) result.getValue() : null;
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 获取缓存失败: cacheName={}, key={}", cacheName, key, e);
        }
        return null;
    }

    /**
     * 获取缓存值（带默认值）
     *
     * @param cacheName    缓存名称
     * @param key          缓存键
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 缓存值或默认值
     */
    public <T> T get(String cacheName, Object key, T defaultValue) {
        T value = get(cacheName, key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取缓存值（如果不存在则加载）
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param loader    加载函数
     * @param expire    过期时间
     * @param <T>       值类型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key, Function<Object, T> loader, Duration expire) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                CacheGetResult<Object> result = cache.GET(key);
                if (result.isSuccess()) {
                    return (T) result.getValue();
                }
                
                // 缓存未命中，加载数据
                T value = loader.apply(key);
                if (value != null) {
                    put(cacheName, key, value, expire);
                }
                return value;
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 获取缓存失败: cacheName={}, key={}", cacheName, key, e);
        }
        return loader.apply(key);
    }

    /**
     * 设置缓存值
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     * @return 是否成功
     */
    public boolean put(String cacheName, Object key, Object value) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                return cache.PUT(key, value).isSuccess();
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 设置缓存失败: cacheName={}, key={}", cacheName, key, e);
        }
        return false;
    }

    /**
     * 设置缓存值（带过期时间）
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     * @param expire    过期时间
     * @return 是否成功
     */
    public boolean put(String cacheName, Object key, Object value, Duration expire) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                return cache.PUT(key, value, expire).isSuccess();
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 设置缓存失败: cacheName={}, key={}, expire={}", cacheName, key, expire, e);
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @return 是否成功
     */
    public boolean evict(String cacheName, Object key) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                return cache.REMOVE(key).isSuccess();
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 删除缓存失败: cacheName={}, key={}", cacheName, key, e);
        }
        return false;
    }

    /**
     * 批量删除缓存
     *
     * @param cacheName 缓存名称
     * @param keys      缓存键集合
     * @return 删除成功的数量
     */
    public int evictBatch(String cacheName, Set<Object> keys) {
        int successCount = 0;
        for (Object key : keys) {
            if (evict(cacheName, key)) {
                successCount++;
            }
        }
        return successCount;
    }

    /**
     * 清空缓存
     *
     * @param cacheName 缓存名称
     * @return 是否成功
     */
    public boolean clear(String cacheName) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                cache.removeAll(null);
                return true;
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 清空缓存失败: cacheName={}", cacheName, e);
        }
        return false;
    }

    /**
     * 检查缓存是否存在
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @return 是否存在
     */
    public boolean exists(String cacheName, Object key) {
        try {
            Cache<Object, Object> cache = jetCacheManager.getCache(cacheName);
            if (cache != null) {
                CacheGetResult<Object> result = cache.GET(key);
                return result.isSuccess();
            }
        } catch (Exception e) {
            log.error("[HaHa JetCache] 检查缓存存在性失败: cacheName={}, key={}", cacheName, key, e);
        }
        return false;
    }

    /**
     * 异步获取缓存值
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param <T>       值类型
     * @return CompletableFuture
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> getAsync(String cacheName, Object key) {
        return CompletableFuture.supplyAsync(() -> get(cacheName, key));
    }

    /**
     * 异步设置缓存值
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     * @return CompletableFuture
     */
    public CompletableFuture<Boolean> putAsync(String cacheName, Object key, Object value) {
        return CompletableFuture.supplyAsync(() -> put(cacheName, key, value));
    }

    /**
     * 获取缓存统计信息
     *
     * @param cacheName 缓存名称
     * @return 统计信息
     */
    public String getStats(String cacheName) {
        return jetCacheManager.getCacheStats(cacheName);
    }
}