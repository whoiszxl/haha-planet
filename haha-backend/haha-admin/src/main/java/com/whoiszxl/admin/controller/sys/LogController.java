package com.whoiszxl.admin.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.whoiszxl.admin.cqrs.command.LogResp;
import com.whoiszxl.admin.cqrs.query.AdminLogQuery;
import com.whoiszxl.admin.cqrs.response.LogDetailResp;
import com.whoiszxl.admin.service.IAdminLogService;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.crud.model.SortQuery;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统日志 API
 * @author whoiszxl
 */
@Tag(name = "系统日志 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/log")
public class LogController {

    private final IAdminLogService baseService;

    @Operation(summary = "分页查询列表", description = "分页查询列表")
    @SaCheckPermission("monitor:log:list")
    @GetMapping
    public R<PageResponse<LogResp>> page(AdminLogQuery query, @Validated PageQuery pageQuery) {
        return R.ok(baseService.page(query, pageQuery));
    }

    @Operation(summary = "查询详情", description = "查询详情")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("monitor:log:list")
    @GetMapping("/{id}")
    public R<LogDetailResp> get(@PathVariable Long id) {
        return R.ok(baseService.get(id));
    }

    @Operation(summary = "导出登录日志", description = "导出登录日志")
    @SaCheckPermission("monitor:log:export")
    @GetMapping("/export/login")
    public void exportLoginLog(AdminLogQuery query, SortQuery sortQuery, HttpServletResponse response) {
        baseService.exportLoginLog(query, sortQuery, response);
    }

    @Operation(summary = "导出操作日志", description = "导出操作日志")
    @SaCheckPermission("monitor:log:export")
    @GetMapping("/export/operation")
    public void exportOperationLog(AdminLogQuery query, SortQuery sortQuery, HttpServletResponse response) {
        baseService.exportOperationLog(query, sortQuery, response);
    }
}
