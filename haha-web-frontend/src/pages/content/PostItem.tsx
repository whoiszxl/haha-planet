import React from 'react';
import styles from './PlanetContentPage.module.css';
import { Post, formatPostTime, formatCount } from '../../apis/post/post';
import { getAvatarUrl, getDefaultAvatarUrl } from '../../utils/image';
import { 
  LikeIcon, CommentIcon, ViewIcon, ShareIcon, MoreIcon, ArrowRightIcon, LinkIcon
} from '../../components/icons/SocialIcons';

interface PostItemProps {
  post: Post & {
    userName?: string;
    userAvatar?: string;
    link?: string;
    extractCode?: string;
    summary?: string;
  };
  isExpanded: boolean;
  onToggleExpansion: (postId: number) => void;
  onViewDetail: (post: any) => void;
  onImageError: (e: React.SyntheticEvent<HTMLImageElement>, key: string) => void;
  failedImages: Set<string>;
}

export const PostItem: React.FC<PostItemProps> = ({
  post,
  isExpanded,
  onToggleExpansion,
  onViewDetail,
  onImageError,
  failedImages
}) => {
  // 获取用户头像
  const getUserAvatar = (avatar?: string) => {
    if (!avatar) return getDefaultAvatarUrl();
    return getAvatarUrl(avatar);
  };

  // 检查内容是否需要折叠
  const shouldCollapseContent = (content: string) => {
    return content.split('\n').length > 10;
  };

  // 获取显示的内容
  const getDisplayContent = (summary: string, postId: number) => {
    const shouldCollapse = shouldCollapseContent(summary);
    
    if (!shouldCollapse || isExpanded) {
      return summary;
    }
    
    const lines = summary.split('\n');
    return lines.slice(0, 10).join('\n');
  };

  const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>) => {
    onImageError(e, `post-avatar-${post.id}`);
  };

  const content = post.summary || post.articleExtension?.content || '';

  return (
    <div className={styles.postItem}>
      {/* 用户头像 */}
      <div className={styles.postAvatar}>
        <img 
          src={getUserAvatar(post.userAvatar)} 
          alt={post.userName} 
          className={styles.avatarImg}
          onError={handleImageError}
        />
      </div>
      
      {/* 帖子内容 */}
      <div className={styles.postContent}>
        {/* 用户信息和时间 */}
        <div className={styles.postHeader}>
          <div className={styles.userName}>{post.userName}</div>
          <div className={styles.postTime}>
            {formatPostTime(post.createdAt)}
            {/* 精华和置顶标签 */}
            {post.isEssence === 1 && (
              <span className={styles.essenceTag}>精华</span>
            )}
            {post.isTop === 1 && (
              <span className={styles.topTag}>置顶</span>
            )}
          </div>
          <div className={styles.postOptions}>
            <MoreIcon className={styles.moreIcon} />
          </div>
        </div>
        
        {/* 帖子主体内容 */}
        <div className={styles.postBody}>
          {post.title && <div className={styles.postTitle}>{post.title}</div>}
          <div className={styles.postText}>
            {getDisplayContent(content, post.id)}
            {shouldCollapseContent(content) && (
              <div className={styles.expandButton}>
                <button 
                  className={styles.expandBtn}
                  onClick={() => onToggleExpansion(post.id)}
                >
                  {isExpanded ? '收起' : '展开全部'}
                </button>
              </div>
            )}
          </div>
          
          {/* 文章类型显示详情链接 */}
          {post.contentType === 2 && post.title && (
            <div className={styles.articleLink}>
              <a href={`/article/${post.id}`} className={styles.readMoreLink} target="_blank" rel="noopener noreferrer">
                <LinkIcon className={styles.linkIconSmall} />
                {post.title}
              </a>
            </div>
          )}
          
          {/* 如果有媒体内容 */}
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
                    onImageError(e, imageKey);
                    target.style.display = 'none'; // 隐藏加载失败的图片
                  }
                }}
              />
            </div>
          )}
          
          {/* 链接和提取码 */}
          {post.link && (
            <div className={styles.postLink}>
              <div className={styles.linkText}>链接：<a href={post.link} target="_blank" rel="noopener noreferrer">{post.link}</a></div>
              {post.extractCode && <div className={styles.extractCode}>提取码：{post.extractCode}</div>}
            </div>
          )}
        </div>
        
        {/* 帖子底部操作栏 */}
        <div className={styles.postFooter}>
          <div className={styles.postAction}>
            <LikeIcon className={styles.actionIcon} />
            <span>{formatCount(post.likeCount)}</span>
          </div>
          <div className={styles.postAction}>
            <CommentIcon className={styles.actionIcon} />
            <span>{formatCount(post.commentCount)}</span>
          </div>
          <div className={styles.postAction}>
            <ViewIcon className={styles.actionIcon} />
            <span>{formatCount(post.viewCount)}</span>
          </div>
          <div className={styles.postAction}>
            <ShareIcon className={styles.actionIcon} />
            <span>{formatCount(post.shareCount)}</span>
          </div>
          <div className={styles.postActionRight} onClick={() => onViewDetail(post)}>
            <span className={styles.actionText}>查看详情</span>
            <ArrowRightIcon className={styles.arrowIcon} />
          </div>
        </div>
      </div>
    </div>
  );
};