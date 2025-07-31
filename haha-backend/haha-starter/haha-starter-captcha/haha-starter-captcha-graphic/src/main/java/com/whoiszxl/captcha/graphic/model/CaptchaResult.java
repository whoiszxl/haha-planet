package com.whoiszxl.captcha.graphic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 验证码结果
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaResult {

    /**
     * 验证码键
     */
    private String key;

    /**
     * 验证码图片Base64编码
     */
    private String imageBase64;

    /**
     * 验证码图片URL（如果使用文件存储）
     */
    private String imageUrl;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}