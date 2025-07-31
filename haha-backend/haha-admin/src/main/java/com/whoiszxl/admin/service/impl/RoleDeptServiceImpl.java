package com.whoiszxl.admin.service.impl;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;
import cn.hutool.core.collection.CollUtil;
import com.whoiszxl.admin.entity.RoleDept;
import com.whoiszxl.admin.mapper.RoleDeptMapper;
import com.whoiszxl.admin.service.IRoleDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whoiszxl.starter.crud.constants.FillConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色与部门关联表 服务实现类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Service
@RequiredArgsConstructor
public class RoleDeptServiceImpl extends ServiceImpl<RoleDeptMapper, RoleDept> implements IRoleDeptService {

    private final RoleDeptMapper roleDeptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(List<Long> deptIds, Long roleId) {
        // 检查是否有变更
        List<Long> oldDeptIdList = this.lambdaQuery()
                .select(RoleDept::getDeptId)
                .eq(RoleDept::getRoleId, roleId)
                .list()
                .stream()
                .map(RoleDept::getDeptId)
                .toList();
        if (CollUtil.isEmpty(CollUtil.disjunction(deptIds, oldDeptIdList))) {
            return false;
        }
        // 删除原有关联
        this.lambdaUpdate().eq(RoleDept::getRoleId, roleId).remove();
        // 保存最新关联
        List<RoleDept> roleDeptList = deptIds.stream().map(deptId -> new RoleDept(roleId, deptId)).toList();
        return this.saveBatch(roleDeptList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIds(List<Long> roleIds) {
        this.lambdaUpdate().in(RoleDept::getRoleId, roleIds).remove();
    }

    @Override
    @ContainerMethod(namespace = FillConstants.ROLE_DEPT_ID_LIST, type = MappingType.ORDER_OF_KEYS)
    public List<Long> listDeptIdByRoleId(Long roleId) {
        return roleDeptMapper.selectDeptIdByRoleId(roleId);
    }
}
