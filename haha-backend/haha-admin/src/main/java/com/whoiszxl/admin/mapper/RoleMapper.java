package com.whoiszxl.admin.mapper;

import com.whoiszxl.admin.entity.Role;
import com.whoiszxl.common.mybatis.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface RoleMapper extends DataPermissionMapper<Role> {

}
