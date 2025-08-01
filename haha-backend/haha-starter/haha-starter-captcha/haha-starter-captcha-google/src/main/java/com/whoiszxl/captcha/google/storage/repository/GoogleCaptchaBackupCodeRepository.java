package com.whoiszxl.captcha.google.storage.repository;

import com.whoiszxl.captcha.google.storage.entity.GoogleCaptchaBackupCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 谷歌验证码备用恢复码Repository
 *
 * @author whoiszxl
 */
@Repository
public interface GoogleCaptchaBackupCodeRepository extends JpaRepository<GoogleCaptchaBackupCodeEntity, Long> {

    /**
     * 根据用户ID查找有效的备用恢复码
     */
    @Query("SELECT b FROM GoogleCaptchaBackupCodeEntity b WHERE b.userId = :userId AND b.expireTime > :now")
    List<GoogleCaptchaBackupCodeEntity> findValidByUserId(@Param("userId") String userId, @Param("now") LocalDateTime now);

    /**
     * 删除指定的备用恢复码
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaBackupCodeEntity b WHERE b.userId = :userId AND b.backupCode = :backupCode")
    void deleteByUserIdAndBackupCode(@Param("userId") String userId, @Param("backupCode") String backupCode);

    /**
     * 删除用户的所有备用恢复码
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaBackupCodeEntity b WHERE b.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    /**
     * 删除过期的备用恢复码
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaBackupCodeEntity b WHERE b.expireTime <= :now")
    int deleteExpired(@Param("now") LocalDateTime now);
}