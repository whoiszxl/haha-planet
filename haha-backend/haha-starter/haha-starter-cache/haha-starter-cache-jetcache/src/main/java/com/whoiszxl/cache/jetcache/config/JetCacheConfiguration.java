package com.whoiszxl.cache.jetcache.config;

import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.redis.lettuce.RedisLettuceCacheBuilder;
import com.alicp.jetcache.support.Fastjson2KeyConvertor;
import com.alicp.jetcache.support.Fastjson2ValueDecoder;
import com.alicp.jetcache.support.Fastjson2ValueEncoder;
import com.whoiszxl.cache.jetcache.properties.JetCacheProperties;
import io.lettuce.core.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JetCache 配置
 *
 * @author whoiszxl
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass(RedisClient.class)
public class JetCacheConfiguration {

    private final JetCacheProperties jetCacheProperties;

    @Autowired(required = false)
    private RedisClient redisClient;

    @Bean
    @DependsOn("redisClient")
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider();
    }

    @Bean
    public GlobalCacheConfig globalCacheConfig(SpringConfigProvider configProvider) {
        Map<String, CacheBuilder> localBuilders = new HashMap<>();
        Map<String, CacheBuilder> remoteBuilders = new HashMap<>();

        // 本地缓存构建器
        CaffeineCacheBuilder<CaffeineCacheBuilder.CaffeineCacheBuilderImpl> localBuilder = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .limit(jetCacheProperties.getLocal().getLimit())
                .expireAfterWrite(jetCacheProperties.getLocal().getDefaultExpire().toMillis(), TimeUnit.MILLISECONDS)
                .keyConvertor(new Fastjson2KeyConvertor());
        localBuilders.put(CacheConsts.DEFAULT_AREA, localBuilder);

        // 远程缓存构建器
        if (redisClient != null) {
            RedisLettuceCacheBuilder<RedisLettuceCacheBuilder.RedisLettuceCacheBuilderImpl> remoteBuilder = RedisLettuceCacheBuilder.createRedisLettuceCacheBuilder()
                    .redisClient(redisClient)
                    .keyConvertor(new Fastjson2KeyConvertor())
                    .valueEncoder(new Fastjson2ValueEncoder(true))
                    .valueDecoder(new Fastjson2ValueDecoder(true))
                    .keyPrefix(jetCacheProperties.getRemote().getKeyPrefix())
                    .expireAfterWrite(jetCacheProperties.getRemote().getDefaultExpire().toMillis(), TimeUnit.MILLISECONDS);
            
            // 设置广播通道
            if (jetCacheProperties.getRemote().isBroadcastChannel()) {
                remoteBuilder.broadcastChannel("haha_cache_channel");
            }
            
            remoteBuilders.put(CacheConsts.DEFAULT_AREA, remoteBuilder);
        }

        // 处理自定义区域配置
        jetCacheProperties.getAreas().forEach((areaName, areaConfig) -> {
            if (areaConfig.getLocal() != null) {
                CaffeineCacheBuilder<CaffeineCacheBuilder.CaffeineCacheBuilderImpl> areaLocalBuilder = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                        .limit(areaConfig.getLocal().getLimit())
                        .expireAfterWrite(areaConfig.getLocal().getDefaultExpire().toMillis(), TimeUnit.MILLISECONDS)
                        .keyConvertor(new Fastjson2KeyConvertor());
                localBuilders.put(areaName, areaLocalBuilder);
            }

            if (areaConfig.getRemote() != null && redisClient != null) {
                RedisLettuceCacheBuilder<RedisLettuceCacheBuilder.RedisLettuceCacheBuilderImpl> areaRemoteBuilder = RedisLettuceCacheBuilder.createRedisLettuceCacheBuilder()
                        .redisClient(redisClient)
                        .keyConvertor(new Fastjson2KeyConvertor())
                        .valueEncoder(new Fastjson2ValueEncoder(true))
                        .valueDecoder(new Fastjson2ValueDecoder(true))
                        .keyPrefix(areaConfig.getRemote().getKeyPrefix())
                        .expireAfterWrite(areaConfig.getRemote().getDefaultExpire().toMillis(), TimeUnit.MILLISECONDS);
                
                // 设置广播通道
                if (areaConfig.getRemote().isBroadcastChannel()) {
                    areaRemoteBuilder.broadcastChannel("haha_cache_channel_" + areaName);
                }
                
                remoteBuilders.put(areaName, areaRemoteBuilder);
            }
        });

        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setLocalCacheBuilders(localBuilders);
        globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
        globalCacheConfig.setStatIntervalMinutes((int) jetCacheProperties.getStatIntervalMinutes().toMinutes());

        log.info("[HaHa JetCache] GlobalCacheConfig 配置完成");
        return globalCacheConfig;
    }
}