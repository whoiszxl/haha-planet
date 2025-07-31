package com.whoiszxl.cache.caffeine.config;

import com.whoiszxl.cache.caffeine.manager.CaffeineCacheManager;
import com.whoiszxl.cache.caffeine.properties.CaffeineProperties;
import com.whoiszxl.cache.caffeine.util.CaffeineUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Caffeine自动配置类
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(com.github.benmanes.caffeine.cache.Cache.class)
@EnableConfigurationProperties(CaffeineProperties.class)
@ConditionalOnProperty(prefix = "haha.cache.caffeine", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableCaching
public class CaffeineAutoConfiguration {

    private final CaffeineProperties properties;

    /**
     * 创建Caffeine缓存管理器
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(properties);
        log.info("Caffeine缓存管理器初始化完成");
        return cacheManager;
    }

    /**
     * 创建Caffeine工具类
     */
    @Bean
    @ConditionalOnMissingBean
    public CaffeineUtil caffeineUtil(CacheManager cacheManager) {
        return new CaffeineUtil(cacheManager);
    }
}