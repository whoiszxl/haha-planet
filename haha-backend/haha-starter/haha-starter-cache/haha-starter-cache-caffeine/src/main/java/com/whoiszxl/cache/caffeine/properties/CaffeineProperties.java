package com.whoiszxl.cache.caffeine.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caffeine缓存配置属性
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Data
@Validated
@ConfigurationProperties(prefix = "haha.cache.caffeine")
public class CaffeineProperties {

    /**
     * 是否启用Caffeine缓存
     */
    private boolean enabled = true;

    /**
     * 默认缓存配置
     */
    private CacheConfig defaultConfig = new CacheConfig();

    /**
     * 具体缓存配置，key为缓存名称
     */
    private Map<String, CacheConfig> caches = new ConcurrentHashMap<>();

    /**
     * 监控配置
     */
    private MonitorConfig monitor = new MonitorConfig();

    /**
     * 序列化配置
     */
    private SerializationConfig serialization = new SerializationConfig();

    /**
     * 缓存配置
     */
    @Data
    public static class CacheConfig {
        /**
         * 缓存规格配置
         */
        @NotBlank(message = "缓存规格不能为空")
        private String spec = "maximumSize=1000,expireAfterWrite=30m";

        /**
         * 最大缓存条目数
         */
        @Positive(message = "最大缓存条目数必须大于0")
        private long maximumSize = 1000L;

        /**
         * 最大权重
         */
        private long maximumWeight = -1L;

        /**
         * 初始容量
         */
        @Positive(message = "初始容量必须大于0")
        private int initialCapacity = 16;

        /**
         * 写入后过期时间
         */
        private Duration expireAfterWrite = Duration.ofMinutes(30);

        /**
         * 访问后过期时间
         */
        private Duration expireAfterAccess;

        /**
         * 刷新时间
         */
        private Duration refreshAfterWrite;

        /**
         * 是否启用弱引用键
         */
        private boolean weakKeys = false;

        /**
         * 是否启用弱引用值
         */
        private boolean weakValues = false;

        /**
         * 是否启用软引用值
         */
        private boolean softValues = false;

        /**
         * 是否记录统计信息
         */
        private boolean recordStats = true;

        /**
         * 是否允许空值
         */
        private boolean allowNullValues = true;

        /**
         * 缓存加载器类名
         */
        private String cacheLoader;

        /**
         * 缓存移除监听器类名
         */
        private String removalListener;

        /**
         * 权重计算器类名
         */
        private String weigher;
    }

    /**
     * 监控配置
     */
    @Data
    public static class MonitorConfig {
        /**
         * 是否启用监控
         */
        private boolean enabled = true;

        /**
         * 是否启用JMX监控
         */
        private boolean jmxEnabled = true;

        /**
         * 是否启用Micrometer指标
         */
        private boolean micrometerEnabled = true;

        /**
         * 统计信息输出间隔
         */
        private Duration statsInterval = Duration.ofMinutes(5);

        /**
         * 是否启用详细统计
         */
        private boolean detailedStats = false;

        /**
         * 监控指标前缀
         */
        private String metricsPrefix = "caffeine.cache";
    }

    /**
     * 序列化配置
     */
    @Data
    public static class SerializationConfig {
        /**
         * 键序列化器类型
         */
        private SerializerType keySerializer = SerializerType.STRING;

        /**
         * 值序列化器类型
         */
        private SerializerType valueSerializer = SerializerType.JSON;

        /**
         * 自定义键序列化器类名
         */
        private String customKeySerializer;

        /**
         * 自定义值序列化器类名
         */
        private String customValueSerializer;

        /**
         * 序列化器类型枚举
         */
        public enum SerializerType {
            STRING, JSON, JAVA, KRYO, PROTOBUF, CUSTOM
        }
    }
}