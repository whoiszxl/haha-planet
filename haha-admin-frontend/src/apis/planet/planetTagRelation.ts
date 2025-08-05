import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetTagRelation'

export interface PlanetTagRelationResp {
  id: string
  planetId: string
  tagId: string
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
export interface PlanetTagRelationDetailResp {
  id: string
  planetId: string
  tagId: string
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
export interface PlanetTagRelationQuery {
  planetId: string
  tagId: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetTagRelationPageQuery extends PlanetTagRelationQuery, PageQuery {}

/** @desc 查询星球标签关联列表 */
export function listPlanetTagRelation(query: PlanetTagRelationPageQuery) {
  return http.get<PageRes<PlanetTagRelationResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球标签关联详情 */
export function getPlanetTagRelation(id: string) {
  return http.get<PlanetTagRelationDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球标签关联 */
export function addPlanetTagRelation(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球标签关联 */
export function updatePlanetTagRelation(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球标签关联 */
export function deletePlanetTagRelation(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球标签关联 */
export function exportPlanetTagRelation(query: PlanetTagRelationQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
