package com.whoiszxl.common.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据权限枚举
 */
@Getter
@RequiredArgsConstructor
public enum DataScopeEnum implements IBaseEnum<Integer> {

    ALL(1, "全部数据权限"),
    DEPT_AND_CHILD(2, "本部门及以下数据权限"),
    DEPT(3, "本部门数据权限"),
    SELF(4, "仅本人数据权限"),
    CUSTOM(5, "自定义数据权限"),;

    private final Integer value;
    private final String description;
}
