package com.whoiszxl.captcha.google.storage.repository;

import com.whoiszxl.captcha.google.storage.entity.GoogleCaptchaUsedCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 谷歌验证码已使用验证码Repository
 *
 * @author whoiszxl
 */
@Repository
public interface GoogleCaptchaUsedCodeRepository extends JpaRepository<GoogleCaptchaUsedCodeEntity, Long> {

    /**
     * 检查验证码是否已使用
     */
    @Query("SELECT COUNT(u) > 0 FROM GoogleCaptchaUsedCodeEntity u WHERE u.userId = :userId AND u.code = :code AND u.expireTime > :now")
    boolean existsByUserIdAndCodeAndNotExpired(@Param("userId") String userId, @Param("code") String code, @Param("now") LocalDateTime now);

    /**
     * 删除过期的已使用验证码
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaUsedCodeEntity u WHERE u.expireTime <= :now")
    int deleteExpired(@Param("now") LocalDateTime now);
}