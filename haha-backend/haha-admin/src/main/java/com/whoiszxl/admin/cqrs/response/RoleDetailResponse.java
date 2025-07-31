package com.whoiszxl.admin.cqrs.response;

import cn.crane4j.annotation.Assemble;
import cn.crane4j.annotation.Mapping;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.whoiszxl.common.enums.DataScopeEnum;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.constants.FillConstants;
import com.whoiszxl.starter.crud.converter.ExcelBaseEnumConverter;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

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
@ExcelIgnoreUnannotated
@Schema(description = "角色详情信息")
@Assemble(container = FillConstants.ROLE_DEPT_ID_LIST, key = "id", props = @Mapping(ref = "deptIds"))
public class RoleDetailResponse extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称", example = "测试人员")
    @ExcelProperty(value = "名称")
    private String name;

    @Schema(description = "编码", example = "test")
    @ExcelProperty(value = "编码")
    private String code;

    @Schema(description = "描述", example = "测试人员描述信息")
    @ExcelProperty(value = "描述")
    private String description;

    @Schema(name = "数据权限")
    @ExcelProperty(value = "数据权限", converter = ExcelBaseEnumConverter.class)
    private DataScopeEnum dataScope;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(description = "功能权限：菜单 ID 列表", example = "1000,1010,1011,1012,1013,1014")
    private List<Long> menuIds;

    @Schema(description = "权限范围：部门 ID 列表", example = "5")
    private List<Long> deptIds;
}
