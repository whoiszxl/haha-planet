package com.whoiszxl.planet.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球标签详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球标签详情信息")
public class PlanetTagDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    @ExcelProperty(value = "标签名称")
    private String name;

    /**
     * 标签颜色
     */
    @Schema(description = "标签颜色")
    @ExcelProperty(value = "标签颜色")
    private String color;

    /**
     * 使用次数
     */
    @Schema(description = "使用次数")
    @ExcelProperty(value = "使用次数")
    private Integer useCount;

    /**
     * 所属分类ID
     */
    @Schema(description = "所属分类ID")
    @ExcelProperty(value = "所属分类ID")
    private Long categoryId;

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