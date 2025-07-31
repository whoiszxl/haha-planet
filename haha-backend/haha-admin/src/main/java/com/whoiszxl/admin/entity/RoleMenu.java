package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 角色与菜单关联表
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@TableName("sys_role_menu")
@NoArgsConstructor
public class RoleMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "角色ID")
    private Long roleId;

    @Schema(name = "菜单ID")
    private Long menuId;

    public RoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

}
