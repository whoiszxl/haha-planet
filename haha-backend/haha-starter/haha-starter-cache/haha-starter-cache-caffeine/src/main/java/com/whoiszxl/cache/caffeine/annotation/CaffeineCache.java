package com.whoiszxl.cache.caffeine.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine缓存注解
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CaffeineCache {

    /**
     * 缓存名称
     */
    String cacheName() default "";

    /**
     * 缓存key，支持SpEL表达式
     */
    String key();

    /**
     * 缓存key前缀
     */
    String keyPrefix() default "";

    /**
     * 缓存过期时间
     */
    long expire() default -1;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否允许缓存null值
     */
    boolean allowNullValue() default true;

    /**
     * 缓存条件，支持SpEL表达式
     */
    String condition() default "";

    /**
     * 排除条件，支持SpEL表达式
     */
    String unless() default "";

    /**
     * 是否异步加载
     */
    boolean async() default false;

    /**
     * 缓存操作类型
     */
    CacheOperation operation() default CacheOperation.GET;

    /**
     * 缓存操作类型枚举
     */
    enum CacheOperation {
        GET, PUT, EVICT, CLEAR
    }
}