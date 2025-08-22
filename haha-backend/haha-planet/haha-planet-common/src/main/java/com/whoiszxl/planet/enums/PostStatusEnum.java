package com.whoiszxl.planet.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 帖子业务状态枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum PostStatusEnum implements IBaseEnum<Integer> {

    /**
     * 正常
     */
    NORMAL(1, "正常", "#52c41a"),

    /**
     * 已删除
     */
    DELETED(2, "已删除", "#ff4d4f"),

    /**
     * 已隐藏
     */
    HIDDEN(3, "已隐藏", "#faad14");

    private final Integer value;
    private final String description;
    private final String color;

    /**
     * 根据值获取枚举
     */
    public static PostStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PostStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为正常状态
     */
    public boolean isNormal() {
        return this == NORMAL;
    }

    /**
     * 判断是否已删除
     */
    public boolean isDeleted() {
        return this == DELETED;
    }

    /**
     * 判断是否已隐藏
     */
    public boolean isHidden() {
        return this == HIDDEN;
    }

    /**
     * 判断是否可见（正常状态）
     */
    public boolean isVisible() {
        return this == NORMAL;
    }
}