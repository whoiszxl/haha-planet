package com.whoiszxl.starter.security.password.service;

import java.time.LocalDateTime;

/**
 * 密码审计服务
 *
 * @author whoiszxl
 */
public interface PasswordAuditService {

    /**
     * 记录密码验证成功事件
     *
     * @param userId 用户ID
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     */
    void logPasswordValidationSuccess(String userId, String clientIp, String userAgent);

    /**
     * 记录密码验证失败事件
     *
     * @param userId 用户ID
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @param reason 失败原因
     */
    void logPasswordValidationFailure(String userId, String clientIp, String userAgent, String reason);

    /**
     * 记录密码更改事件
     *
     * @param userId 用户ID
     * @param clientIp 客户端IP
     * @param userAgent 用户代理
     * @param changeType 更改类型（重置、修改等）
     */
    void logPasswordChange(String userId, String clientIp, String userAgent, String changeType);

    /**
     * 记录密码强度验证事件
     *
     * @param userId 用户ID
     * @param strengthScore 强度分数
     * @param violations 违规项
     */
    void logPasswordStrengthValidation(String userId, int strengthScore, String violations);

    /**
     * 审计事件类型
     */
    enum AuditEventType {
        PASSWORD_VALIDATION_SUCCESS,
        PASSWORD_VALIDATION_FAILURE,
        PASSWORD_CHANGE,
        PASSWORD_STRENGTH_VALIDATION
    }

    /**
     * 审计事件
     */
    class AuditEvent {
        private final String userId;
        private final AuditEventType eventType;
        private final String details;
        private final String clientIp;
        private final String userAgent;
        private final LocalDateTime timestamp;

        public AuditEvent(String userId, AuditEventType eventType, String details, 
                         String clientIp, String userAgent, LocalDateTime timestamp) {
            this.userId = userId;
            this.eventType = eventType;
            this.details = details;
            this.clientIp = clientIp;
            this.userAgent = userAgent;
            this.timestamp = timestamp;
        }

        // Getters
        public String getUserId() { return userId; }
        public AuditEventType getEventType() { return eventType; }
        public String getDetails() { return details; }
        public String getClientIp() { return clientIp; }
        public String getUserAgent() { return userAgent; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
}