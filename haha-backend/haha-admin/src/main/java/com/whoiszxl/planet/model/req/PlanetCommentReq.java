package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改帖子评论信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改帖子评论信息")
public class PlanetCommentReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    @NotNull(message = "帖子ID不能为空")
    private Long postId;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @NotNull(message = "星球ID不能为空")
    private Long planetId;

    /**
     * 评论用户ID
     */
    @Schema(description = "评论用户ID")
    @NotNull(message = "评论用户ID不能为空")
    private Long userId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    @Length(max = 65535, message = "评论内容长度不能超过 {max} 个字符")
    private String content;

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