package com.whoiszxl.enums;

import com.whoiszxl.common.constants.UiConstants;
import com.whoiszxl.common.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 认证类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum AuthTypeEnum implements BaseEnum<String> {

    ACCOUNT("ACCOUNT", "账号", UiConstants.COLOR_SUCCESS),
    EMAIL("EMAIL", "邮箱", UiConstants.COLOR_PRIMARY),
    PHONE("PHONE", "手机号", UiConstants.COLOR_PRIMARY),
    SOCIAL("SOCIAL", "第三方账号", UiConstants.COLOR_ERROR);

    private final String value;
    private final String description;
    private final String color;
}
