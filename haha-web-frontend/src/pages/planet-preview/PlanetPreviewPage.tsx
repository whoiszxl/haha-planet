import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import styles from "./PlanetPreviewPage.module.css";
import { Footer, Header } from "../../components";
import { getPlanetDetail, Planet } from "../../apis/planet/planet";
import { getAvatarUrl, getDefaultAvatarUrl } from "../../utils/image";

interface PlanetDetailPageProps {}

export const PlanetDetailPage: React.FC<PlanetDetailPageProps> = () => {
  const { planetId } = useParams<{ planetId: string }>();
  const [planet, setPlanet] = useState<Planet | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 加载星球详情数据
  const loadPlanetDetail = async () => {
    if (!planetId) {
      setError("星球ID参数无效");
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      setError(null);
      
      const response = await getPlanetDetail(Number(planetId));
      
      if (response.code === 'SUCCESS' && response.data) {
        setPlanet(response.data);
      } else if (response.code === 'CACHE_NOT_READY') {
        setError("数据正在加载中，请稍后重试");
      } else {
        setError(response.message || "星球不存在或已被删除");
      }
    } catch (err) {
      console.error("加载星球详情失败:", err);
      setError("加载失败，请稍后重试");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPlanetDetail();
  }, [planetId]);

  // 格式化价格显示
  const formatPrice = (price: number) => {
    return `¥${price}`;
  };

  // 格式化数字显示（如成员数、帖子数等）
  const formatNumber = (num: number) => {
    if (num >= 10000) {
      return `${(num / 10000).toFixed(1)}万+`;
    }
    return num.toString();
  };

  // 格式化时间显示
  const formatLastActiveTime = (timeStr: string | null) => {
    if (!timeStr) return "刚刚";
    const now = new Date();
    const lastActive = new Date(timeStr);
    const diffMs = now.getTime() - lastActive.getTime();
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffHours / 24);
    
    if (diffHours < 1) return "刚刚";
    if (diffHours < 24) return `${diffHours}小时前`;
    if (diffDays < 7) return `${diffDays}天前`;
    return timeStr.split(' ')[0]; // 返回日期部分
  };

  // 计算运营时间
  const calculateOperatingTime = (createTime: string | null) => {
    if (!createTime) return "未知";
    const now = new Date();
    const created = new Date(createTime);
    const diffMs = now.getTime() - created.getTime();
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    const years = Math.floor(diffDays / 365);
    const remainingDays = diffDays % 365;
    
    if (years > 0) {
      return `${years} 年零 ${remainingDays} 天`;
    }
    return `${diffDays} 天`;
  };

  // 处理加入星球
  const handleJoinPlanet = () => {
    if (!planet) return;
    
    // 这里可以添加加入星球的逻辑
    console.log("加入星球:", planet.name);
    alert("加入星球功能待实现");
  };

  return (
    <div className={styles.planetDetailPage}>
      <Header />
      
      <div className={styles.pageContent}>
        {loading && (
          <div className={styles.loading}>
            <div className={styles.loadingSpinner}></div>
            <div>正在加载星球详情...</div>
          </div>
        )}

        {error && (
          <div className={styles.error}>
            <div className={styles.icon}>⚠️</div>
            <div>{error}</div>
            <button onClick={loadPlanetDetail}>重试</button>
          </div>
        )}

        {planet && !loading && !error && (
          <div className={styles.pageContainer}>
            {/* 顶部导航 */}
            <div className={styles.topNav}>
              <span className={styles.navItem}>全部</span>
              <span className={styles.navItem}>&gt;</span>
              <span className={styles.navItem}>{planet.categoryName}</span>
              <span className={styles.navItem}>&gt;</span>
              <span className={styles.navItem}>{planet.name}</span>
            </div>

            {/* 星球主要信息 */}
            <div className={styles.planetInfoCard}>
              <div className={styles.planetHeader}>
                <img 
                  src={getAvatarUrl(planet.avatar) || getDefaultAvatarUrl()} 
                  alt={planet.name}
                  className={styles.planetAvatar}
                />
                <div className={styles.planetInfo}>
                  <div className={styles.planetTitle}>
                    <h1 className={styles.planetName}>{planet.name}</h1>
                    <span className={styles.verifyIcon}>🏅</span>
                  </div>
                  <div className={styles.planetMeta}>
                    <span className={styles.updateTime}>最近更新时间：{formatLastActiveTime(planet.lastActiveTime)}</span>
                    <span className={styles.memberCount}>成员数 {formatNumber(planet.memberCount)}</span>
                  </div>
                  <div className={styles.planetStats}>
                    <span className={styles.statsHighlight}>
                      星球已运营 {calculateOperatingTime(planet.createdAt)}，更新了 {formatNumber(planet.postCount)} 篇内容
                    </span>
                  </div>
                </div>
              </div>
            </div>

            {/* 价格和加入区域 */}
            <div className={styles.priceCard}>
              <div className={styles.priceInfo}>
                <span className={styles.currentPrice}>{formatPrice(planet.price)}</span>
                <div className={styles.priceDetails}>
                  {planet.originalPrice && planet.originalPrice > planet.price && (
                    <span className={styles.originalPrice}>原价 {formatPrice(planet.originalPrice)}</span>
                  )}
                  <span className={styles.priceNote}>加入星球72小时内无条件退款</span>
                </div>
              </div>
              <div className={styles.joinArea}>
                <button className={styles.joinButton} onClick={handleJoinPlanet}>
                  加入星球
                </button>
                <span className={styles.validPeriod}>有效期：一年</span>
              </div>
            </div>

            {/* 最新动态 */}
            <div className={styles.activityCard}>
              {planet.adminUsers && planet.adminUsers.length > 0 ? (
                planet.adminUsers.map((adminUser, index) => (
                  <div key={adminUser.userId} className={styles.activityItem}>
                    <img 
                      src={getAvatarUrl(adminUser.avatar) || getDefaultAvatarUrl()} 
                      alt={adminUser.nickname} 
                      className={styles.userAvatar} 
                    />
                    <div className={styles.activityContent}>
                      <div className={styles.activityHeader}>
                        <span className={styles.userName}>
                          {adminUser.planetNickname || adminUser.nickname}
                        </span>
                        <span className={styles.userTag}>{adminUser.memberTypeName}</span>
                        <span className={styles.activityTime}>今天活跃过</span>
                      </div>
                      <div className={styles.activityText}>
                        {adminUser.memberType === 3 
                          ? `${adminUser.memberTypeName}正在管理这个星球，欢迎大家积极参与讨论！` 
                          : `${adminUser.memberTypeName}协助管理星球，为大家提供优质内容和服务。`}
                      </div>
                    </div>
                  </div>
                ))
              ) : (
                <div className={styles.activityItem}>
                  <img src={getDefaultAvatarUrl()} alt="默认头像" className={styles.userAvatar} />
                  <div className={styles.activityContent}>
                    <div className={styles.activityHeader}>
                      <span className={styles.userName}>星球管理员</span>
                      <span className={styles.userTag}>管理员</span>
                      <span className={styles.activityTime}>今天活跃过</span>
                    </div>
                    <div className={styles.activityText}>
                      欢迎加入星球，一起交流学习！
                    </div>
                  </div>
                </div>
              )}
            </div>

            {/* 星球介绍 */}
            <div className={styles.introCard}>
              <h2 className={styles.sectionTitle}>
                星球介绍 
                {planet.isOfficial === 1 && <span className={styles.verifyBadge}>官方认证</span>}
                {planet.isFeatured === 1 && <span className={styles.verifyBadge}>精选推荐</span>}
              </h2>
              <div className={styles.introContent}>
                <div className={styles.categoryInfo}>
                  <span className={styles.categoryLabel}>分类：</span>
                  <span className={styles.categoryName}>{planet.categoryName}</span>
                </div>
                
                <div className={styles.description}>
                  {planet.description}
                </div>
                
                {planet.tags && (
                  <div className={styles.tagsSection}>
                    <span className={styles.tagsLabel}>标签：</span>
                    <div className={styles.tagsList}>
                      {planet.tags.split(',').map((tag, index) => (
                        <span key={index} className={styles.tag}>{tag.trim()}</span>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>

          </div>
        )}
      </div>

      <Footer />
    </div>
  );
};