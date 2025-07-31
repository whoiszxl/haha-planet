package com.whoiszxl.common.model;

import com.whoiszxl.common.enums.DataScopeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色信息
 */
@Data
public class RoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色数据权限
     */
    private DataScopeEnum dataScope;
}
