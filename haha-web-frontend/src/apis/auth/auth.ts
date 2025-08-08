import http from '../../utils/http'
import type * as T from './type'
const BASE_URL = '/user/api/auth'


/** @desc 登录接口，兼容手机号、账号、邮箱、第三方平台登录 */
export function login(req: any) {
  return http.post<T.LoginResp>(`${BASE_URL}/login`, req)
}

/** @desc 退出登录 */
export function logout() {
  return http.post(`${BASE_URL}/logout`)
}

/** @desc 三方账号登录授权 */
export function socialAuth(source: string) {
  return http.get<T.SocialAuthAuthorizeResp>(`${BASE_URL}/social/${source}`)
}

/** @desc 绑定三方账号 */
export function bindSocialAccount(source: string, data: any) {
  return http.post(`${BASE_URL}/social/bind/${source}`, data)
}

/** @desc 获取当前登录用户信息 */
export function getMemberInfo() {
  return http.get<T.MemberInfoResponse>(`${BASE_URL}/member/info`)
}
