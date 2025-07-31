package com.whoiszxl.cache.jetcache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * JetCache 缓存注解
 *
 * @author whoiszxl
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JetCacheable {

    /**
     * 缓存名称
     */
    String cacheName() default "default";

    /**
     * 缓存区域
     */
    String area() default "default";

    /**
     * 缓存键，支持 SpEL 表达式
     */
    String key() default "";

    /**
     * 缓存键前缀
     */
    String keyPrefix() default "";

    /**
     * 过期时间
     */
    long expire() default 3600;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否允许缓存 null 值
     */
    boolean allowNullValue() default true;

    /**
     * 缓存条件，支持 SpEL 表达式
     */
    String condition() default "";

    /**
     * 排除条件，支持 SpEL 表达式
     */
    String unless() default "";

    /**
     * 是否异步更新
     */
    boolean async() default false;
}