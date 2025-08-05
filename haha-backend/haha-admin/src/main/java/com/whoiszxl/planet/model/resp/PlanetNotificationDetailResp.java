package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球通知详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球通知详情信息")
public class PlanetNotificationDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @ExcelProperty(value = "星球ID")
    private Long planetId;

    /**
     * 接收通知的用户ID
     */
    @Schema(description = "接收通知的用户ID")
    @ExcelProperty(value = "接收通知的用户ID")
    private Long userId;

    /**
     * 发送者ID
     */
    @Schema(description = "发送者ID")
    @ExcelProperty(value = "发送者ID")
    private Long senderId;

    /**
     * 通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告
     */
    @Schema(description = "通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告")
    @ExcelProperty(value = "通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告")
    private Integer notificationType;

    /**
     * 通知标题
     */
    @Schema(description = "通知标题")
    @ExcelProperty(value = "通知标题")
    private String title;

    /**
     * 通知内容
     */
    @Schema(description = "通知内容")
    @ExcelProperty(value = "通知内容")
    private String content;

    /**
     * 关联目标类型: 1-帖子 2-评论 3-星球
     */
    @Schema(description = "关联目标类型: 1-帖子 2-评论 3-星球")
    @ExcelProperty(value = "关联目标类型: 1-帖子 2-评论 3-星球")
    private Integer targetType;

    /**
     * 关联目标ID
     */
    @Schema(description = "关联目标ID")
    @ExcelProperty(value = "关联目标ID")
    private Long targetId;

    /**
     * 是否已读: 0-未读 1-已读
     */
    @Schema(description = "是否已读: 0-未读 1-已读")
    @ExcelProperty(value = "是否已读: 0-未读 1-已读")
    private Integer isRead;

    /**
     * 阅读时间
     */
    @Schema(description = "阅读时间")
    @ExcelProperty(value = "阅读时间")
    private LocalDateTime readTime;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    @ExcelProperty(value = "业务状态")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @ExcelProperty(value = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;
}