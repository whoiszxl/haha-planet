// 用户星球相关API
import http from '../../utils/http'

// 用户星球信息类型
export interface UserPlanet {
  id: number;
  planetCode: string;
  name: string;
  avatar: string;
  coverImage: string;
  description: string;
  categoryName: string;
  price: number;
  memberCount: number;
  postCount: number;
  hotScore: number;
  qualityScore: number;
  isFeatured: number;
  isOfficial: number;
  status: number;
  createdAt: string;
  lastActiveTime: string;
  memberType?: number; // 用户在该星球的角色类型
  memberTypeName?: string;
  joinTime?: string;
  planetNickname?: string;
}

// 分页响应类型
export interface PageResponse<T> {
  list: T[];
  total: number;
}

// 用户星球统计信息
export interface UserPlanetStats {
  createdCount: number;    // 创建的星球数量
  joinedCount: number;     // 加入的星球数量
  managedCount: number;    // 管理的星球数量
}

/**
 * 获取用户创建的星球列表
 */
export const getUserCreatedPlanets = async (page: number = 1, pageSize: number = 20) => {
  try {
    const response = await http.get<PageResponse<UserPlanet>>(`/planet/api/user/planet/created?page=${page}&pageSize=${pageSize}`);
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取用户创建的星球失败:', error);
    throw error;
  }
};

/**
 * 获取用户加入的星球列表
 */
export const getUserJoinedPlanets = async (page: number = 1, pageSize: number = 20, memberType?: number) => {
  try {
    let url = `/planet/api/user/planet/joined?page=${page}&pageSize=${pageSize}`;
    if (memberType !== undefined) {
      url += `&memberType=${memberType}`;
    }
    
    const response = await http.get<PageResponse<UserPlanet>>(url);
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取用户加入的星球失败:', error);
    throw error;
  }
};

/**
 * 获取用户管理的星球列表
 */
export const getUserManagedPlanets = async (page: number = 1, pageSize: number = 20) => {
  try {
    const response = await http.get<PageResponse<UserPlanet>>(`/planet/api/user/planet/managed?page=${page}&pageSize=${pageSize}`);
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取用户管理的星球失败:', error);
    throw error;
  }
};

/**
 * 获取用户星球统计信息
 */
export const getUserPlanetStats = async () => {
  try {
    const response = await http.get<UserPlanetStats>('/planet/api/user/planet/stats');
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取用户星球统计信息失败:', error);
    throw error;
  }
};
