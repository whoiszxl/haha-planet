package com.whoiszxl.planet.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;

/**
 * 星球通知查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球通知查询条件")
public class PlanetNotificationQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @Query(type = QueryType.EQ)
    private Long planetId;

    /**
     * 接收通知的用户ID
     */
    @Schema(description = "接收通知的用户ID")
    @Query(type = QueryType.EQ)
    private Long userId;

    /**
     * 通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告
     */
    @Schema(description = "通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告")
    @Query(type = QueryType.EQ)
    private Integer notificationType;

    /**
     * 通知标题
     */
    @Schema(description = "通知标题")
    @Query(type = QueryType.EQ)
    private String title;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    @Query(type = QueryType.EQ)
    private Long createdBy;
}