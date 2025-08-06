package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;

/**
 * 创建或修改用户粉丝(关注我的人)信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改用户粉丝(关注我的人)信息")
public class UserFollowerReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID(被关注者)
     */
    @Schema(description = "用户ID(被关注者)")
    @NotNull(message = "用户ID(被关注者)不能为空")
    private Long userId;

    /**
     * 关注者ID
     */
    @Schema(description = "关注者ID")
    @NotNull(message = "关注者ID不能为空")
    private Long followerId;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}