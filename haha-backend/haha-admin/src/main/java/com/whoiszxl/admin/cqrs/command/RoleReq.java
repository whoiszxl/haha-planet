package com.whoiszxl.admin.cqrs.command;

import com.whoiszxl.common.enums.DataScopeEnum;
import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "角色添加命令")
public class RoleReq extends BaseReq {

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

    @Schema(description = "权限范围：部门 ID 列表", example = "5")
    private List<Long> deptIds = new ArrayList<>();

    @Schema(description = "功能权限：菜单 ID 列表", example = "1000,1010,1011,1012,1013,1014")
    private List<Long> menuIds = new ArrayList<>();
}
