package com.whoiszxl.planet.model.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 星球帖子实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_post")
public class PlanetPostDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 发帖用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 内容类型: 1-文本 2-图片 3-视频 4-音频 5-文件 6-链接
     */
    private Integer contentType;

    /**
     * 媒体文件URLs，JSON数组
     */
    private String mediaUrls;

    /**
     * 是否匿名发帖: 0-否 1-是
     */
    private Integer isAnonymous;

    /**
     * 是否置顶: 0-否 1-是
     */
    private Integer isTop;

    /**
     * 是否精华: 0-否 1-是
     */
    private Integer isEssence;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 分享次数
     */
    private Integer shareCount;

    /**
     * 收藏次数
     */
    private Integer collectCount;

    /**
     * 审核状态: 1-待审核 2-审核通过 3-审核拒绝
     */
    private Integer auditStatus;

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
     * 最后评论时间
     */
    private LocalDateTime lastCommentTime;

    /**
     * 热度分数
     */
    private Integer hotScore;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 业务状态: 1-正常 2-已删除 3-已隐藏
     */
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    private Integer isDeleted;
}