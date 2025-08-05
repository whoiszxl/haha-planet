package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球申请详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球申请详情信息")
public class PlanetApplyDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @ExcelProperty(value = "星球ID")
    private Long planetId;

    /**
     * 申请用户ID
     */
    @Schema(description = "申请用户ID")
    @ExcelProperty(value = "申请用户ID")
    private Long userId;

    /**
     * 申请理由
     */
    @Schema(description = "申请理由")
    @ExcelProperty(value = "申请理由")
    private String applyReason;

    /**
     * 问题答案
     */
    @Schema(description = "问题答案")
    @ExcelProperty(value = "问题答案")
    private String answer;

    /**
     * 申请状态: 1-待审核 2-已通过 3-已拒绝
     */
    @Schema(description = "申请状态: 1-待审核 2-已通过 3-已拒绝")
    @ExcelProperty(value = "申请状态: 1-待审核 2-已通过 3-已拒绝")
    private Integer applyStatus;

    /**
     * 审核原因
     */
    @Schema(description = "审核原因")
    @ExcelProperty(value = "审核原因")
    private String auditReason;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    @ExcelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    /**
     * 审核人
     */
    @Schema(description = "审核人")
    @ExcelProperty(value = "审核人")
    private Long auditBy;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    @ExcelProperty(value = "业务状态")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @ExcelProperty(value = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;
}