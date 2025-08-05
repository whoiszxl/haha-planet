import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetMember'

export interface PlanetMemberResp {
  id: string
  planetId: string
  userId: string
  memberType: string
  joinTime: string
  expireTime: string
  joinSource: string
  inviterId: string
  orderId: string
  nickname: string
  isMuted: string
  muteEndTime: string
  lastReadTime: string
  totalPosts: string
  totalLikes: string
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
export interface PlanetMemberDetailResp {
  id: string
  planetId: string
  userId: string
  memberType: string
  joinTime: string
  expireTime: string
  joinSource: string
  inviterId: string
  orderId: string
  nickname: string
  isMuted: string
  muteEndTime: string
  lastReadTime: string
  totalPosts: string
  totalLikes: string
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
export interface PlanetMemberQuery {
  planetId: string
  userId: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetMemberPageQuery extends PlanetMemberQuery, PageQuery {}

/** @desc 查询星球成员列表 */
export function listPlanetMember(query: PlanetMemberPageQuery) {
  return http.get<PageRes<PlanetMemberResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球成员详情 */
export function getPlanetMember(id: string) {
  return http.get<PlanetMemberDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球成员 */
export function addPlanetMember(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球成员 */
export function updatePlanetMember(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球成员 */
export function deletePlanetMember(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球成员 */
export function exportPlanetMember(query: PlanetMemberQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
