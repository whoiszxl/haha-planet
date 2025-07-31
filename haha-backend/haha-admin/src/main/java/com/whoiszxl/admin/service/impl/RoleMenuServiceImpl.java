package com.whoiszxl.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.whoiszxl.admin.entity.Role;
import com.whoiszxl.admin.entity.RoleMenu;
import com.whoiszxl.admin.mapper.RoleMenuMapper;
import com.whoiszxl.admin.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色与菜单关联表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    private final RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(List<Long> menuIds, Long roleId) {
        // 检查是否有变更
        List<Long> oldMenuIdList = this.lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, roleId)
                .list()
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(CollUtil.disjunction(menuIds, oldMenuIdList))) {
            return false;
        }
        // 删除原有关联
        this.lambdaUpdate().eq(RoleMenu::getRoleId, roleId).remove();
        // 保存最新关联
        List<RoleMenu> roleMenuList = menuIds.stream().map(menuId -> new RoleMenu(roleId, menuId)).toList();
        return this.saveBatch(roleMenuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(List<Long> roleIds) {
        this.lambdaUpdate().in(RoleMenu::getRoleId, roleIds).remove();
    }

    @Override
    public List<Long> listMenuIdByRoleIds(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return new ArrayList<>(0);
        }
        List<RoleMenu> roleMenuList = this.lambdaQuery()
                .select(RoleMenu::getMenuId).in(RoleMenu::getRoleId, roleIds).list();

        return roleMenuList.stream().map(RoleMenu::getMenuId).toList();
    }
}
