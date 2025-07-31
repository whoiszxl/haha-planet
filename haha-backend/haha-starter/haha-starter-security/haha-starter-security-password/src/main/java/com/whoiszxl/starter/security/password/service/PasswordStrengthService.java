package com.whoiszxl.starter.security.password.service;

import java.util.List;

/**
 * 密码强度验证服务
 *
 * @author whoiszxl
 */
public interface PasswordStrengthService {

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 验证结果
     */
    PasswordStrengthResult validatePassword(String password);

    /**
     * 验证密码强度（带用户信息）
     *
     * @param password 密码
     * @param username 用户名
     * @param email 邮箱
     * @return 验证结果
     */
    PasswordStrengthResult validatePassword(String password, String username, String email);

    /**
     * 生成强密码建议
     *
     * @param length 密码长度
     * @return 生成的强密码
     */
    String generateStrongPassword(int length);

    /**
     * 密码强度验证结果
     */
    class PasswordStrengthResult {
        private final boolean valid;
        private final int score;
        private final List<String> violations;
        private final List<String> suggestions;

        public PasswordStrengthResult(boolean valid, int score, List<String> violations, List<String> suggestions) {
            this.valid = valid;
            this.score = score;
            this.violations = violations;
            this.suggestions = suggestions;
        }

        public boolean isValid() {
            return valid;
        }

        public int getScore() {
            return score;
        }

        public List<String> getViolations() {
            return violations;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }
    }
}