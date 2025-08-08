package com.whoiszxl.starter.auth.justauth;

import com.whoiszxl.cache.redisson.util.RedissonUtil;
import me.zhyd.oauth.cache.AuthStateCache;

import java.time.Duration;

/**
 * @author whoiszxl
 */
public class AuthStateCacheRedisDefaultImpl implements AuthStateCache {

    private static final String KEY_PREFIX = "SOCIAL_AUTH_STATE";

    private final RedissonUtil redissonUtil;

    public AuthStateCacheRedisDefaultImpl(RedissonUtil redissonUtil) {
        this.redissonUtil = redissonUtil;
    }

    /**
     * 存入缓存
     *
     * @param key   key
     * @param value 内容
     */
    @Override
    public void cache(String key, String value) {
        // 参考：在 JustAuth 中，内置了一个基于 map 的 state 缓存器，默认缓存有效期为 3 分钟
        redissonUtil.set(redissonUtil.formatKey(KEY_PREFIX, key), value, Duration.ofMinutes(3));
    }

    /**
     * 存入缓存
     *
     * @param key     key
     * @param value   内容
     * @param timeout 缓存过期时间（毫秒）
     */
    @Override
    public void cache(String key, String value, long timeout) {
        redissonUtil.set(redissonUtil.formatKey(KEY_PREFIX, key), value, Duration.ofMillis(timeout));
    }

    /**
     * 获取缓存内容
     *
     * @param key key
     * @return 内容
     */
    @Override
    public String get(String key) {
        return redissonUtil.get(redissonUtil.formatKey(KEY_PREFIX, key));
    }

    /**
     * 是否存在 key，如果对应 key 的 value 值已过期，也返回 false
     *
     * @param key key
     * @return true：存在 key，并且 value 没过期；false：key 不存在或者已过期
     */
    @Override
    public boolean containsKey(String key) {
        return redissonUtil.exists(redissonUtil.formatKey(KEY_PREFIX, key));
    }
}