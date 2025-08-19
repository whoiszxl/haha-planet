import http from '@/utils/http'

const BASE_URL = '/planet/planetTag'

export interface PlanetTagResp {
  id: string
  name: string
  color: string
  useCount: string
  categoryId: string
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
export interface PlanetTagDetailResp {
  id: string
  name: string
  color: string
  useCount: string
  categoryId: string
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
export interface PlanetTagQuery {
  name: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetTagPageQuery extends PlanetTagQuery, PageQuery {}

/** @desc 查询星球标签列表 */
export function listPlanetTag(query: PlanetTagPageQuery) {
  return http.get<PageRes<PlanetTagResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球标签详情 */
export function getPlanetTag(id: string) {
  return http.get<PlanetTagDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球标签 */
export function addPlanetTag(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球标签 */
export function updatePlanetTag(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球标签 */
export function deletePlanetTag(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球标签 */
export function exportPlanetTag(query: PlanetTagQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
