import http from '@/utils/http'

const BASE_URL = '/user/userClient'

export interface UserClientResp {
  id: string
  clientKey: string
  clientSecret: string
  authType: string
  clientType: string
  activeTimeout: string
  timeout: string
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
export interface UserClientDetailResp {
  id: string
  clientKey: string
  clientSecret: string
  authType: string
  clientType: string
  activeTimeout: string
  timeout: string
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
export interface UserClientQuery {
  clientKey: string
  clientSecret: string
  authType: string
  clientType: string
  version: string
  sort: Array<string>
}
export interface UserClientPageQuery extends UserClientQuery, PageQuery {}

/** @desc 查询用户客户端列表 */
export function listUserClient(query: UserClientPageQuery) {
  return http.get<PageRes<UserClientResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户客户端详情 */
export function getUserClient(id: string) {
  return http.get<UserClientDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户客户端 */
export function addUserClient(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户客户端 */
export function updateUserClient(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户客户端 */
export function deleteUserClient(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户客户端 */
export function exportUserClient(query: UserClientQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
