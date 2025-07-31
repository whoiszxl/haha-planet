package com.whoiszxl.cache.redisson.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redisson缓存注解
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonCache {

    /**
     * 缓存key，支持SpEL表达式
     */
    String key();

    /**
     * 缓存前缀
     */
    String prefix() default "cache:";

    /**
     * 缓存过期时间，默认1小时
     */
    long expire() default 3600;

    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 是否允许缓存null值
     */
    boolean allowNullValue() default false;

    /**
     * 缓存条件，支持SpEL表达式
     */
    String condition() default "";

    /**
     * 排除条件，支持SpEL表达式
     */
    String unless() default "";
}