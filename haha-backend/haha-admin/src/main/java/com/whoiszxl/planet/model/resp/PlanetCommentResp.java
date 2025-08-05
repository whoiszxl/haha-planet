package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 帖子评论信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "帖子评论信息")
public class PlanetCommentResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    private Long postId;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    private Long planetId;

    /**
     * 评论用户ID
     */
    @Schema(description = "评论用户ID")
    private Long userId;

    /**
     * 父评论ID，0表示顶级评论
     */
    @Schema(description = "父评论ID，0表示顶级评论")
    private Long parentId;

    /**
     * 回复的用户ID
     */
    @Schema(description = "回复的用户ID")
    private Long replyToUserId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    private String content;

    /**
     * 媒体文件URLs
     */
    @Schema(description = "媒体文件URLs")
    private String mediaUrls;

    /**
     * 是否匿名评论: 0-否 1-是
     */
    @Schema(description = "是否匿名评论: 0-否 1-是")
    private Integer isAnonymous;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    private Integer likeCount;

    /**
     * 回复数
     */
    @Schema(description = "回复数")
    private Integer replyCount;

    /**
     * 审核状态: 1-待审核 2-审核通过 3-审核拒绝
     */
    @Schema(description = "审核状态: 1-待审核 2-审核通过 3-审核拒绝")
    private Integer auditStatus;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    private Long version;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;

    /**
     * 更新者
     */
    @Schema(description = "更新者")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}