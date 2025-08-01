package com.whoiszxl.captcha.google.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.KeyRepresentation;
import com.whoiszxl.captcha.google.properties.GoogleCaptchaProperties;
import com.whoiszxl.captcha.google.service.GoogleCaptchaService;
import com.whoiszxl.captcha.google.service.impl.GoogleCaptchaServiceImpl;
import com.whoiszxl.captcha.google.storage.GoogleCaptchaStorage;
import com.whoiszxl.captcha.google.storage.impl.MemoryGoogleCaptchaStorage;
import com.whoiszxl.captcha.google.storage.impl.MysqlGoogleCaptchaStorage;
import com.whoiszxl.captcha.google.storage.impl.RedisGoogleCaptchaStorage;
import com.whoiszxl.captcha.google.storage.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

/**
 * 谷歌验证码自动配置类
 *
 * @author whoiszxl
 */
@Slf4j
@AutoConfiguration
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties(GoogleCaptchaProperties.class)
@ConditionalOnProperty(prefix = "haha.captcha.google", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GoogleCaptchaAutoConfiguration {

    private final GoogleCaptchaProperties properties;

    /**
     * 谷歌验证器配置
     */
    @Bean
    @ConditionalOnMissingBean
    public GoogleAuthenticatorConfig googleAuthenticatorConfig() {
        return new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(properties.getPeriod()))
                .setWindowSize(properties.getWindowSize())
                .setCodeDigits(properties.getDigits())
                .setKeyRepresentation(KeyRepresentation.BASE32)
                .build();
    }

    /**
     * 谷歌验证器
     */
    @Bean
    @ConditionalOnMissingBean
    public GoogleAuthenticator googleAuthenticator(GoogleAuthenticatorConfig config) {
        log.info("[HaHa Google Captcha] 初始化谷歌验证器");
        return new GoogleAuthenticator(config);
    }

    /**
     * Redis存储
     */
    @Bean
    @ConditionalOnClass(StringRedisTemplate.class)
    @ConditionalOnProperty(prefix = "haha.captcha.google.storage", name = "type", havingValue = "redis")
    public GoogleCaptchaStorage redisGoogleCaptchaStorage(StringRedisTemplate redisTemplate) {
        log.info("[HaHa Google Captcha] 初始化Redis存储");
        return new RedisGoogleCaptchaStorage(redisTemplate, properties);
    }

    /**
     * MySQL存储
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.data.jpa.repository.JpaRepository")
    @ConditionalOnProperty(prefix = "haha.captcha.google.storage", name = "type", havingValue = "mysql")
    public GoogleCaptchaStorage mysqlGoogleCaptchaStorage(
            GoogleCaptchaSecretRepository secretRepository,
            GoogleCaptchaUsedCodeRepository usedCodeRepository,
            GoogleCaptchaBackupCodeRepository backupCodeRepository,
            GoogleCaptchaFailureRepository failureRepository) {
        log.info("[HaHa Google Captcha] 初始化MySQL存储");
        return new MysqlGoogleCaptchaStorage(secretRepository, usedCodeRepository, 
                backupCodeRepository, failureRepository, properties);
    }

    /**
     * 内存存储
     */
    @Bean
    @ConditionalOnMissingBean
    public GoogleCaptchaStorage memoryGoogleCaptchaStorage() {
        log.info("[HaHa Google Captcha] 初始化内存存储");
        return new MemoryGoogleCaptchaStorage();
    }

    /**
     * 谷歌验证码服务
     */
    @Bean
    @ConditionalOnMissingBean
    public GoogleCaptchaService googleCaptchaService(
            GoogleAuthenticator googleAuthenticator,
            GoogleCaptchaStorage storage) {
        log.info("[HaHa Google Captcha] 初始化谷歌验证码服务");
        return new GoogleCaptchaServiceImpl(properties, googleAuthenticator, storage);
    }
}