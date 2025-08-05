package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改星球信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改星球信息")
public class PlanetReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球编码，唯一标识
     */
    @Schema(description = "星球编码，唯一标识")
    @NotBlank(message = "星球编码，唯一标识不能为空")
    @Length(max = 32, message = "星球编码，唯一标识长度不能超过 {max} 个字符")
    private String planetCode;

    /**
     * 星球名称
     */
    @Schema(description = "星球名称")
    @NotBlank(message = "星球名称不能为空")
    @Length(max = 100, message = "星球名称长度不能超过 {max} 个字符")
    private String name;

    /**
     * 星球主ID
     */
    @Schema(description = "星球主ID")
    @NotNull(message = "星球主ID不能为空")
    private Long ownerId;

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