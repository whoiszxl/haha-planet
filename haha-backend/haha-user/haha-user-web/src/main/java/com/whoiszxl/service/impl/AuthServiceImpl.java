package com.whoiszxl.service.impl;

import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.enums.AuthTypeEnum;
import com.whoiszxl.handler.LoginHandler;
import com.whoiszxl.handler.LoginHandlerFactory;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.AuthService;
import com.whoiszxl.service.UserClientService;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 鉴权服务接口实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserClientService userClientService;
    private final LoginHandlerFactory loginHandlerFactory;

    @Override
    public LoginResponse login(LoginCommand loginCommand, HttpServletRequest request) {
        AuthTypeEnum authType = loginCommand.getAuthType();
        UserClientResponse client = userClientService.getByClientId(loginCommand.getClientKey());
        ValidationUtils.throwIfNull(client, "客户端不存在");
        ValidationUtils.throwIf(DisEnableStatusEnum.DISABLE.getValue().equals(client.getStatus()), "客户端已禁用");
        ValidationUtils.throwIf(!client.getAuthType().contains(authType.getValue()), "该客户端暂未授权 [{}] 认证", authType
                .getDescription());

        LoginHandler<LoginCommand> handler = loginHandlerFactory.getHandler(authType);
        handler.preLogin(loginCommand, client, request);
        LoginResponse loginResponse = handler.login(loginCommand, client, request);
        handler.postLogin(loginCommand, client, request);
        return loginResponse;
    }
}
