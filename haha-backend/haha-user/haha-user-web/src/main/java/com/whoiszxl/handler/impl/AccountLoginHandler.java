package com.whoiszxl.handler.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.common.utils.SecureUtils;
import com.whoiszxl.enums.AuthTypeEnum;
import com.whoiszxl.enums.LoginResponseTypeEnum;
import com.whoiszxl.handler.AbstractLoginHandler;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.UserTokenService;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.user.model.entity.UserInfoDO;
import com.whoiszxl.user.model.entity.UserTokenDO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 账号登录处理器
 * @author whoiszxl
 */
@Component
@RequiredArgsConstructor
public class AccountLoginHandler extends AbstractLoginHandler<LoginCommand.AccountLoginCommand> {

    private final UserTokenService userTokenService;

    private final PasswordEncoder passwordEncoder;

    private final RedissonUtil redissonUtil;

    @Override
    public LoginResponse login(LoginCommand.AccountLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        // 解密密码
        String rawPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(req.getPassword()));
        ValidationUtils.throwIfBlank(rawPassword, "密码解密失败");

        // 验证用户名密码
        String username = req.getUsername();
        UserInfoDO userInfoDO = userInfoService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        boolean passwordMatches = passwordEncoder.matches(rawPassword, userInfoDO.getPassword());
        ValidationUtils.throwIf(passwordMatches, "用户名或密码错误");

        return new LoginResponse(this.authenticate(userInfoDO, client), LoginResponseTypeEnum.LOGIN.getDescription());
    }

    @Override
    public void preLogin(LoginCommand.AccountLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        super.preLogin(req, client, request);
        ValidationUtils.throwIfBlank(req.getCaptcha(), "验证码不能为空");
        ValidationUtils.throwIfBlank(req.getUuid(), "验证码标识不能为空");
    }

    @Override
    public void postLogin(LoginCommand.AccountLoginCommand req, UserClientResponse client, HttpServletRequest request) {
        String bindKey = req.getBindKey();
        // 如果存在 bindKey，则需要将第三方的信息绑定到此用户
        if(StrUtil.isNotBlank(bindKey)) {
            String bindValue = redissonUtil.get(bindKey);
            ValidationUtils.throwIfBlank(bindValue, "超时了，请重新绑定");

            AuthUser user = JSONUtil.toBean(bindValue, AuthUser.class);

            UserTokenDO userToken = new UserTokenDO();
            UserInfoDO userInfoDO = userInfoService.getByUsername(req.getUsername())
                    .orElseThrow(() -> new RuntimeException("找不到本地用户信息"));

            userToken.setUserId(userInfoDO.getId());
            userToken.setToken(user.getUuid());
            userToken.setSource(user.getSource());
            userToken.setLoginTime(LocalDateTime.now());
            userToken.setMetaJson(JSONUtil.toJsonStr(user));
            userTokenService.add(userToken);
        }
        super.postLogin(req, client, request);
    }

    @Override
    public AuthTypeEnum getAuthType() {
        return AuthTypeEnum.ACCOUNT;
    }
}
