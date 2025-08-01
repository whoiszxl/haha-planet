package com.whoiszxl.captcha.google.storage.repository;

import com.whoiszxl.captcha.google.storage.entity.GoogleCaptchaFailureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 谷歌验证码失败次数Repository
 *
 * @author whoiszxl
 */
@Repository
public interface GoogleCaptchaFailureRepository extends JpaRepository<GoogleCaptchaFailureEntity, Long> {

    /**
     * 根据用户ID查找有效的失败记录
     */
    @Query("SELECT f FROM GoogleCaptchaFailureEntity f WHERE f.userId = :userId AND f.expireTime > :now")
    Optional<GoogleCaptchaFailureEntity> findValidByUserId(@Param("userId") String userId, @Param("now") LocalDateTime now);

    /**
     * 删除用户的失败记录
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaFailureEntity f WHERE f.userId = :userId")
    void deleteByUserId(@Param("userId") String userId);

    /**
     * 删除过期的失败记录
     */
    @Modifying
    @Query("DELETE FROM GoogleCaptchaFailureEntity f WHERE f.expireTime <= :now")
    int deleteExpired(@Param("now") LocalDateTime now);
}