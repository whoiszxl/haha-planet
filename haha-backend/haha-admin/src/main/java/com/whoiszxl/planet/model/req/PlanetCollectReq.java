package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改收藏记录信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改收藏记录信息")
public class PlanetCollectReq extends BaseReq {

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
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    @NotNull(message = "帖子ID不能为空")
    private Long postId;

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