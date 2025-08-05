package com.whoiszxl.planet.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 点赞记录实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_like")
public class PlanetLikeDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 点赞目标类型: 1-帖子 2-评论
     */
    private Integer targetType;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 被点赞的用户ID
     */
    private Long targetUserId;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 业务状态
     */
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    private Integer isDeleted;
}