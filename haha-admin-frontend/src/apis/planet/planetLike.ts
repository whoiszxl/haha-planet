import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetLike'

export interface PlanetLikeResp {
  id: string
  planetId: string
  userId: string
  targetType: string
  targetId: string
  targetUserId: string
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
export interface PlanetLikeDetailResp {
  id: string
  planetId: string
  userId: string
  targetType: string
  targetId: string
  targetUserId: string
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
export interface PlanetLikeQuery {
  planetId: string
  userId: string
  targetType: string
  targetId: string
  targetUserId: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetLikePageQuery extends PlanetLikeQuery, PageQuery {}

/** @desc 查询点赞记录列表 */
export function listPlanetLike(query: PlanetLikePageQuery) {
  return http.get<PageRes<PlanetLikeResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询点赞记录详情 */
export function getPlanetLike(id: string) {
  return http.get<PlanetLikeDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增点赞记录 */
export function addPlanetLike(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改点赞记录 */
export function updatePlanetLike(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除点赞记录 */
export function deletePlanetLike(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出点赞记录 */
export function exportPlanetLike(query: PlanetLikeQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
