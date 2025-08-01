package com.whoiszxl.captcha.google.storage.impl;

import com.whoiszxl.captcha.google.properties.GoogleCaptchaProperties;
import com.whoiszxl.captcha.google.storage.GoogleCaptchaStorage;
import com.whoiszxl.captcha.google.storage.entity.*;
import com.whoiszxl.captcha.google.storage.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MySQL存储实现
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class MysqlGoogleCaptchaStorage implements GoogleCaptchaStorage {

    private final GoogleCaptchaSecretRepository secretRepository;
    private final GoogleCaptchaUsedCodeRepository usedCodeRepository;
    private final GoogleCaptchaBackupCodeRepository backupCodeRepository;
    private final GoogleCaptchaFailureRepository failureRepository;
    private final GoogleCaptchaProperties properties;

    @Override
    @Transactional
    public void storeSecret(String userId, String secretKey, Duration ttl) {
        // 先删除用户的旧密钥
        secretRepository.deleteByUserId(userId);
        
        // 保存新密钥
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        GoogleCaptchaSecretEntity entity = GoogleCaptchaSecretEntity.builder()
                .userId(userId)
                .secretKey(secretKey)
                .expireTime(expireTime)
                .build();
        secretRepository.save(entity);
        log.debug("MySQL存储用户 {} 的密钥，过期时间: {}", userId, expireTime);
    }

    @Override
    public String getSecret(String userId) {
        Optional<GoogleCaptchaSecretEntity> entity = secretRepository.findValidByUserId(userId, LocalDateTime.now());
        return entity.map(GoogleCaptchaSecretEntity::getSecretKey).orElse(null);
    }

    @Override
    @Transactional
    public void removeSecret(String userId) {
        secretRepository.deleteByUserId(userId);
        log.debug("MySQL删除用户 {} 的密钥", userId);
    }

    @Override
    @Transactional
    public void storeUsedCode(String userId, String code, Duration ttl) {
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        GoogleCaptchaUsedCodeEntity entity = GoogleCaptchaUsedCodeEntity.builder()
                .userId(userId)
                .code(code)
                .expireTime(expireTime)
                .build();
        usedCodeRepository.save(entity);
        log.debug("MySQL存储用户 {} 的已使用验证码 {}，过期时间: {}", userId, code, expireTime);
    }

    @Override
    public boolean isCodeUsed(String userId, String code) {
        return usedCodeRepository.existsByUserIdAndCodeAndNotExpired(userId, code, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void storeBackupCodes(String userId, Set<String> backupCodes, Duration ttl) {
        // 先删除用户的旧备用恢复码
        backupCodeRepository.deleteByUserId(userId);
        
        // 保存新的备用恢复码
        LocalDateTime expireTime = LocalDateTime.now().plus(ttl);
        List<GoogleCaptchaBackupCodeEntity> entities = backupCodes.stream()
                .map(code -> GoogleCaptchaBackupCodeEntity.builder()
                        .userId(userId)
                        .backupCode(code)
                        .expireTime(expireTime)
                        .build())
                .collect(Collectors.toList());
        backupCodeRepository.saveAll(entities);
        log.debug("MySQL存储用户 {} 的备用恢复码，数量: {}，过期时间: {}", userId, backupCodes.size(), expireTime);
    }

    @Override
    public Set<String> getBackupCodes(String userId) {
        List<GoogleCaptchaBackupCodeEntity> entities = backupCodeRepository.findValidByUserId(userId, LocalDateTime.now());
        return entities.stream()
                .map(GoogleCaptchaBackupCodeEntity::getBackupCode)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void removeBackupCode(String userId, String backupCode) {
        backupCodeRepository.deleteByUserIdAndBackupCode(userId, backupCode);
        log.debug("MySQL删除用户 {} 的备用恢复码: {}", userId, backupCode);
    }

    @Override
    @Transactional
    public long incrementFailureCount(String userId, Duration ttl) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plus(ttl);
        
        Optional<GoogleCaptchaFailureEntity> existingEntity = failureRepository.findValidByUserId(userId, now);
        
        GoogleCaptchaFailureEntity entity;
        if (existingEntity.isPresent()) {
            entity = existingEntity.get();
            entity.setFailureCount(entity.getFailureCount() + 1);
            entity.setExpireTime(expireTime);
        } else {
            entity = GoogleCaptchaFailureEntity.builder()
                    .userId(userId)
                    .failureCount(1L)
                    .expireTime(expireTime)
                    .build();
        }
        
        entity = failureRepository.save(entity);
        long count = entity.getFailureCount();
        log.debug("MySQL用户 {} 验证失败次数增加到: {}", userId, count);
        return count;
    }

    @Override
    public long getFailureCount(String userId) {
        Optional<GoogleCaptchaFailureEntity> entity = failureRepository.findValidByUserId(userId, LocalDateTime.now());
        return entity.map(GoogleCaptchaFailureEntity::getFailureCount).orElse(0L);
    }

    @Override
    @Transactional
    public void clearFailureCount(String userId) {
        failureRepository.deleteByUserId(userId);
        log.debug("MySQL清除用户 {} 的验证失败次数", userId);
    }

    @Override
    @Scheduled(fixedRate = 3600000) // 每小时清理一次
    @Transactional
    public void clearExpired() {
        LocalDateTime now = LocalDateTime.now();
        
        int secretCount = secretRepository.deleteExpired(now);
        int usedCodeCount = usedCodeRepository.deleteExpired(now);
        int backupCodeCount = backupCodeRepository.deleteExpired(now);
        int failureCount = failureRepository.deleteExpired(now);
        
        log.info("MySQL存储清理完成 - 密钥: {}, 已使用验证码: {}, 备用恢复码: {}, 失败记录: {}",
                secretCount, usedCodeCount, backupCodeCount, failureCount);
    }
}