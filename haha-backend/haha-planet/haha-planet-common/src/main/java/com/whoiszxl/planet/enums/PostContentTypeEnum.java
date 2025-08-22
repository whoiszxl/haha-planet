package com.whoiszxl.planet.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 帖子内容类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum PostContentTypeEnum implements IBaseEnum<Integer> {

    /**
     * 主题
     */
    TOPIC(1, "主题"),

    /**
     * 文章
     */
    ARTICLE(2, "文章");

    private final Integer value;
    private final String description;

    /**
     * 根据值获取枚举
     */
    public static PostContentTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (PostContentTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为文章类型
     */
    public boolean isArticle() {
        return this == ARTICLE;
    }

    /**
     * 判断是否为主题类型
     */
    public boolean isTopic() {
        return this == TOPIC;
    }
}