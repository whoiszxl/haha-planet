package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.response.dashboard.*;

import java.util.List;

/**
 * 仪表盘业务接口
 * @author whoiszxl
 */
public interface DashboardService {

    /**
     * 查询总计信息
     *
     * @return 总计信息
     */
    DashboardTotalResp getTotal();

    /**
     * 查询访问趋势信息
     *
     * @param days 日期数
     * @return 访问趋势信息
     */
    List<DashboardAccessTrendResp> listAccessTrend(Integer days);

    /**
     * 查询热门模块列表
     *
     * @return 热门模块列表
     */
    List<DashboardPopularModuleResp> listPopularModule();

    /**
     * 查询访客地域分布信息
     *
     * @return 访客地域分布信息
     */
    DashboardGeoDistributionResp getGeoDistribution();

    /**
     * 查询系统性能指标
     *
     * @return 系统性能指标
     */
    DashboardPerformanceResp getPerformance();

    /**
     * 查询活跃时段分布
     *
     * @return 活跃时段分布
     */
    List<DashboardHourlyActivityResp> listHourlyActivity();

    /**
     * 查询客户端统计信息
     *
     * @return 客户端统计信息
     */
    DashboardClientStatsResp getClientStats();

    /**
     * 查询最近活跃用户
     *
     * @return 最近活跃用户
     */
    List<DashboardRecentUsersResp> listRecentUsers();

}
