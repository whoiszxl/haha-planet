package com.whoiszxl.handler;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.whoiszxl.common.context.UserContext;
import com.whoiszxl.common.context.UserContextHolder;
import com.whoiszxl.common.context.UserExtraContext;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.starter.web.util.ServletUtils;
import com.whoiszxl.user.model.entity.UserInfoDO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 抽象登录处理器
 * @author whoiszxl
 */
@Component
public abstract class AbstractLoginHandler<T extends LoginCommand> implements LoginHandler<T> {

    @Resource
    protected UserInfoService userInfoService;

    protected static final String CAPTCHA_EXPIRED = "验证码已失效";
    protected static final String CAPTCHA_ERROR = "验证码错误";
    protected static final String CLIENT_KEY = "clientKey";

    @Override
    public void preLogin(T req, UserClientResponse client, HttpServletRequest request) {
        System.out.println("登录前置操作 todo");
    }

    @Override
    public void postLogin(T req, UserClientResponse client, HttpServletRequest request) {
        System.out.println("登录后置操作 todo");
    }

    protected String authenticate(UserInfoDO userInfo, UserClientResponse client) {
        Long memberId = userInfo.getId();

        // 获取用户的上下文信息
        UserContext userContext = new UserContext();
        userContext.setClientKey(client.getClientKey());
        userContext.setClientType(client.getClientType());
        BeanUtil.copyProperties(userInfo, userContext);

        UserExtraContext userExtraContext = new UserExtraContext(ServletUtils.getRequest());

        SaLoginModel saLoginModel = new SaLoginModel()
                .setDevice(client.getClientType())
                .setActiveTimeout(client.getActiveTimeout())
                .setTimeout(client.getTimeout())
                .setExtra(CLIENT_KEY, client.getClientKey())
                .setExtraData(BeanUtil.beanToMap(userExtraContext));
        StpUtil.login(memberId, saLoginModel);
        UserContextHolder.setContext(userContext);

        return StpUtil.getTokenValue();
    }

}

