import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetCategory'

export interface PlanetCategoryResp {
  id: string
  categoryName: string
  iconUrl: string
  parentId: string
  level: string
  sort: string
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
export interface PlanetCategoryDetailResp {
  id: string
  categoryName: string
  iconUrl: string
  parentId: string
  level: string
  sort: string
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
export interface PlanetCategoryQuery {
  categoryName: string
  iconUrl: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetCategoryPageQuery extends PlanetCategoryQuery, PageQuery {}

/** @desc 查询星球分类列表 */
export function listPlanetCategory(query: PlanetCategoryPageQuery) {
  return http.get<PageRes<PlanetCategoryResp[]>>(`${BASE_URL}/tree`, query)
}

/** @desc 查询星球分类详情 */
export function getPlanetCategory(id: string) {
  return http.get<PlanetCategoryDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球分类 */
export function addPlanetCategory(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球分类 */
export function updatePlanetCategory(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球分类 */
export function deletePlanetCategory(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球分类 */
export function exportPlanetCategory(query: PlanetCategoryQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
