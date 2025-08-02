package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.command.LogResp;
import com.whoiszxl.admin.cqrs.query.AdminLogQuery;
import com.whoiszxl.admin.cqrs.response.LogDetailResp;
import com.whoiszxl.admin.cqrs.response.dashboard.*;
import com.whoiszxl.admin.entity.AdminLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.crud.model.SortQuery;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IAdminLogService extends IService<AdminLog> {

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 分页列表信息
     */
    PageResponse<LogResp> page(AdminLogQuery query, PageQuery pageQuery);

    /**
     * 查询详情
     *
     * @param id ID
     * @return 详情信息
     */
    LogDetailResp get(Long id);

    /**
     * 导出登录日志
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @param response  响应对象
     */
    void exportLoginLog(AdminLogQuery query, SortQuery sortQuery, HttpServletResponse response);

    /**
     * 导出操作日志
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @param response  响应对象
     */
    void exportOperationLog(AdminLogQuery query, SortQuery sortQuery, HttpServletResponse response);

    /**
     * 查询仪表盘总计信息
     *
     * @return 仪表盘总计信息
     */
    DashboardTotalResp getDashboardTotal();

    /**
     * 查询仪表盘访问趋势信息
     *
     * @param days 日期数
     * @return 仪表盘访问趋势信息
     */
    List<DashboardAccessTrendResp> listDashboardAccessTrend(Integer days);

    /**
     * 查询仪表盘热门模块列表
     *
     * @return 仪表盘热门模块列表
     */
    List<DashboardPopularModuleResp> listDashboardPopularModule();

    /**
     * 查询仪表盘访客地域分布信息
     *
     * @return 仪表盘访客地域分布信息
     */
    List<Map<String, Object>> listDashboardGeoDistribution();

    /**
     * 查询仪表盘系统性能指标
     *
     * @return 仪表盘系统性能指标
     */
    DashboardPerformanceResp getDashboardPerformance();

    /**
     * 查询仪表盘24小时活跃分布
     *
     * @return 仪表盘24小时活跃分布
     */
    List<DashboardHourlyActivityResp> listDashboardHourlyActivity();

    /**
     * 查询仪表盘客户端统计信息
     *
     * @return 仪表盘客户端统计信息
     */
    List<Map<String, Object>> listDashboardClientStats();

    /**
     * 查询仪表盘最近活跃用户
     *
     * @return 仪表盘最近活跃用户
     */
    List<DashboardRecentUsersResp> listDashboardRecentUsers();
}
