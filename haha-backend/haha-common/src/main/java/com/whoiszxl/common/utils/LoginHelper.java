package com.whoiszxl.common.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.whoiszxl.common.constants.RedisPrefixConstants;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.core.utils.IpUtil;
import com.whoiszxl.starter.crud.service.CommonAdminService;
import com.whoiszxl.starter.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

/**
 * 登录助手
 */
public class LoginHelper {

    private LoginHelper() {
    }

    /**
     * 用户登录并缓存用户信息
     *
     * @param loginAdmin 登录用户信息
     * @return 令牌
     */
    public static String login(LoginAdmin loginAdmin) {
        // 记录登录信息
        HttpServletRequest request = ServletUtils.getRequest();
        loginAdmin.setIp(JakartaServletUtil.getClientIP(request));
        loginAdmin.setAddress(IpUtil.getAddress(loginAdmin.getIp()));
        loginAdmin.setBrowser(ServletUtils.getBrowser(request));
        loginAdmin.setLoginTime(LocalDateTime.now());
        loginAdmin.setOs(StrUtil.subBefore(ServletUtils.getOs(request), " or", false));
        // 登录并缓存用户信息
        StpUtil.login(loginAdmin.getId());
        SaHolder.getStorage().set(RedisPrefixConstants.Admin.ADMIN_LOGIN_KEY, loginAdmin);
        String tokenValue = StpUtil.getTokenValue();
        loginAdmin.setToken(tokenValue);
        StpUtil.getTokenSession().set(RedisPrefixConstants.Admin.ADMIN_LOGIN_KEY, loginAdmin);
        return tokenValue;
    }

    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     * @throws NotLoginException 未登录异常
     */
    public static LoginAdmin getLoginAdmin() throws NotLoginException {
        String key = RedisPrefixConstants.Admin.ADMIN_LOGIN_KEY;
        StpUtil.checkLogin();
        LoginAdmin loginAdmin = (LoginAdmin)SaHolder.getStorage().get(key);
        if (null != loginAdmin) {
            return loginAdmin;
        }
        SaSession tokenSession = StpUtil.getTokenSession();
        loginAdmin = (LoginAdmin)tokenSession.get(key);
        SaHolder.getStorage().set(key, loginAdmin);
        return loginAdmin;
    }

    /**
     * 根据 Token 获取登录用户信息
     *
     * @param token 用户 Token
     * @return 登录用户信息
     */
    public static LoginAdmin getLoginAdmin(String token) {
        SaSession tokenSession = StpUtil.getTokenSessionByToken(token);
        if (null == tokenSession) {
            return null;
        }
        return (LoginAdmin)tokenSession.get(RedisPrefixConstants.Admin.ADMIN_LOGIN_KEY);
    }

    /**
     * 获取登录用户 ID
     *
     * @return 登录用户 ID
     */
    public static Long getAdminId() {
        return ExceptionUtils.exToNull(() -> getLoginAdmin().getId());
    }

    /**
     * 获取登录用户名
     *
     * @return 登录用户名
     */
    public static String getUsername() {
        return getLoginAdmin().getUsername();
    }


    public static String getNickname() {
        return getNickname(getAdminId());
    }

    public static String getNickname(Long adminId) {
        return ExceptionUtils.exToNull(() -> SpringUtil.getBean(CommonAdminService.class).getNicknameById(adminId));
    }
}
