package com.whoiszxl.user.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 用户关注(我关注的人)实体
 *
 * @author whoiszxl
 */
@Data
@TableName("uc_user_following")
public class UserFollowingDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID(关注者)
     */
    private Long userId;

    /**
     * 被关注者ID
     */
    private Long followingId;

    /**
     * 关注类型(1:普通关注 2:特别关注)
     */
    private Integer followType;

    /**
     * 是否互相关注(0:否 1:是)
     */
    private Boolean isMutual;

    /**
     * 是否取消关注(0:未取消 1:已取消)
     */
    private Boolean isCancelled;

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