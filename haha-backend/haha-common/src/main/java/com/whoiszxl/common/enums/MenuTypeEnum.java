package com.whoiszxl.common.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 菜单类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum implements IBaseEnum<Integer> {

    DIR(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),;

    private final Integer value;
    private final String description;
}
