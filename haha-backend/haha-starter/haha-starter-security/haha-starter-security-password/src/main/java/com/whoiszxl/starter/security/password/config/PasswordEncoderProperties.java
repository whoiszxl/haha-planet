package com.whoiszxl.starter.security.password.config;

import com.whoiszxl.starter.core.constants.PropertiesConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.List;

/**
 * 密码编解码配置属性
 *
 * @author whoiszxl
 */
@Setter
@Getter
@ConfigurationProperties(PropertiesConstants.SECURITY_PASSWORD)
public class PasswordEncoderProperties {

    /**
     * 是否启用密码编解码配置
     */
    private boolean enabled = false;

    /**
     * 默认启用的编码器 ID
     */
    private String encodingId = "bcrypt";

    /**
     * BCrypt 配置
     */
    @NestedConfigurationProperty
    private BcryptConfig bcrypt = new BcryptConfig();

    /**
     * Argon2 配置
     */
    @NestedConfigurationProperty
    private Argon2Config argon2 = new Argon2Config();

    /**
     * SCrypt 配置
     */
    @NestedConfigurationProperty
    private ScryptConfig scrypt = new ScryptConfig();

    /**
     * PBKDF2 配置
     */
    @NestedConfigurationProperty
    private Pbkdf2Config pbkdf2 = new Pbkdf2Config();

    /**
     * 密码强度验证配置
     */
    @NestedConfigurationProperty
    private PasswordStrengthConfig strength = new PasswordStrengthConfig();

    /**
     * 密码历史记录配置
     */
    @NestedConfigurationProperty
    private PasswordHistoryConfig history = new PasswordHistoryConfig();

    /**
     * 密码过期配置
     */
    @NestedConfigurationProperty
    private PasswordExpirationConfig expiration = new PasswordExpirationConfig();

    /**
     * 审计日志配置
     */
    @NestedConfigurationProperty
    private AuditConfig audit = new AuditConfig();

    /**
     * BCrypt 配置
     */
    @Setter
    @Getter
    public static class BcryptConfig {
        /**
         * 强度（4-31）
         */
        private int strength = 12;
    }

    /**
     * Argon2 配置
     */
    @Setter
    @Getter
    public static class Argon2Config {
        /**
         * 盐长度
         */
        private int saltLength = 16;
        /**
         * 哈希长度
         */
        private int hashLength = 32;
        /**
         * 并行度
         */
        private int parallelism = 1;
        /**
         * 内存使用量（KB）
         */
        private int memory = 4096;
        /**
         * 迭代次数
         */
        private int iterations = 3;
    }

    /**
     * SCrypt 配置
     */
    @Setter
    @Getter
    public static class ScryptConfig {
        /**
         * CPU 成本参数
         */
        private int cpuCost = 16384;
        /**
         * 内存成本参数
         */
        private int memoryCost = 8;
        /**
         * 并行化参数
         */
        private int parallelization = 1;
        /**
         * 密钥长度
         */
        private int keyLength = 32;
        /**
         * 盐长度
         */
        private int saltLength = 64;
    }

    /**
     * PBKDF2 配置
     */
    @Setter
    @Getter
    public static class Pbkdf2Config {
        /**
         * 算法
         */
        private String algorithm = "PBKDF2WithHmacSHA256";
        /**
         * 迭代次数
         */
        private int iterations = 310000;
        /**
         * 盐长度
         */
        private int saltLength = 16;
        /**
         * 哈希长度
         */
        private int hashLength = 32;
    }

    /**
     * 密码强度验证配置
     */
    @Setter
    @Getter
    public static class PasswordStrengthConfig {
        /**
         * 是否启用密码强度验证
         */
        private boolean enabled = true;
        /**
         * 最小长度
         */
        private int minLength = 8;
        /**
         * 最大长度
         */
        private int maxLength = 128;
        /**
         * 是否要求大写字母
         */
        private boolean requireUppercase = true;
        /**
         * 是否要求小写字母
         */
        private boolean requireLowercase = true;
        /**
         * 是否要求数字
         */
        private boolean requireDigit = true;
        /**
         * 是否要求特殊字符
         */
        private boolean requireSpecialChar = true;
        /**
         * 禁止的字符序列
         */
        private List<String> forbiddenSequences = List.of("123456", "abcdef", "qwerty");
        /**
         * 常见密码黑名单文件路径
         */
        private String commonPasswordsFile = "classpath:common-passwords.txt";
    }

    /**
     * 密码历史记录配置
     */
    @Setter
    @Getter
    public static class PasswordHistoryConfig {
        /**
         * 是否启用密码历史记录
         */
        private boolean enabled = false;
        /**
         * 保留历史密码数量
         */
        private int historySize = 5;
        /**
         * 历史记录保留时间
         */
        private Duration retentionPeriod = Duration.ofDays(365);
    }

    /**
     * 密码过期配置
     */
    @Setter
    @Getter
    public static class PasswordExpirationConfig {
        /**
         * 是否启用密码过期
         */
        private boolean enabled = false;
        /**
         * 密码有效期
         */
        private Duration validityPeriod = Duration.ofDays(90);
        /**
         * 过期前提醒天数
         */
        private int warningDays = 7;
    }

    /**
     * 审计日志配置
     */
    @Setter
    @Getter
    public static class AuditConfig {
        /**
         * 是否启用审计日志
         */
        private boolean enabled = true;
        /**
         * 是否记录密码验证成功事件
         */
        private boolean logSuccessfulValidation = false;
        /**
         * 是否记录密码验证失败事件
         */
        private boolean logFailedValidation = true;
        /**
         * 是否记录密码更改事件
         */
        private boolean logPasswordChange = true;
        /**
         * 日志级别
         */
        private String logLevel = "INFO";
    }
}