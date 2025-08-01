package com.whoiszxl.captcha.google.storage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 谷歌验证码备用恢复码实体
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "google_captcha_backup_code", indexes = {
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_backup_code", columnList = "backupCode"),
        @Index(name = "idx_expire_time", columnList = "expireTime")
})
public class GoogleCaptchaBackupCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户标识
     */
    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    /**
     * 备用恢复码
     */
    @Column(name = "backup_code", nullable = false, length = 32)
    private String backupCode;

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