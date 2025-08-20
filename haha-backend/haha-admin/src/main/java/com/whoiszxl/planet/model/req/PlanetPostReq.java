package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改星球帖子信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改星球帖子信息")
public class PlanetPostReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @NotNull(message = "星球ID不能为空")
    private Long planetId;

    /**
     * 发帖用户ID
     */
    @Schema(description = "发帖用户ID")
    @NotNull(message = "发帖用户ID不能为空")
    private Long userId;

    /**
     * 帖子概要
     */
    @Schema(description = "帖子概要")
    @NotBlank(message = "帖子概要不能为空")
    @Length(max = 2147483647, message = "帖子概要长度不能超过 {max} 个字符")
    private String summary;

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