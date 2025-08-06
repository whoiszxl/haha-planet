package com.whoiszxl.user.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 积分记录详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "积分记录详情信息")
public class PointsRecordDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 积分变化(正数增加，负数减少)
     */
    @Schema(description = "积分变化(正数增加，负数减少)")
    @ExcelProperty(value = "积分变化(正数增加，负数减少)")
    private Integer pointsChange;

    /**
     * 变化前积分
     */
    @Schema(description = "变化前积分")
    @ExcelProperty(value = "变化前积分")
    private Long pointsBefore;

    /**
     * 变化后积分
     */
    @Schema(description = "变化后积分")
    @ExcelProperty(value = "变化后积分")
    private Long pointsAfter;

    /**
     * 变化类型(sign:签到 post:发帖 like:点赞 comment:评论)
     */
    @Schema(description = "变化类型(sign:签到 post:发帖 like:点赞 comment:评论)")
    @ExcelProperty(value = "变化类型(sign:签到 post:发帖 like:点赞 comment:评论)")
    private String changeType;

    /**
     * 变化原因
     */
    @Schema(description = "变化原因")
    @ExcelProperty(value = "变化原因")
    private String changeReason;

    /**
     * 关联ID(如帖子ID、评论ID等)
     */
    @Schema(description = "关联ID(如帖子ID、评论ID等)")
    @ExcelProperty(value = "关联ID(如帖子ID、评论ID等)")
    private String relatedId;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    @ExcelProperty(value = "状态(0:无效 1:有效)")
    private Integer status;
}