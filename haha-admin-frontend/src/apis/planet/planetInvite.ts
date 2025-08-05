import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetInvite'

export interface PlanetInviteResp {
  id: string
  planetId: string
  inviterId: string
  inviteeId: string
  inviteCode: string
  inviteType: string
  inviteMessage: string
  expireTime: string
  maxUseCount: string
  usedCount: string
  inviteStatus: string
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
export interface PlanetInviteDetailResp {
  id: string
  planetId: string
  inviterId: string
  inviteeId: string
  inviteCode: string
  inviteType: string
  inviteMessage: string
  expireTime: string
  maxUseCount: string
  usedCount: string
  inviteStatus: string
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
export interface PlanetInviteQuery {
  planetId: string
  inviterId: string
  inviteCode: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetInvitePageQuery extends PlanetInviteQuery, PageQuery {}

/** @desc 查询星球邀请列表 */
export function listPlanetInvite(query: PlanetInvitePageQuery) {
  return http.get<PageRes<PlanetInviteResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球邀请详情 */
export function getPlanetInvite(id: string) {
  return http.get<PlanetInviteDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球邀请 */
export function addPlanetInvite(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球邀请 */
export function updatePlanetInvite(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球邀请 */
export function deletePlanetInvite(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球邀请 */
export function exportPlanetInvite(query: PlanetInviteQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
