
package com.whoiszxl.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 第三方账号平台枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum SocialSourceEnum {

    GITEE("码云"),
    GITHUB("GitHub"),;

    private final String description;
}
