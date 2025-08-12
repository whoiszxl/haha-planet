package com.whoiszxl.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 星球列表查询请求类
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球列表查询请求")
public class PlanetListReq {

    @Schema(description = "分类ID")
    private Long categoryId = -1L;

    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码必须大于0")
    private Integer page = 1;

    @Schema(description = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer pageSize = 10;

    @Schema(description = "排序方式: 1-最新 2-最热 3-推荐", example = "1")
    private Integer sortType = 1;
}
