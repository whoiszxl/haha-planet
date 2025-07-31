package com.whoiszxl.common.satoken;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.sign.SaSignTemplate;
import cn.dev33.satoken.sign.SaSignUtil;
import com.whoiszxl.starter.core.constants.StringConstants;
import com.whoiszxl.starter.core.exception.ServiceException;
import com.whoiszxl.starter.satoken.config.SaTokenExtensionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfiguration {

    private final SaTokenExtensionProperties properties;

    /**
     * SaToken 拦截器配置
     */
    @Bean
    public SaInterceptor saInterceptor() {
        return new SaExtensionInterceptor(handle -> SaRouter.match(StringConstants.PATH_PATTERN)
                .notMatch(properties.getSecurity().getExcludes())
                .check(r -> {
                    // 如果包含 sign，进行 API 接口参数签名验证
                    SaRequest saRequest = SaHolder.getRequest();
                    List<String> paramNames = saRequest.getParamNames();
                    if (paramNames.stream().anyMatch(SaSignTemplate.sign::equals)) {
                        try {
                            SaSignUtil.checkRequest(saRequest);
                        } catch (Exception e) {
                            throw new ServiceException(e.getMessage());
                        }
                        return;
                    }
                    // 此处可以批量进行登录校验
                    // StpUtil.checkLogin();
                }));
    }
}
