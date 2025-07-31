package com.whoiszxl.admin.service;

import com.whoiszxl.admin.entity.RoleDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色与部门关联表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IRoleDeptService extends IService<RoleDept> {

    /**
     * 添加角色与部门关联
     * @param deptIds 部门ID
     * @param roleId 角色ID
     * @return 是否添加成功
     */
    boolean add(List<Long> deptIds, Long roleId);

    /**
     * 删除角色和部门的关联
     * @param roleIds 角色ID集合
     */
    void deleteByRoleIds(List<Long> roleIds);


    /**
     * 查询角色下的所有部门ID集合
     * @param roleId 角色ID
     * @return 部门ID集合
     */
    List<Long> listDeptIdByRoleId(Long roleId);


}
