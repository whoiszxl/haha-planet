package com.whoiszxl.starter.web.annotation;

import com.whoiszxl.starter.web.config.exception.GlobalExceptionHandlerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 全局异常、错误处理开启注解
 * @author whoiszxl
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GlobalExceptionHandlerAutoConfiguration.class})
public @interface EnableGlobalExceptionHandler {
}
