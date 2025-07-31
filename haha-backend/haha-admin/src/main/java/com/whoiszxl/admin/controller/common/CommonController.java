package com.whoiszxl.admin.controller.common;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.whoiszxl.admin.cqrs.query.DeptQuery;
import com.whoiszxl.admin.cqrs.query.MenuQuery;
import com.whoiszxl.admin.cqrs.query.RoleQuery;
import com.whoiszxl.admin.service.IDeptService;
import com.whoiszxl.admin.service.IMenuService;
import com.whoiszxl.admin.service.IRoleService;
import com.whoiszxl.common.model.LabelValueResp;
import com.whoiszxl.starter.core.config.ProjectProperties;
import com.whoiszxl.starter.crud.model.SortQuery;
import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author whoiszxl
 */
@Tag(name = "公共 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final ProjectProperties projectProperties;

    private final IDeptService deptService;

    private final IMenuService menuService;

    private final IRoleService roleService;

    @Operation(summary = "查询部门树", description = "查询树结构的部门列表")
    @GetMapping("/tree/dept")
    public R<List<Tree<Long>>> listDeptTree(DeptQuery query, SortQuery sortQuery) {
        return R.ok(deptService.tree(query, sortQuery, true));
    }

    @Operation(summary = "查询菜单树", description = "查询树结构的菜单列表")
    @GetMapping("/tree/menu")
    public R<List<Tree<Long>>> listMenuTree(MenuQuery query, SortQuery sortQuery) {
        return R.ok(menuService.tree(query, sortQuery, true));
    }

    @Operation(summary = "查询角色字典", description = "查询角色字典列表")
    @GetMapping("/dict/role")
    public R<List<LabelValueResp<Long>>> listRoleDict(RoleQuery query, SortQuery sortQuery) {
        return R.ok(roleService.buildDict(roleService.list(query, sortQuery)));
    }

    @Operation(summary = "查询字典", description = "查询字典列表")
    @Parameter(name = "code", description = "字典编码", example = "notice_type", in = ParameterIn.PATH)
    @GetMapping("/dict/{code}")
    public R<List<LabelValueResp<Serializable>>> listDict(@PathVariable String code) {
        Optional<Class<?>> enumClassOptional = this.getEnumClassByName(code);
        return R.ok(enumClassOptional.map(this::listEnumDict).get());
    }

    /**
     * 根据枚举类名查询
     *
     * @param enumClassName 枚举类名
     * @return 枚举类型
     */
    private Optional<Class<?>> getEnumClassByName(String enumClassName) {
        Set<Class<?>> classSet = ClassUtil.scanPackageBySuper(projectProperties.getBasePackage(), IBaseEnum.class);
        return classSet.stream()
                .filter(c -> StrUtil.equalsAnyIgnoreCase(c.getSimpleName(), enumClassName, StrUtil
                        .toCamelCase(enumClassName)))
                .findFirst();
    }

    /**
     * 查询枚举字典
     *
     * @param enumClass 枚举类型
     * @return 枚举字典
     */
    private List<LabelValueResp<Serializable>> listEnumDict(Class<?> enumClass) {
        Object[] enumConstants = enumClass.getEnumConstants();
        return Arrays.stream(enumConstants).map(e -> {
            IBaseEnum baseEnum = (IBaseEnum)e;
            return new LabelValueResp<>(baseEnum.getDescription(), baseEnum.getValue(), baseEnum.getColor());
        }).toList();
    }

}
