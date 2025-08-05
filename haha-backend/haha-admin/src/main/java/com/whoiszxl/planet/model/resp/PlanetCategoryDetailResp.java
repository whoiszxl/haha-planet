package com.whoiszxl.planet.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球分类详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球分类详情信息")
public class PlanetCategoryDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    @ExcelProperty(value = "分类名称")
    private String categoryName;

    /**
     * 分类图标链接
     */
    @Schema(description = "分类图标链接")
    @ExcelProperty(value = "分类图标链接")
    private String iconUrl;

    /**
     * 父分类ID，0表示顶级分类
     */
    @Schema(description = "父分类ID，0表示顶级分类")
    @ExcelProperty(value = "父分类ID，0表示顶级分类")
    private Long parentId;

    /**
     * 分类级别:1->1级; 2->2级 3->3级
     */
    @Schema(description = "分类级别:1->1级; 2->2级 3->3级")
    @ExcelProperty(value = "分类级别:1->1级; 2->2级 3->3级")
    private Boolean level;

    /**
     * 排序,数值越大越靠前
     */
    @Schema(description = "排序,数值越大越靠前")
    @ExcelProperty(value = "排序,数值越大越靠前")
    private Integer sort;

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