package com.whoiszxl.planet.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;

/**
 * 星球邀请查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球邀请查询条件")
public class PlanetInviteQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @Query(type = QueryType.EQ)
    private Long planetId;

    /**
     * 邀请人ID
     */
    @Schema(description = "邀请人ID")
    @Query(type = QueryType.EQ)
    private Long inviterId;

    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    @Query(type = QueryType.EQ)
    private String inviteCode;

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