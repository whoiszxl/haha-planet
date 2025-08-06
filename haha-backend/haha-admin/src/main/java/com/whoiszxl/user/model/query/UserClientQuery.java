package com.whoiszxl.user.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;

/**
 * 用户客户端查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户客户端查询条件")
public class UserClientQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @Schema(description = "key")
    @Query(type = QueryType.EQ)
    private String clientKey;

    /**
     * secret
     */
    @Schema(description = "secret")
    @Query(type = QueryType.EQ)
    private String clientSecret;

    /**
     * 授权类型
     */
    @Schema(description = "授权类型")
    @Query(type = QueryType.EQ)
    private String authType;

    /**
     * 客户端类型
     */
    @Schema(description = "客户端类型")
    @Query(type = QueryType.EQ)
    private String clientType;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;
}