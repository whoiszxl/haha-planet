package com.whoiszxl.user.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 用户等级详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "用户等级详情信息")
public class UserLevelDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 等级
     */
    @Schema(description = "等级")
    @ExcelProperty(value = "等级")
    private Integer level;

    /**
     * 等级名称
     */
    @Schema(description = "等级名称")
    @ExcelProperty(value = "等级名称")
    private String levelName;

    /**
     * 最小经验值
     */
    @Schema(description = "最小经验值")
    @ExcelProperty(value = "最小经验值")
    private Long minExperience;

    /**
     * 最大经验值
     */
    @Schema(description = "最大经验值")
    @ExcelProperty(value = "最大经验值")
    private Long maxExperience;

    /**
     * 等级图标
     */
    @Schema(description = "等级图标")
    @ExcelProperty(value = "等级图标")
    private String levelIcon;

    /**
     * 等级颜色
     */
    @Schema(description = "等级颜色")
    @ExcelProperty(value = "等级颜色")
    private String levelColor;

    /**
     * 等级权益
     */
    @Schema(description = "等级权益")
    @ExcelProperty(value = "等级权益")
    private String privileges;

    /**
     * 等级描述
     */
    @Schema(description = "等级描述")
    @ExcelProperty(value = "等级描述")
    private String description;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    @ExcelProperty(value = "状态(0:无效 1:有效)")
    private Integer status;
}