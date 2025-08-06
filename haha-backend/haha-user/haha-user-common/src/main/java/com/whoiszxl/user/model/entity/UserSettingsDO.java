package com.whoiszxl.user.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 用户设置实体
 *
 * @author whoiszxl
 */
@Data
@TableName("uc_user_settings")
public class UserSettingsDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 隐私级别(1:公开 2:好友可见 3:仅自己)
     */
    private Integer privacyLevel;

    /**
     * 允许被搜索(0:不允许 1:允许)
     */
    private Boolean allowSearch;

    /**
     * 允许好友申请(0:不允许 1:允许)
     */
    private Boolean allowFriendRequest;

    /**
     * 允许私信(0:不允许 1:允许)
     */
    private Boolean allowMessage;

    /**
     * 邮件通知(0:关闭 1:开启)
     */
    private Boolean emailNotification;

    /**
     * 短信通知(0:关闭 1:开启)
     */
    private Boolean smsNotification;

    /**
     * 推送通知(0:关闭 1:开启)
     */
    private Boolean pushNotification;

    /**
     * 主题设置
     */
    private String theme;

    /**
     * 语言设置
     */
    private String language;

    /**
     * 时区设置
     */
    private String timezone;

    /**
     * 状态(0:无效 1:有效)
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 逻辑删除 1: 已删除, 0: 未删除
     */
    private Integer isDeleted;
}