package com.whoiszxl.captcha.google.storage.impl;

import com.whoiszxl.captcha.google.storage.GoogleCaptchaStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存存储实现
 *
 * @author whoiszxl
 */
@Slf4j
public class MemoryGoogleCaptchaStorage implements GoogleCaptchaStorage {

    /**
     * 用户密钥存储
     */
    private final Map<String, SecretEntry> secretStorage = new ConcurrentHashMap<>();

    /**
     * 已使用验证码存储
     */
    private final Map<String, UsedCodeEntry> usedCodeStorage = new ConcurrentHashMap<>();

    /**
     * 备用恢复码存储
     */
    private final Map<String, BackupCodesEntry> backupCodesStorage = new ConcurrentHashMap<>();

    /**
     * 验证失败次数存储
     */
    private final Map<String, FailureEntry> failureStorage = new ConcurrentHashMap<>();

    @Override
    public void storeSecret(String userId, String secretKey, Duration ttl) {
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        secretStorage.put(userId, new SecretEntry(secretKey, expireTime));
        log.debug("存储用户 {} 的密钥，过期时间: {}", userId, expireTime);
    }

    @Override
    public String getSecret(String userId) {
        SecretEntry entry = secretStorage.get(userId);
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                secretStorage.remove(userId);
            }
            return null;
        }
        return entry.getSecretKey();
    }

    @Override
    public void removeSecret(String userId) {
        secretStorage.remove(userId);
        log.debug("删除用户 {} 的密钥", userId);
    }

    @Override
    public void storeUsedCode(String userId, String code, Duration ttl) {
        String key = userId + ":" + code;
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        usedCodeStorage.put(key, new UsedCodeEntry(expireTime));
        log.debug("存储用户 {} 的已使用验证码 {}，过期时间: {}", userId, code, expireTime);
    }

    @Override
    public boolean isCodeUsed(String userId, String code) {
        String key = userId + ":" + code;
        UsedCodeEntry entry = usedCodeStorage.get(key);
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                usedCodeStorage.remove(key);
            }
            return false;
        }
        return true;
    }

    @Override
    public void storeBackupCodes(String userId, Set<String> backupCodes, Duration ttl) {
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        backupCodesStorage.put(userId, new BackupCodesEntry(backupCodes, expireTime));
        log.debug("存储用户 {} 的备用恢复码，数量: {}，过期时间: {}", userId, backupCodes.size(), expireTime);
    }

    @Override
    public Set<String> getBackupCodes(String userId) {
        BackupCodesEntry entry = backupCodesStorage.get(userId);
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                backupCodesStorage.remove(userId);
            }
            return null;
        }
        return entry.getBackupCodes();
    }

    @Override
    public void removeBackupCode(String userId, String backupCode) {
        BackupCodesEntry entry = backupCodesStorage.get(userId);
        if (entry != null && !entry.isExpired()) {
            entry.getBackupCodes().remove(backupCode);
            if (entry.getBackupCodes().isEmpty()) {
                backupCodesStorage.remove(userId);
            }
            log.debug("删除用户 {} 的备用恢复码: {}", userId, backupCode);
        }
    }

    @Override
    public long incrementFailureCount(String userId, Duration ttl) {
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        FailureEntry entry = failureStorage.computeIfAbsent(userId, 
            k -> new FailureEntry(new AtomicLong(0), expireTime));
        
        if (entry.isExpired()) {
            entry.setCount(new AtomicLong(1));
            entry.setExpireTime(expireTime);
            return 1;
        }
        
        long count = entry.getCount().incrementAndGet();
        log.debug("用户 {} 验证失败次数增加到: {}", userId, count);
        return count;
    }

    @Override
    public long getFailureCount(String userId) {
        FailureEntry entry = failureStorage.get(userId);
        if (entry == null || entry.isExpired()) {
            if (entry != null && entry.isExpired()) {
                failureStorage.remove(userId);
            }
            return 0;
        }
        return entry.getCount().get();
    }

    @Override
    public void clearFailureCount(String userId) {
        failureStorage.remove(userId);
        log.debug("清除用户 {} 的验证失败次数", userId);
    }

    @Override
    @Scheduled(fixedRate = 300000) // 每5分钟清理一次
    public void clearExpired() {
        LocalDateTime now = LocalDateTime.now();
        
        // 清理过期的密钥
        secretStorage.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                log.debug("清理过期密钥: {}", entry.getKey());
            }
            return expired;
        });
        
        // 清理过期的已使用验证码
        usedCodeStorage.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                log.debug("清理过期已使用验证码: {}", entry.getKey());
            }
            return expired;
        });
        
        // 清理过期的备用恢复码
        backupCodesStorage.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                log.debug("清理过期备用恢复码: {}", entry.getKey());
            }
            return expired;
        });
        
        // 清理过期的失败次数
        failureStorage.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired();
            if (expired) {
                log.debug("清理过期失败次数: {}", entry.getKey());
            }
            return expired;
        });
        
        log.debug("内存存储清理完成，当前存储数量 - 密钥: {}, 已使用验证码: {}, 备用恢复码: {}, 失败次数: {}",
                secretStorage.size(), usedCodeStorage.size(), backupCodesStorage.size(), failureStorage.size());
    }

    /**
     * 密钥条目
     */
    private static class SecretEntry {
        @Getter
        private final String secretKey;
        private final LocalDateTime expireTime;

        public SecretEntry(String secretKey, LocalDateTime expireTime) {
            this.secretKey = secretKey;
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }
    }

    /**
     * 已使用验证码条目
     */
    private static class UsedCodeEntry {
        private final LocalDateTime expireTime;

        public UsedCodeEntry(LocalDateTime expireTime) {
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }
    }

    /**
     * 备用恢复码条目
     */
    private static class BackupCodesEntry {
        private final Set<String> backupCodes;
        private final LocalDateTime expireTime;

        public BackupCodesEntry(Set<String> backupCodes, LocalDateTime expireTime) {
            this.backupCodes = backupCodes;
            this.expireTime = expireTime;
        }

        public Set<String> getBackupCodes() {
            return backupCodes;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }
    }

    /**
     * 失败次数条目
     */
    @Setter
    @Getter
    private static class FailureEntry {
        private AtomicLong count;
        private LocalDateTime expireTime;

        public FailureEntry(AtomicLong count, LocalDateTime expireTime) {
            this.count = count;
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expireTime);
        }
    }
}