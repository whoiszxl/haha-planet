package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改点赞记录信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改点赞记录信息")
public class PlanetLikeReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @NotNull(message = "星球ID不能为空")
    private Long planetId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 点赞目标类型: 1-帖子 2-评论
     */
    @Schema(description = "点赞目标类型: 1-帖子 2-评论")
    @NotNull(message = "点赞目标类型: 1-帖子 2-评论不能为空")
    private Integer targetType;

    /**
     * 目标ID
     */
    @Schema(description = "目标ID")
    @NotNull(message = "目标ID不能为空")
    private Long targetId;

    /**
     * 被点赞的用户ID
     */
    @Schema(description = "被点赞的用户ID")
    @NotNull(message = "被点赞的用户ID不能为空")
    private Long targetUserId;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    @NotNull(message = "创建者不能为空")
    private Long createdBy;
}