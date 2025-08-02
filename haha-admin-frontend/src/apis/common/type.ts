/** 图形验证码类型 */
export interface ImageCaptchaResp {
  uuid: string
  img: string
  expireTime: number
}

/** 仪表盘访问趋势类型 */
export interface DashboardAccessTrendResp {
  date: string
  pvCount: number
  ipCount: number
}

/** 仪表盘公告类型 */
export interface DashboardNoticeResp {
  id: number
  title: string
  type: number
}

/** 仪表盘总计信息类型 */
export interface DashboardTotalResp {
  pvCount: number
  ipCount: number
  todayPvCount: number
  newPvFromYesterday: number
}

/** 仪表盘热门模块类型 */
export interface DashboardPopularModuleResp {
  module: string
  pvCount: number
  newPvFromYesterday: number
}

/** 仪表盘地域分布类型 */
export interface DashboardGeoDistributionResp {
  locations: string[]
  locationIpStatistics: Array<{
    name: string
    value: number
  }>
}

/** 系统性能指标类型 */
export interface DashboardPerformanceResp {
  avgResponseTime: number
  slowRequestCount: number
  errorRate: number
  totalRequests: number
}

/** 活跃时段分布类型 */
export interface DashboardHourlyActivityResp {
  hour: number
  count: number
}

/** 浏览器/操作系统分布类型 */
export interface DashboardClientStatsResp {
  browsers: Array<{
    name: string
    value: number
  }>
  operatingSystems: Array<{
    name: string
    value: number
  }>
}

/** 最近活跃用户类型 */
export interface DashboardRecentUsersResp {
  nickname: string
  lastAccessTime: string
  accessCount: number
  ipAddress: string
}

/* 行为验证码类型 */
export interface BehaviorCaptchaResp {
  originalImageBase64: string
  point: {
    x: number
    y: number
  }
  jigsawImageBase64: string
  token: string
  secretKey: string
  wordList: string[]
}

export interface CheckBehaviorCaptchaResp {
  repCode: string
  repMsg: string
}

/** Google验证码生成响应类型 */
export interface GoogleCaptchaResp {
  userId: string
  secretKey: string
  qrCodeUrl: string
  qrCodeImage: string
  backupCodes: string[]
  timestamp: number
}

/** 验证响应类型 */
export interface ValidateResp {
  valid: boolean
  message: string
}

/** Google验证码状态响应类型 */
export interface GoogleStatusResp {
  bound: boolean
  message: string
}

/** 备用恢复码响应类型 */
export interface BackupCodesResp {
  backupCodes: string[]
  count: number
}

/** 解绑响应类型 */
export interface UnbindResp {
  message: string
}
