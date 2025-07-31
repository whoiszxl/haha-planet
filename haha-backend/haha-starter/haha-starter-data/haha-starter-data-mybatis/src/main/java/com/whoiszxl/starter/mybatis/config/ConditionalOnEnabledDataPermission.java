package com.whoiszxl.starter.mybatis.config;

import com.whoiszxl.starter.core.constants.PropertiesConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 * 是否启用数据权限注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnProperty(prefix = "mybatis-plus.extension.data-permission", name = PropertiesConstants.ENABLED, havingValue = "true")
public @interface ConditionalOnEnabledDataPermission {
}