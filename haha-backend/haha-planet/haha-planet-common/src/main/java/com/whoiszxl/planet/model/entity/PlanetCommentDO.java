package com.whoiszxl.planet.model.entity;

import java.io.Serial;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 帖子评论实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_comment")
public class PlanetCommentDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 评论用户ID
     */
    private Long userId;

    /**
     * 父评论ID，0表示顶级评论
     */
    private Long parentId;

    /**
     * 回复的用户ID
     */
    private Long replyToUserId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 媒体文件URLs
     */
    private String mediaUrls;

    /**
     * 是否匿名评论: 0-否 1-是
     */
    private Integer isAnonymous;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 审核状态: 1-待审核 2-审核通过 3-审核拒绝
     */
    private Integer auditStatus;

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