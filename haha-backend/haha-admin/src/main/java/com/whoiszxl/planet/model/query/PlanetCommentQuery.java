package com.whoiszxl.planet.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;

/**
 * 帖子评论查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "帖子评论查询条件")
public class PlanetCommentQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    @Query(type = QueryType.EQ)
    private Long postId;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @Query(type = QueryType.EQ)
    private Long planetId;

    /**
     * 评论用户ID
     */
    @Schema(description = "评论用户ID")
    @Query(type = QueryType.EQ)
    private Long userId;

    /**
     * 评论内容
     */
    @Schema(description = "评论内容")
    @Query(type = QueryType.EQ)
    private String content;

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