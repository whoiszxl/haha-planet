package com.whoiszxl.planet.model.resp;

import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 星球标签关联信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球标签关联信息")
public class PlanetTagRelationResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    private Long planetId;

    /**
     * 标签ID
     */
    @Schema(description = "标签ID")
    private Long tagId;

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