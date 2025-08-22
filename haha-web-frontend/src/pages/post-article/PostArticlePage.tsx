import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import { getPostDetail, Post, formatCount, formatPostTime } from '../../apis/post/post';
import { LikeIcon, CommentIcon, ViewIcon, ShareIcon } from '../../components/icons/SocialIcons';
import { getAvatarUrl, getDefaultAvatarUrl, getImageUrl } from '../../utils/image';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import mermaid from 'mermaid';
import styles from './PostArticlePage.module.css';

interface PostArticlePageProps {}

// 自定义代码高亮主题
const steamCodeStyle = {
  'code[class*="language-"]': {
    color: '#d6deeb',
    background: '#0d1117',
    textShadow: '0 1px rgba(0, 0, 0, 0.3)',
    fontFamily: 'Consolas, Monaco, "Andale Mono", "Ubuntu Mono", monospace',
    fontSize: '14px',
    textAlign: 'left',
    whiteSpace: 'pre',
    wordSpacing: 'normal',
    wordBreak: 'normal',
    wordWrap: 'normal',
    lineHeight: '2',
    MozTabSize: '4',
    OTabSize: '4',
    tabSize: '4',
    WebkitHyphens: 'none',
    MozHyphens: 'none',
    msHyphens: 'none',
    hyphens: 'none',
    padding: '1em',
    margin: '0.5em 0',
    overflow: 'auto',
    borderRadius: '4px',
    boxShadow: '0 2px 10px rgba(0, 0, 0, 0.5)',
  },
  'pre[class*="language-"]': {
    color: '#d6deeb',
    background: '#0d1117',
    textShadow: '0 1px rgba(0, 0, 0, 0.3)',
    fontFamily: 'Consolas, Monaco, "Andale Mono", "Ubuntu Mono", monospace',
    fontSize: '14px',
    textAlign: 'left',
    whiteSpace: 'pre',
    wordSpacing: 'normal',
    wordBreak: 'normal',
    wordWrap: 'normal',
    MozTabSize: '4',
    OTabSize: '4',
    tabSize: '4',
    WebkitHyphens: 'none',
    MozHyphens: 'none',
    msHyphens: 'none',
    hyphens: 'none',
    margin: '0.5em 0',
    overflow: 'auto',
    borderRadius: '4px',
    boxShadow: '0 2px 10px rgba(0, 0, 0, 0.5)',
  },
  'comment': {
    color: '#66c0f4',
    fontStyle: 'italic',
  },
  'string': {
    color: '#a5c261',
  },
  'keyword': {
    color: '#67c1f5',
  },
  'function': {
    color: '#a5c261',
  },
  'number': {
    color: '#ff9900',
  },
  'operator': {
    color: '#d6deeb',
  },
};

// 初始化 mermaid 配置
try {
  mermaid.initialize({
    startOnLoad: false,
    theme: 'default',
    securityLevel: 'loose',
    logLevel: 'error',
    fontFamily: 'Consolas, Monaco, "Andale Mono", "Ubuntu Mono", monospace',
    themeVariables: {
      primaryColor: '#e6f3ff',
      primaryTextColor: '#0c2d48',
      primaryBorderColor: '#1a73e8',
      lineColor: '#32a852',
      secondaryColor: '#f0f7ff',
      tertiaryColor: '#f8faff',
      edgeLabelBackground: '#ffffff',
      actorBorder: '#1a73e8',
      actorTextColor: '#0c2d48',
      actorLineColor: '#32a852',
      signalColor: '#ff6b6b',
      signalTextColor: '#0c2d48',
      labelBoxBkgColor: '#e6f3ff',
      labelBoxBorderColor: '#1a73e8',
      labelTextColor: '#0c2d48',
      loopTextColor: '#0c2d48',
      noteBkgColor: '#fff8dc',
      noteTextColor: '#333333',
      noteBorderColor: '#ffcc00',
      classFontSize: '14px',
      titleColor: '#1a73e8',
      altBackground: '#f0f7ff',
      activationBorderColor: '#32a852',
      activationBkgColor: '#e6f3ff',
      sequenceNumberColor: '#0c2d48',
      nodeBorder: '#1a73e8',
      nodeTextColor: '#0c2d48',
      fillType0: '#e6f3ff',
      fillType1: '#fff8dc',
      fillType2: '#f0fff0',
      fillType3: '#fff0f5',
      fillType4: '#f5f5f5',
      fillType5: '#f0f8ff',
      fillType6: '#fffafa',
      fillType7: '#f5fffa'
    }
  });
  console.log('Mermaid 初始化成功');
} catch (error) {
  console.error('Mermaid 初始化失败:', error);
}

// 全局渲染缓存
const globalMermaidCache = new Map<string, string>();

// 优化SVG内容函数
const optimizeSvgForZoom = (svgContent: string): string => {
  if (!svgContent) return '';
  
  let optimized = svgContent.replace(/(<svg[^>]*)\s+width="[^"]*"([^>]*>)/g, '$1$2');
  optimized = optimized.replace(/(<svg[^>]*)\s+height="[^"]*"([^>]*>)/g, '$1$2');
  
  if (!optimized.includes('viewBox')) {
    const widthMatch = svgContent.match(/width="([^"]*)"/); 
    const heightMatch = svgContent.match(/height="([^"]*)"/);  
    
    if (widthMatch && heightMatch) {
      const width = widthMatch[1].replace('px', '');
      const height = heightMatch[1].replace('px', '');
      optimized = optimized.replace(/(<svg[^>]*)(>)/, `$1 viewBox="0 0 ${width} ${height}"$2`);
    } else {
      optimized = optimized.replace(/(<svg[^>]*)(>)/, '$1 viewBox="0 0 800 600"$2');
    }
  }
  
  optimized = optimized.replace(/(<svg[^>]*)(>)/, '$1 shape-rendering="geometricPrecision" text-rendering="optimizeLegibility"$2');
  optimized = optimized.replace(/(<text\s+[^>]*)(>)/g, '$1 font-smooth="always" text-rendering="optimizeLegibility"$2');
  optimized = optimized.replace(/(<path\s+[^>]*)(>)/g, '$1 vector-effect="non-scaling-stroke"$2');
  
  return optimized;
};

// 图表放大弹窗组件
const MermaidZoomModal: React.FC<{
  svgContent: string;
  visible: boolean;
  onClose: () => void;
}> = ({ svgContent, visible, onClose }) => {
  const [scale, setScale] = useState<number>(1);
  const [position, setPosition] = useState<{ x: number, y: number }>({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState<boolean>(false);
  const [dragStart, setDragStart] = useState<{ x: number, y: number }>({ x: 0, y: 0 });
  
  const svgContainerRef = useRef<HTMLDivElement>(null);
  
  const handleZoomIn = () => {
    setScale(prevScale => Math.min(prevScale + 0.2, 3));
  };
  
  const handleZoomOut = () => {
    setScale(prevScale => Math.max(prevScale - 0.2, 0.5));
  };
  
  const handleReset = () => {
    setScale(1);
    setPosition({ x: 0, y: 0 });
  };
  
  const handleMouseDown = (e: React.MouseEvent<HTMLDivElement>) => {
    e.preventDefault();
    setIsDragging(true);
    setDragStart({ x: e.clientX - position.x, y: e.clientY - position.y });
  };
  
  const handleMouseMove = (e: React.MouseEvent<HTMLDivElement>) => {
    if (!isDragging) return;
    e.preventDefault();
    const newX = e.clientX - dragStart.x;
    const newY = e.clientY - dragStart.y;
    setPosition({ x: newX, y: newY });
  };
  
  const handleMouseUp = () => {
    setIsDragging(false);
  };
  
  const handleMouseLeave = () => {
    if (isDragging) {
      setIsDragging(false);
    }
  };
  
  const handleWheel = (e: React.WheelEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.stopPropagation();
    
    const delta = e.deltaY > 0 ? -0.1 : 0.1;
    const newScale = Math.max(0.5, Math.min(3, scale + delta));
    setScale(newScale);
  };
  
  useEffect(() => {
    if (!visible) return;
    
    const handleKeyDown = (e: KeyboardEvent) => {
      switch(e.key) {
        case 'Escape':
          onClose();
          break;
        case '+':
        case '=':
          handleZoomIn();
          break;
        case '-':
          handleZoomOut();
          break;
        case '0':
          handleReset();
          break;
        case 'ArrowLeft':
          setPosition(prev => ({ x: prev.x + 20, y: prev.y }));
          break;
        case 'ArrowRight':
          setPosition(prev => ({ x: prev.x - 20, y: prev.y }));
          break;
        case 'ArrowUp':
          setPosition(prev => ({ x: prev.x, y: prev.y + 20 }));
          break;
        case 'ArrowDown':
          setPosition(prev => ({ x: prev.x, y: prev.y - 20 }));
          break;
      }
    };
    
    window.addEventListener('keydown', handleKeyDown);
    return () => {
      window.removeEventListener('keydown', handleKeyDown);
    };
  }, [visible, onClose]);
  
  useEffect(() => {
    if (!visible) {
      setScale(1);
      setPosition({ x: 0, y: 0 });
    }
  }, [visible]);
  
  if (!visible) return null;

  return (
    <div 
      className={styles.modalOverlay} 
      onClick={onClose}
      onWheel={(e) => {
        e.preventDefault();
        e.stopPropagation();
      }}      
    >
      <div 
        className={styles.modalContent} 
        onClick={(e) => e.stopPropagation()}
        onWheel={(e) => {
          e.preventDefault();
          e.stopPropagation();
        }}
      >
        <div className={styles.modalHeader}>
          <h3>图表详情</h3>
          <div className={styles.modalControls}>
            <button className={styles.controlButton} onClick={handleZoomIn} title="放大">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 4V20M4 12H20" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
              </svg>
            </button>
            <button className={styles.controlButton} onClick={handleZoomOut} title="缩小">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M4 12H20" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
              </svg>
            </button>
            <button className={styles.controlButton} onClick={handleReset} title="重置">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12Z" stroke="currentColor" strokeWidth="2"/>
                <path d="M16 12H8" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
                <path d="M12 8V16" stroke="currentColor" strokeWidth="2" strokeLinecap="round"/>
              </svg>
            </button>
            <button className={styles.closeButton} onClick={onClose} title="关闭">×</button>
          </div>
        </div>
        <div 
          className={styles.modalBody} 
          onWheel={handleWheel}
        >
          <div 
            ref={svgContainerRef}
            className={styles.svgContainer}
            onMouseDown={handleMouseDown}
            onMouseMove={handleMouseMove}
            onMouseUp={handleMouseUp}
            onMouseLeave={handleMouseLeave}
            style={{
              transform: `translate(${position.x}px, ${position.y}px) scale(${scale})`,
              cursor: isDragging ? 'grabbing' : 'grab',
            }}
            dangerouslySetInnerHTML={{ __html: svgContent }} 
          />
        </div>
        <div className={styles.modalFooter}>
          <div className={styles.zoomInfo}>{Math.round(scale * 100)}%</div>
          <div className={styles.helpText}>
            <span>使用鼠标拖动移动图表，滚轮缩放，或使用键盘（+/-/方向键/0）</span>
          </div>
          <button className={styles.zoomButton} onClick={onClose}>关闭</button>
        </div>
      </div>
    </div>
  );
};

// Mermaid 图表组件
const MermaidChart = React.memo(({ chart }: { chart: string }) => {
  const [svgContent, setSvgContent] = useState<string>('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [zoomVisible, setZoomVisible] = useState<boolean>(false);
  
  const chartId = useRef<string>(`mermaid-${Date.now()}-${Math.floor(Math.random() * 10000)}`).current;
  const renderCache = useRef<Map<string, string>>(globalMermaidCache).current;
  
  useEffect(() => {
    if (!chart || chart.trim() === '') {
      setLoading(false);
      setError(null);
      setSvgContent('');
      return;
    }
    
    if (renderCache.has(chart)) {
      setSvgContent(optimizeSvgForZoom(renderCache.get(chart)!));
      setLoading(false);
      setError(null);
      return;
    }
    
    let isMounted = true;
    
    const renderChart = async () => {
      try {
        setLoading(true);
        setError(null);
        const { svg } = await mermaid.render(chartId, chart.trim());
        
        if (isMounted) {
          const optimizedSvg = optimizeSvgForZoom(svg);
          renderCache.set(chart, optimizedSvg);
          setSvgContent(optimizedSvg);
          setLoading(false);
        }
      } catch (err: any) {
        if (isMounted) {
          setError(`Error parsing ${err.toString()}`);
          setLoading(false);
        }
      }finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };
    
    renderChart();
    
    return () => { isMounted = false; };
  }, [chart, chartId, renderCache]);
  
  const handleZoomClick = () => setZoomVisible(true);
  const handleCloseZoom = () => setZoomVisible(false);
  
  if (error) {
    return (
      <div className={styles.mermaidWrapper}>
        <div className={styles.mermaidError}>{error}</div>
        <div className={styles.mermaidContainer}>
          <pre>{chart}</pre>
        </div>
      </div>
    );
  }
  
  return (
    <>
      <div className={styles.mermaidWrapper}>
        {svgContent ? (
          <>
            <div 
              className={styles.mermaidContainer} 
              dangerouslySetInnerHTML={{ __html: svgContent }} 
              onClick={handleZoomClick}
            />
            <div className={styles.zoomIconContainer} onClick={handleZoomClick}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M15 3L21 3M21 3V9M21 3L14 10M9 21L3 21M3 21L3 15M3 21L10 14" stroke="white" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
              <span>点击放大</span>
            </div>
          </>
        ) : loading ? (
          <div className={styles.mermaidContainer} style={{display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '20px'}}>
            <div>图表渲染中...</div>
          </div>
        ) : (
          <div className={styles.mermaidError}>渲染图表时出现问题</div>
        )}
      </div>
      <MermaidZoomModal 
        svgContent={svgContent} 
        visible={zoomVisible} 
        onClose={handleCloseZoom} 
      />
    </>
  );
}, (prevProps, nextProps) => prevProps.chart === nextProps.chart);

// Markdown渲染组件
const MarkdownContent: React.FC<{ content: string }> = ({ content }) => {
  return (
    <div className={styles.markdownContainer}>
      <ReactMarkdown
        remarkPlugins={[remarkGfm]}
        components={{
          code({ node, inline, className, children, ...props }: any) {
            const match = /language-(\w+)/.exec(className || '');
            const codeContent = String(children).replace(/\n$/, '');
            
            // 处理 mermaid 图表
            if (!inline && match && match[1] === 'mermaid') {
              return <MermaidChart chart={codeContent} />;
            }
            
            // 处理普通代码
            return !inline && match ? (
              <div style={{ 
                maxWidth: '100%', 
                overflow: 'auto',
                margin: '1em 0'
              }}>
                <SyntaxHighlighter
                  style={steamCodeStyle}
                  language={match[1]}
                  PreTag="div"
                  customStyle={{
                    margin: 0,
                    padding: '1em',
                    maxWidth: '100%',
                    boxSizing: 'border-box',
                    overflow: 'auto'
                  }}
                  {...props}
                >
                  {codeContent}
                </SyntaxHighlighter>
              </div>
            ) : (
              <code className={className} {...props}>
                {children}
              </code>
            );
          },
          a({ node, children, href, ...props }: any) {
            return (
              <a 
                href={href} 
                target="_blank"
                rel="noopener noreferrer"
                {...props}
              >
                {children}
              </a>
            );
          },
        }}
      >
        {content}
      </ReactMarkdown>
    </div>
  );
};

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

export const PostArticlePage: React.FC<PostArticlePageProps> = () => {
  const { postId } = useParams<{ postId: string }>();
  
  const [post, setPost] = useState<Post | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [failedImages, setFailedImages] = useState<Set<string>>(new Set());

  // 加载帖子详情
  const loadPostDetail = async (id: string) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await getPostDetail({
        postId: id
      });
      
      if (response.code === 'SUCCESS' && response.data) {
        
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



  // 重新获取帖子详情
  const fetchPostDetail = () => {
    if (postId) {
      loadPostDetail(postId);
    }
  };

  // 页面初始化
  useEffect(() => {
    if (postId) {
      loadPostDetail(postId);
    } else {
      setError('缺少帖子ID参数');
    }
  }, [postId]);

  // 初始化 mermaid
  useEffect(() => {
    try {
      mermaid.contentLoaded();
      console.log('Mermaid content loaded');
    } catch (err) {
      console.error('初始化 Mermaid 失败:', err);
    }
  }, []);



  return (
    <div className={styles.pageContainer} style={{ userSelect: 'none', WebkitUserSelect: 'none', MozUserSelect: 'none', msUserSelect: 'none' }}>
      
      <div className={styles.pageContent}>
        <div className={styles.mainContent}>

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
              {/* 封面图片 - 类似Notion的长条图片 */}
              {post.articleExtension?.coverImage && (
                <div className={styles.coverImageContainer}>
                  <img 
                    src={getImageUrl(post.articleExtension.coverImage)} 
                    alt={post.title || '文章封面'}
                    className={styles.coverImage}
                    onError={(e) => {
                      const target = e.target as HTMLImageElement;
                      const imageKey = `cover-image-${post.id}`;
                      if (!failedImages.has(imageKey)) {
                        setFailedImages(prev => new Set(prev).add(imageKey));
                        // 封面图片加载失败时隐藏整个容器
                        const container = target.parentElement;
                        if (container) {
                          container.style.display = 'none';
                        }
                      }
                    }}
                  />
                </div>
              )}
              
              {/* 帖子标题 */}
              {post.title && (
                <h1 className={styles.postTitle}>{post.title}</h1>
              )}
              
              {/* 作者信息 */}
              <div className={styles.authorSection}>
                <div className={styles.authorLeft}>
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
                      <span className={styles.contentType}>{getContentTypeText(post.contentType)}</span>
                      {post.isTop === 1 && <span className={styles.topBadge}>置顶</span>}
                      {post.isEssence === 1 && <span className={styles.essenceBadge}>精华</span>}
                    </div>
                  </div>
                </div>
                <div className={styles.postTime}>
                  {formatPostTime(post.createdAt)}
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

              {/* 帖子内容 */}
              <div className={styles.postContent}>
                
                <div className={styles.postBody}>
                  {/* 使用 Markdown 渲染帖子内容 */}
                  <MarkdownContent content={post.articleExtension?.content || post.summary || ''} />
                  
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
            </div>
          )}
        </div>
      </div>
    </div>
  );
};