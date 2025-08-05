package com.whoiszxl.planet.model.resp;

import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 星球标签信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球标签信息")
public class PlanetTagResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    @Schema(description = "标签名称")
    private String name;

    /**
     * 标签颜色
     */
    @Schema(description = "标签颜色")
    private String color;

    /**
     * 使用次数
     */
    @Schema(description = "使用次数")
    private Integer useCount;

    /**
     * 所属分类ID
     */
    @Schema(description = "所属分类ID")
    private Long categoryId;

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