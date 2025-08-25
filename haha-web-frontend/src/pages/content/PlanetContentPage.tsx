import React, { useEffect, useState, useRef, useCallback } from "react";
import { useSearchParams } from "react-router-dom";
import styles from "./PlanetContentPage.module.css";
import { Header } from "../../components";
import { 
  getUserAllPlanets,
  UserPlanet 
} from "../../apis/user/userPlanet";
import { getPlanetDetail, Planet } from "../../apis/planet/planet";
import { getPostsByPlanetId, Post as ApiPost, formatPostTime, formatCount, createPost, CreatePostParams } from "../../apis/post/post";
import { getAvatarUrl, getDefaultAvatarUrl, getFileNameFromUploadResponse, getImageUrl } from "../../utils/image";
import { 
  EmojiIcon, ImageIcon, LinkIcon, BoldIcon, HeadingIcon, WriteIcon, DocumentIcon,
  LikeIcon, CommentIcon, ViewIcon, ShareIcon, MoreIcon, ArrowRightIcon 
} from "../../components/icons/SocialIcons";
import PlanetContentSkeleton from "../../components/skeleton/PlanetContentSkeleton";
import { uploadPostFile, uploadPostImage } from "../../apis/upload/upload";
import { PublishSection } from "./PublishSection";
import { PostList } from "./PostList";
import { PlanetSidebar } from "./PlanetSidebar";
import { PlanetInfoPanel } from "./PlanetInfoPanel";
import { CategoryTabs } from "./CategoryTabs";
import { PublishModal } from "./PublishModal";

// 扩展帖子类型定义，添加前端需要的额外字段
interface Post extends ApiPost {
  userName?: string;
  userAvatar?: string;
  link?: string;
  extractCode?: string;
  summary?: string;
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
  
  // 发布模态框相关状态
  const [showPublishModal, setShowPublishModal] = useState(false);
  const [publishLoading, setPublishLoading] = useState(false);
  const [publishForm, setPublishForm] = useState({
    title: '',
    summary: '',
    contentType: 1, // 1-主题 2-文章
    content: '',
    isAnonymous: false
  });
  
  // 折叠状态管理
  const [collapsedSections, setCollapsedSections] = useState<{[key: string]: boolean}>({
    created: false,
    joined: false,
    managed: false
  });

  // 图片加载失败状态追踪
  const [failedImages, setFailedImages] = useState<Set<string>>(new Set());
  
  // 初始化状态追踪，避免重复加载
  const [isInitialized, setIsInitialized] = useState(false);
  
  // 帖子展开状态管理
  const [expandedPosts, setExpandedPosts] = useState<Set<number>>(new Set());
  
  // 防止自动加载标记
  const [justLoaded, setJustLoaded] = useState(false);
  
  // 帖子详情模态框状态
  const [showPostDetailModal, setShowPostDetailModal] = useState(false);
  const [selectedPostDetail, setSelectedPostDetail] = useState<Post | null>(null);
  
  // 文件上传相关状态
  const [uploadingImage, setUploadingImage] = useState(false);
  const [uploadedImages, setUploadedImages] = useState<{
    file: {fileName: string; originalFileName?: string}[];
    image: {fileName: string; originalFileName?: string}[];
  }>({ file: [], image: [] });
  const fileInputRef = useRef<HTMLInputElement>(null);

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
  const loadAllPlanets = useCallback(async () => {
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
            setIsInitialized(true); // 标记已初始化
          } else {
            // 如果在用户星球列表中找不到，仍然尝试加载详情
            const id = parseInt(planetId);
            await loadPlanetDetail(id);
            await loadPosts(id, 1, 1);
            setIsInitialized(true); // 标记已初始化
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
            setIsInitialized(true); // 标记已初始化
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
  }, [planetId]);

  // 加载帖子列表
  const loadPosts = async (planetId: number, page: number = 1, sort: number = 1) => {
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
        sortType: sort
      });
      
      if (response.code === 'SUCCESS' && response.data) {
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
      
      // 如果是第一页加载，设置刚加载完成标记，防止立即触发第二页
      if (page === 1) {
        setJustLoaded(true);
        // 2秒后清除标记，允许正常的无限滚动
        setTimeout(() => {
          setJustLoaded(false);
        }, 2000);
      }
    }
  };

  // 选择星球
  const handlePlanetSelect = async (planet: UserPlanet) => {
    // 如果选择的是当前已选择的星球，则不需要重新加载
    if (selectedPlanet && selectedPlanet.id === planet.id) {
      return;
    }
    
    // 如果还在初始化过程中，避免重复加载
    if (!isInitialized && selectedPlanet && selectedPlanet.id === planet.id) {
      return;
    }
    
    setSelectedPlanet(planet);
    
    // 切换星球时加载对应的草稿内容
    const draftKey = `draft_post_content_${planet.id}`;
    const savedDraft = localStorage.getItem(draftKey);
    if (savedDraft) {
      try {
        const draftData = JSON.parse(savedDraft);
        setPublishForm({
          title: draftData.title || '',
          summary: draftData.summary || '',
          contentType: draftData.contentType || 1,
          content: draftData.content || '',
          isAnonymous: draftData.isAnonymous || false
        });
      } catch (error) {
        console.error('恢复草稿失败:', error);
      }
    } else {
      // 如果没有草稿，重置表单
      setPublishForm({
        title: '',
        summary: '',
        contentType: 1,
        content: '',
        isAnonymous: false
      });
    }
    
    // 重置帖子相关状态
    setPosts([]);
    setCurrentPage(1);
    setSortType(1);
    setJustLoaded(false); // 重置防护标记
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
    setJustLoaded(false); // 重置防护标记
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
        // 如果加载指示器进入视口，且不是正在加载状态，且还有更多帖子可加载，且不是刚加载完成
        if (entries[0].isIntersecting && !postLoading && !loadingMore && posts.length < totalPosts && posts.length > 0 && !justLoaded) {
          // 添加延迟，避免在数据刚加载完成时立即触发
          setTimeout(() => {
            if (!postLoading && !loadingMore && posts.length < totalPosts && !justLoaded) {
              loadMorePosts();
            }
          }, 100);
        }
      },
      { threshold: 0.5 } // 当50%的元素可见时触发
    );

    // 如果加载指示器元素存在，开始观察它
    if (loadingRef.current) {
      observerRef.current.observe(loadingRef.current);
    }
  }, [postLoading, loadingMore, loadMorePosts, posts.length, totalPosts, justLoaded]);

  // 当相关依赖项变化时，重新初始化观察器
  useEffect(() => {
    // 延迟初始化观察器，避免在数据加载完成后立即触发
    const timer = setTimeout(() => {
      initObserver();
    }, 200);
    
    // 组件卸载时清理观察器和定时器
    return () => {
      clearTimeout(timer);
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

  // 处理图片加载失败，防止重复加载
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

  // 切换帖子展开状态
  const togglePostExpansion = (postId: number) => {
    setExpandedPosts(prev => {
      const newSet = new Set(prev);
      if (newSet.has(postId)) {
        newSet.delete(postId);
      } else {
        newSet.add(postId);
      }
      return newSet;
    });
  };

  // 检查内容是否需要折叠
  const shouldCollapseContent = (content: string) => {
    return content.split('\n').length > 10;
  };

  // 处理点击发表主题输入框
  const handlePublishClick = () => {
    // 只有在表单为空时才从本地存储恢复草稿
    const currentHasContent = publishForm.title.trim() || publishForm.summary.trim();
    if (!currentHasContent && selectedPlanet) {
      const draftKey = `draft_post_content_${selectedPlanet.id}`;
      const savedDraft = localStorage.getItem(draftKey);
      if (savedDraft) {
        try {
          const draftData = JSON.parse(savedDraft);
          setPublishForm({
            title: draftData.title || '',
            summary: draftData.summary || '',
            contentType: draftData.contentType || 1,
            content: draftData.content || '',
            isAnonymous: draftData.isAnonymous || false
          });
        } catch (error) {
          console.error('恢复草稿失败:', error);
        }
      }
    }
    setShowPublishModal(true);
  };

  // 处理发布表单变化
  const handlePublishFormChange = (field: string, value: any) => {
    const newForm = {
      ...publishForm,
      [field]: value
    };
    setPublishForm(newForm);
    
    // 自动保存到本地存储（按星球ID区分）
    if (selectedPlanet) {
      const draftKey = `draft_post_content_${selectedPlanet.id}`;
      const hasContent = newForm.title.trim() || newForm.summary.trim();
      if (hasContent) {
        localStorage.setItem(draftKey, JSON.stringify(newForm));
      } else {
        // 如果内容为空，删除本地存储
        localStorage.removeItem(draftKey);
      }
    }
  };

  // 处理图片上传按钮点击
  const handleImageUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  // 处理文件上传按钮点击
  const handleFileUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  // 删除已上传的文件
  const handleRemoveFile = (fileName: string, fileType: 'image' | 'file') => {
    setUploadedImages(prev => ({
      ...prev,
      [fileType]: prev[fileType].filter(item => item.fileName !== fileName)
    }));
  };

  // 删除文件函数，自动识别文件类型
  const handleRemoveImage = (fileName: string) => {
    // 检查是否在图片数组中
    const isInImageArray = uploadedImages.image.some(item => item.fileName === fileName);
    // 检查是否在文件数组中
    const isInFileArray = uploadedImages.file.some(item => item.fileName === fileName);
    
    if (isInImageArray) {
      handleRemoveFile(fileName, 'image');
    } else if (isInFileArray) {
      handleRemoveFile(fileName, 'file');
    }
  };

  // 处理文件选择
  const handleFileSelect = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // 验证文件大小（10MB）
    const maxSize = 10 * 1024 * 1024;
    if (file.size > maxSize) {
      alert('文件大小不能超过10MB');
      return;
    }

    try {
      setUploadingImage(true);
      
      // 根据文件类型判断是图片还是文件
      const isImage = file.type.startsWith('image/');
      const fileType = isImage ? 'image' : 'file';
      
      // 根据文件类型选择不同的上传方法
      const response = isImage ? await uploadPostImage(file) : await uploadPostFile(file);
      
      if (response.code === 'SUCCESS' && response.data) {
        // 使用工具方法提取文件名
        const fileName = getFileNameFromUploadResponse(response);
        // 提取原始文件名
        const originalFileName = response.data.originalFileName || file.name;
        
        // 添加文件信息到相应的数组中
        setUploadedImages(prev => ({
          ...prev,
          [fileType]: [...prev[fileType], { fileName: fileName, originalFileName: originalFileName }]
        }));
        
        // 文件上传成功，不显示alert提示
      }
    } catch (error) {
      console.error('文件上传失败:', error);
      alert('文件上传失败，请重试');
    } finally {
      setUploadingImage(false);
      // 清空文件输入框
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    }
  };

  // 处理发布帖子
  const handlePublishPost = async () => {
    if (!selectedPlanet) {
      alert('请先选择一个星球');
      return;
    }

    if (!publishForm.title.trim()) {
      alert('请输入标题');
      return;
    }

    if (!publishForm.summary.trim()) {
      alert('请输入内容');
      return;
    }

    setPublishLoading(true);
    try {
      // 构建新的 mediaUrls 对象结构
      const mediaUrls = {
        file: uploadedImages.file.map(item => item.fileName),
        image: uploadedImages.image.map(item => item.fileName)
      };
      
      const params: CreatePostParams = {
        planetId: selectedPlanet.id,
        title: publishForm.title,
        summary: publishForm.summary,
        contentType: publishForm.contentType,
        content: publishForm.content || undefined,
        mediaUrls: (mediaUrls.file.length > 0 || mediaUrls.image.length > 0) ? mediaUrls : undefined,
        isAnonymous: publishForm.isAnonymous
      };

      const response = await createPost(params);
      if (response.code === 'SUCCESS') {
        alert('发布成功！');
        setShowPublishModal(false);
        // 清空草稿内容和本地存储
        handleClearDraft();
        // 清空已上传的文件
        setUploadedImages({ file: [], image: [] });
        // 刷新帖子列表
        await loadPosts(selectedPlanet.id, 1, sortType);
      } else {
        alert('发布失败，请重试');
      }
    } catch (error) {
      console.error('发布帖子失败:', error);
      alert('发布失败，请重试');
    } finally {
      setPublishLoading(false);
    }
  };

  // 关闭发布模态框
  const handleClosePublishModal = () => {
    setShowPublishModal(false);
    // 注意：这里不清理本地存储，也不重置表单状态
    // 保留草稿内容，下次打开时继续编辑
  };

  // 清空草稿内容
  const handleClearDraft = () => {
    setPublishForm({
      title: '',
      summary: '',
      contentType: 1,
      content: '',
      isAnonymous: false
    });
    // 清除文件上传状态
    setUploadedImages({ file: [], image: [] });
    setUploadingImage(false);
    // 清除当前星球的本地存储草稿
    if (selectedPlanet) {
      const draftKey = `draft_post_content_${selectedPlanet.id}`;
      localStorage.removeItem(draftKey);
    }
  };

  // 智能关闭模态框（有内容时不关闭）
  const handleModalOverlayClick = () => {
    const hasContent = publishForm.title.trim() || publishForm.summary.trim();
    if (!hasContent) {
      handleClosePublishModal();
    }
  };

  // 处理写文章按钮点击
  const handleWriteArticleClick = () => {
    if (!selectedPlanet) {
      alert('请先选择一个星球');
      return;
    }
    // 在新标签页中打开文章编辑页面，携带当前星球ID
    window.open(`/article/editor/${selectedPlanet.id}`, '_blank');
  };

  // 获取显示的内容
  const getDisplayContent = (summary: string, postId: number) => {
    const isExpanded = expandedPosts.has(postId);
    const shouldCollapse = shouldCollapseContent(summary);
    
    if (!shouldCollapse || isExpanded) {
      return summary;
    }
    
    const lines = summary.split('\n');
    return lines.slice(0, 10).join('\n');
  };

  // 处理查看详情点击
  const handleViewDetail = (post: Post) => {
    setSelectedPostDetail(post);
    setShowPostDetailModal(true);
  };

  // 关闭帖子详情模态框
  const handleClosePostDetailModal = () => {
    setShowPostDetailModal(false);
    setSelectedPostDetail(null);
  };

  // 渲染帖子列表
  const renderPostList = () => {
    if (postError) {
      return (
        <div className={styles.error}>
          {postError}
          <button 
            className={styles.retryButton} 
            onClick={() => {
              if (selectedPlanet) {
                loadPosts(selectedPlanet.id, 1, sortType);
              }
            }}
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
                  onError={(e) => handleImageError(e, `post-avatar-${post.id}`)}
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
                    {getDisplayContent(post.summary || post.articleExtension?.content || '', post.id)}
                    {shouldCollapseContent(post.summary || post.articleExtension?.content || '') && (
                      <div className={styles.expandButton}>
                        <button 
                          className={styles.expandBtn}
                          onClick={() => togglePostExpansion(post.id)}
                        >
                          {expandedPosts.has(post.id) ? '收起' : '展开全部'}
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
                            setFailedImages(prev => new Set(prev).add(imageKey));
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
                  <div className={styles.postActionRight} onClick={() => handleViewDetail(post)}>
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
  }, [loadAllPlanets]);

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
                    onError={(e) => handleImageError(e, `planet-${planet.id}`, '/default-planet.png')}
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
        <PlanetSidebar
          createdPlanets={createdPlanets}
          joinedPlanets={joinedPlanets}
          managedPlanets={managedPlanets}
          selectedPlanet={selectedPlanet}
          collapsedSections={collapsedSections}
          error={error}
          onPlanetSelect={handlePlanetSelect}
          onToggleSection={toggleSection}
          onImageError={handleImageError}
          formatNumber={formatNumber}
        />

        {/* 中间主内容区 */}
        <div className={styles.mainContent}>
          {/* 发布区域 */}
          {selectedPlanet && (
            <PublishSection 
              selectedPlanet={selectedPlanet}
              onPublishClick={handlePublishClick}
              onWriteArticleClick={handleWriteArticleClick}
              onImageUploadClick={handleImageUploadClick}
              uploadingImage={uploadingImage}
              fileInputRef={fileInputRef}
              onFileSelect={handleFileSelect}
              onImageError={handleImageError}
            />
          )}

          {/* 分类标签区域 - 独立区域 */}
          {selectedPlanet && (
            <CategoryTabs 
              sortType={sortType}
              onSortChange={handleSortChange}
            />
          )}

          {/* 内容区域 */}
          <div className={styles.contentBody}>
            {selectedPlanet ? (
              postLoading ? (
                <PlanetContentSkeleton type="postList" />
              ) : (
                <PostList 
                  posts={posts}
                  totalPosts={posts.length}
                  loadingMore={false}
                  postError={null}
                  expandedPosts={expandedPosts}
                  failedImages={failedImages}
                  sortType={sortType.toString()}
                  loadingRef={loadingRef}
                  onTogglePostExpansion={togglePostExpansion}
                  onViewDetail={handleViewDetail}
                  onImageError={handleImageError}
                  onRetry={() => {}}
                  onSortChange={(type) => handleSortChange(parseInt(type))}
                />
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
          <PlanetInfoPanel 
            selectedPlanet={selectedPlanet}
            planetDetail={planetDetail}
            detailLoading={detailLoading}
            onImageError={handleImageError}
            formatNumber={formatNumber}
            getMemberBadgeClass={getMemberBadgeClass}
          />
        )}
      </div>
      
      {/* 发布帖子模态框 */}
      <PublishModal
        show={showPublishModal}
        selectedPlanet={selectedPlanet}
        publishForm={publishForm}
        publishLoading={publishLoading}
        uploadingImage={uploadingImage}
        uploadedImages={uploadedImages}
        onClose={handleClosePublishModal}
        onPublishFormChange={handlePublishFormChange}
        onPublishPost={handlePublishPost}
        onImageUploadClick={handleImageUploadClick}
        onFileUploadClick={handleFileUploadClick}
        onFileSelect={handleFileSelect}
        onRemoveImage={handleRemoveImage}
      />
      
      {/* 帖子详情模态框 */}
      {showPostDetailModal && selectedPostDetail && (
        <div className={styles.modalOverlay} onClick={handleClosePostDetailModal}>
          <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
            
            
            <div className={styles.modalBody}>
              {/* 帖子头部信息 */}
              <div className={styles.publishInputArea}>
                <div className={styles.userAvatarSection}>
                  <img 
                    src={getUserAvatar(selectedPostDetail.userAvatar)} 
                    alt={selectedPostDetail.userName} 
                    className={styles.modalUserAvatar}
                    onError={(e) => handleImageError(e, `modal-avatar-${selectedPostDetail.id}`)}
                  />
                </div>
                <div className={styles.inputSection}>
                  <div className={styles.postDetailHeader}>
                    <div className={styles.postDetailUserName}>{selectedPostDetail.userName}</div>
                    <div className={styles.postDetailTime}>
                      {formatPostTime(selectedPostDetail.createdAt)}
                      {/* 精华和置顶标签 */}
                      {selectedPostDetail.isEssence === 1 && (
                        <span className={styles.essenceTag}>精华</span>
                      )}
                      {selectedPostDetail.isTop === 1 && (
                        <span className={styles.topTag}>置顶</span>
                      )}
                    </div>
                  </div>

                </div>
              </div>
              
              {/* 帖子内容 */}
              <div className={styles.contentInputArea}>
                <div className={styles.postDetailContent}>
                  {selectedPostDetail.summary}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};