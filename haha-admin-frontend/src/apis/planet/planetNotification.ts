import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetNotification'

export interface PlanetNotificationResp {
  id: string
  planetId: string
  userId: string
  senderId: string
  notificationType: string
  title: string
  content: string
  targetType: string
  targetId: string
  isRead: string
  readTime: string
  version: string
  status: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface PlanetNotificationDetailResp {
  id: string
  planetId: string
  userId: string
  senderId: string
  notificationType: string
  title: string
  content: string
  targetType: string
  targetId: string
  isRead: string
  readTime: string
  version: string
  status: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface PlanetNotificationQuery {
  planetId: string
  userId: string
  notificationType: string
  title: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetNotificationPageQuery extends PlanetNotificationQuery, PageQuery {}

/** @desc 查询星球通知列表 */
export function listPlanetNotification(query: PlanetNotificationPageQuery) {
  return http.get<PageRes<PlanetNotificationResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球通知详情 */
export function getPlanetNotification(id: string) {
  return http.get<PlanetNotificationDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球通知 */
export function addPlanetNotification(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球通知 */
export function updatePlanetNotification(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球通知 */
export function deletePlanetNotification(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球通知 */
export function exportPlanetNotification(query: PlanetNotificationQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
