package com.whoiszxl.handler.impl;

import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.common.constants.RedisPrefixConstants;
import com.whoiszxl.enums.AuthTypeEnum;
import com.whoiszxl.enums.LoginResponseTypeEnum;
import com.whoiszxl.handler.AbstractLoginHandler;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.user.model.entity.UserInfoDO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 手机号登录处理器
 * @author whoiszxl
 */
@Component
@RequiredArgsConstructor
public class PhoneLoginHandler extends AbstractLoginHandler<LoginCommand.PhoneLoginCommand> {

    private final UserInfoService userInfoService;
    private final RedissonUtil redissonUtil;

    @Override
    public LoginResponse login(LoginCommand.PhoneLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        Optional<UserInfoDO> memberOptional = userInfoService.getByPhone(req.getPhone());
        UserInfoDO userInfo = memberOptional.orElseThrow(() -> new RuntimeException("您还未注册"));
        return new LoginResponse(super.authenticate(userInfo, client), LoginResponseTypeEnum.LOGIN.getDescription());
    }

    @Override
    public void preLogin(LoginCommand.PhoneLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        String phone = req.getPhone();
        String captchaKey = RedisPrefixConstants.User.USER_CAPTCHA_PHONE_KEY + phone;
        String captcha = redissonUtil.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(req.getCaptcha(), captcha, CAPTCHA_ERROR);
        redissonUtil.delete(captchaKey);
    }

    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.PHONE;
    }
}
