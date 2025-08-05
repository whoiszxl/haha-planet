import http from '@/utils/http'

const BASE_URL = '/planet/planetApply'

export interface PlanetApplyResp {
  id: string
  planetId: string
  userId: string
  applyReason: string
  answer: string
  applyStatus: string
  auditReason: string
  auditTime: string
  auditBy: string
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
export interface PlanetApplyDetailResp {
  id: string
  planetId: string
  userId: string
  applyReason: string
  answer: string
  applyStatus: string
  auditReason: string
  auditTime: string
  auditBy: string
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
export interface PlanetApplyQuery {
  planetId: string
  userId: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetApplyPageQuery extends PlanetApplyQuery, PageQuery {}

/** @desc 查询星球申请列表 */
export function listPlanetApply(query: PlanetApplyPageQuery) {
  return http.get<PageRes<PlanetApplyResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球申请详情 */
export function getPlanetApply(id: string) {
  return http.get<PlanetApplyDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球申请 */
export function addPlanetApply(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球申请 */
export function updatePlanetApply(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球申请 */
export function deletePlanetApply(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球申请 */
export function exportPlanetApply(query: PlanetApplyQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
