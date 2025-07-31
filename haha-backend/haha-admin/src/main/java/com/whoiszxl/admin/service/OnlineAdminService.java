package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.query.OnlineUserQuery;
import com.whoiszxl.admin.cqrs.response.OnlineUserResp;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户业务接口
 * @author whoiszxl
 */
public interface OnlineAdminService {

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 分页列表信息
     */
    PageResponse<OnlineUserResp> page(OnlineUserQuery query, PageQuery pageQuery);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表信息
     */
    List<LoginAdmin> list(OnlineUserQuery query);

    /**
     * 查询 Token 最后活跃时间
     *
     * @param token Token
     * @return 最后活跃时间
     */
    LocalDateTime getLastActiveTime(String token);

    /**
     * 根据角色 ID 清除
     *
     * @param roleId 角色 ID
     */
    void cleanByRoleId(Long roleId);

    /**
     * 根据用户 ID 清除登录
     *
     * @param userId 用户 ID
     */
    void cleanByUserId(Long userId);
}
