package com.whoiszxl.admin.mapper;

import com.whoiszxl.admin.entity.RoleMenu;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色与菜单关联表 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
