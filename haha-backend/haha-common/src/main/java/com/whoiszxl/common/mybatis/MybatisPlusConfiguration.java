package com.whoiszxl.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.whoiszxl.starter.mybatis.datapermission.DataPermissionFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * MyBatis Plus 配置
 */
@Configuration
public class MybatisPlusConfiguration {

    /**
     * 元对象处理器配置（插入或修改时自动填充）
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyBatisPlusMetaObjectHandler();
    }

    /**
     * 数据权限过滤器
     */
    @Bean
    public DataPermissionFilter dataPermissionFilter() {
        return new DataPermissionFilterImpl();
    }

    /**
     * BCrypt 加/解密处理器
     */
    @Bean
    public BCryptEncryptor bCryptEncryptor(PasswordEncoder passwordEncoder) {
        return new BCryptEncryptor(passwordEncoder);
    }
}
