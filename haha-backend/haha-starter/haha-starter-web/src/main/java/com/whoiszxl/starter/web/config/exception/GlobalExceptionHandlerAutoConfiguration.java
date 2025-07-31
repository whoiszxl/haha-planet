package com.whoiszxl.starter.web.config.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(BasicErrorController.class)
@Import({GlobalExceptionHandler.class})
public class GlobalExceptionHandlerAutoConfiguration {

}
