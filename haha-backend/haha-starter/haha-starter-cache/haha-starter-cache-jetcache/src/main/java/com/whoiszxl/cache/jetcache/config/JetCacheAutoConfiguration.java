package com.whoiszxl.cache.jetcache.config;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.whoiszxl.cache.jetcache.manager.JetCacheManager;
import com.whoiszxl.cache.jetcache.properties.JetCacheProperties;
import com.whoiszxl.cache.jetcache.util.JetCacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * JetCache 自动配置类
 *
 * @author whoiszxl
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(JetCacheProperties.class)
@ConditionalOnProperty(prefix = "haha.cache.jetcache", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.whoiszxl")
@Import(JetCacheConfiguration.class)
public class JetCacheAutoConfiguration {

    private final JetCacheProperties jetCacheProperties;

    @Bean
    @ConditionalOnMissingBean
    public JetCacheManager jetCacheManager() {
        log.info("[HaHa JetCache] 初始化 JetCacheManager");
        return new JetCacheManager(jetCacheProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JetCacheUtil jetCacheUtil(JetCacheManager jetCacheManager) {
        log.info("[HaHa JetCache] 初始化 JetCacheUtil");
        return new JetCacheUtil(jetCacheManager);
    }
}