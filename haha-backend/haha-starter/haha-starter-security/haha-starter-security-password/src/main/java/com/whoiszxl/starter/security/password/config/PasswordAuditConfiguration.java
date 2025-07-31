package com.whoiszxl.starter.security.password.config;

import com.whoiszxl.starter.security.password.service.PasswordAuditService;
import com.whoiszxl.starter.security.password.service.impl.DefaultPasswordAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 密码审计配置
 *
 * @author whoiszxl
 */
@Configuration
@RequiredArgsConstructor
public class PasswordAuditConfiguration {

    private final PasswordEncoderProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "haha.security.password.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
    public PasswordAuditService passwordAuditService() {
        return new DefaultPasswordAuditService(properties.getAudit());
    }
}