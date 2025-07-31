package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.command.RoleReq;
import com.whoiszxl.admin.cqrs.query.RoleQuery;
import com.whoiszxl.admin.cqrs.response.RoleDetailResponse;
import com.whoiszxl.admin.cqrs.response.RoleResponse;
import com.whoiszxl.common.model.LabelValueResp;
import com.whoiszxl.common.model.RoleDTO;
import com.whoiszxl.admin.entity.Role;
import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.starter.mybatis.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IRoleService extends BaseService<RoleResponse, RoleDetailResponse, RoleQuery, RoleReq>, IService<Role> {

    /**
     * 根据管理员 ID 查询角色
     * @param adminId 管理员 ID
     * @return 角色码集合
     */
    Set<String> listRoleCodeByAdminId(Long adminId);

    /**
     * 根据管理员 ID 查询角色
     * @param adminId 管理员 ID
     * @return 角色信息集合
     */
    Set<RoleDTO> listByAdminId(Long adminId);


    /**
     * 根据管理员 ID 查询权限
     * @param adminId 管理员 ID
     * @return 权限码集合
     */
    Set<String> listPermissionByAdminId(Long adminId);

    /**
     * 根据管理员 ID 查询角色名称集合
     * @param adminId 管理员 ID
     * @return 角色名称集合
     */
    List<String> listRoleNameByAdminId(Long adminId);

    /**
     * 构建角色字典
     * @param roleResponses 角色信息列表
     * @return 键值对信息
     */
    List<LabelValueResp<Long>> buildDict(List<RoleResponse> roleResponses);

    /**
     * 根据角色ID集合查询角色的名称列表
     * @param roleIdList 角色ID集合
     * @return 角色名称列表
     */
    List<String> listRoleNameByRoleIdList(List<Long> roleIdList);

    /**
     * 添加角色
     * @param command 添加命令
     * @return 角色ID
     */
    @Override
    Long add(RoleReq command);
}
