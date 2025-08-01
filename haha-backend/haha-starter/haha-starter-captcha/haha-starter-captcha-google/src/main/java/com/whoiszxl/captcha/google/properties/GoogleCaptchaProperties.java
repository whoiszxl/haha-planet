package com.whoiszxl.captcha.google.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;

/**
 * 谷歌验证码配置属性
 *
 * @author whoiszxl
 */
@Data
@Validated
@ConfigurationProperties(prefix = "haha.captcha.google")
public class GoogleCaptchaProperties {

    /**
     * 是否启用谷歌验证码
     */
    private boolean enabled = true;

    /**
     * 应用名称（显示在谷歌验证器中）
     */
    @NotBlank(message = "应用名称不能为空")
    private String appName = "HaHa Platform";

    /**
     * 发行者名称
     */
    @NotBlank(message = "发行者名称不能为空")
    private String issuer = "HaHa";

    /**
     * 密钥长度（字节）
     */
    @Min(value = 10, message = "密钥长度不能小于10字节")
    @Max(value = 64, message = "密钥长度不能大于64字节")
    private int secretLength = 20;

    /**
     * 验证码有效期（秒）
     */
    @Min(value = 30, message = "验证码有效期不能小于30秒")
    @Max(value = 300, message = "验证码有效期不能大于300秒")
    private int period = 30;

    /**
     * 验证码位数
     */
    @Min(value = 6, message = "验证码位数不能小于6位")
    @Max(value = 8, message = "验证码位数不能大于8位")
    private int digits = 6;

    /**
     * 时间窗口容差（允许前后几个时间窗口的验证码）
     */
    @Min(value = 0, message = "时间窗口容差不能小于0")
    @Max(value = 5, message = "时间窗口容差不能大于5")
    private int windowSize = 1;

    /**
     * 二维码配置
     */
    private QrCodeConfig qrCode = new QrCodeConfig();

    /**
     * 存储配置
     */
    private StorageConfig storage = new StorageConfig();

    /**
     * 安全配置
     */
    private SecurityConfig security = new SecurityConfig();

    /**
     * 二维码配置
     */
    @Data
    public static class QrCodeConfig {
        /**
         * 二维码宽度
         */
        @Min(value = 100, message = "二维码宽度不能小于100")
        private int width = 300;

        /**
         * 二维码高度
         */
        @Min(value = 100, message = "二维码高度不能小于100")
        private int height = 300;

        /**
         * 二维码格式
         */
        private String format = "PNG";

        /**
         * 二维码边距
         */
        @Min(value = 0, message = "二维码边距不能小于0")
        private int margin = 1;
    }

    /**
     * 存储配置
     */
    @Data
    public static class StorageConfig {
        /**
         * 存储类型
         */
        private StorageType type = StorageType.MEMORY;

        /**
         * Redis键前缀
         */
        @NotBlank(message = "Redis键前缀不能为空")
        private String keyPrefix = "captcha:google:";

        /**
         * 密钥过期时间（用于临时存储）
         */
        @NotNull(message = "密钥过期时间不能为空")
        private Duration keyExpireTime = Duration.ofHours(24);

        /**
         * 数据库表前缀
         */
        private String tablePrefix = "google_captcha_";

        /**
         * 存储类型枚举
         */
        public enum StorageType {
            /**
             * 内存存储
             */
            MEMORY,
            /**
             * Redis存储
             */
            REDIS,
            /**
             * MySQL存储
             */
            MYSQL
        }
    }

    /**
     * 安全配置
     */
    @Data
    public static class SecurityConfig {
        /**
         * 是否启用防重放攻击
         */
        private boolean enableAntiReplay = true;

        /**
         * 验证码使用记录过期时间
         */
        @NotNull(message = "验证码使用记录过期时间不能为空")
        private Duration usedCodeExpireTime = Duration.ofMinutes(5);

        /**
         * 最大验证失败次数
         */
        @Min(value = 1, message = "最大验证失败次数不能小于1")
        private int maxFailureCount = 5;

        /**
         * 验证失败锁定时间
         */
        @NotNull(message = "验证失败锁定时间不能为空")
        private Duration lockoutDuration = Duration.ofMinutes(15);
    }
}