package com.whoiszxl.admin.enums;

import com.whoiszxl.common.constants.UiConstants;
import com.whoiszxl.common.enums.BaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum MessageTypeEnum implements BaseEnum<Integer> {

    /**
     * 安全消息
     */
    SECURITY(1, "安全消息", UiConstants.COLOR_PRIMARY),;

    private final Integer value;
    private final String description;
    private final String color;
}
