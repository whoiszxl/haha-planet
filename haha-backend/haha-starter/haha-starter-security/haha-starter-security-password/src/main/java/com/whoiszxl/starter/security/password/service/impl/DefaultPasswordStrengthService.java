package com.whoiszxl.starter.security.password.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.whoiszxl.starter.security.password.config.PasswordEncoderProperties;
import com.whoiszxl.starter.security.password.service.PasswordStrengthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.passay.*;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认密码强度验证服务实现
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultPasswordStrengthService implements PasswordStrengthService {

    private final PasswordEncoderProperties.PasswordStrengthConfig config;
    private final PasswordValidator validator;
    private final Set<String> commonPasswords;
    private final SecureRandom secureRandom = new SecureRandom();

    public DefaultPasswordStrengthService(PasswordEncoderProperties.PasswordStrengthConfig config) {
        this.config = config;
        this.validator = createPasswordValidator();
        this.commonPasswords = loadCommonPasswords();
    }

    @Override
    public PasswordStrengthResult validatePassword(String password) {
        return validatePassword(password, null, null);
    }

    @Override
    public PasswordStrengthResult validatePassword(String password, String username, String email) {
        if (CharSequenceUtil.isBlank(password)) {
            return new PasswordStrengthResult(false, 0, 
                List.of("密码不能为空"), 
                List.of("请输入密码"));
        }

        List<String> violations = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        int score = 0;

        // 使用 Passay 进行基础验证
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            violations.addAll(validator.getMessages(result));
        } else {
            score += 60;
        }

        // 检查常见密码
        if (commonPasswords.contains(password.toLowerCase())) {
            violations.add("密码过于常见，请使用更复杂的密码");
            suggestions.add("避免使用常见密码如123456、password等");
        } else {
            score += 10;
        }

        // 检查用户信息相关性
        if (username != null && password.toLowerCase().contains(username.toLowerCase())) {
            violations.add("密码不能包含用户名");
            suggestions.add("密码应与个人信息无关");
        } else {
            score += 10;
        }

        if (email != null && password.toLowerCase().contains(email.split("@")[0].toLowerCase())) {
            violations.add("密码不能包含邮箱前缀");
            suggestions.add("密码应与个人信息无关");
        } else {
            score += 10;
        }

        // 检查禁止的字符序列
        for (String forbidden : config.getForbiddenSequences()) {
            if (password.toLowerCase().contains(forbidden.toLowerCase())) {
                violations.add("密码包含禁止的字符序列: " + forbidden);
                suggestions.add("避免使用连续的字符序列");
                break;
            }
        }

        // 额外的复杂度检查
        score += calculateComplexityScore(password);

        boolean isValid = violations.isEmpty();
        
        if (!isValid) {
            suggestions.add("密码长度至少" + config.getMinLength() + "位");
            if (config.isRequireUppercase()) {
                suggestions.add("包含大写字母");
            }
            if (config.isRequireLowercase()) {
                suggestions.add("包含小写字母");
            }
            if (config.isRequireDigit()) {
                suggestions.add("包含数字");
            }
            if (config.isRequireSpecialChar()) {
                suggestions.add("包含特殊字符");
            }
        }

        log.debug("[HaHa Security] 密码强度验证完成，分数: {}, 有效: {}", score, isValid);
        
        return new PasswordStrengthResult(isValid, Math.min(score, 100), violations, suggestions);
    }

    @Override
    public String generateStrongPassword(int length) {
        if (length < config.getMinLength()) {
            length = config.getMinLength();
        }
        if (length > config.getMaxLength()) {
            length = config.getMaxLength();
        }

        StringBuilder password = new StringBuilder();
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";

        // 确保包含所需的字符类型
        if (config.isRequireUppercase()) {
            password.append(upperCase.charAt(secureRandom.nextInt(upperCase.length())));
        }
        if (config.isRequireLowercase()) {
            password.append(lowerCase.charAt(secureRandom.nextInt(lowerCase.length())));
        }
        if (config.isRequireDigit()) {
            password.append(digits.charAt(secureRandom.nextInt(digits.length())));
        }
        if (config.isRequireSpecialChar()) {
            password.append(specialChars.charAt(secureRandom.nextInt(specialChars.length())));
        }

        // 填充剩余长度
        String allChars = upperCase + lowerCase + digits + specialChars;
        while (password.length() < length) {
            password.append(allChars.charAt(secureRandom.nextInt(allChars.length())));
        }

        // 打乱字符顺序
        return shuffleString(password.toString());
    }

    private PasswordValidator createPasswordValidator() {
        List<Rule> rules = new ArrayList<>();

        // 长度规则
        rules.add(new LengthRule(config.getMinLength(), config.getMaxLength()));

        // 字符类型规则
        if (config.isRequireUppercase()) {
            rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        }
        if (config.isRequireLowercase()) {
            rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        }
        if (config.isRequireDigit()) {
            rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        }
        if (config.isRequireSpecialChar()) {
            rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
        }

        // 禁止空白字符
        rules.add(new WhitespaceRule());

        return new PasswordValidator(rules);
    }

    private Set<String> loadCommonPasswords() {
        try {
            String content = ResourceUtil.readUtf8Str(config.getCommonPasswordsFile());
            return Arrays.stream(content.split("\\n"))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(CharSequenceUtil::isNotBlank)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.warn("[HaHa Security] 无法加载常见密码文件: {}", config.getCommonPasswordsFile(), e);
            return Set.of("password", "123456", "123456789", "qwerty", "abc123", "password123");
        }
    }

    private int calculateComplexityScore(String password) {
        int score = 0;
        
        // 长度奖励
        if (password.length() >= 12) {
            score += 5;
        }
        if (password.length() >= 16) {
            score += 5;
        }
        
        // 字符类型多样性
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        
        int typeCount = (hasUpper ? 1 : 0) + (hasLower ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);
        score += typeCount * 2;
        
        // 字符重复度检查
        long uniqueChars = password.chars().distinct().count();
        double uniqueRatio = (double) uniqueChars / password.length();
        if (uniqueRatio > 0.8) {
            score += 5;
        }
        
        return score;
    }

    private String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}