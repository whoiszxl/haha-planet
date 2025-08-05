package com.whoiszxl.planet.model.resp;

import java.io.Serial;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 收藏记录详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "收藏记录详情信息")
public class PlanetCollectDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @ExcelProperty(value = "星球ID")
    private Long planetId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 帖子ID
     */
    @Schema(description = "帖子ID")
    @ExcelProperty(value = "帖子ID")
    private Long postId;

    /**
     * 收藏夹名称
     */
    @Schema(description = "收藏夹名称")
    @ExcelProperty(value = "收藏夹名称")
    private String folderName;

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