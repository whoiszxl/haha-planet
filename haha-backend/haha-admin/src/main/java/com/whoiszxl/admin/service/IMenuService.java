package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.command.MenuReq;
import com.whoiszxl.admin.cqrs.query.MenuQuery;
import com.whoiszxl.admin.cqrs.response.MenuResponse;
import com.whoiszxl.admin.entity.Menu;
import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.starter.mybatis.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IMenuService extends BaseService<MenuResponse, MenuResponse, MenuQuery, MenuReq>, IService<Menu> {

    /**
     * 根据管理员 ID 查询权限集合
     * @param adminId 管理员 ID
     * @return 权限集合
     */
    Set<String> listPermissionByAdminId(Long adminId);

    /**
     * 根据角色编码查询所有的菜单权限
     * @param roleCode 角色编码
     * @return 菜单权限列表
     */
    List<Menu> listByRoleCode(String roleCode);

    /**
     * 查询所有的菜单
     * @return 菜单列表
     */
    List<MenuResponse> listAll();

}
