package com.whoiszxl.captcha.google.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 谷歌验证码生成结果
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCaptchaResult {

    /**
     * 用户标识
     */
    private String userId;

    /**
     * 密钥（Base32编码）
     */
    private String secretKey;

    /**
     * 二维码URL
     */
    private String qrCodeUrl;

    /**
     * 二维码图片（Base64编码）
     */
    private String qrCodeImage;

    /**
     * 备用恢复码
     */
    private String[] backupCodes;

    /**
     * 创建时间戳
     */
    private long timestamp;
}