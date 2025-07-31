package com.whoiszxl.admin.generator.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 表信息查询条件
 * @author whoiszxl
 */
@Data
@Schema(description = "表信息查询条件")
public class TableQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 表名称
     */
    @Schema(description = "表名称", example = "sys_user")
    private String tableName;
}
