package com.whoiszxl.user.model.query;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户令牌查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户令牌查询条件")
public class UserTokenQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @Query(type = QueryType.EQ)
    private Long userId;

    /**
     * token
     */
    @Schema(description = "token")
    @Query(type = QueryType.EQ)
    private String token;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;
}