package com.whoiszxl.user.model.query;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户粉丝(关注我的人)查询条件
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户粉丝(关注我的人)查询条件")
public class UserFollowerQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID(被关注者)
     */
    @Schema(description = "用户ID(被关注者)")
    @Query(type = QueryType.EQ)
    private Long userId;

    /**
     * 关注者ID
     */
    @Schema(description = "关注者ID")
    @Query(type = QueryType.EQ)
    private Long followerId;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @Query(type = QueryType.EQ)
    private Long version;
}