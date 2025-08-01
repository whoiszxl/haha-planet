package com.whoiszxl.captcha.google.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 谷歌验证码验证请求
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCaptchaValidateRequest {

    /**
     * 用户标识
     */
    @NotBlank(message = "用户标识不能为空")
    private String userId;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6,8}$", message = "验证码格式不正确")
    private String code;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 用户代理
     */
    private String userAgent;
}