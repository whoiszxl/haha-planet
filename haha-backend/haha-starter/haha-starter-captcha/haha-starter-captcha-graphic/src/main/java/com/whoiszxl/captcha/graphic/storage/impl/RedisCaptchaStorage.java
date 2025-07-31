package com.whoiszxl.captcha.graphic.storage.impl;

import com.whoiszxl.captcha.graphic.properties.GraphicCaptchaProperties;
import com.whoiszxl.captcha.graphic.storage.CaptchaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * Redis验证码存储实现
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class RedisCaptchaStorage implements CaptchaStorage {

    private final StringRedisTemplate redisTemplate;
    private final GraphicCaptchaProperties properties;

    @Override
    public void store(String key, String value, Duration duration) {
        String redisKey = getRedisKey(key);
        redisTemplate.opsForValue().set(redisKey, value, duration);
        log.debug("存储验证码到Redis: key={}, duration={}", redisKey, duration);
    }

    @Override
    public String get(String key) {
        String redisKey = getRedisKey(key);
        String value = redisTemplate.opsForValue().get(redisKey);
        log.debug("从Redis获取验证码: key={}, found={}", redisKey, value != null);
        return value;
    }

    @Override
    public void remove(String key) {
        String redisKey = getRedisKey(key);
        redisTemplate.delete(redisKey);
        log.debug("从Redis移除验证码: key={}", redisKey);
    }

    @Override
    public boolean exists(String key) {
        String redisKey = getRedisKey(key);
        Boolean exists = redisTemplate.hasKey(redisKey);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public void clearExpired() {
        // Redis会自动清理过期键，无需手动实现
        log.debug("Redis自动清理过期验证码");
    }

    private String getRedisKey(String key) {
        return properties.getStorage().getKeyPrefix() + key;
    }
}