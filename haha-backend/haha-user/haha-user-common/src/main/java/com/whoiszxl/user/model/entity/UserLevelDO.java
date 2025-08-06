package com.whoiszxl.user.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 *  , 实体
 *
 * @author whoiszxl
 */
@Data
@TableName("uc_user_level")
public class UserLevelDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 最小经验值
     */
    private Long minExperience;

    /**
     * 最大经验值
     */
    private Long maxExperience;

    /**
     * 等级图标
     */
    private String levelIcon;

    /**
     * 等级颜色
     */
    private String levelColor;

    /**
     * 等级权益
     */
    private String privileges;

    /**
     * 等级描述
     */
    private String description;

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