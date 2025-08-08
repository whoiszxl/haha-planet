package com.whoiszxl.handler.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.enums.AuthTypeEnum;
import com.whoiszxl.enums.LoginResponseTypeEnum;
import com.whoiszxl.handler.AbstractLoginHandler;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.UserTokenService;
import com.whoiszxl.starter.core.exception.BadRequestException;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.user.model.entity.UserInfoDO;
import com.whoiszxl.user.model.entity.UserTokenDO;
import com.xkcoding.justauth.AuthRequestFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 社交账号登录处理器
 * @author whoiszxl
 */
@Component
@RequiredArgsConstructor
public class SocialLoginHandler extends AbstractLoginHandler<LoginCommand.SocialLoginCommand> {

    private final AuthRequestFactory authRequestFactory;

    private final UserTokenService memberTokenService;

    private final RedissonUtil redissonUtil;

    /**
     * 所有的第三方登录，登录成功之后，如果没有本地账号，需要绑定一个本地账号
     * <p>
     * 这种方式的优点如下：
     * 1. 能保证用户的唯一性，不会存在大量的第三方账号。
     * 2. 能减少对第三方平台的依赖，不会因为某个平台下线，导致大量用户流失。
     * 3. 本地账号的邮箱与手机号方便营销等需求
     * 4. 满足监管要求
     * <p>
     * 缺点如下：
     * 1. 用户体验不好，需要多次操作，部分用户直接放弃操作。
     * 2. 用户存在隐私泄露问题。
     *
     * @param req     登录请求参数
     * @param client  客户端信息
     * @param request 请求对象
     * @return 登录返回信息
     */
    @Override
    public LoginResponse login(LoginCommand.SocialLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        // 获取第三方登录请求
        AuthRequest authRequest = this.getAuthRequest(req.getSource());

        AuthCallback callback = new AuthCallback();
        callback.setCode(req.getCode());
        callback.setState(req.getState());

        // 发送第三方登录请求
        AuthResponse<AuthUser> response = authRequest.login(callback);
        ValidationUtils.throwIf(!response.ok(), response.getMsg());

        // 获得结果
        AuthUser authUser = response.getData();
        String source = authUser.getSource();
        String uuid = authUser.getUuid();

        // 判断用户是否第一次使用第三方登录
        UserTokenDO userTokenDO = memberTokenService.getBySourceAndToken(source, uuid);

        if(userTokenDO != null) {
            UserInfoDO userInfo = userInfoService.getById(userTokenDO.getUserId());
            if(userInfo != null) {
                // 如果第三方的账号和本地账号同时存在，更新信息，并直接登录
                userTokenDO.setLoginTime(LocalDateTime.now());
                userTokenDO.setMetaJson(JSONUtil.toJsonStr(authUser));
                memberTokenService.updateById(userTokenDO);

                return new LoginResponse(super.authenticate(userInfo, client), LoginResponseTypeEnum.LOGIN.getDescription());
            }
        }

        // 不存在，则说明此第三方平台账号为第一次登录，需要和本地账号进行绑定
        // 将用户信息暂存 Redis，待用户登录本地账号后进行绑定
        String infoKey = IdUtil.simpleUUID();
        String infoValue = JSONUtil.toJsonStr(authUser);
        redissonUtil.set(infoKey, infoValue, Duration.ofMinutes(5));

        return new LoginResponse(infoKey, LoginResponseTypeEnum.BIND.getDescription());
    }

    @Override
    public void preLogin(LoginCommand.SocialLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        super.preLogin(req, client, request);
        if(StpUtil.isLogin()) {
            StpUtil.logout();
        }
    }

    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.SOCIAL;
    }


    private AuthRequest getAuthRequest(String source) {
        try {
            return authRequestFactory.get(source);
        } catch (Exception e) {
            throw new BadRequestException("暂不支持 [%s] 平台账号登录".formatted(source));
        }
    }
}
