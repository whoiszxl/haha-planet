package com.whoiszxl.user.model.query;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 积分记录查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "积分记录查询条件")
public class PointsRecordQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @Query(type = QueryType.EQ)
    private Long userId;

    /**
     * 积分变化(正数增加，负数减少)
     */
    @Schema(description = "积分变化(正数增加，负数减少)")
    @Query(type = QueryType.EQ)
    private Integer pointsChange;

    /**
     * 变化前积分
     */
    @Schema(description = "变化前积分")
    @Query(type = QueryType.EQ)
    private Long pointsBefore;

    /**
     * 变化后积分
     */
    @Schema(description = "变化后积分")
    @Query(type = QueryType.EQ)
    private Long pointsAfter;

    /**
     * 变化类型(sign:签到 post:发帖 like:点赞 comment:评论)
     */
    @Schema(description = "变化类型(sign:签到 post:发帖 like:点赞 comment:评论)")
    @Query(type = QueryType.EQ)
    private String changeType;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;
}