import http from '@/utils/http'

const BASE_URL = '/user/userLevel'

export interface UserLevelResp {
  id: string
  level: string
  levelName: string
  minExperience: string
  maxExperience: string
  levelIcon: string
  levelColor: string
  privileges: string
  description: string
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
export interface UserLevelDetailResp {
  id: string
  level: string
  levelName: string
  minExperience: string
  maxExperience: string
  levelIcon: string
  levelColor: string
  privileges: string
  description: string
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
export interface UserLevelQuery {
  level: string
  levelName: string
  minExperience: string
  maxExperience: string
  version: string
  sort: Array<string>
}
export interface UserLevelPageQuery extends UserLevelQuery, PageQuery {}

/** @desc 查询用户等级列表 */
export function listUserLevel(query: UserLevelPageQuery) {
  return http.get<PageRes<UserLevelResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户等级详情 */
export function getUserLevel(id: string) {
  return http.get<UserLevelDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户等级 */
export function addUserLevel(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户等级 */
export function updateUserLevel(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户等级 */
export function deleteUserLevel(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户等级 */
export function exportUserLevel(query: UserLevelQuery) {
  return http.download(`${BASE_URL}/export`, query)
}

/** @desc 更新用户等级状态 */
export function updateUserLevelStatus(id: string, status: number) {
  return http.patch(`${BASE_URL}/${id}/status`, { status })
}
