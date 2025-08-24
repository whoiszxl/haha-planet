import React from 'react';
import styles from './PlanetContentPage.module.css';
import { Post } from '../../apis/post/post';
import { PostItem } from './PostItem';

interface PostListProps {
  posts: (Post & {
    userName?: string;
    userAvatar?: string;
    link?: string;
    extractCode?: string;
    summary?: string;
  })[];
  totalPosts: number;
  loadingMore: boolean;
  postError: string | null;
  expandedPosts: Set<number>;
  failedImages: Set<string>;
  sortType: string;
  loadingRef: React.RefObject<HTMLDivElement>;
  onTogglePostExpansion: (postId: number) => void;
  onViewDetail: (post: any) => void;
  onImageError: (e: React.SyntheticEvent<HTMLImageElement>, key: string) => void;
  onRetry: () => void;
  onSortChange: (sortType: string) => void;
}

export const PostList: React.FC<PostListProps> = ({
  posts,
  totalPosts,
  loadingMore,
  postError,
  expandedPosts,
  failedImages,
  sortType,
  loadingRef,
  onTogglePostExpansion,
  onViewDetail,
  onImageError,
  onRetry,
  onSortChange
}) => {
  // 渲染帖子列表
  const renderPostList = () => {
    if (postError) {
      return (
        <div className={styles.error}>
          {postError}
          <button 
            className={styles.retryButton} 
            onClick={onRetry}
          >
            重试
          </button>
        </div>
      );
    }

    // 使用后端返回的用户信息，如果没有则使用默认值
    const postsWithUserInfo = posts.map(post => ({
      ...post,
      // 使用后端返回的userName，如果没有则使用userId作为备选
      userName: post.userName || `用户${post.userId}`,
      // 使用后端返回的userAvatar，如果没有则使用空字符串
      userAvatar: post.userAvatar || ''
    }));

    return (
      <div className={styles.postListContainer}>
        <div className={styles.postList}>
          {postsWithUserInfo.map(post => (
            <PostItem
              key={post.id}
              post={post}
              isExpanded={expandedPosts.has(post.id)}
              onToggleExpansion={onTogglePostExpansion}
              onViewDetail={onViewDetail}
              onImageError={onImageError}
              failedImages={failedImages}
            />
          ))}
          
          {/* 自动加载指示器 */}
          {posts.length < totalPosts && (
            <div 
              className={styles.loadMoreContainer} 
              ref={loadingRef}
            >
              <div className={styles.autoLoadingIndicator}>
                {loadingMore ? '加载中...' : '上拉加载更多'}
              </div>
            </div>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className={styles.contentBody}>
      <div className={styles.postContainer}>
        {/* 帖子列表 */}
        {renderPostList()}
      </div>
    </div>
  );
};