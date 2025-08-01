package com.whoiszxl.captcha.google.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 谷歌验证码失败次数实体
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "google_captcha_failure", indexes = {
        @Index(name = "idx_user_id", columnList = "userId", unique = true),
        @Index(name = "idx_expire_time", columnList = "expireTime")
})
public class GoogleCaptchaFailureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户标识
     */
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    /**
     * 失败次数
     */
    @Column(name = "failure_count", nullable = false)
    private Long failureCount;

    /**
     * 最后失败时间
     */
    @Column(name = "last_failure_time", nullable = false)
    private LocalDateTime lastFailureTime;

    /**
     * 过期时间
     */
    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    @PrePersist
    public void prePersist() {
        if (lastFailureTime == null) {
            lastFailureTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        lastFailureTime = LocalDateTime.now();
    }
}