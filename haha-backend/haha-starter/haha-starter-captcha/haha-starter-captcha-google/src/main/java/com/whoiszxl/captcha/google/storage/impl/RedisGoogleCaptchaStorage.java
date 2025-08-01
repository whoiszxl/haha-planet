package com.whoiszxl.captcha.google.storage.impl;

import com.whoiszxl.captcha.google.properties.GoogleCaptchaProperties;
import com.whoiszxl.captcha.google.storage.GoogleCaptchaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis存储实现
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class RedisGoogleCaptchaStorage implements GoogleCaptchaStorage {

    private final StringRedisTemplate redisTemplate;
    private final GoogleCaptchaProperties properties;

    private static final String SECRET_KEY_PREFIX = "secret:";
    private static final String USED_CODE_PREFIX = "used:";
    private static final String BACKUP_CODES_PREFIX = "backup:";
    private static final String FAILURE_COUNT_PREFIX = "failure:";

    @Override
    public void storeSecret(String userId, String secretKey, Duration ttl) {
        String key = buildKey(SECRET_KEY_PREFIX, userId);
        redisTemplate.opsForValue().set(key, secretKey, ttl);
        log.debug("Redis存储用户 {} 的密钥，过期时间: {} 秒", userId, ttl.getSeconds());
    }

    @Override
    public String getSecret(String userId) {
        String key = buildKey(SECRET_KEY_PREFIX, userId);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void removeSecret(String userId) {
        String key = buildKey(SECRET_KEY_PREFIX, userId);
        redisTemplate.delete(key);
        log.debug("Redis删除用户 {} 的密钥", userId);
    }

    @Override
    public void storeUsedCode(String userId, String code, Duration ttl) {
        String key = buildKey(USED_CODE_PREFIX, userId, code);
        redisTemplate.opsForValue().set(key, "1", ttl);
        log.debug("Redis存储用户 {} 的已使用验证码 {}，过期时间: {} 秒", userId, code, ttl.getSeconds());
    }

    @Override
    public boolean isCodeUsed(String userId, String code) {
        String key = buildKey(USED_CODE_PREFIX, userId, code);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void storeBackupCodes(String userId, Set<String> backupCodes, Duration ttl) {
        String key = buildKey(BACKUP_CODES_PREFIX, userId);
        redisTemplate.opsForSet().add(key, backupCodes.toArray(new String[0]));
        redisTemplate.expire(key, ttl);
        log.debug("Redis存储用户 {} 的备用恢复码，数量: {}，过期时间: {} 秒", userId, backupCodes.size(), ttl.getSeconds());
    }

    @Override
    public Set<String> getBackupCodes(String userId) {
        String key = buildKey(BACKUP_CODES_PREFIX, userId);
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public void removeBackupCode(String userId, String backupCode) {
        String key = buildKey(BACKUP_CODES_PREFIX, userId);
        redisTemplate.opsForSet().remove(key, backupCode);
        log.debug("Redis删除用户 {} 的备用恢复码: {}", userId, backupCode);
    }

    @Override
    public long incrementFailureCount(String userId, Duration ttl) {
        String key = buildKey(FAILURE_COUNT_PREFIX, userId);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            // 第一次设置过期时间
            redisTemplate.expire(key, ttl);
        }
        log.debug("Redis用户 {} 验证失败次数增加到: {}", userId, count);
        return count != null ? count : 0;
    }

    @Override
    public long getFailureCount(String userId) {
        String key = buildKey(FAILURE_COUNT_PREFIX, userId);
        String count = redisTemplate.opsForValue().get(key);
        return count != null ? Long.parseLong(count) : 0;
    }

    @Override
    public void clearFailureCount(String userId) {
        String key = buildKey(FAILURE_COUNT_PREFIX, userId);
        redisTemplate.delete(key);
        log.debug("Redis清除用户 {} 的验证失败次数", userId);
    }

    @Override
    @Scheduled(fixedRate = 600000) // 每10分钟执行一次（Redis会自动清理过期数据，这里主要用于统计）
    public void clearExpired() {
        // Redis会自动清理过期数据，这里可以添加一些统计逻辑
        log.debug("Redis存储清理检查完成");
    }

    /**
     * 构建Redis键
     */
    private String buildKey(String... parts) {
        StringBuilder sb = new StringBuilder(properties.getStorage().getKeyPrefix());
        for (String part : parts) {
            sb.append(part);
            if (!part.endsWith(":")) {
                sb.append(":");
            }
        }
        // 移除最后一个冒号
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ':') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}