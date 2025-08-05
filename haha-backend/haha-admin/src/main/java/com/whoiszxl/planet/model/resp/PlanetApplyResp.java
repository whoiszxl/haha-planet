package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球申请信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球申请信息")
public class PlanetApplyResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    private Long planetId;

    /**
     * 申请用户ID
     */
    @Schema(description = "申请用户ID")
    private Long userId;

    /**
     * 申请理由
     */
    @Schema(description = "申请理由")
    private String applyReason;

    /**
     * 问题答案
     */
    @Schema(description = "问题答案")
    private String answer;

    /**
     * 申请状态: 1-待审核 2-已通过 3-已拒绝
     */
    @Schema(description = "申请状态: 1-待审核 2-已通过 3-已拒绝")
    private Integer applyStatus;

    /**
     * 审核原因
     */
    @Schema(description = "审核原因")
    private String auditReason;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    @Schema(description = "审核人")
    private Long auditBy;

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