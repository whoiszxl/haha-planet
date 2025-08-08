package com.whoiszxl.user.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 用户客户端实体
 *
 * @author whoiszxl
 */
@Data
@TableName("uc_user_client")
public class UserClientDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    private String clientKey;

    /**
     * secret
     */
    private String clientSecret;

    /**
     * 授权类型
     */
    private String authType;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * Token最低活跃频率 单位:秒，-1:不限制，永不冻结）
     */
    private Long activeTimeout;

    /**
     * Token有效期 单位:秒，-1:永不过期）
     */
    private Long timeout;

    /**
     * 状态(0:禁用 1:启用)
     */
    private Integer status;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 逻辑删除 1: 已删除, 0: 未删除
     */
    private Integer isDeleted;
}