package com.whoiszxl.admin.cqrs.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

/**
 * @author whoiszxl
 */
@Data
@ParameterObject
@Schema(description = "角色查询条件")
public class RoleQuery {

    @Schema(description = "用户名")
    private String name;
}
