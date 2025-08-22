package com.whoiszxl.planet.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 通知类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum NotificationTypeEnum implements IBaseEnum<Integer> {

    /**
     * 新帖子
     */
    NEW_POST(1, "新帖子", "#1890ff"),

    /**
     * 新评论
     */
    NEW_COMMENT(2, "新评论", "#52c41a"),

    /**
     * 点赞
     */
    LIKE(3, "点赞", "#ff4d4f"),

    /**
     * 关注
     */
    FOLLOW(4, "关注", "#722ed1"),

    /**
     * 系统通知
     */
    SYSTEM_NOTIFICATION(5, "系统通知", "#faad14"),

    /**
     * 星球公告
     */
    PLANET_ANNOUNCEMENT(6, "星球公告", "#fa8c16");

    private final Integer value;
    private final String description;
    private final String color;

    /**
     * 根据值获取枚举
     */
    public static NotificationTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (NotificationTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为新帖子通知
     */
    public boolean isNewPost() {
        return this == NEW_POST;
    }

    /**
     * 判断是否为新评论通知
     */
    public boolean isNewComment() {
        return this == NEW_COMMENT;
    }

    /**
     * 判断是否为点赞通知
     */
    public boolean isLike() {
        return this == LIKE;
    }

    /**
     * 判断是否为关注通知
     */
    public boolean isFollow() {
        return this == FOLLOW;
    }

    /**
     * 判断是否为系统通知
     */
    public boolean isSystemNotification() {
        return this == SYSTEM_NOTIFICATION;
    }

    /**
     * 判断是否为星球公告
     */
    public boolean isPlanetAnnouncement() {
        return this == PLANET_ANNOUNCEMENT;
    }

    /**
     * 判断是否为用户互动类型（新帖子、新评论、点赞、关注）
     */
    public boolean isUserInteraction() {
        return this == NEW_POST || this == NEW_COMMENT || this == LIKE || this == FOLLOW;
    }

    /**
     * 判断是否为官方通知类型（系统通知、星球公告）
     */
    public boolean isOfficialNotification() {
        return this == SYSTEM_NOTIFICATION || this == PLANET_ANNOUNCEMENT;
    }
}