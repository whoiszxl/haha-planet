package com.whoiszxl.admin.cqrs.response;

import cn.crane4j.annotation.condition.ConditionOnExpression;
import com.alibaba.excel.annotation.ExcelProperty;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.annotation.TreeField;
import com.whoiszxl.starter.crud.converter.ExcelBaseEnumConverter;
import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author whoiszxl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TreeField(value = "id", nameKey = "name")
@Schema(description = "部门信息")
public class DeptResponse extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "名称", example = "测试部")
    @ExcelProperty(value = "名称", order = 2)
    private String name;

    @Schema(description = "上级部门 ID", example = "2")
    @ConditionOnExpression(value = "#target.parentId != 0")
    @ExcelProperty(value = "上级部门 ID", order = 3)
    private Long parentId;

    @Schema(description = "上级部门", example = "天津总部")
    @ExcelProperty(value = "上级部门", order = 4)
    private String parentName;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    @ExcelProperty(value = "状态", converter = ExcelBaseEnumConverter.class, order = 5)
    private DisEnableStatusEnum status;

    @Schema(description = "排序", example = "3")
    @ExcelProperty(value = "排序", order = 6)
    private Integer sort;

    @Schema(description = "描述", example = "测试部描述信息")
    @ExcelProperty(value = "描述", order = 8)
    private String description;

}

