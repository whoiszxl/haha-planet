package com.whoiszxl.starter.satoken.enums;

/**
 * SaToken 持久层类型枚举
 */
public enum SaTokenDaoType {

    /**
     * 默认（内存）
     */
    DEFAULT,

    /**
     * Redis
     */
    REDIS,

    /**
     * 自定义
     */
    CUSTOM
}
