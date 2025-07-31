package com.whoiszxl.messaging.mail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件配置属性
 *
 * @author whoiszxl
 */
@Data
@Validated
@ConfigurationProperties(prefix = "haha.messaging.mail")
public class MailProperties {

    /**
     * 是否启用邮件功能
     */
    private boolean enabled = true;

    /**
     * 默认发件人
     */
    @Valid
    private Sender defaultSender = new Sender();

    /**
     * 模板配置
     */
    @Valid
    private Template template = new Template();

    /**
     * 异步配置
     */
    @Valid
    private Async async = new Async();

    /**
     * 重试配置
     */
    @Valid
    private Retry retry = new Retry();

    /**
     * 限流配置
     */
    @Valid
    private RateLimit rateLimit = new RateLimit();

    /**
     * 发件人配置
     */
    @Data
    public static class Sender {
        /**
         * 发件人邮箱
         */
        @Email(message = "发件人邮箱格式不正确")
        private String email;

        /**
         * 发件人姓名
         */
        private String name;

        /**
         * 回复邮箱
         */
        @Email(message = "回复邮箱格式不正确")
        private String replyTo;
    }

    /**
     * 模板配置
     */
    @Data
    public static class Template {
        /**
         * 是否启用模板
         */
        private boolean enabled = true;

        /**
         * 模板引擎类型
         */
        private EngineType engine = EngineType.THYMELEAF;

        /**
         * 模板路径前缀
         */
        @NotBlank(message = "模板路径前缀不能为空")
        private String prefix = "classpath:/templates/mail/";

        /**
         * 模板文件后缀
         */
        @NotBlank(message = "模板文件后缀不能为空")
        private String suffix = ".html";

        /**
         * 模板缓存
         */
        private boolean cache = true;

        /**
         * 模板引擎类型
         */
        public enum EngineType {
            THYMELEAF, FREEMARKER, VELOCITY
        }
    }

    /**
     * 异步配置
     */
    @Data
    public static class Async {
        /**
         * 是否启用异步发送
         */
        private boolean enabled = true;

        /**
         * 核心线程数
         */
        @Min(value = 1, message = "核心线程数不能小于1")
        private int corePoolSize = 2;

        /**
         * 最大线程数
         */
        @Min(value = 1, message = "最大线程数不能小于1")
        private int maxPoolSize = 10;

        /**
         * 队列容量
         */
        @Min(value = 1, message = "队列容量不能小于1")
        private int queueCapacity = 100;

        /**
         * 线程名前缀
         */
        @NotBlank(message = "线程名前缀不能为空")
        private String threadNamePrefix = "mail-async-";

        /**
         * 线程空闲时间
         */
        private Duration keepAliveTime = Duration.ofSeconds(60);
    }

    /**
     * 重试配置
     */
    @Data
    public static class Retry {
        /**
         * 是否启用重试
         */
        private boolean enabled = true;

        /**
         * 最大重试次数
         */
        @Min(value = 0, message = "最大重试次数不能小于0")
        @Max(value = 10, message = "最大重试次数不能大于10")
        private int maxAttempts = 3;

        /**
         * 重试间隔
         */
        private Duration delay = Duration.ofSeconds(2);

        /**
         * 重试间隔倍数
         */
        @DecimalMin(value = "1.0", message = "重试间隔倍数不能小于1.0")
        private double multiplier = 2.0;

        /**
         * 最大重试间隔
         */
        private Duration maxDelay = Duration.ofMinutes(5);
    }

    /**
     * 限流配置
     */
    @Data
    public static class RateLimit {
        /**
         * 是否启用限流
         */
        private boolean enabled = false;

        /**
         * 每分钟最大发送数量
         */
        @Min(value = 1, message = "每分钟最大发送数量不能小于1")
        private int maxPerMinute = 60;

        /**
         * 每小时最大发送数量
         */
        @Min(value = 1, message = "每小时最大发送数量不能小于1")
        private int maxPerHour = 1000;

        /**
         * 每天最大发送数量
         */
        @Min(value = 1, message = "每天最大发送数量不能小于1")
        private int maxPerDay = 10000;
    }
}