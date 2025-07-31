package com.whoiszxl.admin.mapper;

import com.whoiszxl.admin.entity.RoleDept;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色与部门关联表 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface RoleDeptMapper extends BaseMapper<RoleDept> {

    @Select("""
        SELECT dept_id
        FROM sys_role_dept
        WHERE role_id = #{roleId}
    """)
    List<Long> selectDeptIdByRoleId(@Param("roleId") Long roleId);
}
