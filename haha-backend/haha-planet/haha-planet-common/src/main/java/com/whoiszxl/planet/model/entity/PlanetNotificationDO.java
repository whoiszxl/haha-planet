package com.whoiszxl.planet.model.entity;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import com.whoiszxl.starter.crud.model.entity.BaseDO;

/**
 * 星球通知实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_notification")
public class PlanetNotificationDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 接收通知的用户ID
     */
    private Long userId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告
     */
    private Integer notificationType;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 关联目标类型: 1-帖子 2-评论 3-星球
     */
    private Integer targetType;

    /**
     * 关联目标ID
     */
    private Long targetId;

    /**
     * 是否已读: 0-未读 1-已读
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

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