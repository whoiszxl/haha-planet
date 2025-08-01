package com.whoiszxl.captcha.google.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 谷歌验证码密钥实体
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "google_captcha_secret", indexes = {
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_expire_time", columnList = "expireTime")
})
public class GoogleCaptchaSecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户标识
     */
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    /**
     * 密钥（Base32编码）
     */
    @Column(name = "secret_key", nullable = false, length = 128)
    private String secretKey;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 过期时间
     */
    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}