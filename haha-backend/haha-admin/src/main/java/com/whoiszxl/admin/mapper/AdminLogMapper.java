package com.whoiszxl.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.whoiszxl.admin.cqrs.command.LogResp;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardAccessTrendResp;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardPopularModuleResp;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardTotalResp;
import com.whoiszxl.admin.entity.AdminLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志表 Mapper 接口
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Mapper
public interface AdminLogMapper extends BaseMapper<AdminLog> {

    /**
     * 分页查询列表
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return 分页列表信息
     */
    @Select("""
        SELECT t1.id, t1.description, t1.module, t1.consuming_time, t1.ip, t1.ip_address, t1.browser, t1.os, t1.status,
            t1.created_by, t1.created_at, t2.nickname as createdByString
        FROM sys_admin_log AS t1
        LEFT JOIN sys_admin AS t2 ON t2.id = t1.created_by
        ${ew.customSqlSegment}
    """)
    IPage<LogResp> selectLogPage(@Param("page") IPage<AdminLog> page,
                                 @Param(Constants.WRAPPER) QueryWrapper<AdminLog> queryWrapper);

    /**
     * 查询列表
     *
     * @param queryWrapper 查询条件
     * @return 列表信息
     */
    @Select("""
        SELECT t1.id, t1.description, t1.module, t1.consuming_time, t1.ip, t1.ip_address, t1.browser, t1.os, t1.status,
            t1.created_by, t1.created_at, t2.nickname as createdByString
        FROM sys_admin_log AS t1
        LEFT JOIN sys_admin AS t2 ON t2.id = t1.created_by
        ${ew.customSqlSegment}
    """)
    List<LogResp> selectLogList(@Param(Constants.WRAPPER) QueryWrapper<AdminLog> queryWrapper);

    /**
     * 查询仪表盘总计信息
     *
     * @return 仪表盘总计信息
     */
    @Select("""
        SELECT
        (SELECT COUNT(*) FROM sys_admin_log) AS pvCount,
        (SELECT COUNT(DISTINCT ip) FROM sys_admin_log) AS ipCount,
        (SELECT COUNT(*) FROM sys_admin_log WHERE DATE(created_at) = CURRENT_DATE) AS todayPvCount,
        (SELECT COUNT(*) FROM sys_admin_log WHERE DATE(created_at) = CURRENT_DATE - 1) AS yesterdayPvCount
    """)
    DashboardTotalResp selectDashboardTotal();

    /**
     * 查询仪表盘访问趋势信息
     *
     * @param days 日期数
     * @return 仪表盘访问趋势信息
     */
    @Select("""
            SELECT
                DATE(created_at) AS date,
                COUNT(*) AS pvCount,
                COUNT(DISTINCT ip) AS ipCount
            FROM sys_admin_log
            WHERE DATE(created_at) != CURRENT_DATE
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at) DESC
            LIMIT #{days}
    """)
    List<DashboardAccessTrendResp> selectListDashboardAccessTrend(@Param("days") Integer days);

    /**
     * 查询仪表盘热门模块列表
     *
     * @return 仪表盘热门模块列表
     */
    @Select("""
        SELECT
            module,
            COUNT(*) AS pvCount,
            SUM(CASE WHEN DATE(created_at) = CURRENT_DATE THEN 1 ELSE 0 END) AS todayPvCount,
            SUM(CASE WHEN DATE(created_at) = CURRENT_DATE - 1 THEN 1 ELSE 0 END) AS yesterdayPvCount
        FROM sys_admin_log
        WHERE module != '验证码' AND module != '登录'
        GROUP BY module
        ORDER BY pvCount DESC
        LIMIT 10
    """)
    List<DashboardPopularModuleResp> selectListDashboardPopularModule();

    /**
     * 查询仪表盘访客地域分布信息
     *
     * @return 仪表盘访客地域分布信息
     */
    @Select("""
    SELECT
        CASE
            WHEN POSITION(' ' IN ip_address) > 0 THEN SUBSTRING(ip_address FROM 1 FOR POSITION(' ' IN ip_address) - 1)
            ELSE ip_address
        END AS name,
        COUNT(DISTINCT ip) AS value
    FROM sys_admin_log
    GROUP BY name
    ORDER BY value DESC
    LIMIT 10
    """)
    List<Map<String, Object>> selectListDashboardGeoDistribution();
}
