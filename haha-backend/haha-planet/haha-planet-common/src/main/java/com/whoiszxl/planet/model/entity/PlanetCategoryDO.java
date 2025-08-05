package com.whoiszxl.planet.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 星球分类实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_category")
public class PlanetCategoryDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类图标链接
     */
    private String iconUrl;

    /**
     * 父分类ID，0表示顶级分类
     */
    private Long parentId;

    /**
     * 分类级别:1->1级; 2->2级 3->3级
     */
    private Boolean level;

    /**
     * 排序,数值越大越靠前
     */
    private Integer sort;

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