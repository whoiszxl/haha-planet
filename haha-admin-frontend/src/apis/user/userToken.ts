import http from '@/utils/http'

const BASE_URL = '/user/userToken'

export interface UserTokenResp {
  id: string
  userId: string
  token: string
  tokenType: string
  source: string
  expiresTime: string
  loginIp: string
  loginTime: string
  userAgent: string
  metaJson: string
  status: string
  version: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface UserTokenDetailResp {
  id: string
  userId: string
  token: string
  tokenType: string
  source: string
  expiresTime: string
  loginIp: string
  loginTime: string
  userAgent: string
  metaJson: string
  status: string
  version: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface UserTokenQuery {
  userId: string
  token: string
  version: string
  sort: Array<string>
}
export interface UserTokenPageQuery extends UserTokenQuery, PageQuery {}

/** @desc 查询用户令牌列表 */
export function listUserToken(query: UserTokenPageQuery) {
  return http.get<PageRes<UserTokenResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户令牌详情 */
export function getUserToken(id: string) {
  return http.get<UserTokenDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户令牌 */
export function addUserToken(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户令牌 */
export function updateUserToken(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户令牌 */
export function deleteUserToken(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户令牌 */
export function exportUserToken(query: UserTokenQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
