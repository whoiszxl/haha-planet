package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改星球分类信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改星球分类信息")
public class PlanetCategoryReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    @Length(max = 64, message = "分类名称长度不能超过 {max} 个字符")
    private String categoryName;

    /**
     * 分类图标链接
     */
    @Schema(description = "分类图标链接")
    @NotBlank(message = "分类图标链接不能为空")
    @Length(max = 255, message = "分类图标链接长度不能超过 {max} 个字符")
    private String iconUrl;

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