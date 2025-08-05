package com.whoiszxl.planet.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球设置详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球设置详情信息")
public class PlanetSettingDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @ExcelProperty(value = "星球ID")
    private Long planetId;

    /**
     * 设置键
     */
    @Schema(description = "设置键")
    @ExcelProperty(value = "设置键")
    private String settingKey;

    /**
     * 设置值
     */
    @Schema(description = "设置值")
    @ExcelProperty(value = "设置值")
    private String settingValue;

    /**
     * 设置类型: 1-字符串 2-数字 3-布尔 4-JSON
     */
    @Schema(description = "设置类型: 1-字符串 2-数字 3-布尔 4-JSON")
    @ExcelProperty(value = "设置类型: 1-字符串 2-数字 3-布尔 4-JSON")
    private Integer settingType;

    /**
     * 设置描述
     */
    @Schema(description = "设置描述")
    @ExcelProperty(value = "设置描述")
    private String description;

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