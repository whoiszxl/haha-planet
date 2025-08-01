package com.whoiszxl.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 验证码配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    /**
     * 图形验证码过期时间
     */
    @Value("${captcha.graphic.expirationInMinutes}")
    private long expirationInMinutes;

    /**
     * 邮箱验证码配置
     */
    private CaptchaMail mail;

    /**
     * 短信验证码配置
     */
    private CaptchaSms sms;

    /**
     * 邮箱验证码配置
     */
    @Data
    public static class CaptchaMail {
        /**
         * 内容长度
         */
        private int length;

        /**
         * 过期时间
         */
        private long expirationInMinutes;

        /**
         * 限制时间
         */
        private long limitInSeconds;

        /**
         * 模板路径
         */
        private String templatePath;
    }

    /**
     * 短信验证码配置
     */
    @Data
    public static class CaptchaSms {
        /**
         * 内容长度
         */
        private int length;

        /**
         * 过期时间
         */
        private long expirationInMinutes;

        /**
         * 模板 ID
         */
        private String templateId;
    }
}
