package com.whoiszxl.user.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 积分记录实体
 *
 * @author whoiszxl
 */
@Data
@TableName("uc_points_record")
public class PointsRecordDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 积分变化(正数增加，负数减少)
     */
    private Integer pointsChange;

    /**
     * 变化前积分
     */
    private Long pointsBefore;

    /**
     * 变化后积分
     */
    private Long pointsAfter;

    /**
     * 变化类型(sign:签到 post:发帖 like:点赞 comment:评论)
     */
    private String changeType;

    /**
     * 变化原因
     */
    private String changeReason;

    /**
     * 关联ID(如帖子ID、评论ID等)
     */
    private String relatedId;

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