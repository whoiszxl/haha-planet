package com.whoiszxl.user.model.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 用户令牌实体
 *
 * @author whoiszxl
 */
@Data
@TableName("uc_user_token")
public class UserTokenDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * token
     */
    private String token;

    /**
     * token类型(access:访问令牌 refresh:刷新令牌)
     */
    private String tokenType;

    /**
     * 来源平台(web:网页 app:移动端 api:接口)
     */
    private String source;

    /**
     * 过期时间
     */
    private LocalDateTime expiresTime;

    /**
     * 登录IP
     */
    private String loginIp;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 附加信息
     */
    private String metaJson;

    /**
     * 状态(0:无效 1:有效)
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