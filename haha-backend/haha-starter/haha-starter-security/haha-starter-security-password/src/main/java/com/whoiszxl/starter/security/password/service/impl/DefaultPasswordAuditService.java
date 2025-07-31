package com.whoiszxl.starter.security.password.service.impl;

import com.whoiszxl.starter.security.password.config.PasswordEncoderProperties;
import com.whoiszxl.starter.security.password.service.PasswordAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 默认密码审计服务实现
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultPasswordAuditService implements PasswordAuditService {

    private final PasswordEncoderProperties.AuditConfig config;

    @Override
    public void logPasswordValidationSuccess(String userId, String clientIp, String userAgent) {
        if (!config.isLogSuccessfulValidation()) {
            return;
        }
        
        AuditEvent event = new AuditEvent(
                userId,
                AuditEventType.PASSWORD_VALIDATION_SUCCESS,
                "密码验证成功",
                clientIp,
                userAgent,
                LocalDateTime.now()
        );
        
        logAuditEvent(event);
    }

    @Override
    public void logPasswordValidationFailure(String userId, String clientIp, String userAgent, String reason) {
        if (!config.isLogFailedValidation()) {
            return;
        }
        
        AuditEvent event = new AuditEvent(
                userId,
                AuditEventType.PASSWORD_VALIDATION_FAILURE,
                "密码验证失败: " + reason,
                clientIp,
                userAgent,
                LocalDateTime.now()
        );
        
        logAuditEvent(event);
    }

    @Override
    public void logPasswordChange(String userId, String clientIp, String userAgent, String changeType) {
        if (!config.isLogPasswordChange()) {
            return;
        }
        
        AuditEvent event = new AuditEvent(
                userId,
                AuditEventType.PASSWORD_CHANGE,
                "密码更改: " + changeType,
                clientIp,
                userAgent,
                LocalDateTime.now()
        );
        
        logAuditEvent(event);
    }

    @Override
    public void logPasswordStrengthValidation(String userId, int strengthScore, String violations) {
        AuditEvent event = new AuditEvent(
                userId,
                AuditEventType.PASSWORD_STRENGTH_VALIDATION,
                String.format("密码强度验证 - 分数: %d, 违规: %s", strengthScore, violations),
                null,
                null,
                LocalDateTime.now()
        );
        
        logAuditEvent(event);
    }

    private void logAuditEvent(AuditEvent event) {
        String logMessage = String.format(
                "[HaHa Security Audit] 用户: %s, 事件: %s, 详情: %s, IP: %s, 时间: %s",
                event.getUserId(),
                event.getEventType(),
                event.getDetails(),
                event.getClientIp(),
                event.getTimestamp()
        );
        
        switch (config.getLogLevel().toUpperCase()) {
            case "DEBUG" -> log.debug(logMessage);
            case "WARN" -> log.warn(logMessage);
            case "ERROR" -> log.error(logMessage);
            default -> log.info(logMessage);
        }
    }
}