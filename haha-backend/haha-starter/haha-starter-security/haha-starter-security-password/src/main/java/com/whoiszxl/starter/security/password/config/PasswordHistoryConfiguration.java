package com.whoiszxl.starter.security.password.config;

import com.whoiszxl.starter.security.password.service.PasswordHistoryService;
import com.whoiszxl.starter.security.password.service.impl.DefaultPasswordHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 密码历史记录配置
 *
 * @author whoiszxl
 */
@Configuration
@RequiredArgsConstructor
public class PasswordHistoryConfiguration {

    private final PasswordEncoderProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "haha.security.password.history", name = "enabled", havingValue = "true")
    public PasswordHistoryService passwordHistoryService() {
        return new DefaultPasswordHistoryService(properties.getHistory());
    }
}