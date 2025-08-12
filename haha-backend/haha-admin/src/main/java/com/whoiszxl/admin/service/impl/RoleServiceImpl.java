package com.whoiszxl.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.whoiszxl.admin.cqrs.command.RoleReq;
import com.whoiszxl.admin.cqrs.query.RoleQuery;
import com.whoiszxl.admin.cqrs.response.MenuResponse;
import com.whoiszxl.admin.cqrs.response.RoleDetailResponse;
import com.whoiszxl.admin.cqrs.response.RoleResponse;
import com.whoiszxl.admin.entity.Role;
import com.whoiszxl.admin.mapper.RoleMapper;
import com.whoiszxl.admin.service.*;
import com.whoiszxl.common.constants.SysConstants;
import com.whoiszxl.common.model.LabelValueResp;
import com.whoiszxl.common.model.RoleDTO;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role, RoleResponse, RoleDetailResponse, RoleQuery, RoleReq> implements IRoleService {

    private final IAdminRoleService adminRoleService;

    private final IMenuService menuService;

    private final IRoleMenuService roleMenuService;

    private final IRoleDeptService roleDeptService;

    private final OnlineAdminService onlineAdminService;

    @Override
    public Long add(RoleReq command) {
        String name = command.getName();
        CheckUtils.throwIf(this.isNameExists(name, null), "新增失败，[{}] 已存在", name);
        String code = command.getCode();
        CheckUtils.throwIf(this.isCodeExists(code, null), "新增失败，[{}] 已存在", code);

        Role role = HahaBeanUtil.copyProperties(command, Role.class);
        super.save(role);
        Long roleId = role.getId();

        // 保存角色和菜单关联
        roleMenuService.add(command.getMenuIds(), roleId);
        // 保存角色和部门关联
        roleDeptService.add(command.getDeptIds(), roleId);

        return roleId;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleReq req, Long id) {
        // 角色校验
        String name = req.getName();
        CheckUtils.throwIf(this.isNameExists(name, id), "修改失败，[{}] 已存在", name);
        Role oldRole = super.getById(id);
        CheckUtils.throwIfNotEqual(req.getCode(), oldRole.getCode(), "角色编码不允许修改", oldRole.getName());

        // 更新角色信息
        super.update(req, id);

        // 更新关联信息
        if(!SysConstants.ADMIN_ROLE_CODE.equals(oldRole.getCode())) {
            boolean saveMenuFlag = roleMenuService.add(req.getMenuIds(), id);
            boolean saveDeptFlag = roleDeptService.add(req.getDeptIds(), id);

            // 如果功能权限或数据权限有变更，则清除关联的在线用户（重新登录以获取最新角色权限）
            if (ObjectUtil.notEqual(req.getDataScope(), oldRole.getDataScope()) || saveMenuFlag || saveDeptFlag) {
                onlineAdminService.cleanByRoleId(id);
            }
        }
    }

    @Override
    protected void beforeDelete(List<Long> ids) {
        // 删除角色之前判断其是否和管理员存在关联，存在则需要先删除管理员后再删除角色
        CheckUtils.throwIf(adminRoleService.haveRelation(ids), "所选角色存在用户关联，请解除关联后重试");

        // 删除角色和菜单、部门的关联
        roleMenuService.deleteByRoleIds(ids);
        roleDeptService.deleteByRoleIds(ids);
    }

    @Override
    protected void fill(Object obj) {
        super.fill(obj);
        if(obj instanceof RoleDetailResponse detail) {
            Long roleId = detail.getId();
            if(SysConstants.ADMIN_ROLE_CODE.equals(detail.getCode())) {
                List<MenuResponse> menuList = menuService.listAll();
                List<Long> menuIdList = menuList.stream().map(MenuResponse::getId).toList();
                detail.setMenuIds(menuIdList);
            }else {
                detail.setMenuIds(roleMenuService.listMenuIdByRoleIds(CollUtil.newArrayList(roleId)));
            }
        }
    }

    @Override
    public Set<String> listRoleCodeByAdminId(Long adminId) {
        List<Long> roleIdList = adminRoleService.listRoleIdByAdminId(adminId);
        List<Role> roleList = this.lambdaQuery().select(Role::getCode).in(Role::getId, roleIdList).list();
        return roleList.stream().map(Role::getCode).collect(Collectors.toSet());
    }

    @Override
    public Set<RoleDTO> listByAdminId(Long adminId) {
        List<Long> roleIdList = adminRoleService.listRoleIdByAdminId(adminId);
        List<Role> roleList = this.lambdaQuery().in(Role::getId, roleIdList).list();
        return new HashSet<>(HahaBeanUtil.copyToList(roleList, RoleDTO.class));
    }

    @Override
    public Set<String> listPermissionByAdminId(Long adminId) {
        Set<String> roleCodeSet = listRoleCodeByAdminId(adminId);
        // 如果具有管理员角色，则赋予所有权限
        if(roleCodeSet.contains(SysConstants.ADMIN_ROLE_CODE)) {
            return CollUtil.newHashSet(SysConstants.ALL_PERMISSION);
        }
        // 查询用户的菜单权限
        return menuService.listPermissionByAdminId(adminId);
    }

    @Override
    public List<String> listRoleNameByAdminId(Long adminId) {
        List<Long> roleIdList = adminRoleService.listRoleIdByAdminId(adminId);
        List<Role> roleList = this.lambdaQuery().select(Role::getName).in(Role::getId, roleIdList).list();
        return roleList.stream().map(Role::getName).collect(Collectors.toList());
    }

    @Override
    public List<LabelValueResp<Long>> buildDict(List<RoleResponse> roleResponses) {
        if (CollUtil.isEmpty(roleResponses)) {
            return new ArrayList<>(0);
        }
        return roleResponses.stream().map(r -> new LabelValueResp<>(r.getName(), r.getId())).toList();
    }

    @Override
    public List<String> listRoleNameByRoleIdList(List<Long> roleIdList) {
        List<Role> roleList = this.lambdaQuery().select(Role::getName).in(Role::getId, roleIdList).list();
        return roleList.stream().map(Role::getName).toList();
    }

    private boolean isNameExists(String name, Long id) {
        return this.lambdaQuery().eq(Role::getName, name).ne(null != id, Role::getId, id).exists();
    }

    private boolean isCodeExists(String code, Long id) {
        return this.lambdaQuery().eq(Role::getCode, code).ne(null != id, Role::getId, id).exists();
    }
}
