package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 点赞记录信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "点赞记录信息")
public class PlanetLikeResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    private Long planetId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 点赞目标类型: 1-帖子 2-评论
     */
    @Schema(description = "点赞目标类型: 1-帖子 2-评论")
    private Integer targetType;

    /**
     * 目标ID
     */
    @Schema(description = "目标ID")
    private Long targetId;

    /**
     * 被点赞的用户ID
     */
    @Schema(description = "被点赞的用户ID")
    private Long targetUserId;

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