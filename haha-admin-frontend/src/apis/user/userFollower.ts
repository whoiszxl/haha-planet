import http from '@/utils/http'

const BASE_URL = '/user/userFollower'

export interface UserFollowerResp {
  id: string
  userId: string
  followerId: string
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
export interface UserFollowerDetailResp {
  id: string
  userId: string
  followerId: string
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
export interface UserFollowerQuery {
  userId: string
  followerId: string
  version: string
  sort: Array<string>
}
export interface UserFollowerPageQuery extends UserFollowerQuery, PageQuery {}

/** @desc 查询用户粉丝(关注我的人)列表 */
export function listUserFollower(query: UserFollowerPageQuery) {
  return http.get<PageRes<UserFollowerResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户粉丝(关注我的人)详情 */
export function getUserFollower(id: string) {
  return http.get<UserFollowerDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户粉丝(关注我的人) */
export function addUserFollower(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户粉丝(关注我的人) */
export function updateUserFollower(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户粉丝(关注我的人) */
export function deleteUserFollower(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户粉丝(关注我的人) */
export function exportUserFollower(query: UserFollowerQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
