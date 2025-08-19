import http from '@/utils/http'

const BASE_URL = '/planet/planetCollect'

export interface PlanetCollectResp {
  id: string
  planetId: string
  userId: string
  postId: string
  folderName: string
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
export interface PlanetCollectDetailResp {
  id: string
  planetId: string
  userId: string
  postId: string
  folderName: string
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
export interface PlanetCollectQuery {
  planetId: string
  userId: string
  postId: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetCollectPageQuery extends PlanetCollectQuery, PageQuery {}

/** @desc 查询收藏记录列表 */
export function listPlanetCollect(query: PlanetCollectPageQuery) {
  return http.get<PageRes<PlanetCollectResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询收藏记录详情 */
export function getPlanetCollect(id: string) {
  return http.get<PlanetCollectDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增收藏记录 */
export function addPlanetCollect(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改收藏记录 */
export function updatePlanetCollect(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除收藏记录 */
export function deletePlanetCollect(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出收藏记录 */
export function exportPlanetCollect(query: PlanetCollectQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
