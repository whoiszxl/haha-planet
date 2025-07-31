package com.whoiszxl.admin.mapper;

import com.whoiszxl.admin.entity.Menu;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据管理员 ID 查询权限集合
     * @param adminId 管理员 ID
     * @return 权限集合
     */
    @Select("""
        SELECT DISTINCT sm.permission
        FROM sys_menu AS sm
        LEFT JOIN sys_role_menu AS srm ON srm.menu_id = sm.id
        LEFT JOIN sys_role AS sr ON sr.id = srm.role_id
        LEFT JOIN sys_admin_role AS sar ON sar.role_id = sr.id
        LEFT JOIN sys_admin AS sa ON sa.id = sar.admin_id
        WHERE sa.id = #{adminId}
        AND sm.status = 1 AND sm.permission IS NOT NULL
    """)
    Set<String> selectPermissionByAdminId(Long adminId);

    /**
     * 根据角色编码查询所有的菜单权限
     * @param roleCode 角色编码
     * @return 菜单权限列表
     */
    @Select("""
        SELECT sm.*
        FROM sys_menu AS sm
        LEFT JOIN sys_role_menu AS srm ON srm.menu_id = sm.id
        LEFT JOIN sys_role AS sr ON sr.id = srm.role_id
        WHERE sr.code = #{roleCode}
        AND sm.status = 1
    """)
    List<Menu> selectByRoleCode(String roleCode);
}
