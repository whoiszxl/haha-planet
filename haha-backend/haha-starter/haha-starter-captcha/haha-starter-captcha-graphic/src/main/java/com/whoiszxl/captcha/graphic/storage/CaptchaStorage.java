package com.whoiszxl.captcha.graphic.storage;

import java.time.Duration;

/**
 * 验证码存储接口
 *
 * @author whoiszxl
 */
public interface CaptchaStorage {

    /**
     * 存储验证码
     *
     * @param key      键
     * @param value    值
     * @param duration 过期时间
     */
    void store(String key, String value, Duration duration);

    /**
     * 获取验证码
     *
     * @param key 键
     * @return 值
     */
    String get(String key);

    /**
     * 删除验证码
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * 检查验证码是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 清除过期验证码
     */
    void clearExpired();
}