package com.whoiszxl.captcha.google.service.impl;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.whoiszxl.captcha.google.exception.GoogleCaptchaException;
import com.whoiszxl.captcha.google.model.GoogleCaptchaResult;
import com.whoiszxl.captcha.google.model.GoogleCaptchaValidateRequest;
import com.whoiszxl.captcha.google.properties.GoogleCaptchaProperties;
import com.whoiszxl.captcha.google.service.GoogleCaptchaService;
import com.whoiszxl.captcha.google.storage.GoogleCaptchaStorage;
import com.whoiszxl.captcha.google.util.QrCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * 谷歌验证码服务实现类
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class GoogleCaptchaServiceImpl implements GoogleCaptchaService {

    private final GoogleCaptchaProperties properties;
    private final GoogleAuthenticator googleAuthenticator;
    private final GoogleCaptchaStorage storage;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public GoogleCaptchaResult generateSecret(String userId) {
        return generateSecret(userId, null);
    }

    @Override
    public GoogleCaptchaResult generateSecret(String userId, String username) {
        if (!StringUtils.hasText(userId)) {
            throw new GoogleCaptchaException("用户标识不能为空");
        }

        try {
            // 生成密钥
            GoogleAuthenticatorKey key = googleAuthenticator.createCredentials();
            String secretKey = key.getKey();

            // 构建账户名称
            String accountName = StringUtils.hasText(username) ? username : userId;
            
            // 生成二维码URL
            String qrCodeUrl = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s&digits=%d&period=%d",
                properties.getIssuer(),
                accountName,
                secretKey,
                properties.getIssuer(),
                properties.getDigits(),
                properties.getPeriod()
            );

            // 生成二维码图片（Base64编码）
            String qrCodeImage = QrCodeUtil.generateQrCodeImage(
                qrCodeUrl,
                properties.getQrCode().getWidth(),
                properties.getQrCode().getHeight(),
                properties.getQrCode().getFormat()
            );

            // 生成备用恢复码
            String[] backupCodes = generateBackupCodes(userId, 10);

            // 存储密钥（临时存储，用户确认绑定后再永久存储）
            storage.storeSecret(userId, secretKey, properties.getStorage().getKeyExpireTime());

            // 构建结果
            GoogleCaptchaResult result = GoogleCaptchaResult.builder()
                    .userId(userId)
                    .secretKey(secretKey)
                    .qrCodeUrl(qrCodeUrl)
                    .qrCodeImage(qrCodeImage)
                    .backupCodes(backupCodes)
                    .timestamp(System.currentTimeMillis())
                    .build();

            log.info("为用户 {} 生成谷歌验证码密钥成功", userId);
            return result;

        } catch (Exception e) {
            log.error("为用户 {} 生成谷歌验证码密钥失败", userId, e);
            throw new GoogleCaptchaException("生成谷歌验证码密钥失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validate(GoogleCaptchaValidateRequest request) {
        if (request == null || !StringUtils.hasText(request.getUserId()) || !StringUtils.hasText(request.getCode())) {
            throw new GoogleCaptchaException("验证请求参数不能为空");
        }

        return validate(request.getUserId(), request.getCode());
    }

    @Override
    public boolean validate(String userId, String code) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(code)) {
            throw new GoogleCaptchaException("用户标识和验证码不能为空");
        }

        try {
            // 检查是否被锁定
            if (isUserLocked(userId)) {
                log.warn("用户 {} 因验证失败次数过多被锁定", userId);
                throw new GoogleCaptchaException("用户因验证失败次数过多被锁定，请稍后再试");
            }

            // 获取用户密钥
            String secretKey = storage.getSecret(userId);
            if (!StringUtils.hasText(secretKey)) {
                log.warn("用户 {} 未绑定谷歌验证器", userId);
                throw new GoogleCaptchaException("用户未绑定谷歌验证器");
            }

            // 防重放攻击检查
            if (properties.getSecurity().isEnableAntiReplay() && storage.isCodeUsed(userId, code)) {
                log.warn("用户 {} 验证码 {} 已被使用", userId, code);
                incrementFailureCount(userId);
                throw new GoogleCaptchaException("验证码已被使用");
            }

            // 验证验证码
            boolean isValid = googleAuthenticator.authorize(secretKey, Integer.parseInt(code));
            
            if (isValid) {
                // 验证成功，清除失败次数
                storage.clearFailureCount(userId);
                
                // 记录已使用的验证码（防重放）
                if (properties.getSecurity().isEnableAntiReplay()) {
                    storage.storeUsedCode(userId, code, properties.getSecurity().getUsedCodeExpireTime());
                }
                
                log.info("用户 {} 谷歌验证码验证成功", userId);
                return true;
            } else {
                // 验证失败，增加失败次数
                incrementFailureCount(userId);
                log.warn("用户 {} 谷歌验证码验证失败", userId);
                return false;
            }

        } catch (NumberFormatException e) {
            log.warn("用户 {} 提供的验证码格式无效: {}", userId, code);
            incrementFailureCount(userId);
            throw new GoogleCaptchaException("验证码格式无效");
        } catch (GoogleCaptchaException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户 {} 验证谷歌验证码时发生异常", userId, e);
            incrementFailureCount(userId);
            throw new GoogleCaptchaException("验证谷歌验证码失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getSecret(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new GoogleCaptchaException("用户标识不能为空");
        }

        return storage.getSecret(userId);
    }

    @Override
    public void removeSecret(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new GoogleCaptchaException("用户标识不能为空");
        }

        storage.removeSecret(userId);
        // 同时清理相关数据
        storage.clearFailureCount(userId);
        log.info("删除用户 {} 的谷歌验证码密钥成功", userId);
    }

    @Override
    public String[] generateBackupCodes(String userId, int count) {
        if (!StringUtils.hasText(userId)) {
            throw new GoogleCaptchaException("用户标识不能为空");
        }
        
        if (count <= 0 || count > 20) {
            throw new GoogleCaptchaException("备用恢复码数量必须在1-20之间");
        }

        try {
            Set<String> backupCodes = new HashSet<>();
            
            // 生成指定数量的备用恢复码
            while (backupCodes.size() < count) {
                String code = generateRandomCode(8);
                backupCodes.add(code);
            }

            // 存储备用恢复码，1年有效期
            storage.storeBackupCodes(userId, backupCodes, Duration.ofDays(365));

            log.info("为用户 {} 生成 {} 个备用恢复码", userId, count);
            return backupCodes.toArray(new String[0]);

        } catch (Exception e) {
            log.error("为用户 {} 生成备用恢复码失败", userId, e);
            throw new GoogleCaptchaException("生成备用恢复码失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validateBackupCode(String userId, String backupCode) {
        if (!StringUtils.hasText(userId) || !StringUtils.hasText(backupCode)) {
            throw new GoogleCaptchaException("用户标识和备用恢复码不能为空");
        }

        try {
            // 检查是否被锁定
            if (isUserLocked(userId)) {
                log.warn("用户 {} 因验证失败次数过多被锁定", userId);
                throw new GoogleCaptchaException("用户因验证失败次数过多被锁定，请稍后再试");
            }

            Set<String> backupCodes = storage.getBackupCodes(userId);
            if (backupCodes == null || !backupCodes.contains(backupCode)) {
                incrementFailureCount(userId);
                log.warn("用户 {} 备用恢复码 {} 无效", userId, backupCode);
                return false;
            }

            // 验证成功，移除已使用的备用恢复码
            storage.removeBackupCode(userId, backupCode);
            storage.clearFailureCount(userId);
            
            log.info("用户 {} 备用恢复码验证成功", userId);
            return true;

        } catch (GoogleCaptchaException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户 {} 验证备用恢复码时发生异常", userId, e);
            incrementFailureCount(userId);
            throw new GoogleCaptchaException("验证备用恢复码失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isBound(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new GoogleCaptchaException("用户标识不能为空");
        }

        String secretKey = storage.getSecret(userId);
        return StringUtils.hasText(secretKey);
    }

    @Override
    public boolean isBoundByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new GoogleCaptchaException("用户名不能为空");
        }

        try {
            // 这里需要通过用户名查找用户ID
            // 由于这是一个通用的starter，暂时返回false
            // 实际使用时需要在具体的应用中重写这个方法
            // 或者通过配置提供用户查询服务
            log.warn("通过用户名检查绑定状态的方法需要在具体应用中实现，username: {}", username);
            return false;
        } catch (Exception e) {
            log.error("通过用户名检查Google验证码绑定状态失败", e);
            return false;
        }
    }

    @Override
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void clearExpired() {
        try {
            storage.clearExpired();
            log.debug("清理过期的谷歌验证码数据完成");
        } catch (Exception e) {
            log.error("清理过期的谷歌验证码数据失败", e);
        }
    }

    /**
     * 检查用户是否被锁定
     */
    private boolean isUserLocked(String userId) {
        long failureCount = storage.getFailureCount(userId);
        return failureCount >= properties.getSecurity().getMaxFailureCount();
    }

    /**
     * 增加失败次数
     */
    private void incrementFailureCount(String userId) {
        storage.incrementFailureCount(userId, properties.getSecurity().getLockoutDuration());
    }

    /**
     * 生成随机码
     */
    private String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
}
