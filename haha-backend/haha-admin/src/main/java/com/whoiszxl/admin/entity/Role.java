package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.whoiszxl.common.enums.DataScopeEnum;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "角色名称")
    private String name;

    @Schema(name = "角色代码")
    private String code;

    @Schema(name = "角色描述")
    private String description;

    @Schema(name = "数据权限")
    private DataScopeEnum dataScope;

    @Schema(name = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(name = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;
}
