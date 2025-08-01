package com.whoiszxl.captcha.google.storage.repository;

import com.whoiszxl.captcha.google.storage.entity.GoogleCaptchaSecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 谷歌验证码密钥Repository
 *
 * @author whoiszxl
 */
@Repository
public interface GoogleCaptchaSecretRepository extends JpaRepository<GoogleCaptchaSecretEntity, Long> {

    /**
     * 根据用户ID查找有效的密钥
     */
    @Query("SELECT s FROM GoogleCaptchaSecretEntity s WHERE s.userId = :userId AND s.expireTime > :now")
    Optional<GoogleCaptchaSecretEntity> findValidByUserId(@Param("userId") String userId, @Param("now") LocalDateTime now);

    /**
     * 删除用户的所有密钥
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaSecretEntity s WHERE s.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    /**
     * 删除过期的密钥
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaSecretEntity s WHERE s.expireTime <= :now")
    int deleteExpired(@Param("now") LocalDateTime now);
}