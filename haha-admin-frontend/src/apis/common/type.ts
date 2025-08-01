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
