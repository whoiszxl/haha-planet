package com.whoiszxl.starter.security.password.config;

import com.whoiszxl.starter.security.password.service.PasswordStrengthService;
import com.whoiszxl.starter.security.password.service.impl.DefaultPasswordStrengthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 密码强度验证配置
 *
 * @author whoiszxl
 */
@Configuration
@RequiredArgsConstructor
public class PasswordStrengthConfiguration {

    private final PasswordEncoderProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "haha.security.password.strength", name = "enabled", havingValue = "true", matchIfMissing = true)
    public PasswordStrengthService passwordStrengthService() {
        return new DefaultPasswordStrengthService(properties.getStrength());
    }
}