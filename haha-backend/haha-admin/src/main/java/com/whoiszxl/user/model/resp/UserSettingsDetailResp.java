package com.whoiszxl.user.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 用户设置详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "用户设置详情信息")
public class UserSettingsDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 隐私级别(1:公开 2:好友可见 3:仅自己)
     */
    @Schema(description = "隐私级别(1:公开 2:好友可见 3:仅自己)")
    @ExcelProperty(value = "隐私级别(1:公开 2:好友可见 3:仅自己)")
    private Integer privacyLevel;

    /**
     * 允许被搜索(0:不允许 1:允许)
     */
    @Schema(description = "允许被搜索(0:不允许 1:允许)")
    @ExcelProperty(value = "允许被搜索(0:不允许 1:允许)")
    private Boolean allowSearch;

    /**
     * 允许好友申请(0:不允许 1:允许)
     */
    @Schema(description = "允许好友申请(0:不允许 1:允许)")
    @ExcelProperty(value = "允许好友申请(0:不允许 1:允许)")
    private Boolean allowFriendRequest;

    /**
     * 允许私信(0:不允许 1:允许)
     */
    @Schema(description = "允许私信(0:不允许 1:允许)")
    @ExcelProperty(value = "允许私信(0:不允许 1:允许)")
    private Boolean allowMessage;

    /**
     * 邮件通知(0:关闭 1:开启)
     */
    @Schema(description = "邮件通知(0:关闭 1:开启)")
    @ExcelProperty(value = "邮件通知(0:关闭 1:开启)")
    private Boolean emailNotification;

    /**
     * 短信通知(0:关闭 1:开启)
     */
    @Schema(description = "短信通知(0:关闭 1:开启)")
    @ExcelProperty(value = "短信通知(0:关闭 1:开启)")
    private Boolean smsNotification;

    /**
     * 推送通知(0:关闭 1:开启)
     */
    @Schema(description = "推送通知(0:关闭 1:开启)")
    @ExcelProperty(value = "推送通知(0:关闭 1:开启)")
    private Boolean pushNotification;

    /**
     * 主题设置
     */
    @Schema(description = "主题设置")
    @ExcelProperty(value = "主题设置")
    private String theme;

    /**
     * 语言设置
     */
    @Schema(description = "语言设置")
    @ExcelProperty(value = "语言设置")
    private String language;

    /**
     * 时区设置
     */
    @Schema(description = "时区设置")
    @ExcelProperty(value = "时区设置")
    private String timezone;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    @ExcelProperty(value = "状态(0:无效 1:有效)")
    private Integer status;
}