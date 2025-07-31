package com.whoiszxl.captcha.graphic.config;

import com.whoiszxl.captcha.graphic.properties.GraphicCaptchaProperties;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import com.whoiszxl.captcha.graphic.service.impl.GraphicCaptchaServiceImpl;
import com.whoiszxl.captcha.graphic.storage.CaptchaStorage;
import com.whoiszxl.captcha.graphic.storage.impl.MemoryCaptchaStorage;
import com.whoiszxl.captcha.graphic.storage.impl.RedisCaptchaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 图形验证码自动配置类
 *
 * @author whoiszxl
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(GraphicCaptchaProperties.class)
@ConditionalOnProperty(prefix = "haha.captcha.graphic", name = "enabled", havingValue = "true", matchIfMissing = true)
public class GraphicCaptchaAutoConfiguration {

    private final GraphicCaptchaProperties properties;

    /**
     * Redis验证码存储
     */
    @Bean
    @ConditionalOnClass(StringRedisTemplate.class)
    @ConditionalOnProperty(prefix = "haha.captcha.graphic.storage", name = "type", havingValue = "redis")
    public CaptchaStorage redisCaptchaStorage(StringRedisTemplate redisTemplate) {
        log.info("[HaHa Graphic Captcha] 初始化Redis验证码存储");
        return new RedisCaptchaStorage(redisTemplate, properties);
    }

    /**
     * 内存验证码存储
     */
    @Bean
    @ConditionalOnMissingBean
    public CaptchaStorage memoryCaptchaStorage() {
        log.info("[HaHa Graphic Captcha] 初始化内存验证码存储");
        return new MemoryCaptchaStorage();
    }

    /**
     * 图形验证码服务
     */
    @Bean
    @ConditionalOnMissingBean
    public GraphicCaptchaService graphicCaptchaService(CaptchaStorage captchaStorage) {
        log.info("[HaHa Graphic Captcha] 初始化图形验证码服务");
        return new GraphicCaptchaServiceImpl(properties, captchaStorage);
    }
}