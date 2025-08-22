package com.whoiszxl.planet.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通用是否枚举
 * 用于匿名、置顶、精华、原创等字段
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum YesNoEnum implements IBaseEnum<Integer> {

    /**
     * 否
     */
    NO(0, "否", "#8c8c8c"),

    /**
     * 是
     */
    YES(1, "是", "#52c41a");

    private final Integer value;
    private final String description;
    private final String color;

    /**
     * 根据值获取枚举
     */
    public static YesNoEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (YesNoEnum yesNo : values()) {
            if (yesNo.getValue().equals(value)) {
                return yesNo;
            }
        }
        return null;
    }

    /**
     * 根据布尔值获取枚举
     */
    public static YesNoEnum getByBoolean(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool ? YES : NO;
    }

    /**
     * 判断是否为是
     */
    public boolean isYes() {
        return this == YES;
    }

    /**
     * 判断是否为否
     */
    public boolean isNo() {
        return this == NO;
    }

    /**
     * 转换为布尔值
     */
    public boolean toBoolean() {
        return this == YES;
    }

    /**
     * 从布尔值创建枚举
     */
    public static YesNoEnum fromBoolean(boolean bool) {
        return bool ? YES : NO;
    }
}