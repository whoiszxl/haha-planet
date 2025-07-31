package com.whoiszxl.common.enums;

import com.whoiszxl.common.constants.UiConstants;
import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 启用/禁用状态枚举
 */
@Getter
@RequiredArgsConstructor
public enum DisEnableStatusEnum implements IBaseEnum<Integer> {

    ENABLE(1, "启用", UiConstants.COLOR_SUCCESS),
    DISABLE(2, "禁用", UiConstants.COLOR_ERROR),;

    private final Integer value;
    private final String description;
    private final String color;
}
