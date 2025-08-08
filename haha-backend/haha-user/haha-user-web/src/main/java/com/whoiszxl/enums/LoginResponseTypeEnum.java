
package com.whoiszxl.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 登录响应类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum LoginResponseTypeEnum {

    BIND("bind"),
    LOGIN("login");

    private final String description;
}
