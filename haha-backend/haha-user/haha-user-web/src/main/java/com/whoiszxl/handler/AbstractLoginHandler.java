package com.whoiszxl.handler;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.whoiszxl.common.context.UserContext;
import com.whoiszxl.common.context.UserContextHolder;
import com.whoiszxl.common.context.UserExtraContext;
import com.whoiszxl.feign.PlanetFeignClient;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.starter.web.model.R;
import com.whoiszxl.starter.web.util.ServletUtils;
import com.whoiszxl.user.model.entity.UserInfoDO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 抽象登录处理器
 * @author whoiszxl
 */
@Slf4j
@Component
public abstract class AbstractLoginHandler<T extends LoginCommand> implements LoginHandler<T> {

    @Resource
    protected UserInfoService userInfoService;
    
    @Resource
    protected PlanetFeignClient planetFeignClient;

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
        Long userId = userInfo.getId();

        // 获取用户的上下文信息
        UserContext userContext = new UserContext();
        userContext.setClientKey(client.getClientKey());
        userContext.setClientType(client.getClientType());
        BeanUtil.copyProperties(userInfo, userContext);
        
        // 获取用户所属的星球ID列表
        try {
            R<Set<Long>> result = planetFeignClient.getMyPlanetIds(userId);
            Set<Long> myPlanetIds = result.getData();
            userContext.setMyPlanetSet(myPlanetIds);
        } catch (Exception e) {
            // 如果获取星球ID失败，记录日志但不影响登录流程
            log.error("获取用户星球ID失败: {}", e.getMessage(), e);
        }

        UserExtraContext userExtraContext = new UserExtraContext(ServletUtils.getRequest());

        SaLoginModel saLoginModel = new SaLoginModel()
                .setDevice(client.getClientType())
                .setActiveTimeout(client.getActiveTimeout())
                .setTimeout(client.getTimeout())
                .setExtra(CLIENT_KEY, client.getClientKey())
                .setExtraData(BeanUtil.beanToMap(userExtraContext));
        StpUtil.login(userId, saLoginModel);
        UserContextHolder.setContext(userContext);

        return StpUtil.getTokenValue();
    }

}

