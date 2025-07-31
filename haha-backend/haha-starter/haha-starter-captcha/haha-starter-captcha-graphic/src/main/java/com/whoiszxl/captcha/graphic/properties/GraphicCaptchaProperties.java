package com.whoiszxl.captcha.graphic.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;

/**
 * 图形验证码配置属性
 *
 * @author whoiszxl
 */
@Data
@Validated
@ConfigurationProperties(prefix = "haha.captcha.graphic")
public class GraphicCaptchaProperties {

    /**
     * 是否启用图形验证码
     */
    private boolean enabled = true;

    /**
     * 验证码类型
     */
    private CaptchaType type = CaptchaType.ARITHMETIC;

    /**
     * 验证码长度
     */
    @Min(value = 4, message = "验证码长度不能小于4")
    @Max(value = 8, message = "验证码长度不能大于8")
    private int length = 4;

    /**
     * 验证码宽度
     */
    @Min(value = 80, message = "验证码宽度不能小于80")
    private int width = 130;

    /**
     * 验证码高度
     */
    @Min(value = 30, message = "验证码高度不能小于30")
    private int height = 48;

    /**
     * 验证码过期时间
     */
    @NotNull(message = "验证码过期时间不能为空")
    private Duration expireTime = Duration.ofMinutes(5);

    /**
     * 验证码字体名称
     */
    private String fontName = "Arial";

    /**
     * 验证码字体大小
     */
    @Min(value = 12, message = "字体大小不能小于12")
    private int fontSize = 25;

    /**
     * 干扰线数量
     */
    @Min(value = 0, message = "干扰线数量不能小于0")
    @Max(value = 10, message = "干扰线数量不能大于10")
    private int lineCount = 3;

    /**
     * 存储配置
     */
    private StorageConfig storage = new StorageConfig();

    /**
     * 验证码类型枚举
     */
    public enum CaptchaType {
        /**
         * 算术验证码
         */
        ARITHMETIC,
        /**
         * 中文验证码
         */
        CHINESE,
        /**
         * 英文验证码
         */
        ENGLISH,
        /**
         * 数字验证码
         */
        NUMBER,
        /**
         * 混合验证码
         */
        MIXED
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
         * Redis 键前缀
         */
        @NotBlank(message = "Redis键前缀不能为空")
        private String keyPrefix = "captcha:graphic:";

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
            REDIS
        }
    }
}