package com.whoiszxl.user.model.query;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户等级查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户等级查询条件")
public class UserLevelQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 等级
     */
    @Schema(description = "等级")
    @Query(type = QueryType.EQ)
    private Integer level;

    /**
     * 等级名称
     */
    @Schema(description = "等级名称")
    @Query(type = QueryType.EQ)
    private String levelName;

    /**
     * 最小经验值
     */
    @Schema(description = "最小经验值")
    @Query(type = QueryType.EQ)
    private Long minExperience;

    /**
     * 最大经验值
     */
    @Schema(description = "最大经验值")
    @Query(type = QueryType.EQ)
    private Long maxExperience;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;
}