// 帖子相关API
import http from '../../utils/http';

// ================================ 类型定义 ================================

// 文章扩展信息接口
export interface ArticleExtension {
  content: string;
  coverImage?: string;
  tags?: string;
  wordCount?: number;
  readingTime?: number;
  isOriginal?: number;
  sourceUrl?: string;
}

// 帖子响应接口
export interface Post {
  id: number;
  planetId: number;
  userId: number;
  userName?: string;
  userAvatar?: string;
  title: string;
  summary?: string;
  contentType: number;
  mediaUrls?: string;
  link?: string;
  extractCode?: string;
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
  articleExtension?: ArticleExtension;
}

// 分页响应接口
export interface PostPageResponse {
  list: Post[];
  total: number;
}

// 响应接口
export interface VersionedResponse<T> {
  data: T;
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
}

/**
 * 根据星球ID查询帖子列表
 */
export const getPostsByPlanetId = async (params: PostListParams) => {
  const { planetId, page = 1, pageSize = 20, sortType = 1 } = params;
  
  try {
    // 构建查询参数
    const queryParams = new URLSearchParams({
      page: page.toString(),
      pageSize: pageSize.toString(),
      sortType: sortType.toString()
    });
    
    // 调用API获取帖子列表
    const response = await http.get<VersionedPostPageResponse>(`/planet/api/post/planet/${planetId}?${queryParams.toString()}`);
    
    // 如果缓存不存在或需要稍后重试，返回相应状态
    if (!response.data.exist || response.data.later) {
      console.warn('帖子列表缓存未就绪:', response.data.later ? '稍后重试' : '数据不存在');
      return {
        data: {
          data: {
            list: [],
            total: 0
          },
          exist: response.data.exist,
          later: response.data.later
        },
        code: response.data.later ? 'CACHE_NOT_READY' : 'NO_DATA',
        message: response.data.later ? '缓存正在构建中，请稍后重试' : '暂无帖子数据'
      };
    }
    
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

// 帖子详情请求参数
export interface PostDetailParams {
  postId: string;
}

// 帖子详情响应接口
export interface VersionedPostDetailResponse {
  postDetail: Post;
  postId: string;
  exist: boolean;
  later: boolean;
}

/**
 * 根据帖子ID获取帖子详情
 */
export const getPostDetail = async (params: PostDetailParams) => {
  const { postId } = params;
  
  try {
    // 调用API获取帖子详情
    const response = await http.get<VersionedPostDetailResponse>(`/planet/api/post/detail/${postId}`);
    
    // 如果缓存不存在或需要稍后重试
    if (!response.data.exist || response.data.later) {
      console.warn('帖子详情缓存未就绪:', response.data.later ? '稍后重试' : '数据不存在');
      return {
        data: null,
        code: response.data.later ? 'CACHE_NOT_READY' : 'NO_DATA',
        message: response.data.later ? '缓存正在构建中，请稍后重试' : '帖子不存在或已被删除'
      };
    }
    
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取帖子详情失败:', error);
    throw error;
  }
};

// 创建帖子请求参数
export interface CreatePostParams {
  planetId: number;
  title: string;
  summary: string;
  contentType: number; // 1-主题 2-文章
  content?: string;
  coverImage?: string;
  mediaUrls?: {
    file: string[];
    image: string[];
  };
  tags?: string[];
  isAnonymous?: boolean;
  isTop?: boolean;
  isEssence?: boolean;
  isOriginal?: boolean;
  sourceUrl?: string;
}

/**
 * 创建新帖子
 */
export const createPost = async (params: CreatePostParams) => {
  try {
    const response = await http.post<number>('/planet/api/post', params);
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '发布成功'
    };
  } catch (error) {
    console.error('创建帖子失败:', error);
    throw error;
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
