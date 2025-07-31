package com.whoiszxl.admin.cqrs.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.io.Serial;
import java.io.Serializable;

/**
 * 部门查询条件
 */
@Data
@ParameterObject
@Schema(description = "部门查询条件")
public class DeptQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "关键词", example = "测试部")
    private String description;

    @Schema(description = "状态（1：启用；2：禁用）", example = "1")
    private Integer status;
}
