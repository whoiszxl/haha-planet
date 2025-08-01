package com.whoiszxl.captcha.google.storage;

import java.time.Duration;
import java.util.Set;

/**
 * 谷歌验证码存储接口
 *
 * @author whoiszxl
 */
public interface GoogleCaptchaStorage {

    /**
     * 存储用户密钥
     *
     * @param userId    用户标识
     * @param secretKey 密钥
     * @param ttl       过期时间
     */
    void storeSecret(String userId, String secretKey, Duration ttl);

    /**
     * 获取用户密钥
     *
     * @param userId 用户标识
     * @return 密钥
     */
    String getSecret(String userId);

    /**
     * 删除用户密钥
     *
     * @param userId 用户标识
     */
    void removeSecret(String userId);

    /**
     * 存储已使用的验证码（防重放）
     *
     * @param userId 用户标识
     * @param code   验证码
     * @param ttl    过期时间
     */
    void storeUsedCode(String userId, String code, Duration ttl);

    /**
     * 检查验证码是否已使用
     *
     * @param userId 用户标识
     * @param code   验证码
     * @return 是否已使用
     */
    boolean isCodeUsed(String userId, String code);

    /**
     * 存储备用恢复码
     *
     * @param userId      用户标识
     * @param backupCodes 备用恢复码集合
     * @param ttl         过期时间
     */
    void storeBackupCodes(String userId, Set<String> backupCodes, Duration ttl);

    /**
     * 获取备用恢复码
     *
     * @param userId 用户标识
     * @return 备用恢复码集合
     */
    Set<String> getBackupCodes(String userId);

    /**
     * 移除已使用的备用恢复码
     *
     * @param userId     用户标识
     * @param backupCode 备用恢复码
     */
    void removeBackupCode(String userId, String backupCode);

    /**
     * 记录验证失败次数
     *
     * @param userId 用户标识
     * @param ttl    过期时间
     * @return 当前失败次数
     */
    long incrementFailureCount(String userId, Duration ttl);

    /**
     * 获取验证失败次数
     *
     * @param userId 用户标识
     * @return 失败次数
     */
    long getFailureCount(String userId);

    /**
     * 清除验证失败次数
     *
     * @param userId 用户标识
     */
    void clearFailureCount(String userId);

    /**
     * 清理过期数据
     */
    void clearExpired();
}