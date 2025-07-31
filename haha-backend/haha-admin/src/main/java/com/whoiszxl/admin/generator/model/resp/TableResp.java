package com.whoiszxl.admin.generator.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表信息
 * @author whoiszxl
 */
@Data
@Schema(description = "表信息")
public class TableResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 表名称
     */
    @Schema(description = "表名称", example = "sys_user")
    private String tableName;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "用户表")
    private String comment;

    /**
     * 存储引擎
     */
    @Schema(description = "存储引擎", example = "InnoDB")
    private String engine;

    /**
     * 字符集
     */
    @Schema(description = "字符集", example = "utf8mb4_general_ci")
    private String charset;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-08-08 08:08:08", type = "string")
    private LocalDateTime createTime;

    /**
     * 是否已配置
     */
    @Schema(description = "是否已配置", example = "true")
    private Boolean isConfiged;

    /**
     * 是否禁用
     */
    @Schema(description = "是否禁用", example = "true")
    private Boolean disabled;

    public Boolean getDisabled() {
        return !isConfiged;
    }
}
