package com.whoiszxl.admin.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.whoiszxl.admin.entity.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 管理员&角色关联表 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 批量新增
     * @param list 记录列表
     * @return 是否成功
     */
    default boolean insertBatch(Collection<AdminRole> list) {
        return Db.saveBatch(list);
    }

    /**
     * 根据管理员 ID 查询角色 ID 列表
     * @param adminId 管理员 ID
     * @return 角色 ID 列表
     */
    @Select("""
        SELECT role_id
        FROM sys_admin_role
        WHERE admin_id = #{adminId}
    """)
    List<Long> selectRoleIdByAdminId(Long adminId);
}
