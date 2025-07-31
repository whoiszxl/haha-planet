package com.whoiszxl.admin.generator.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 表单类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum FormTypeEnum implements IBaseEnum<Integer> {

    /**
     * 文本框
     */
    TEXT(1, "文本框"),
    /**
     * 文本域
     */
    TEXT_AREA(2, "文本域"),
    /**
     * 下拉框
     */
    SELECT(3, "下拉框"),
    /**
     * 单选框
     */
    RADIO(4, "单选框"),
    /**
     * 日期框
     */
    DATE(5, "日期框"),
    /**
     * 日期时间框
     */
    DATE_TIME(6, "日期时间框"),;

    private final Integer value;
    private final String description;
}
