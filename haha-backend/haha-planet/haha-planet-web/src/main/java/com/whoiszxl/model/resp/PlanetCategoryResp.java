package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 星球分类响应类
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球分类响应")
public class PlanetCategoryResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "分类ID")
    private Long id;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分类图标链接")
    private String iconUrl;

    @Schema(description = "父分类ID，0表示顶级分类")
    private Long parentId;

    @Schema(description = "分类级别:1->1级; 2->2级 3->3级")
    private Boolean level;

    @Schema(description = "排序,数值越大越靠前")
    private Integer sort;

    @Schema(description = "业务状态")
    private Integer status;
}
