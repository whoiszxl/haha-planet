package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改星球标签信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改星球标签信息")
public class PlanetTagReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    @NotBlank(message = "标签名称不能为空")
    @Length(max = 50, message = "标签名称长度不能超过 {max} 个字符")
    private String name;

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