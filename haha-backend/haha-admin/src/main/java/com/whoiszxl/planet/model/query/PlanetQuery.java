package com.whoiszxl.planet.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;

/**
 * 星球查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球查询条件")
public class PlanetQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球编码，唯一标识
     */
    @Schema(description = "星球编码，唯一标识")
    @Query(type = QueryType.EQ)
    private String planetCode;

    /**
     * 星球名称
     */
    @Schema(description = "星球名称")
    @Query(type = QueryType.EQ)
    private String name;

    /**
     * 星球主ID
     */
    @Schema(description = "星球主ID")
    @Query(type = QueryType.EQ)
    private Long ownerId;

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