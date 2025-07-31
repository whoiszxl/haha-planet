package com.whoiszxl.starter.crud.model;

import cn.crane4j.annotation.Assemble;
import cn.crane4j.annotation.Mapping;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whoiszxl.starter.crud.constants.FillConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 响应基类
 */
@Getter
@Setter
@Schema(description = "响应基类")
public class BaseResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID", example = "1")
    @ExcelProperty(value = {"ID"}, order = 1)
    private Long id;

    @Schema(description = "创建人ID")
    @JsonIgnore
    @Assemble(container = FillConstants.ADMIN_NICKNAME, props = @Mapping(ref = "createdByString"))
    private Long createdBy;

    @Schema(description = "创建人", example = "超级管理员")
    @ExcelProperty(value = "创建人", order = Integer.MAX_VALUE - 4)
    private String createdByString;

    @Schema(description = "修改时间", example = "2008-08-08 08:08:08", type = "string")
    @ExcelProperty(value = "修改时间", order = Integer.MAX_VALUE - 1)
    private LocalDateTime createdAt;

    @Schema(description = "更新人ID")
    @JsonIgnore
    @Assemble(container = FillConstants.ADMIN_NICKNAME, props = @Mapping(ref = "updatedByString"))
    private Long updatedBy;

    @Schema(description = "更新人", example = "超级管理员")
    @ExcelProperty(value = "更新人", order = Integer.MAX_VALUE - 4)
    private String updatedByString;

    @Schema(description = "修改时间", example = "2008-08-08 08:08:08", type = "string")
    @ExcelProperty(value = "修改时间", order = Integer.MAX_VALUE - 1)
    private LocalDateTime updatedAt;

}
