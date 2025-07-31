package com.whoiszxl.cache.jetcache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * JetCache 配置属性
 *
 * @author whoiszxl
 */
@Data
@ConfigurationProperties(prefix = "haha.cache.jetcache")
public class JetCacheProperties {

    /**
     * 是否启用 JetCache
     */
    private boolean enabled = true;

    /**
     * 统计间隔时间
     */
    private Duration statIntervalMinutes = Duration.ofMinutes(15);

    /**
     * 是否隐藏包名
     */
    private boolean hidePackages = true;

    /**
     * 本地缓存配置
     */
    @NestedConfigurationProperty
    @Valid
    private LocalCacheConfig local = new LocalCacheConfig();

    /**
     * 远程缓存配置
     */
    @NestedConfigurationProperty
    @Valid
    private RemoteCacheConfig remote = new RemoteCacheConfig();

    /**
     * 缓存区域配置
     */
    private Map<String, CacheAreaConfig> areas = new HashMap<>();

    /**
     * 本地缓存配置
     */
    @Data
    public static class LocalCacheConfig {
        /**
         * 缓存类型：caffeine, linkedhashmap
         */
        private String type = "caffeine";

        /**
         * 默认过期时间
         */
        private Duration defaultExpire = Duration.ofMinutes(30);

        /**
         * 最大缓存数量
         */
        private int limit = 10000;

        /**
         * key 转换器
         */
        private String keyConvertor = "fastjson2";
    }

    /**
     * 远程缓存配置
     */
    @Data
    public static class RemoteCacheConfig {
        /**
         * 缓存类型：redis.lettuce, redis.jedis
         */
        private String type = "redis.lettuce";

        /**
         * 默认过期时间
         */
        private Duration defaultExpire = Duration.ofHours(1);

        /**
         * key 转换器
         */
        private String keyConvertor = "fastjson2";

        /**
         * value 序列化器
         */
        private String valueEncoder = "fastjson2";

        /**
         * value 反序列化器
         */
        private String valueDecoder = "fastjson2";

        /**
         * key 前缀
         */
        private String keyPrefix = "haha:cache:";

        /**
         * 是否广播本地缓存失效消息
         */
        private boolean broadcastChannel = true;
    }

    /**
     * 缓存区域配置
     */
    @Data
    public static class CacheAreaConfig {
        /**
         * 本地缓存配置
         */
        @NestedConfigurationProperty
        private LocalCacheConfig local;

        /**
         * 远程缓存配置
         */
        @NestedConfigurationProperty
        private RemoteCacheConfig remote;
    }
}