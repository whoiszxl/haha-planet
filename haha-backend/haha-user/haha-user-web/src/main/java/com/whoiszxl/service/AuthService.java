package com.whoiszxl.service;

import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 鉴权服务接口
 *
 * @author whoiszxl
 */
public interface AuthService {

    LoginResponse login(LoginCommand loginCommand, HttpServletRequest request);

}
