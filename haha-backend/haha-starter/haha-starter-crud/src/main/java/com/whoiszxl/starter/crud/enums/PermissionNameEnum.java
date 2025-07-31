package com.whoiszxl.starter.crud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限名称，枚举
 * @author whoiszxl
 */
@Getter
@AllArgsConstructor
public enum PermissionNameEnum {

    LIST("list"),
    ADD("add"),
    DELETE("delete"),
    UPDATE("update"),
    EXPORT("export"),
    ;
    private final String name;
}