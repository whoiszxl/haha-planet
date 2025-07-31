package com.whoiszxl.cache.redisson.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁的key，支持SpEL表达式
     */
    String key();

    /**
     * 锁的前缀
     */
    String prefix() default "lock:";

    /**
     * 等待锁的时间，默认3秒
     */
    long waitTime() default 3;

    /**
     * 锁的持有时间，默认10秒
     */
    long leaseTime() default 10;

    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 获取锁失败时的处理策略
     */
    LockFailStrategy failStrategy() default LockFailStrategy.EXCEPTION;

    /**
     * 获取锁失败时的错误消息
     */
    String failMessage() default "获取分布式锁失败";

    /**
     * 锁失败处理策略
     */
    enum LockFailStrategy {
        /**
         * 抛出异常
         */
        EXCEPTION,
        /**
         * 返回null
         */
        RETURN_NULL,
        /**
         * 返回默认值
         */
        RETURN_DEFAULT
    }
}