package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.response.RouteResponse;

import java.util.List;

/**
 * @author whoiszxl
 */
public interface ILoginService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return token 令牌
     */
    String login(String username, String password);

    /**
     * 构建路有树
     * @param adminId 管理员 ID
     * @return 路由树信息
     */
    List<RouteResponse> buildRouteTree(Long adminId);

}
