package com.whoiszxl.starter.mybatis.base;

import com.baomidou.mybatisplus.annotation.IEnum;

import java.io.Serializable;

/**
 * 枚举接口
 * @author whoiszxl
 */
public interface IBaseEnum<T extends Serializable> extends IEnum<T> {

    /**
     * 枚举描述
     *
     * @return 枚举描述
     */
    String getDescription();

    /**
     * 颜色
     *
     * @return 颜色
     */
    default String getColor() {
        return null;
    }
}
