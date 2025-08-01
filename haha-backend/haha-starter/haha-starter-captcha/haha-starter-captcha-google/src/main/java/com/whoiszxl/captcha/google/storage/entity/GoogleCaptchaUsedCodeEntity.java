package com.whoiszxl.captcha.google.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 谷歌验证码已使用验证码实体
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "google_captcha_used_code", indexes = {
        @Index(name = "idx_user_code", columnList = "userId,code", unique = true),
        @Index(name = "idx_expire_time", columnList = "expireTime")
})
public class GoogleCaptchaUsedCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户标识
     */
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    /**
     * 验证码
     */
    @Column(name = "code", nullable = false, length = 16)
    private String code;

    /**
     * 使用时间
     */
    @Column(name = "use_time", nullable = false)
    private LocalDateTime useTime;

    /**
     * 过期时间
     */
    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    @PrePersist
    public void prePersist() {
        if (useTime == null) {
            useTime = LocalDateTime.now();
        }
    }
}