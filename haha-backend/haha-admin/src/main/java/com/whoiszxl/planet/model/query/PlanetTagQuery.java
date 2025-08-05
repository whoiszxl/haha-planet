package com.whoiszxl.planet.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;

/**
 * 星球标签查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球标签查询条件")
public class PlanetTagQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    @Query(type = QueryType.EQ)
    private String name;

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