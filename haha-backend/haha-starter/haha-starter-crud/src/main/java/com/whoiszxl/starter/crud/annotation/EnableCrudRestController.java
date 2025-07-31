package com.whoiszxl.starter.crud.annotation;

import com.whoiszxl.starter.crud.config.CrudRestControllerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * CRUD REST Controller 启用注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CrudRestControllerAutoConfiguration.class})
public @interface EnableCrudRestController {
}
