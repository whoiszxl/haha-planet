package com.whoiszxl.captcha.google.service;

import com.whoiszxl.captcha.google.model.GoogleCaptchaResult;
import com.whoiszxl.captcha.google.model.GoogleCaptchaValidateRequest;

/**
 * 谷歌验证码服务接口
 *
 * @author whoiszxl
 */
public interface GoogleCaptchaService {

    /**
     * 为用户生成谷歌验证码密钥和二维码
     *
     * @param userId 用户标识
     * @return 验证码结果
     */
    GoogleCaptchaResult generateSecret(String userId);

    /**
     * 为用户生成谷歌验证码密钥和二维码（带用户名）
     *
     * @param userId   用户标识
     * @param username 用户名
     * @return 验证码结果
     */
    GoogleCaptchaResult generateSecret(String userId, String username);

    /**
     * 验证谷歌验证码
     *
     * @param request 验证请求
     * @return 验证结果
     */
    boolean validate(GoogleCaptchaValidateRequest request);

    /**
     * 验证谷歌验证码（简化版本）
     *
     * @param userId 用户标识
     * @param code   验证码
     * @return 验证结果
     */
    boolean validate(String userId, String code);

    /**
     * 获取用户的密钥
     *
     * @param userId 用户标识
     * @return 密钥（Base32编码）
     */
    String getSecret(String userId);

    /**
     * 删除用户的密钥
     *
     * @param userId 用户标识
     */
    void removeSecret(String userId);

    /**
     * 生成备用恢复码
     *
     * @param userId 用户标识
     * @param count  生成数量
     * @return 恢复码数组
     */
    String[] generateBackupCodes(String userId, int count);

    /**
     * 验证备用恢复码
     *
     * @param userId     用户标识
     * @param backupCode 备用恢复码
     * @return 验证结果
     */
    boolean validateBackupCode(String userId, String backupCode);

    /**
     * 检查用户是否已绑定谷歌验证器
     *
     * @param userId 用户标识
     * @return 是否已绑定
     */
    boolean isBound(String userId);

    /**
     * 通过用户名检查用户是否已绑定谷歌验证器
     *
     * @param username 用户名
     * @return 是否已绑定
     */
    boolean isBoundByUsername(String username);

    /**
     * 清理过期数据
     */
    void clearExpired();
}