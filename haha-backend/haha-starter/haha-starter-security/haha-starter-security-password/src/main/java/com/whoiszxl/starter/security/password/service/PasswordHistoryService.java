package com.whoiszxl.starter.security.password.service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 密码历史记录服务
 *
 * @author whoiszxl
 */
public interface PasswordHistoryService {

    /**
     * 检查密码是否在历史记录中
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否在历史记录中
     */
    boolean isPasswordInHistory(String userId, String newPassword);

    /**
     * 添加密码到历史记录
     *
     * @param userId 用户ID
     * @param encodedPassword 编码后的密码
     */
    void addPasswordToHistory(String userId, String encodedPassword);

    /**
     * 获取用户密码历史记录
     *
     * @param userId 用户ID
     * @return 密码历史记录列表
     */
    List<PasswordHistoryEntry> getPasswordHistory(String userId);

    /**
     * 清理过期的密码历史记录
     */
    void cleanupExpiredHistory();

    /**
     * 删除用户的所有密码历史记录
     *
     * @param userId 用户ID
     */
    void deleteUserHistory(String userId);

    /**
     * 密码历史记录条目
     */
    class PasswordHistoryEntry {
        private final String userId;
        private final String encodedPassword;
        private final LocalDateTime createdAt;

        public PasswordHistoryEntry(String userId, String encodedPassword, LocalDateTime createdAt) {
            this.userId = userId;
            this.encodedPassword = encodedPassword;
            this.createdAt = createdAt;
        }

        public String getUserId() {
            return userId;
        }

        public String getEncodedPassword() {
            return encodedPassword;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}