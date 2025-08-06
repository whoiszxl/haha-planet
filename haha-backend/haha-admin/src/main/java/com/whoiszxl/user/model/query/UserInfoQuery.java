package com.whoiszxl.user.model.query;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户基础信息查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户基础信息查询条件")
public class UserInfoQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    @Schema(description = "用户唯一标识")
    @Query(type = QueryType.EQ)
    private String userCode;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @Query(type = QueryType.EQ)
    private String username;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;
}