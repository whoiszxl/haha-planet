import React, { useEffect, useState, useRef, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import styles from "./PlanetContentPage.module.css";
import { Header } from "../../components";
import { 
  getUserAllPlanets,
  UserPlanet,
  UserPlanetGroupResp 
} from "../../apis/user/userPlanet";
import { getPlanetDetail, Planet } from "../../apis/planet/planet";
import { getPostsByPlanetId, Post as ApiPost, formatPostTime, formatCount } from "../../apis/post/post";
import { getAvatarUrl, getDefaultAvatarUrl } from "../../utils/image";
import { 
  EmojiIcon, ImageIcon, LinkIcon, BoldIcon, HeadingIcon, WriteIcon,
  LikeIcon, CommentIcon, ViewIcon, ShareIcon, MoreIcon, ArrowRightIcon 
} from "../../components/icons/SocialIcons";
import PlanetContentSkeleton from "../../components/skeleton/PlanetContentSkeleton";

// 扩展帖子类型定义，添加前端需要的额外字段
interface Post extends ApiPost {
  userName?: string;
  userAvatar?: string;
  link?: string;
  extractCode?: string;
}

interface PlanetContentPageProps {}

export const PlanetContentPage: React.FC<PlanetContentPageProps> = () => {
  const [searchParams] = useSearchParams();
  const planetId = searchParams.get('planetId');
  
  const [createdPlanets, setCreatedPlanets] = useState<UserPlanet[]>([]);
  const [joinedPlanets, setJoinedPlanets] = useState<UserPlanet[]>([]);
  const [managedPlanets, setManagedPlanets] = useState<UserPlanet[]>([]);
  const [selectedPlanet, setSelectedPlanet] = useState<UserPlanet | null>(null);
  const [planetDetail, setPlanetDetail] = useState<Planet | null>(null);
  const [loading, setLoading] = useState(false);
  const [detailLoading, setDetailLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  // 帖子列表相关状态
  const [posts, setPosts] = useState<Post[]>([]);
  const [postLoading, setPostLoading] = useState(false);
  const [loadingMore, setLoadingMore] = useState(false); // 区分加载更多和初始加载
  const [postError, setPostError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPosts, setTotalPosts] = useState(0);
  const [sortType, setSortType] = useState(1); // 1-最新发布 2-最多点赞 3-最多评论 4-最多浏览
  const [cacheVersion, setCacheVersion] = useState<string | undefined>(undefined); // 缓存版本号，后端需要 Long 类型
  
  // 折叠状态管理
  const [collapsedSections, setCollapsedSections] = useState<{[key: string]: boolean}>({
    created: false,
    joined: false,
    managed: false
  });

  // 加载星球详情
  const loadPlanetDetail = async (id: number) => {
    try {
      setDetailLoading(true);
      const response = await getPlanetDetail(id);
      
      if (response.code === 'SUCCESS' && response.data) {
        setPlanetDetail(response.data);
      } else {
        console.warn('获取星球详情失败:', response.message);
        setPlanetDetail(null);
      }
    } catch (err) {
      console.error('加载星球详情失败:', err);
      setPlanetDetail(null);
    } finally {
      setDetailLoading(false);
    }
  };

  // 加载所有类型的星球列表
  const loadAllPlanets = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // 使用新的统一接口
      const response = await getUserAllPlanets(50);
      
      if (response.code === 'SUCCESS' && response.data) {
        // 设置各类型星球列表
        setCreatedPlanets(response.data.createdPlanets || []);
        setJoinedPlanets(response.data.joinedPlanets || []);
        setManagedPlanets(response.data.managedPlanets || []);
        
        // 合并所有星球列表
        const allPlanets = [
          ...(response.data.createdPlanets || []),
          ...(response.data.joinedPlanets || []),
          ...(response.data.managedPlanets || [])
        ];
        
        // 如果有planetId参数，优先选择指定的星球
        if (planetId) {
          const targetPlanet = allPlanets.find(planet => planet.id.toString() === planetId);
          if (targetPlanet) {
            setSelectedPlanet(targetPlanet);
            // 加载星球详情和帖子
            await loadPlanetDetail(targetPlanet.id);
            await loadPosts(targetPlanet.id, 1, 1);
          } else {
            // 如果在用户星球列表中找不到，仍然尝试加载详情
            const id = parseInt(planetId);
            await loadPlanetDetail(id);
            await loadPosts(id, 1, 1);
          }
        } else {
          // 没有指定planetId时，默认选择第一个星球
          const firstPlanet = response.data.createdPlanets?.[0] || 
                             response.data.joinedPlanets?.[0] || 
                             response.data.managedPlanets?.[0];
          if (firstPlanet) {
            setSelectedPlanet(firstPlanet);
            await loadPlanetDetail(firstPlanet.id);
            await loadPosts(firstPlanet.id, 1, 1);
          }
        }
      } else {
        console.warn('获取星球列表失败:', response.message);
        setError('获取星球列表失败');
      }
      
    } catch (err) {
      console.error('加载星球列表失败:', err);
      setError('加载失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  // 加载帖子列表
  const loadPosts = async (planetId: number, page: number = 1, sort: number = 1, version?: string) => {
    try {
      // 区分初始加载和加载更多
      if (page === 1) {
        setPostLoading(true);
      } else {
        setLoadingMore(true);
      }
      setPostError(null);
      
      const response = await getPostsByPlanetId({
        planetId,
        page,
        pageSize: 10,
        sortType: sort,
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
          setPostError('服务繁忙，请稍后重试');
          return;
        }
        
        // 处理数据不存在状态
        if (!response.data.exist) {
          console.info('帖子列表数据不存在');
          setPosts([]);
          setTotalPosts(0);
          setCurrentPage(page);
          return;
        }
        
        // 处理成功返回的数据
        const postData = response.data.data;
        if (postData) {
          if (page === 1) {
            setPosts(postData.list || []);
          } else {
            setPosts(prev => [...prev, ...(postData.list || [])]);
          }
          setTotalPosts(postData.total || 0);
          setCurrentPage(page);
        } else {
          console.warn('返回的帖子数据为空');
          if (page === 1) {
            setPosts([]);
            setTotalPosts(0);
          }
        }
      } else {
        console.warn('获取帖子列表失败:', response.message);
        setPostError('加载帖子失败');
      }
    } catch (err) {
      console.error('加载帖子列表失败:', err);
      setPostError('加载帖子失败，请稍后重试');
    } finally {
      setPostLoading(false);
      setLoadingMore(false);
    }
  };

  // 选择星球
  const handlePlanetSelect = async (planet: UserPlanet) => {
    setSelectedPlanet(planet);
    // 重置帖子相关状态
    setPosts([]);
    setCurrentPage(1);
    setSortType(1);
    setCacheVersion(undefined); // 重置缓存版本号
    // 加载选中星球的详情和帖子
    await loadPlanetDetail(planet.id);
    await loadPosts(planet.id, 1, 1);
  };

  // 格式化数字
  const formatNumber = (count: number) => {
    if (count >= 10000) {
      return `${(count / 10000).toFixed(1)}万`;
    }
    return count.toString();
  };

  // 获取成员角色徽章样式
  const getMemberBadgeClass = (memberType?: number) => {
    if (memberType === 3) return `${styles.memberBadge} ${styles.owner}`;
    if (memberType === 2) return `${styles.memberBadge} ${styles.admin}`;
    return styles.memberBadge;
  };

  // 处理排序变更
  const handleSortChange = async (newSortType: number) => {
    if (!selectedPlanet) return;
    setSortType(newSortType);
    setCacheVersion(undefined); // 重置缓存版本号，确保获取最新数据
    await loadPosts(selectedPlanet.id, 1, newSortType);
  };

  // 加载更多帖子
  const loadMorePosts = async () => {
    if (!selectedPlanet || postLoading || loadingMore || posts.length >= totalPosts) return;
    const nextPage = currentPage + 1;
    await loadPosts(selectedPlanet.id, nextPage, sortType);
  };

  // 创建观察器引用
  const observerRef = useRef<IntersectionObserver | null>(null);
  const loadingRef = useRef<HTMLDivElement | null>(null);

  // 初始化观察器的回调函数
  const initObserver = useCallback(() => {
    // 如果已经有观察器，先断开连接
    if (observerRef.current) {
      observerRef.current.disconnect();
    }

    // 创建新的观察器
    observerRef.current = new IntersectionObserver(
      (entries) => {
        // 如果加载指示器进入视口，且不是正在加载状态，且还有更多帖子可加载
        if (entries[0].isIntersecting && !postLoading && !loadingMore && posts.length < totalPosts) {
          loadMorePosts();
        }
      },
      { threshold: 0.5 } // 当50%的元素可见时触发
    );

    // 如果加载指示器元素存在，开始观察它
    if (loadingRef.current) {
      observerRef.current.observe(loadingRef.current);
    }
  }, [postLoading, loadingMore, posts.length, totalPosts]);

  // 当相关依赖项变化时，重新初始化观察器
  useEffect(() => {
    initObserver();
    
    // 组件卸载时清理观察器
    return () => {
      if (observerRef.current) {
        observerRef.current.disconnect();
      }
    };
  }, [initObserver]);

  // 获取用户头像
  const getUserAvatar = (avatar?: string) => {
    if (!avatar || avatar.trim() === '') return getDefaultAvatarUrl();
    return getAvatarUrl(avatar);
  };

  // 刷新帖子列表（强制刷新缓存）
  const refreshPosts = async () => {
    if (!selectedPlanet) return;
    // 生成新的版本号强制刷新缓存，转换为字符串类型以匹配后端 Long 类型
    const newVersion = Date.now().toString();
    setCacheVersion(newVersion);
    setPosts([]);
    setCurrentPage(1);
    await loadPosts(selectedPlanet.id, 1, sortType, newVersion);
  };

  // 渲染帖子列表
  const renderPostList = () => {
    if (postError) {
      return (
        <div className={styles.error}>
          {postError}
          <button 
            className={styles.retryButton} 
            onClick={() => selectedPlanet && loadPosts(selectedPlanet.id, 1, sortType)}
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
            <div key={post.id} className={styles.postItem}>
              {/* 用户头像 */}
              <div className={styles.postAvatar}>
                <img 
                  src={getUserAvatar(post.userAvatar)} 
                  alt={post.userName} 
                  className={styles.avatarImg}
                  onError={(e) => {
                    const target = e.target as HTMLImageElement;
                    target.src = getDefaultAvatarUrl();
                  }}
                />
              </div>
              
              {/* 帖子内容 */}
              <div className={styles.postContent}>
                {/* 用户信息和时间 */}
                <div className={styles.postHeader}>
                  <div className={styles.userName}>{post.userName}</div>
                  <div className={styles.postTime}>{formatPostTime(post.createdAt)}</div>
                  <div className={styles.postOptions}>
                    <MoreIcon className={styles.moreIcon} />
                  </div>
                </div>
                
                {/* 帖子主体内容 */}
                <div className={styles.postBody}>
                  {post.title && <div className={styles.postTitle}>{post.title}</div>}
                  <div className={styles.postText}>{post.content}</div>
                  
                  {/* 如果有媒体内容 */}
                  {post.mediaUrls && post.contentType === 2 && (
                    <div className={styles.postMedia}>
                      <img src={post.mediaUrls} alt="帖子图片" className={styles.postImage} />
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
                  <div className={styles.postActionRight}>
                    <span className={styles.actionText}>查看详情</span>
                    <ArrowRightIcon className={styles.arrowIcon} />
                  </div>
                </div>
              </div>
            </div>
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

  // 页面初始化
  useEffect(() => {
    loadAllPlanets();
  }, []);

  // 切换分组折叠状态
  const toggleSection = (sectionKey: string) => {
    setCollapsedSections(prev => ({
      ...prev,
      [sectionKey]: !prev[sectionKey]
    }));
  };

  // 渲染星球列表组件
  const renderPlanetSection = (title: string, planets: UserPlanet[], sectionKey: string) => {
    if (planets.length === 0) return null;
    
    const isCollapsed = collapsedSections[sectionKey];
    
    return (
      <div className={styles.planetSection}>
        <div 
          className={styles.sectionHeader}
          onClick={() => toggleSection(sectionKey)}
        >
          <span className={styles.sectionTitle}>{title}</span>
          <span className={styles.sectionCount}>({planets.length})</span>
          <span className={`${styles.collapseIcon} ${isCollapsed ? styles.collapsed : ''}`}>
            ▼
          </span>
        </div>
        {!isCollapsed && (
          <div className={styles.sectionList}>
            {planets.map((planet) => (
              <div
                key={planet.id}
                className={`${styles.planetItem} ${selectedPlanet?.id === planet.id ? styles.selected : ''}`}
                onClick={() => handlePlanetSelect(planet)}
              >
                <div className={styles.planetAvatar}>
                  <img 
                    src={getAvatarUrl(planet.avatar || '')} 
                    alt={planet.name}
                    onError={(e) => {
                      const target = e.target as HTMLImageElement;
                      target.src = '/default-planet.png';
                    }}
                  />
                </div>
                <div className={styles.planetInfo}>
                  <div className={styles.planetName}>{planet.name}</div>
                  <div className={styles.planetMeta}>
                    <span className={styles.memberCount}>
                      {formatNumber(planet.memberCount)} 成员
                    </span>
                    <span className={styles.postCount}>
                      {formatNumber(planet.postCount)} 内容
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    );
  };

  // 如果初始加载中，显示全页面骨架屏
  if (loading && !selectedPlanet) {
    return (
      <div className={styles.planetContentPage}>
        <Header />
        <PlanetContentSkeleton type="fullPage" />
      </div>
    );
  }

  return (
    <div className={styles.planetContentPage}>
      <Header />
      
      <div className={styles.pageContent}>
        {/* 左侧边栏 */}
        <div className={styles.sidebar}>
          <div className={styles.sidebarHeader}>
            <h2 className={styles.sidebarTitle}>我的星球</h2>
          </div>
          
          <div className={styles.planetList}>
            {error ? (
              <div className={styles.error}>{error}</div>
            ) : (
              <>
                {renderPlanetSection('我创建的星球', createdPlanets, 'created')}
                {renderPlanetSection('我加入的星球', joinedPlanets, 'joined')}
                {renderPlanetSection('我管理的星球', managedPlanets, 'managed')}
                
                {createdPlanets.length === 0 && joinedPlanets.length === 0 && managedPlanets.length === 0 && (
                  <div className={styles.emptyState}>
                    <div className={styles.emptyIcon}>🌟</div>
                    <div className={styles.emptyText}>暂无星球</div>
                    <div className={styles.emptySubtext}>快去创建或加入一个星球吧！</div>
                  </div>
                )}
              </>
            )}
          </div>
        </div>

        {/* 中间主内容区 */}
        <div className={styles.mainContent}>
          {/* 发布区域 - 独立区域 */}
          {selectedPlanet && (
            <div className={styles.publishSection}>
              <div className={styles.publishContainer}>
                {/* 发布框 */}
                <div className={styles.publishBox}>
                  <div className={styles.inputContainer}>
                    <div className={styles.avatarContainer}>
                      <img src={getAvatarUrl(selectedPlanet?.avatar)} alt="用户头像" className={styles.userAvatar} />
                    </div>
                    <div className={styles.inputPlaceholder}>点击发表主题...</div>
                  </div>
                </div>

                {/* 工具栏 */}
                <div className={styles.toolbar}>
                  <div className={styles.toolIcon}>
                    <EmojiIcon className={styles.emojiIcon} />
                  </div>
                  <div className={styles.toolIcon}>
                    <ImageIcon className={styles.imageIcon} />
                  </div>
                  <div className={styles.toolIcon}>
                    <LinkIcon className={styles.linkIcon} />
                  </div>
                  <div className={styles.toolIcon}>
                    <BoldIcon className={styles.boldIcon} />
                  </div>
                  <div className={styles.toolIcon}>
                    <HeadingIcon className={styles.headingIcon} />
                  </div>
                  <div className={styles.toolIcon}>
                    <WriteIcon className={styles.writeIcon} />
                    <span>写文章</span>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* 分类标签区域 - 独立区域 */}
          {selectedPlanet && (
            <div className={styles.categorySection}>
              <div className={styles.categoryContainer}>
                <div className={styles.tabsContainer}>
                  <div 
                    className={`${styles.tab} ${sortType === 1 ? styles.activeTab : ''}`}
                    onClick={() => handleSortChange(1)}
                  >
                    最新
                  </div>
                  <div 
                    className={`${styles.tab} ${sortType === 2 ? styles.activeTab : ''}`}
                    onClick={() => handleSortChange(2)}
                  >
                    最多点赞
                  </div>
                  <div 
                    className={`${styles.tab} ${sortType === 3 ? styles.activeTab : ''}`}
                    onClick={() => handleSortChange(3)}
                  >
                    最多评论
                  </div>
                  <div 
                    className={`${styles.tab} ${sortType === 4 ? styles.activeTab : ''}`}
                    onClick={() => handleSortChange(4)}
                  >
                    最多浏览
                  </div>
                  <div className={styles.tab}>
                    精华
                  </div>
                  <div className={styles.tab}>
                    只看星主
                  </div>
                  <div className={styles.tab}>
                    问答
                  </div>
                  <div className={styles.tab}>
                    文件
                  </div>
                </div>
                <div className={styles.refreshContainer}>
                  <button 
                    className={styles.refreshButton}
                    onClick={refreshPosts}
                    disabled={postLoading}
                    title="刷新帖子列表"
                  >
                    🔄 刷新
                  </button>
                </div>
              </div>
            </div>
          )}

          {/* 内容区域 */}
          <div className={styles.contentBody}>
            {selectedPlanet ? (
              postLoading ? (
                <PlanetContentSkeleton type="postList" />
              ) : (
                <div className={styles.postContainer}>
                  {/* 帖子列表 */}
                  {renderPostList()}
                </div>
              )
            ) : (
              <div className={styles.emptyState}>
                <div className={styles.emptyIcon}>🌍</div>
                <div className={styles.emptyText}>请从左侧选择一个星球</div>
                <div className={styles.emptySubtext}>查看和管理星球内容</div>
              </div>
            )}
          </div>
        </div>

        {/* 右侧星球信息面板 */}
        {selectedPlanet && (
          <div className={styles.rightPanel}>
            <div className={styles.planetInfoPanel}>
              {detailLoading ? (
                <PlanetContentSkeleton type="rightPanel" />
              ) : (
                <>
                  <div 
                    className={styles.planetHeader}
                    style={{
                      backgroundImage: `url(${getAvatarUrl((planetDetail || selectedPlanet)?.coverImage || (planetDetail || selectedPlanet)?.avatar || '')})`
                    }}
                  >
                    <div className={styles.planetHeaderOverlay}></div>
                    <div className={styles.planetHeaderContent}>
                      <div className={styles.planetAvatarLarge}>
                        <img 
                          src={getAvatarUrl((planetDetail || selectedPlanet)?.avatar || '')} 
                          alt={(planetDetail || selectedPlanet)?.name}
                          onError={(e) => {
                            const target = e.target as HTMLImageElement;
                            target.src = '/default-planet.png';
                          }}
                        />
                      </div>
                      <div className={styles.planetBasicInfo}>
                        <h3 className={styles.planetTitle}>{(planetDetail || selectedPlanet)?.name}</h3>
                        <div className={styles.planetStats}>
                          <span className={styles.statItem}>
                            <span className={styles.statNumber}>
                              {formatNumber((planetDetail || selectedPlanet)?.memberCount || 0)}
                            </span>
                            <span className={styles.statLabel}>成员</span>
                          </span>
                          <span className={styles.statItem}>
                            <span className={styles.statNumber}>
                              {(planetDetail || selectedPlanet)?.postCount || 0}
                            </span>
                            <span className={styles.statLabel}>内容</span>
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className={styles.planetDetails}>
                    <div className={styles.detailSection}>
                      <h4 className={styles.sectionTitle}>星球简介</h4>
                      <p className={styles.planetDescription}>
                        {(planetDetail || selectedPlanet)?.description || '暂无简介'}
                      </p>
                    </div>

                    <div className={styles.detailSection}>
                      <h4 className={styles.sectionTitle}>基本信息</h4>
                      <div className={styles.infoList}>
                        <div className={styles.infoRow}>
                          <span className={styles.infoLabel}>分类</span>
                          <span className={styles.infoValue}>
                            {(planetDetail || selectedPlanet)?.categoryName || '未分类'}
                          </span>
                        </div>
                        {planetDetail && (
                          <>
                            <div className={styles.infoRow}>
                              <span className={styles.infoLabel}>星球编码</span>
                              <span className={styles.infoValue}>{planetDetail.planetCode}</span>
                            </div>
                            <div className={styles.infoRow}>
                              <span className={styles.infoLabel}>加入方式</span>
                              <span className={styles.infoValue}>
                                {planetDetail.joinType === 1 ? '自由加入' : '申请加入'}
                              </span>
                            </div>
                            <div className={styles.infoRow}>
                              <span className={styles.infoLabel}>是否公开</span>
                              <span className={styles.infoValue}>
                                {planetDetail.isPublic === 1 ? '公开' : '私密'}
                              </span>
                            </div>
                            {planetDetail.maxMembers > 0 && (
                              <div className={styles.infoRow}>
                                <span className={styles.infoLabel}>成员上限</span>
                                <span className={styles.infoValue}>{planetDetail.maxMembers}人</span>
                              </div>
                            )}
                            {planetDetail.tags && (
                              <div className={styles.infoRow}>
                                <span className={styles.infoLabel}>标签</span>
                                <span className={styles.infoValue}>{planetDetail.tags}</span>
                              </div>
                            )}
                          </>
                        )}
                        {selectedPlanet?.memberTypeName && (
                          <div className={styles.infoRow}>
                            <span className={styles.infoLabel}>我的角色</span>
                            <span className={`${styles.infoValue} ${getMemberBadgeClass(selectedPlanet.memberType)}`}>
                              {selectedPlanet.memberTypeName}
                            </span>
                          </div>
                        )}
                      </div>
                    </div>

                    {/* 管理员信息 */}
                    {planetDetail?.adminUsers && planetDetail.adminUsers.length > 0 && (
                      <div className={styles.detailSection}>
                        <h4 className={styles.sectionTitle}>管理团队</h4>
                        <div className={styles.adminList}>
                          {planetDetail.adminUsers.map((admin) => (
                            <div key={admin.userId} className={styles.adminItem}>
                              <div className={styles.adminAvatar}>
                                <img 
                                  src={getAvatarUrl(admin.avatar || '')} 
                                  alt={admin.nickname}
                                  onError={(e) => {
                                    const target = e.target as HTMLImageElement;
                                    target.src = '/default-avatar.png';
                                  }}
                                />
                              </div>
                              <div className={styles.adminInfo}>
                                <div className={styles.adminName}>{admin.nickname}</div>
                                <div className={`${styles.adminRole} ${getMemberBadgeClass(admin.memberType)}`}>
                                  {admin.memberTypeName}
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>

                  <div className={styles.footerSection}>
                    <div className={styles.footerContent}>
                      <div className={styles.footerLogo}>
                        <span className={styles.footerLogoText}>HAHA PLANET</span>
                      </div>
                      
                      <div className={styles.footerCopyright}>
                        © 2024 哈哈星球 版权所有。让学习变得更有趣，让知识在星球间传播。
                      </div>
                      
                      <div className={styles.footerLinks}>
                        <div className={styles.footerLinkGroup}>
                          {['关于我们', '课程中心', '学习社区', '帮助中心'].map(text => (
                            <button key={text} className={styles.footerLink}>{text}</button>
                          ))}
                        </div>
                        <div className={styles.footerLinkGroup}>
                          {['技术博客', '加入我们', '联系我们'].map(text => (
                            <button key={text} className={styles.footerLink}>{text}</button>
                          ))}
                        </div>
                      </div>
                      
                      <div className={styles.footerPolicies}>
                        {['隐私政策', '服务条款', '用户协议', '退款政策', 'Cookie政策'].map((text, index, arr) => (
                          <React.Fragment key={text}>
                            <button className={styles.footerPolicyLink}>{text}</button>
                            {index < arr.length - 1 && <span className={styles.footerSeparator}>|</span>}
                          </React.Fragment>
                        ))}
                      </div>
                      
                      <div className={styles.footerExtra}>
                        <p>让每个人都能在哈哈星球上找到属于自己的学习乐趣</p>
                        <p>ICP备案号：京ICP备2024000000号 | 网络文化经营许可证</p>
                      </div>
                    </div>
                  </div>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};