package com.whoiszxl.handler;

import com.whoiszxl.enums.AuthTypeEnum;
import com.whoiszxl.model.command.LoginCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 登录处理器工厂
 * @author whoiszxl
 */
@Component
public class LoginHandlerFactory {

    private final Map<AuthTypeEnum, LoginHandler<? extends LoginCommand>> handlerMap
            = new EnumMap<>(AuthTypeEnum.class);

    @Autowired
    public LoginHandlerFactory(List<LoginHandler<? extends LoginCommand>> handlers) {
        for (LoginHandler<? extends LoginCommand> handler : handlers) {
            handlerMap.put(handler.getAuthType(), handler);
        }
    }

    /**
     * 根据认证类型获取
     *
     * @param authType 认证类型
     * @return 认证处理器
     */
    public LoginHandler<LoginCommand> getHandler(AuthTypeEnum authType) {
        return (LoginHandler<LoginCommand>)handlerMap.get(authType);
    }
}
