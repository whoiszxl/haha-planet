package com.whoiszxl.user.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 用户关注(我关注的人)信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户关注(我关注的人)信息")
public class UserFollowingResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID(关注者)
     */
    @Schema(description = "用户ID(关注者)")
    private Long userId;

    /**
     * 被关注者ID
     */
    @Schema(description = "被关注者ID")
    private Long followingId;

    /**
     * 关注类型(1:普通关注 2:特别关注)
     */
    @Schema(description = "关注类型(1:普通关注 2:特别关注)")
    private Integer followType;

    /**
     * 是否互相关注(0:否 1:是)
     */
    @Schema(description = "是否互相关注(0:否 1:是)")
    private Boolean isMutual;

    /**
     * 是否取消关注(0:未取消 1:已取消)
     */
    @Schema(description = "是否取消关注(0:未取消 1:已取消)")
    private Boolean isCancelled;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    private Integer status;
}