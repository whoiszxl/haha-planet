package com.whoiszxl.cache.caffeine.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.whoiszxl.cache.caffeine.properties.CaffeineProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine缓存管理器
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
public class CaffeineCacheManager extends AbstractCacheManager {

    private final CaffeineProperties properties;
    private final Map<String, Cache<Object, Object>> cacheMap = new ConcurrentHashMap<>();
    private boolean allowNullValues = true;

    public CaffeineCacheManager(CaffeineProperties properties) {
        this.properties = properties;
        this.allowNullValues = properties.getDefaultConfig().isAllowNullValues();
    }

    @Override
    protected Collection<org.springframework.cache.Cache> loadCaches() {
        return cacheMap.values().stream()
                .map(cache -> new CaffeineCache(getCacheName(cache), cache, allowNullValues))
                .map(org.springframework.cache.Cache.class::cast)
                .toList();
    }

    @Override
    protected org.springframework.cache.Cache getMissingCache(String name) {
        Cache<Object, Object> cache = cacheMap.computeIfAbsent(name, this::createCache);
        return new CaffeineCache(name, cache, allowNullValues);
    }

    /**
     * 创建缓存实例
     */
    private Cache<Object, Object> createCache(String name) {
        CaffeineProperties.CacheConfig config = properties.getCaches().getOrDefault(name, properties.getDefaultConfig());
        
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        
        // 基本配置
        if (config.getInitialCapacity() > 0) {
            builder.initialCapacity(config.getInitialCapacity());
        }
        
        if (config.getMaximumSize() > 0) {
            builder.maximumSize(config.getMaximumSize());
        }
        
        if (config.getMaximumWeight() > 0) {
            builder.maximumWeight(config.getMaximumWeight());
        }
        
        // 过期策略
        if (config.getExpireAfterWrite() != null) {
            builder.expireAfterWrite(config.getExpireAfterWrite().toMillis(), TimeUnit.MILLISECONDS);
        }
        
        if (config.getExpireAfterAccess() != null) {
            builder.expireAfterAccess(config.getExpireAfterAccess().toMillis(), TimeUnit.MILLISECONDS);
        }
        
        if (config.getRefreshAfterWrite() != null) {
            builder.refreshAfterWrite(config.getRefreshAfterWrite().toMillis(), TimeUnit.MILLISECONDS);
        }
        
        // 引用类型
        if (config.isWeakKeys()) {
            builder.weakKeys();
        }
        
        if (config.isWeakValues()) {
            builder.weakValues();
        }
        
        if (config.isSoftValues()) {
            builder.softValues();
        }
        
        // 统计
        if (config.isRecordStats()) {
            builder.recordStats();
        }
        
        // 移除监听器
        if (StringUtils.hasText(config.getRemovalListener())) {
            try {
                Class<?> listenerClass = Class.forName(config.getRemovalListener());
                RemovalListener<Object, Object> listener = (RemovalListener<Object, Object>) listenerClass.getDeclaredConstructor().newInstance();
                builder.removalListener(listener);
            } catch (Exception e) {
                log.warn("无法创建移除监听器: {}", config.getRemovalListener(), e);
            }
        }
        
        // 权重计算器
        if (StringUtils.hasText(config.getWeigher())) {
            try {
                Class<?> weigherClass = Class.forName(config.getWeigher());
                com.github.benmanes.caffeine.cache.Weigher<Object, Object> weigher = 
                    (com.github.benmanes.caffeine.cache.Weigher<Object, Object>) weigherClass.getDeclaredConstructor().newInstance();
                builder.weigher(weigher);
            } catch (Exception e) {
                log.warn("无法创建权重计算器: {}", config.getWeigher(), e);
            }
        }
        
        // 缓存加载器
        if (StringUtils.hasText(config.getCacheLoader())) {
            try {
                Class<?> loaderClass = Class.forName(config.getCacheLoader());
                com.github.benmanes.caffeine.cache.CacheLoader<Object, Object> loader = 
                    (com.github.benmanes.caffeine.cache.CacheLoader<Object, Object>) loaderClass.getDeclaredConstructor().newInstance();
                LoadingCache<Object, Object> loadingCache = builder.build(loader);
                log.info("创建LoadingCache: {}", name);
                return loadingCache;
            } catch (Exception e) {
                log.warn("无法创建缓存加载器: {}", config.getCacheLoader(), e);
            }
        }
        
        Cache<Object, Object> cache = builder.build();
        log.info("创建Cache: {}", name);
        return cache;
    }
    
    /**
     * 获取缓存名称
     */
    private String getCacheName(Cache<Object, Object> cache) {
        return cacheMap.entrySet().stream()
                .filter(entry -> entry.getValue() == cache)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("unknown");
    }
    
    /**
     * 获取原生Caffeine缓存
     */
    public Cache<Object, Object> getNativeCache(String name) {
        return cacheMap.get(name);
    }
    
    /**
     * 获取所有缓存名称
     */
    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }
}