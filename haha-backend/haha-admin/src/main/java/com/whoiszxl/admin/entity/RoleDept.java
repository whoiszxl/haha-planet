package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 角色与部门关联表
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@TableName("sys_role_dept")
@NoArgsConstructor
public class RoleDept implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "角色ID")
    private Long roleId;

    @Schema(name = "部门ID")
    private Long deptId;

    public RoleDept(Long roleId, Long deptId) {
        this.roleId = roleId;
        this.deptId = deptId;
    }
}
