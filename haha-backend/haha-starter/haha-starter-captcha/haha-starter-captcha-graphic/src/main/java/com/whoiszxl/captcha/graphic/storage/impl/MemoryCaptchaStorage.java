package com.whoiszxl.captcha.graphic.storage.impl;

import com.whoiszxl.captcha.graphic.storage.CaptchaStorage;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内存验证码存储实现
 *
 * @author whoiszxl
 */
@Slf4j
public class MemoryCaptchaStorage implements CaptchaStorage {

    private final ConcurrentHashMap<String, CaptchaEntry> storage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public MemoryCaptchaStorage() {
        // 每分钟清理一次过期验证码
        scheduler.scheduleAtFixedRate(this::clearExpired, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void store(String key, String value, Duration duration) {
        LocalDateTime expireTime = LocalDateTime.now().plus(duration);
        storage.put(key, new CaptchaEntry(value, expireTime));
        log.debug("存储验证码: key={}, expireTime={}", key, expireTime);
    }

    @Override
    public String get(String key) {
        CaptchaEntry entry = storage.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            storage.remove(key);
            log.debug("验证码已过期并被移除: key={}", key);
            return null;
        }
        return entry.getValue();
    }

    @Override
    public void remove(String key) {
        storage.remove(key);
        log.debug("移除验证码: key={}", key);
    }

    @Override
    public boolean exists(String key) {
        CaptchaEntry entry = storage.get(key);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            storage.remove(key);
            return false;
        }
        return true;
    }

    @Override
    public void clearExpired() {
        int removedCount = 0;
        for (String key : storage.keySet()) {
            CaptchaEntry entry = storage.get(key);
            if (entry != null && entry.isExpired()) {
                storage.remove(key);
                removedCount++;
            }
        }
        if (removedCount > 0) {
            log.debug("清理过期验证码: 移除数量={}", removedCount);
        }
    }

    /**
     * 验证码条目
     */
    private static class CaptchaEntry {
        private final String value;
        private final LocalDateTime expireTime;

        public CaptchaEntry(String value, LocalDateTime expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public String getValue() {
            return value;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }
    }
}