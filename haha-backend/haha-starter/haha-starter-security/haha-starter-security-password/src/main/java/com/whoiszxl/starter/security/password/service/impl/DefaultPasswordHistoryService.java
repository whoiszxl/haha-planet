package com.whoiszxl.starter.security.password.service.impl;

import com.whoiszxl.starter.security.password.config.PasswordEncoderProperties;
import com.whoiszxl.starter.security.password.service.PasswordHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 默认密码历史记录服务实现（内存存储）
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultPasswordHistoryService implements PasswordHistoryService {

    private final PasswordEncoderProperties.PasswordHistoryConfig config;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, List<PasswordHistoryEntry>> historyStore = new ConcurrentHashMap<>();

    public DefaultPasswordHistoryService(PasswordEncoderProperties.PasswordHistoryConfig config) {
        this.config = config;
        this.passwordEncoder = null;
    }

    @Override
    public boolean isPasswordInHistory(String userId, String newPassword) {
        List<PasswordHistoryEntry> history = historyStore.get(userId);
        if (history == null || history.isEmpty()) {
            return false;
        }

        // 检查新密码是否与历史密码匹配
        for (PasswordHistoryEntry entry : history) {
            if (passwordEncoder != null && passwordEncoder.matches(newPassword, entry.getEncodedPassword())) {
                log.debug("[HaHa Security] 密码在用户 {} 的历史记录中找到", userId);
                return true;
            }
        }

        return false;
    }

    @Override
    public void addPasswordToHistory(String userId, String encodedPassword) {
        List<PasswordHistoryEntry> history = historyStore.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());
        
        // 添加新的密码记录
        PasswordHistoryEntry newEntry = new PasswordHistoryEntry(userId, encodedPassword, LocalDateTime.now());
        history.add(0, newEntry);
        
        // 保持历史记录数量限制
        while (history.size() > config.getHistorySize()) {
            history.remove(history.size() - 1);
        }
        
        log.debug("[HaHa Security] 为用户 {} 添加密码历史记录，当前记录数: {}", userId, history.size());
    }

    @Override
    public List<PasswordHistoryEntry> getPasswordHistory(String userId) {
        List<PasswordHistoryEntry> history = historyStore.get(userId);
        return history != null ? List.copyOf(history) : List.of();
    }

    @Override
    public void cleanupExpiredHistory() {
        LocalDateTime cutoffTime = LocalDateTime.now().minus(config.getRetentionPeriod());
        
        historyStore.forEach((userId, history) -> {
            List<PasswordHistoryEntry> validEntries = history.stream()
                    .filter(entry -> entry.getCreatedAt().isAfter(cutoffTime))
                    .collect(Collectors.toList());
            
            if (validEntries.size() != history.size()) {
                history.clear();
                history.addAll(validEntries);
                log.debug("[HaHa Security] 清理用户 {} 的过期密码历史记录", userId);
            }
        });
        
        log.info("[HaHa Security] 密码历史记录清理完成");
    }

    @Override
    public void deleteUserHistory(String userId) {
        historyStore.remove(userId);
        log.debug("[HaHa Security] 删除用户 {} 的所有密码历史记录", userId);
    }
}