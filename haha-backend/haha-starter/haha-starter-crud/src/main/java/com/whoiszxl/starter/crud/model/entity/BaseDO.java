package com.whoiszxl.starter.crud.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 实体类基础类
 */
@Getter
@Setter
public class BaseDO extends BaseIdDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "创建者")
    @TableField(fill = FieldFill.INSERT)
    private Long createdBy;

    @Schema(name = "更新者")
    @TableField(fill = FieldFill.UPDATE)
    private Long updatedBy;

    @Schema(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(name = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;

}
