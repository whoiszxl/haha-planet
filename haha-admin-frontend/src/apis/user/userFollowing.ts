import http from '@/utils/http'

const BASE_URL = '/user/userFollowing'

export interface UserFollowingResp {
  id: string
  userId: string
  followingId: string
  followType: string
  isMutual: string
  isCancelled: string
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
export interface UserFollowingDetailResp {
  id: string
  userId: string
  followingId: string
  followType: string
  isMutual: string
  isCancelled: string
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
export interface UserFollowingQuery {
  userId: string
  followingId: string
  version: string
  sort: Array<string>
}
export interface UserFollowingPageQuery extends UserFollowingQuery, PageQuery {}

/** @desc 查询用户关注(我关注的人)列表 */
export function listUserFollowing(query: UserFollowingPageQuery) {
  return http.get<PageRes<UserFollowingResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户关注(我关注的人)详情 */
export function getUserFollowing(id: string) {
  return http.get<UserFollowingDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户关注(我关注的人) */
export function addUserFollowing(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户关注(我关注的人) */
export function updateUserFollowing(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户关注(我关注的人) */
export function deleteUserFollowing(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户关注(我关注的人) */
export function exportUserFollowing(query: UserFollowingQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
