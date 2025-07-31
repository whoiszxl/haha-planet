package com.whoiszxl.captcha.graphic.service;

import com.whoiszxl.captcha.graphic.model.CaptchaResult;
import com.whoiszxl.captcha.graphic.model.CaptchaValidateRequest;

/**
 * 图形验证码服务接口
 *
 * @author whoiszxl
 */
public interface GraphicCaptchaService {

    /**
     * 生成验证码
     *
     * @return 验证码结果
     */
    CaptchaResult generate();

    /**
     * 生成验证码
     *
     * @param key 验证码键
     * @return 验证码结果
     */
    CaptchaResult generate(String key);

    /**
     * 验证验证码
     *
     * @param request 验证请求
     * @return 验证结果
     */
    boolean validate(CaptchaValidateRequest request);

    /**
     * 验证验证码
     *
     * @param key   验证码键
     * @param value 验证码值
     * @return 验证结果
     */
    boolean validate(String key, String value);

    /**
     * 删除验证码
     *
     * @param key 验证码键
     */
    void remove(String key);

    /**
     * 清除过期验证码
     */
    void clearExpired();
}