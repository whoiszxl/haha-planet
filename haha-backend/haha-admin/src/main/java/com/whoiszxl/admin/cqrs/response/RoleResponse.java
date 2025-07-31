package com.whoiszxl.admin.cqrs.response;

import com.whoiszxl.common.enums.DataScopeEnum;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleResponse extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private Long id;

    @Schema(name = "角色名称")
    private String name;

    @Schema(name = "角色代码")
    private String code;

    @Schema(name = "角色描述")
    private String description;

    @Schema(name = "数据权限")
    private DataScopeEnum dataScope;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;
}
