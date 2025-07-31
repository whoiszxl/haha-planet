package com.whoiszxl.starter.crud.utils;

import jakarta.validation.groups.Default;

/**
 * 分组校验
 */
public interface ValidateGroup extends Default {

    /**
     * 分组校验-增删改查
     */
    interface Crud extends ValidateGroup {
        /**
         * 分组校验-创建
         */
        interface Add extends Crud {
        }

        /**
         * 分组校验-修改
         */
        interface Update extends Crud {
        }
    }
}
