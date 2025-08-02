package com.whoiszxl.admin.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.whoiszxl.admin.cqrs.response.dashboard.*;
import com.whoiszxl.admin.cqrs.response.dashboard.DashboardClientStatsResp.ClientStatItem;
import com.whoiszxl.admin.service.DashboardService;
import com.whoiszxl.admin.service.IAdminLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘业务实现
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final IAdminLogService logService;

    @Override
    public DashboardTotalResp getTotal() {
        DashboardTotalResp totalResp = logService.getDashboardTotal();
        Long todayPvCount = totalResp.getTodayPvCount() != null ? totalResp.getTodayPvCount() : 0L;
        Long yesterdayPvCount = totalResp.getYesterdayPvCount() != null ? totalResp.getYesterdayPvCount() : 0L;
        BigDecimal newPvCountFromYesterday = NumberUtil.sub(todayPvCount, yesterdayPvCount);
        BigDecimal newPvFromYesterday = (yesterdayPvCount == null || yesterdayPvCount == 0)
            ? BigDecimal.valueOf(100)
            : NumberUtil.round(NumberUtil.mul(NumberUtil.div(newPvCountFromYesterday, yesterdayPvCount), 100), 1);
        totalResp.setNewPvFromYesterday(newPvFromYesterday);
        return totalResp;
    }

    @Override
    public List<DashboardAccessTrendResp> listAccessTrend(Integer days) {
        return logService.listDashboardAccessTrend(days);
    }

    @Override
    public List<DashboardPopularModuleResp> listPopularModule() {
        List<DashboardPopularModuleResp> popularModuleList = logService.listDashboardPopularModule();
        for (DashboardPopularModuleResp popularModule : popularModuleList) {
            Long todayPvCount = popularModule.getTodayPvCount() != null ? popularModule.getTodayPvCount() : 0L;
            Long yesterdayPvCount = popularModule.getYesterdayPvCount() != null ? popularModule.getYesterdayPvCount() : 0L;
            BigDecimal newPvCountFromYesterday = NumberUtil.sub(todayPvCount, yesterdayPvCount);
            BigDecimal newPvFromYesterday = (yesterdayPvCount == null || yesterdayPvCount == 0)
                ? BigDecimal.valueOf(100)
                : NumberUtil.round(NumberUtil.mul(NumberUtil.div(newPvCountFromYesterday, yesterdayPvCount), 100), 1);
            popularModule.setNewPvFromYesterday(newPvFromYesterday);
        }
        return popularModuleList;
    }

    @Override
    public DashboardGeoDistributionResp getGeoDistribution() {
        List<Map<String, Object>> locationIpStatistics = logService.listDashboardGeoDistribution();
        DashboardGeoDistributionResp geoDistribution = new DashboardGeoDistributionResp();
        geoDistribution.setLocationIpStatistics(locationIpStatistics);
        geoDistribution.setLocations(locationIpStatistics.stream().map(m -> Convert.toStr(m.get("name"))).toList());
        return geoDistribution;
    }

    @Override
    public DashboardPerformanceResp getPerformance() {
        return logService.getDashboardPerformance();
    }

    @Override
    public List<DashboardHourlyActivityResp> listHourlyActivity() {
        return logService.listDashboardHourlyActivity();
    }

    @Override
    public DashboardClientStatsResp getClientStats() {
        List<Map<String, Object>> clientStats = logService.listDashboardClientStats();
        
        DashboardClientStatsResp result = new DashboardClientStatsResp();
        List<ClientStatItem> browsers = new ArrayList<>();
        List<ClientStatItem> operatingSystems = new ArrayList<>();
        
        for (Map<String, Object> stat : clientStats) {
            String type = Convert.toStr(stat.get("type"));
            String name = Convert.toStr(stat.get("name"));
            Long value = Convert.toLong(stat.get("value"));
            
            ClientStatItem item = new ClientStatItem();
            item.setName(name);
            item.setValue(value);
            
            if ("browsers".equals(type)) {
                browsers.add(item);
            } else if ("os".equals(type)) {
                operatingSystems.add(item);
            }
        }
        
        result.setBrowsers(browsers);
        result.setOperatingSystems(operatingSystems);
        return result;
    }

    @Override
    public List<DashboardRecentUsersResp> listRecentUsers() {
        return logService.listDashboardRecentUsers();
    }
}
