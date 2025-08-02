package com.whoiszxl.admin.controller.common;

import com.whoiszxl.admin.cqrs.response.dashboard.*;
import com.whoiszxl.admin.service.DashboardService;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.log.core.annotation.Log;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 仪表盘 API
 * @author whoiszxl
 */
@Tag(name = "仪表盘 API")
@Log(ignore = true)
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "查询总计信息", description = "查询总计信息")
    @GetMapping("/total")
    public R<DashboardTotalResp> getTotal() {
        return R.ok(dashboardService.getTotal());
    }

    @Operation(summary = "查询访问趋势信息", description = "查询访问趋势信息")
    @Parameter(name = "days", description = "日期数", example = "30", in = ParameterIn.PATH)
    @GetMapping("/access/trend/{days}")
    public R<List<DashboardAccessTrendResp>> listAccessTrend(@PathVariable Integer days) {
        ValidationUtils.throwIf(7 != days && 30 != days, "仅支持查询近 7/30 天访问趋势信息");
        return R.ok(dashboardService.listAccessTrend(days));
    }

    @Operation(summary = "查询热门模块列表", description = "查询热门模块列表")
    @GetMapping("/popular/module")
    public R<List<DashboardPopularModuleResp>> listPopularModule() {
        return R.ok(dashboardService.listPopularModule());
    }

    @Operation(summary = "查询访客地域分布信息", description = "查询访客地域分布信息")
    @GetMapping("/geo/distribution")
    public R<DashboardGeoDistributionResp> getGeoDistribution() {
        return R.ok(dashboardService.getGeoDistribution());
    }

    @Operation(summary = "查询系统性能指标", description = "查询系统性能指标")
    @GetMapping("/performance")
    public R<DashboardPerformanceResp> getPerformance() {
        return R.ok(dashboardService.getPerformance());
    }

    @Operation(summary = "查询活跃时段分布", description = "查询24小时活跃时段分布")
    @GetMapping("/hourly/activity")
    public R<List<DashboardHourlyActivityResp>> listHourlyActivity() {
        return R.ok(dashboardService.listHourlyActivity());
    }

    @Operation(summary = "查询客户端统计信息", description = "查询浏览器和操作系统分布")
    @GetMapping("/client/stats")
    public R<DashboardClientStatsResp> getClientStats() {
        return R.ok(dashboardService.getClientStats());
    }

    @Operation(summary = "查询最近活跃用户", description = "查询最近活跃用户列表")
    @GetMapping("/recent/users")
    public R<List<DashboardRecentUsersResp>> listRecentUsers() {
        return R.ok(dashboardService.listRecentUsers());
    }

}
