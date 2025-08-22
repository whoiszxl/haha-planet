package com.whoiszxl.planet.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 目标类型枚举
 * 用于通知、点赞等功能的目标对象类型
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum TargetTypeEnum implements IBaseEnum<Integer> {

    /**
     * 帖子
     */
    POST(1, "帖子", "#1890ff"),

    /**
     * 评论
     */
    COMMENT(2, "评论", "#52c41a"),

    /**
     * 星球
     */
    PLANET(3, "星球", "#722ed1");

    private final Integer value;
    private final String description;
    private final String color;

    /**
     * 根据值获取枚举
     */
    public static TargetTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (TargetTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为帖子类型
     */
    public boolean isPost() {
        return this == POST;
    }

    /**
     * 判断是否为评论类型
     */
    public boolean isComment() {
        return this == COMMENT;
    }

    /**
     * 判断是否为星球类型
     */
    public boolean isPlanet() {
        return this == PLANET;
    }

    /**
     * 判断是否为内容类型（帖子或评论）
     */
    public boolean isContent() {
        return this == POST || this == COMMENT;
    }
}