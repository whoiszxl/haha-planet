package com.whoiszxl.planet.model.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 星球申请实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_apply")
public class PlanetApplyDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 申请用户ID
     */
    private Long userId;

    /**
     * 申请理由
     */
    private String applyReason;

    /**
     * 问题答案
     */
    private String answer;

    /**
     * 申请状态: 1-待审核 2-已通过 3-已拒绝
     */
    private Integer applyStatus;

    /**
     * 审核原因
     */
    private String auditReason;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    private Long auditBy;

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