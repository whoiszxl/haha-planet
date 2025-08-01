package com.whoiszxl.starter.crud.annotation;

import com.whoiszxl.starter.crud.enums.Api;

import java.lang.annotation.*;

/**
 * CRUD（增删改查）请求映射器注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CrudRequestMapping {

    /**
     * 路径映射 URI（等同于：@RequestMapping("/foo1")）
     */
    String value() default "";

    /**
     * API 列表
     */
    Api[] api() default {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT};
}
