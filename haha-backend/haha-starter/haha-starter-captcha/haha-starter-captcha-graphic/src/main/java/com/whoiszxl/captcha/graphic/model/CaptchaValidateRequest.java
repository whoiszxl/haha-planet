package com.whoiszxl.captcha.graphic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 验证码验证请求
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaValidateRequest {

    /**
     * 验证码键
     */
    @NotBlank(message = "验证码键不能为空")
    private String key;

    /**
     * 验证码值
     */
    @NotBlank(message = "验证码值不能为空")
    private String value;

    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase = true;

    /**
     * 验证后是否删除
     */
    private boolean removeAfterValidate = true;
}