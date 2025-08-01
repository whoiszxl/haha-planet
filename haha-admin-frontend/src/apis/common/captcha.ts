import type * as Common from './type'
import http from '@/utils/http'

const BASE_URL = '/captcha'

/** @desc 获取图片验证码 */
export function getImageCaptcha() {
  return http.get<Common.ImageCaptchaResp>(`${BASE_URL}/image`)
}

/** @desc 获取短信验证码 */
export function getSmsCaptcha(query: { phone: string }) {
  return http.get<boolean>(`${BASE_URL}/sms`, query)
}

/** @desc 获取邮箱验证码 */
export function getEmailCaptcha(query: { email: string }) {
  return http.get<boolean>(`${BASE_URL}/mail`, query)
}

/** @desc 获取行为验证码 */
export function getBehaviorCaptcha(req: any) {
  return http.get<Common.BehaviorCaptchaResp>(`${BASE_URL}/behavior`, req)
}

/** @desc 校验行为验证码 */
export function checkBehaviorCaptcha(req: any) {
  return http.post<Common.CheckBehaviorCaptchaResp>(`${BASE_URL}/behavior`, req)
}

/** @desc 生成Google验证码 */
export function generateGoogleCaptcha() {
  return http.get<Common.GoogleCaptchaResp>(`${BASE_URL}/google`)
}

/** @desc 验证Google验证码 */
export function validateGoogleCaptcha(data: { code: string }) {
  return http.post<Common.ValidateResp>(`${BASE_URL}/google/validate`, {}, { params: data })
}

/** @desc 检查Google验证码绑定状态 */
export function checkGoogleCaptchaStatus() {
  return http.get<Common.GoogleStatusResp>(`${BASE_URL}/google/status`)
}

/** @desc 生成备用恢复码 */
export function generateBackupCodes(data: { count?: number } = {}) {
  return http.post<Common.BackupCodesResp>(`${BASE_URL}/google/backup-codes`, {}, { params: data })
}

/** @desc 验证备用恢复码 */
export function validateBackupCode(data: { backupCode: string }) {
  return http.post<Common.ValidateResp>(`${BASE_URL}/google/backup-codes/validate`, {}, { params: data })
}

/** @desc 解绑Google验证器 */
export function unbindGoogleCaptcha() {
  return http.del(`${BASE_URL}/google/unbind`)
}

/** @desc 公开检查Google验证码绑定状态（登录前使用） */
export function checkGoogleCaptchaStatusByUsername(username: string) {
  return http.get<Common.GoogleStatusResp>(`${BASE_URL}/google/status/public`, { username })
}
