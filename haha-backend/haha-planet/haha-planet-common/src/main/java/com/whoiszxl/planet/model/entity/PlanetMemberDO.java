package com.whoiszxl.planet.model.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 星球成员实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_member")
public class PlanetMemberDO extends BaseDO {

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
     * 成员类型: 1-普通成员 2-管理员 3-星球主
     */
    private Integer memberType;

    /**
     * 加入时间
     */
    private LocalDateTime joinTime;

    /**
     * 到期时间
     */
    private LocalDateTime expireTime;

    /**
     * 加入来源: 1-直接加入 2-邀请 3-分享
     */
    private Integer joinSource;

    /**
     * 邀请人ID
     */
    private Long inviterId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 在星球中的昵称
     */
    private String nickname;

    /**
     * 是否被禁言: 0-否 1-是
     */
    private Integer isMuted;

    /**
     * 禁言结束时间
     */
    private LocalDateTime muteEndTime;

    /**
     * 最后阅读时间
     */
    private LocalDateTime lastReadTime;

    /**
     * 总发帖数
     */
    private Integer totalPosts;

    /**
     * 总获赞数
     */
    private Integer totalLikes;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 业务状态: 1-正常 2-已退出
     */
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    private Integer isDeleted;
}