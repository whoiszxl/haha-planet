package com.whoiszxl.admin.cqrs.query;

import cn.hutool.core.date.DatePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author whoiszxl
 */
@Data
@ParameterObject
@Schema(description = "管理员查询条件")
public class AdminQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "部门 ID", example = "1")
    private Long deptId;

    @Schema(description = "关键词", example = "香蕉")
    private String description;

    @Schema(description = "状态（1：启用；2：禁用）", example = "1")
    private Integer status;

    @Schema(description = "创建时间", example = "2023-08-08 00:00:00,2023-08-08 23:59:59")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @Size(max = 2, message = "创建时间必须是一个范围")
    private List<Date> createdAt;
}
