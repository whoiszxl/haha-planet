import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getPostDetail, Post, formatCount, formatPostTime } from '../../apis/post/post';
import { ArrowLeftIcon, LikeIcon, CommentIcon, ViewIcon, ShareIcon, MoreIcon } from '../../components/icons/SocialIcons';
import { Header } from '../../components';
import { getAvatarUrl, getDefaultAvatarUrl } from '../../utils/image';
import styles from './PostDetailPage.module.css';

interface PostDetailPageProps {}

// 获取内容类型文本
const getContentTypeText = (contentType: number): string => {
  switch (contentType) {
    case 1:
      return '文本';
    case 2:
      return '图片';
    case 3:
      return '视频';
    case 4:
      return '文件';
    default:
      return '未知';
  }
};

export const PostDetailPage: React.FC<PostDetailPageProps> = () => {
  const { postId } = useParams<{ postId: string }>();
  const navigate = useNavigate();
  
  const [post, setPost] = useState<Post | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [cacheVersion, setCacheVersion] = useState<string | undefined>(undefined);
  const [failedImages, setFailedImages] = useState<Set<string>>(new Set());

  // 加载帖子详情
  const loadPostDetail = async (id: number, version?: string) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await getPostDetail({
        postId: id,
        version: version || cacheVersion || '0'
      });
      
      if (response.code === 'SUCCESS' && response.data) {
        // 保存最新的缓存版本号
        if (response.data.version) {
          setCacheVersion(response.data.version);
        }
        
        // 处理稍后重试状态
        if (response.data.later) {
          console.warn('服务繁忙，需要稍后重试');
          setError('服务繁忙，请稍后重试');
          return;
        }
        
        // 处理数据不存在状态
        if (!response.data.exist) {
          console.info('帖子详情数据不存在');
          setError('帖子不存在或已被删除');
          return;
        }
        
        // 处理成功返回的数据
        const postData = response.data.postDetail;
        if (postData) {
          setPost(postData);
        } else {
          console.warn('返回的帖子数据为空');
          setError('帖子数据为空');
        }
      } else {
        console.warn('获取帖子详情失败:', response.message);
        setError('加载帖子详情失败');
      }
    } catch (err) {
      console.error('加载帖子详情失败:', err);
      setError('加载帖子详情失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  // 获取用户头像
  const getUserAvatar = (avatar?: string) => {
    if (!avatar || avatar.trim() === '') return getDefaultAvatarUrl();
    return getAvatarUrl(avatar);
  };

  // 处理图片加载失败
  const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>, imageKey: string, fallbackUrl?: string) => {
    const target = e.target as HTMLImageElement;
    const currentSrc = target.src;
    
    // 如果已经是默认图片或者已经标记为失败，则不再处理
    if (failedImages.has(imageKey) || currentSrc.includes('default-')) {
      return;
    }
    
    // 标记为失败
    setFailedImages(prev => new Set(prev).add(imageKey));
    
    // 设置默认图片
    target.src = fallbackUrl || getDefaultAvatarUrl();
  };

  // 返回上一页
  const handleGoBack = () => {
    navigate(-1);
  };

  // 重新获取帖子详情
  const fetchPostDetail = () => {
    if (postId) {
      const id = parseInt(postId);
      if (!isNaN(id)) {
        loadPostDetail(id);
      }
    }
  };

  // 页面初始化
  useEffect(() => {
    if (postId) {
      const id = parseInt(postId);
      if (!isNaN(id)) {
        loadPostDetail(id);
      } else {
        setError('无效的帖子ID');
      }
    } else {
      setError('缺少帖子ID参数');
    }
  }, [postId]);



  return (
    <div className={styles.pageContainer}>
      <Header />
      
      <div className={styles.pageContent}>
        <div className={styles.mainContent}>
          {/* 返回按钮 */}
          <button 
            className={styles.backButton}
            onClick={() => navigate(-1)}
          >
            <ArrowLeftIcon className={styles.backIcon} />
            返回
          </button>

          {/* 加载状态 */}
          {loading && (
            <div className={styles.loading}>
              <div>加载中...</div>
            </div>
          )}

          {/* 错误状态 */}
          {!loading && error && (
            <div className={styles.error}>
              <div className={styles.errorIcon}>⚠️</div>
              <div className={styles.errorMessage}>{error}</div>
              <button 
                className={styles.retryButton}
                onClick={fetchPostDetail}
              >
                重试
              </button>
            </div>
          )}

          {/* 空状态 */}
          {!loading && !error && !post && (
            <div className={styles.empty}>
              <div className={styles.emptyIcon}>📝</div>
              <div className={styles.emptyMessage}>帖子不存在或已被删除</div>
            </div>
          )}

          {/* 帖子详情 */}
          {!loading && !error && post && (
            <div className={styles.postDetail}>
              {/* 作者信息 */}
              <div className={styles.authorSection}>
                <div className={styles.authorAvatar}>
                  <img 
                    src={getUserAvatar(post.userAvatar)} 
                    alt={post.userName || `用户${post.userId}`}
                    className={styles.avatarImg}
                    onError={(e) => handleImageError(e, `author-avatar-${post.id}`)}
                  />
                </div>
                <div className={styles.authorDetails}>
                  <div className={styles.authorName}>
                    {post.userName || `用户${post.userId}`}
                  </div>
                  <div className={styles.postMeta}>
                    <span className={styles.postTime}>{formatPostTime(post.createdAt)}</span>
                    <span className={styles.separator}>·</span>
                    <span className={styles.contentType}>{getContentTypeText(post.contentType)}</span>
                    {post.isTop === 1 && <span className={styles.topBadge}>置顶</span>}
                    {post.isEssence === 1 && <span className={styles.essenceBadge}>精华</span>}
                  </div>
                </div>
              </div>

              {/* 帖子内容 */}
              <div className={styles.postContent}>
                {post.title && (
                  <h1 className={styles.postTitle}>{post.title}</h1>
                )}
                
                <div className={styles.postBody}>
                  <div className={styles.postText}>{post.content}</div>
                  
                  {/* 媒体内容 */}
                  {post.mediaUrls && post.contentType === 2 && (
                    <div className={styles.postMedia}>
                      <img 
                        src={post.mediaUrls} 
                        alt="帖子图片" 
                        className={styles.postImage}
                        onError={(e) => {
                          const target = e.target as HTMLImageElement;
                          const imageKey = `post-media-${post.id}`;
                          if (!failedImages.has(imageKey)) {
                            setFailedImages(prev => new Set(prev).add(imageKey));
                            target.style.display = 'none';
                          }
                        }}
                      />
                    </div>
                  )}
                  
                  {/* 链接内容 */}
                  {post.link && (
                    <div className={styles.linkContent}>
                      <div className={styles.linkText}>
                        <a href={post.link} target="_blank" rel="noopener noreferrer">
                          {post.link}
                        </a>
                      </div>
                      {post.extractCode && (
                        <div className={styles.extractCode}>
                          提取码: {post.extractCode}
                        </div>
                      )}
                    </div>
                  )}
                </div>
              </div>

              {/* 社交统计 */}
              <div className={styles.socialStats}>
                <div className={styles.statItem}>
                  <LikeIcon className={styles.statIcon} />
                  <span className={styles.statText}>{formatCount(post.likeCount)}</span>
                </div>
                <div className={styles.statItem}>
                  <CommentIcon className={styles.statIcon} />
                  <span className={styles.statText}>{formatCount(post.commentCount)}</span>
                </div>
                <div className={styles.statItem}>
                  <ViewIcon className={styles.statIcon} />
                  <span className={styles.statText}>{formatCount(post.viewCount)}</span>
                </div>
                <div className={styles.statItem}>
                  <ShareIcon className={styles.statIcon} />
                  <span className={styles.statText}>分享</span>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};