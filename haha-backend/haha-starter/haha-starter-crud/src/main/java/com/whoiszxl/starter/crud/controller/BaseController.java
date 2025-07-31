package com.whoiszxl.starter.crud.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.text.CharSequenceUtil;
import com.whoiszxl.starter.core.constants.StringConstants;
import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.enums.Api;
import com.whoiszxl.starter.crud.model.BaseReq;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.crud.model.SortQuery;
import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.starter.crud.utils.ValidateGroup;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础控制器
 * @param <S> 业务接口
 * @param <L> 列表类型
 * @param <D> 详情类型
 * @param <Q> 查询条件
 * @param <C> 创建或修改类型
 * @author whoiszxl
 */
public abstract class BaseController<S extends BaseService<L, D, Q, C>, L, D, Q, C extends BaseReq> {

    @Autowired
    protected S baseService;

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 分页信息
     */
    @Operation(summary = "分页查询列表", description = "分页查询列表")
    @ResponseBody
    @GetMapping
    public R<PageResponse<L>> page(Q query, @Validated PageQuery pageQuery) {
        this.checkPermission(Api.LIST);
        return R.ok(baseService.page(query, pageQuery));
    }

    /**
     * 查询树列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 树列表信息
     */
    @Operation(summary = "查询树列表", description = "查询树列表")
    @ResponseBody
    @GetMapping("/tree")
    public R<List<Tree<Long>>> tree(Q query, SortQuery sortQuery) {
        this.checkPermission(Api.LIST);
        return R.ok(baseService.tree(query, sortQuery, false));
    }

    /**
     * 查询列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 列表信息
     */
    @Operation(summary = "查询列表", description = "查询列表")
    @ResponseBody
    @GetMapping("/list")
    public R<List<L>> list(Q query, SortQuery sortQuery) {
        this.checkPermission(Api.LIST);
        return R.ok(baseService.list(query, sortQuery));
    }

    /**
     * 查询详情
     *
     * @param id ID
     * @return 详情信息
     */
    @Operation(summary = "查询详情", description = "查询详情")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @ResponseBody
    @GetMapping("/{id}")
    public R<D> get(@PathVariable("id") Long id) {
        this.checkPermission(Api.LIST);
        return R.ok(baseService.get(id));
    }

    /**
     * 新增
     *
     * @param req 创建信息
     * @return 自增 ID
     */
    @Operation(summary = "新增数据", description = "新增数据")
    @ResponseBody
    @PostMapping
    public R<Long> add(@Validated(ValidateGroup.Crud.Add.class) @RequestBody C req) {
        this.checkPermission(Api.ADD);
        return R.ok("新增成功", baseService.add(req));
    }

    /**
     * 修改
     *
     * @param req 修改信息
     * @param id  ID
     * @return /
     */
    @Operation(summary = "修改数据", description = "修改数据")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @ResponseBody
    @PutMapping("/{id}")
    public R<Void> update(@Validated(ValidateGroup.Crud.Update.class) @RequestBody C req, @PathVariable Long id) {
        this.checkPermission(Api.UPDATE);
        baseService.update(req, id);
        return R.ok("修改成功");
    }

    /**
     * 删除
     *
     * @param ids ID 列表
     * @return /
     */
    @Operation(summary = "删除数据", description = "删除数据")
    @Parameter(name = "ids", description = "ID 列表", example = "1,2", in = ParameterIn.PATH)
    @ResponseBody
    @DeleteMapping("/{ids}")
    public R<Void> delete(@PathVariable("ids") List<Long> ids) {
        this.checkPermission(Api.DELETE);
        baseService.delete(ids);
        return R.ok("删除成功");
    }

    /**
     * 导出
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @param response  响应对象
     */
    @Operation(summary = "导出数据", description = "导出数据")
    @GetMapping("/export")
    public void export(Q query, SortQuery sortQuery, HttpServletResponse response) {
        this.checkPermission(Api.EXPORT);
        baseService.export(query, sortQuery, response);
    }

    /**
     * 根据 API 类型进行权限验证
     *
     * @param api API 类型
     */
    protected void checkPermission(Api api) {
        CrudRequestMapping crudRequestMapping = this.getClass().getDeclaredAnnotation(CrudRequestMapping.class);
        String path = crudRequestMapping.value();
        String permissionPrefix = String.join(StringConstants.COLON, CharSequenceUtil
                .splitTrim(path, StringConstants.SLASH));
        StpUtil.checkPermission("%s:%s".formatted(permissionPrefix, api.name().toLowerCase()));
    }
}
