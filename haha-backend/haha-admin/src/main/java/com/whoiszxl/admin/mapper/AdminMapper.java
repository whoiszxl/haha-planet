package com.whoiszxl.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.whoiszxl.admin.entity.Admin;
import com.whoiszxl.common.mybatis.DataPermissionMapper;
import com.whoiszxl.starter.security.crypto.annotation.FieldEncrypt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface AdminMapper extends DataPermissionMapper<Admin> {

    /**
     * 分页查询管理员列表
     * @param page 分页条件
     * @param queryWrapper 查询条件
     * @return 分页信息列表
     */
    @Select("""
        SELECT t1.*
        FROM sys_admin AS t1
        LEFT JOIN sys_dept AS t2 ON t2.id = t1.dept_id
        ${ew.customSqlSegment}
    """)
    IPage<Admin> selectAdminPage(@Param("page")IPage<Object> page, @Param(Constants.WRAPPER) QueryWrapper<Admin> queryWrapper);

    @Select("SELECT * FROM sys_admin WHERE email = #{email}")
    Admin selectByEmail(@FieldEncrypt @Param("email") String email);

    @Select("SELECT nickname FROM sys_admin WHERE id = #{id}")
    String selectNicknameById(@Param("id") Long id);

    @Select("SELECT * FROM sys_admin WHERE mobile = #{mobile}")
    Admin selectByMobile(@FieldEncrypt @Param("mobile") String mobile);

    @Select("SELECT * FROM sys_admin WHERE username = #{username}")
    Admin selectByUsername(@Param("username") String username);


    @Select("""
        SELECT count(*)
        FROM sys_admin
        WHERE mobile = #{mobile}
        AND id != #{id}
    """)
    Long selectCountByMobile(@FieldEncrypt @Param("mobile") String mobile, @Param("id") Long id);

    @Select("""
        SELECT count(*)
        FROM sys_admin
        WHERE emaiL = #{email}
        AND id != #{id}
    """)
    Long selectCountByEmail(@FieldEncrypt @Param("email") String email, @Param("id") Long id);
}
