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

// 分页响应类型（保留兼容性）
export interface PageResponse<T> {
  list: T[];
  total: number;
}

// 用户星球分组响应
export interface UserPlanetGroupResp {
  createdPlanets: UserPlanet[];    // 我创建的星球
  managedPlanets: UserPlanet[];    // 我管理的星球
  joinedPlanets: UserPlanet[];     // 我加入的星球
  stats: {
    createdCount: number;
    managedCount: number;
    joinedCount: number;
    totalCount: number;
  };
}

/**
 * 获取用户所有星球，按类型分组
 */
export const getUserAllPlanets = async (limit: number = 50) => {
  try {
    const response = await http.get<UserPlanetGroupResp>(`/planet/api/user/planet/all?limit=${limit}`);
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取用户所有星球失败:', error);
    throw error;
  }
};

// 为了兼容性，保留单独的获取方法
export const getUserCreatedPlanets = async (limit: number = 50) => {
  const response = await getUserAllPlanets(limit);
  if (response.code === 'SUCCESS' && response.data) {
    return {
      data: {
        list: response.data.createdPlanets,
        total: response.data.createdPlanets.length
      },
      code: 'SUCCESS',
      message: '获取成功'
    };
  }
  throw new Error('获取失败');
};

export const getUserJoinedPlanets = async (limit: number = 50) => {
  const response = await getUserAllPlanets(limit);
  if (response.code === 'SUCCESS' && response.data) {
    return {
      data: {
        list: response.data.joinedPlanets,
        total: response.data.joinedPlanets.length
      },
      code: 'SUCCESS',
      message: '获取成功'
    };
  }
  throw new Error('获取失败');
};

export const getUserManagedPlanets = async (limit: number = 50) => {
  const response = await getUserAllPlanets(limit);
  if (response.code === 'SUCCESS' && response.data) {
    return {
      data: {
        list: response.data.managedPlanets,
        total: response.data.managedPlanets.length
      },
      code: 'SUCCESS',
      message: '获取成功'
    };
  }
  throw new Error('获取失败');
};
