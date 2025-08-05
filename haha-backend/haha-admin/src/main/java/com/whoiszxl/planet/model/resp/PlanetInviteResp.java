package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球邀请信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球邀请信息")
public class PlanetInviteResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    private Long planetId;

    /**
     * 邀请人ID
     */
    @Schema(description = "邀请人ID")
    private Long inviterId;

    /**
     * 被邀请人ID
     */
    @Schema(description = "被邀请人ID")
    private Long inviteeId;

    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    private String inviteCode;

    /**
     * 邀请类型: 1-邀请码 2-链接邀请
     */
    @Schema(description = "邀请类型: 1-邀请码 2-链接邀请")
    private Integer inviteType;

    /**
     * 邀请消息
     */
    @Schema(description = "邀请消息")
    private String inviteMessage;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    /**
     * 最大使用次数
     */
    @Schema(description = "最大使用次数")
    private Integer maxUseCount;

    /**
     * 已使用次数
     */
    @Schema(description = "已使用次数")
    private Integer usedCount;

    /**
     * 邀请状态: 1-有效 2-已使用 3-已过期 4-已取消
     */
    @Schema(description = "邀请状态: 1-有效 2-已使用 3-已过期 4-已取消")
    private Integer inviteStatus;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    private Long version;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;

    /**
     * 更新者
     */
    @Schema(description = "更新者")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}