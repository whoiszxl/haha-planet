package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球成员详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球成员详情信息")
public class PlanetMemberDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @ExcelProperty(value = "星球ID")
    private Long planetId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 成员类型: 1-普通成员 2-管理员 3-星球主
     */
    @Schema(description = "成员类型: 1-普通成员 2-管理员 3-星球主")
    @ExcelProperty(value = "成员类型: 1-普通成员 2-管理员 3-星球主")
    private Integer memberType;

    /**
     * 加入时间
     */
    @Schema(description = "加入时间")
    @ExcelProperty(value = "加入时间")
    private LocalDateTime joinTime;

    /**
     * 到期时间
     */
    @Schema(description = "到期时间")
    @ExcelProperty(value = "到期时间")
    private LocalDateTime expireTime;

    /**
     * 加入来源: 1-直接加入 2-邀请 3-分享
     */
    @Schema(description = "加入来源: 1-直接加入 2-邀请 3-分享")
    @ExcelProperty(value = "加入来源: 1-直接加入 2-邀请 3-分享")
    private Integer joinSource;

    /**
     * 邀请人ID
     */
    @Schema(description = "邀请人ID")
    @ExcelProperty(value = "邀请人ID")
    private Long inviterId;

    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    @ExcelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 在星球中的昵称
     */
    @Schema(description = "在星球中的昵称")
    @ExcelProperty(value = "在星球中的昵称")
    private String nickname;

    /**
     * 是否被禁言: 0-否 1-是
     */
    @Schema(description = "是否被禁言: 0-否 1-是")
    @ExcelProperty(value = "是否被禁言: 0-否 1-是")
    private Integer isMuted;

    /**
     * 禁言结束时间
     */
    @Schema(description = "禁言结束时间")
    @ExcelProperty(value = "禁言结束时间")
    private LocalDateTime muteEndTime;

    /**
     * 最后阅读时间
     */
    @Schema(description = "最后阅读时间")
    @ExcelProperty(value = "最后阅读时间")
    private LocalDateTime lastReadTime;

    /**
     * 总发帖数
     */
    @Schema(description = "总发帖数")
    @ExcelProperty(value = "总发帖数")
    private Integer totalPosts;

    /**
     * 总获赞数
     */
    @Schema(description = "总获赞数")
    @ExcelProperty(value = "总获赞数")
    private Integer totalLikes;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 业务状态: 1-正常 2-已退出
     */
    @Schema(description = "业务状态: 1-正常 2-已退出")
    @ExcelProperty(value = "业务状态: 1-正常 2-已退出")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @ExcelProperty(value = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;
}