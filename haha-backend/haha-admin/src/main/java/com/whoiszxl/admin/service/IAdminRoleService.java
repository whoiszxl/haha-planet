package com.whoiszxl.admin.service;

import com.whoiszxl.admin.entity.AdminRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 管理员&角色关联表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IAdminRoleService extends IService<AdminRole> {

    /**
     * 保存管理员的角色信息
     * @param roleIds 角色ID集合
     * @param adminId 管理员ID
     * @return 是否保存成功
     */
    boolean saveRoleRelation(List<Long> roleIds, Long adminId);

    /**
     * 重新设置管理员的角色
     * @param roleIds 角色ID集合
     * @param adminId 管理员ID
     */
    void resetRole(List<Long> roleIds, Long adminId);

    /**
     * 根据管理员ID查询角色ID集合
     * @param adminId 管理员 ID
     * @return 角色ID集合
     */
    List<Long> listRoleIdByAdminId(Long adminId);

    /**
     * 通过管理员ID集合批量删除管理员
     * @param adminIds 管理员ID集合
     */
    void deleteByAdminIds(List<Long> adminIds);

    /**
     * 判断角色 ID 是否和管理员存在关联
     * @param ids 角色 ID 集合
     * @return 是否存在关联
     */
    boolean haveRelation(List<Long> ids);
}
