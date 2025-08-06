import http from '@/utils/http'

const BASE_URL = '/user/userSettings'

export interface UserSettingsResp {
  id: string
  userId: string
  privacyLevel: string
  allowSearch: string
  allowFriendRequest: string
  allowMessage: string
  emailNotification: string
  smsNotification: string
  pushNotification: string
  theme: string
  language: string
  timezone: string
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
export interface UserSettingsDetailResp {
  id: string
  userId: string
  privacyLevel: string
  allowSearch: string
  allowFriendRequest: string
  allowMessage: string
  emailNotification: string
  smsNotification: string
  pushNotification: string
  theme: string
  language: string
  timezone: string
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
export interface UserSettingsQuery {
  userId: string
  version: string
  sort: Array<string>
}
export interface UserSettingsPageQuery extends UserSettingsQuery, PageQuery {}

/** @desc 查询用户设置列表 */
export function listUserSettings(query: UserSettingsPageQuery) {
  return http.get<PageRes<UserSettingsResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户设置详情 */
export function getUserSettings(id: string) {
  return http.get<UserSettingsDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户设置 */
export function addUserSettings(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户设置 */
export function updateUserSettings(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户设置 */
export function deleteUserSettings(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户设置 */
export function exportUserSettings(query: UserSettingsQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
