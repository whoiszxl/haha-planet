package com.whoiszxl.starter.satoken.config;

import com.whoiszxl.starter.satoken.config.dao.SaTokenDaoProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * SaToken 扩展配置属性
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "sa-token.extension")
public class SaTokenExtensionProperties {

    /**
     * 是否启用扩展
     */
    private boolean enabled = false;

    /**
     * 启用 JWT
     */
    private boolean enableJwt = false;

    /**
     * 持久层配置
     */
    @NestedConfigurationProperty
    private SaTokenDaoProperties dao;

    /**
     * 安全配置
     */
    @NestedConfigurationProperty
    private SaTokenSecurityProperties security;

}
