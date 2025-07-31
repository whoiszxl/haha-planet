package com.whoiszxl.admin.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 操作状态枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum LogStatusEnum implements IBaseEnum<Integer> {

    /**
     * 成功
     */
    SUCCESS(1, "成功"),

    /**
     * 失败
     */
    FAILURE(2, "失败"),;

    private final Integer value;
    private final String description;
}
