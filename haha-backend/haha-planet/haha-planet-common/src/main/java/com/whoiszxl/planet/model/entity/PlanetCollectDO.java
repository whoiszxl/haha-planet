package com.whoiszxl.planet.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 收藏记录实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_collect")
public class PlanetCollectDO extends BaseDO {

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
     * 帖子ID
     */
    private Long postId;

    /**
     * 收藏夹名称
     */
    private String folderName;

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