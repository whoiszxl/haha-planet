package com.whoiszxl.handler;

import com.whoiszxl.enums.AuthTypeEnum;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import com.whoiszxl.model.response.UserClientResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 登录处理器接口
 * @author whoiszxl
 */
public interface LoginHandler<T extends LoginCommand> {


    /**
     * 登录
     *
     * @param req     登录请求参数
     * @param client  客户端信息
     * @param request 请求对象
     * @return 登录响应参数
     */
    LoginResponse login(T req, UserClientResponse client, HttpServletRequest request);

    /**
     * 登录前置处理
     *
     * @param req     登录请求参数
     * @param client  客户端信息
     * @param request 请求对象
     */
    void preLogin(T req, UserClientResponse client, HttpServletRequest request);

    /**
     * 登录后置处理
     *
     * @param req     登录请求参数
     * @param client  客户端信息
     * @param request 请求对象
     */
    void postLogin(T req, UserClientResponse client, HttpServletRequest request);

    /**
     * 获取认证类型
     *
     * @return 认证类型
     */
    AuthTypeEnum getAuthType();
}
