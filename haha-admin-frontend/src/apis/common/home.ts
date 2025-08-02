import type * as Common from './type'
import http from '@/utils/http'

const BASE_URL = '/dashboard'

/** @desc 查询总计信息 */
export function getDashboardTotal() {
  return http.get<Common.DashboardTotalResp>(`${BASE_URL}/total`)
}

/** @desc 查询访问趋势 */
export function listDashboardAccessTrend(days: number) {
  return http.get<Common.DashboardAccessTrendResp[]>(`${BASE_URL}/access/trend/${days}`)
}

/** @desc 查询热门模块列表 */
export function listDashboardPopularModule() {
  return http.get<Common.DashboardPopularModuleResp[]>(`${BASE_URL}/popular/module`)
}

// 获取地域分布
export const getDashboardGeoDistribution = () => {
  return http.get<Common.DashboardGeoDistributionResp>(`${BASE_URL}/geo/distribution`)
}

// 获取系统性能指标
export const getDashboardPerformance = () => {
  return http.get<Common.DashboardPerformanceResp>(`${BASE_URL}/performance`)
}

// 获取活跃时段分布
export const listDashboardHourlyActivity = () => {
  return http.get<Common.DashboardHourlyActivityResp[]>(`${BASE_URL}/hourly/activity`)
}

// 获取浏览器/操作系统分布
export const getDashboardClientStats = () => {
  return http.get<Common.DashboardClientStatsResp>(`${BASE_URL}/client/stats`)
}

// 获取最近活跃用户
export const listDashboardRecentUsers = () => {
  return http.get<Common.DashboardRecentUsersResp[]>(`${BASE_URL}/recent/users`)
}

/** @desc 查询公告列表 */
export function listDashboardNotice() {
  return http.get<Common.DashboardNoticeResp[]>(`${BASE_URL}/notice`)
}
