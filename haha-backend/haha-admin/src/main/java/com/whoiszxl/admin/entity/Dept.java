package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
public class Dept extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "上级部门ID")
    private Long parentId;

    @Schema(name = "祖级列表")
    private String ancestors;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(name = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;

}
