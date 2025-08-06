import http from '@/utils/http'

const BASE_URL = '/user/pointsRecord'

export interface PointsRecordResp {
  id: string
  userId: string
  pointsChange: string
  pointsBefore: string
  pointsAfter: string
  changeType: string
  changeReason: string
  relatedId: string
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
export interface PointsRecordDetailResp {
  id: string
  userId: string
  pointsChange: string
  pointsBefore: string
  pointsAfter: string
  changeType: string
  changeReason: string
  relatedId: string
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
export interface PointsRecordQuery {
  userId: string
  pointsChange: string
  pointsBefore: string
  pointsAfter: string
  changeType: string
  version: string
  sort: Array<string>
}
export interface PointsRecordPageQuery extends PointsRecordQuery, PageQuery {}

/** @desc 查询积分记录列表 */
export function listPointsRecord(query: PointsRecordPageQuery) {
  return http.get<PageRes<PointsRecordResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询积分记录详情 */
export function getPointsRecord(id: string) {
  return http.get<PointsRecordDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增积分记录 */
export function addPointsRecord(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改积分记录 */
export function updatePointsRecord(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除积分记录 */
export function deletePointsRecord(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出积分记录 */
export function exportPointsRecord(query: PointsRecordQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
