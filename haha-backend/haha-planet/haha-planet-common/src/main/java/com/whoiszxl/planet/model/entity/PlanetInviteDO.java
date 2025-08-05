package com.whoiszxl.planet.model.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 星球邀请实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_invite")
public class PlanetInviteDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 邀请人ID
     */
    private Long inviterId;

    /**
     * 被邀请人ID
     */
    private Long inviteeId;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请类型: 1-邀请码 2-链接邀请
     */
    private Integer inviteType;

    /**
     * 邀请消息
     */
    private String inviteMessage;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 最大使用次数
     */
    private Integer maxUseCount;

    /**
     * 已使用次数
     */
    private Integer usedCount;

    /**
     * 邀请状态: 1-有效 2-已使用 3-已过期 4-已取消
     */
    private Integer inviteStatus;

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