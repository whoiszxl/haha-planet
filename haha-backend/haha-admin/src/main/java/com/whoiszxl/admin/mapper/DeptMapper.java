package com.whoiszxl.admin.mapper;

import com.whoiszxl.admin.entity.Dept;
import com.whoiszxl.common.mybatis.DataPermissionMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface DeptMapper extends DataPermissionMapper<Dept> {

}
