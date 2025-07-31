package com.whoiszxl.admin.service.impl;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;
import cn.hutool.core.collection.CollUtil;
import com.whoiszxl.admin.entity.AdminRole;
import com.whoiszxl.admin.mapper.AdminRoleMapper;
import com.whoiszxl.admin.service.IAdminRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.starter.crud.constants.FillConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员&角色关联表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    private final AdminRoleMapper adminRoleMapper;

    @Override
    public boolean saveRoleRelation(List<Long> roleIds, Long adminId) {
        if(CollUtil.isEmpty(roleIds)) {
            return false;
        }
        // 删除管理员原有的角色
        this.lambdaUpdate().eq(AdminRole::getAdminId, adminId).remove();
        // 批量添加管理员新的角色
        List<AdminRole> adminRoleList
                = roleIds.stream().map(roleId -> new AdminRole(roleId, adminId)).collect(Collectors.toList());
        return adminRoleMapper.insertBatch(adminRoleList);
    }

    @Override
    public void resetRole(List<Long> roleIds, Long adminId) {

    }

    @Override
    @ContainerMethod(namespace = FillConstants.ADMIN_ROLE_ID_LIST, type = MappingType.ORDER_OF_KEYS)
    public List<Long> listRoleIdByAdminId(Long adminId) {
        return baseMapper.selectRoleIdByAdminId(adminId);
    }

    @Override
    public void deleteByAdminIds(List<Long> adminIds) {
        this.lambdaUpdate().in(AdminRole::getAdminId, adminIds).remove();
    }

    @Override
    public boolean haveRelation(List<Long> ids) {
        return this.lambdaQuery().in(AdminRole::getRoleId, ids).exists();
    }
}
