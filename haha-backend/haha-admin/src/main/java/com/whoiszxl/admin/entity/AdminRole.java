package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 管理员&角色关联表
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@TableName("sys_admin_role")
public class AdminRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "管理员ID")
    private Long adminId;

    @Schema(name = "角色ID")
    private Long roleId;

    public AdminRole(Long roleId, Long adminId) {
        this.roleId = roleId;
        this.adminId = adminId;
    }

}
