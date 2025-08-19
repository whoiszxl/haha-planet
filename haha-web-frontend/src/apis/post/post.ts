import http from '../../utils/http';

// 帖子响应接口
export interface Post {
  id: number;
  planetId: number;
  userId: number;
  title: string;
  content: string;
  contentType: number;
  mediaUrls?: string;
  isAnonymous: number;
  isTop: number;
  isEssence: number;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  shareCount: number;
  collectCount: number;
  auditStatus: number;
  lastCommentTime?: string;
  hotScore: number;
  status: number;
  createdAt: string;
  updatedAt: string;
}

// 分页响应接口
export interface PostPageResponse {
  list: Post[];
  total: number;
}

// 带版本号的响应接口
export interface VersionedResponse<T> {
  data: T;
  version: string;
  exist: boolean;
  later: boolean;
}

// 带版本号的帖子分页响应接口
export interface VersionedPostPageResponse extends VersionedResponse<PostPageResponse> {}

// 帖子列表请求参数
export interface PostListParams {
  planetId: number;
  page?: number;
  pageSize?: number;
  sortType?: number; // 1-最新发布 2-最多点赞 3-最多评论 4-最多浏览
  version?: string; // 缓存版本号，用于缓存版本控制，作为路径参数传递
}

/**
 * 根据星球ID查询帖子列表
 */
export const getPostsByPlanetId = async (params: PostListParams) => {
  const { planetId, page = 1, pageSize = 20, sortType = 1, version = '0' } = params;
  
  try {
    // 构建查询参数
    const queryParams = new URLSearchParams({
      page: page.toString(),
      pageSize: pageSize.toString(),
      sortType: sortType.toString()
    });
    
    // 使用新的 API 路径格式，版本号作为路径参数
    const response = await http.get<VersionedPostPageResponse>(`/planet/api/post/planet/${planetId}/${version}?${queryParams.toString()}`);
    
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取帖子列表失败:', error);
    throw error;
  }
};

/**
 * 格式化内容类型
 */
export const getContentTypeText = (contentType: number): string => {
  const typeMap: { [key: number]: string } = {
    1: '文本',
    2: '图片',
    3: '视频',
    4: '音频',
    5: '文件',
    6: '链接'
  };
  return typeMap[contentType] || '未知';
};

/**
 * 格式化时间显示
 */
export const formatPostTime = (dateString: string): string => {
  const date = new Date(dateString);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  
  const minutes = Math.floor(diff / (1000 * 60));
  const hours = Math.floor(diff / (1000 * 60 * 60));
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));
  
  if (minutes < 1) {
    return '刚刚';
  } else if (minutes < 60) {
    return `${minutes}分钟前`;
  } else if (hours < 24) {
    return `${hours}小时前`;
  } else if (days < 7) {
    return `${days}天前`;
  } else {
    return date.toLocaleDateString('zh-CN');
  }
};

/**
 * 格式化数字显示
 */
export const formatCount = (count: number): string => {
  if (count >= 10000) {
    return `${(count / 10000).toFixed(1)}万`;
  } else if (count >= 1000) {
    return `${(count / 1000).toFixed(1)}k`;
  }
  return count.toString();
};
