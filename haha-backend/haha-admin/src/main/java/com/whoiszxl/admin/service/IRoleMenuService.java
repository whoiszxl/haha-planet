package com.whoiszxl.admin.service;

import com.whoiszxl.admin.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色与菜单关联表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 添加角色和菜单的关联
     * @param menuIds 菜单ID列表
     * @param roleId 角色ID
     * @return 是否成功
     */
    boolean add(List<Long> menuIds, Long roleId);

    /**
     * 根据角色ID删除其和菜单的关联
     * @param roleIds 角色ID集合
     */
    void deleteByRoleIds(List<Long> roleIds);

    /**
     * 根据角色ID集合查询菜单ID集合
     * @param roleIds 角色ID集合
     * @return 菜单ID集合
     */
    List<Long> listMenuIdByRoleIds(List<Long> roleIds);

}
