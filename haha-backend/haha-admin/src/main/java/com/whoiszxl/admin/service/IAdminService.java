package com.whoiszxl.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whoiszxl.admin.cqrs.command.AdminRequest;
import com.whoiszxl.admin.cqrs.command.RoleUpdateCommand;
import com.whoiszxl.admin.cqrs.command.AdminPasswordResetCommand;
import com.whoiszxl.admin.cqrs.query.AdminQuery;
import com.whoiszxl.admin.cqrs.response.AdminDetailResponse;
import com.whoiszxl.admin.entity.Admin;
import com.whoiszxl.starter.crud.service.BaseService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IAdminService extends BaseService<AdminDetailResponse, AdminDetailResponse, AdminQuery, AdminRequest>, IService<Admin> {

    /**
     * 新增管理员
     * @param admin 管理员信息
     * @return 管理员ID
     */
    Long add(Admin admin);

    /**
     * 修改密码
     * @param oldPassword 当前密码
     * @param newPassword 新密码
     * @param id          ID
     */
    void updatePassword(String oldPassword, String newPassword, Long id);

    /**
     * 修改手机号
     *
     * @param newMobile    新手机号
     * @param oldPassword 当前密码
     * @param id          ID
     */
    void updateMobile(String newMobile, String oldPassword, Long id);

    /**
     * 修改邮箱
     *
     * @param newEmail    新邮箱
     * @param oldPassword 当前密码
     * @param id          ID
     */
    void updateEmail(String newEmail, String oldPassword, Long id);

    /**
     * 重置密码
     *
     * @param req 重置信息
     * @param id  ID
     */
    void resetPassword(AdminPasswordResetCommand req, Long id);

    /**
     * 修改角色
     *
     * @param updateReq 修改信息
     * @param id        ID
     */
    void updateRole(RoleUpdateCommand updateReq, Long id);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return 用户信息
     */
    Admin getByUsername(String username);

    /**
     * 根据手机号查询
     *
     * @param phone 手机号
     * @return 用户信息
     */
    Admin getByMobile(String phone);

    /**
     * 根据邮箱查询
     *
     * @param email 邮箱
     * @return 用户信息
     */
    Admin getByEmail(String email);

    /**
     * 根据部门 ID 列表查询
     *
     * @param deptIds 部门 ID 列表
     * @return 用户数量
     */
    Long countByDeptIds(List<Long> deptIds);

    /**
     * 密码是否已过期
     *
     * @param pwdResetTime 上次重置密码时间
     * @return 是否过期
     */
    Boolean isPasswordExpired(LocalDateTime pwdResetTime);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param avatarUrl 头像URL
     */
    void updateAvatar(Long userId, String avatarUrl);
}
