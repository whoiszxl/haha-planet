import React, { useState, useEffect } from "react";
import styles from "./DiscoveryPage.module.css";
import { Footer, Header } from "../../components";
import { 
  getPlanetCategories, 
  getPlanetsByCategory, 
  type PlanetCategory, 
  type Planet, 
  type PlanetListReq 
} from "../../apis/planet/planet";

type SortType = 1 | 2 | 3;

export const DiscoveryPage: React.FC = () => {
  const [categories, setCategories] = useState<PlanetCategory[]>([]);
  const [planets, setPlanets] = useState<Planet[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number>(0);
  const [sortType, setSortType] = useState<SortType>(1);
  const [loading, setLoading] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [total, setTotal] = useState(0);
  const pageSize = 10;

  // 加载分类列表
  useEffect(() => {
    const loadCategories = async () => {
      try {
        const response = await getPlanetCategories();
        if (response.data) {
          const allCategory: PlanetCategory = { id: 0, categoryName: '全部', iconUrl: '', parentId: 0, level: true, sort: 999, status: 1 };
          setCategories([allCategory, ...response.data]);
        }
      } catch (error) {
        console.error('加载分类失败:', error);
      }
    };
    loadCategories();
  }, []);

  // 加载星球列表
  const loadPlanets = async (categoryId: number, sort: SortType, page: number = 1) => {
    setLoading(true);
    try {
      const params: PlanetListReq = {
        categoryId,
        page,
        pageSize,
        sortType: sort
      };
      const response = await getPlanetsByCategory(params);
      if (response.data && response.data.list) {
        setPlanets(response.data.list || []);
        setTotal(response.data.total || 0);
        setCurrentPage(page);
      } else {
        setPlanets([]);
        setTotal(0);
      }
    } catch (error) {
      console.error('加载星球列表失败:', error);
      setPlanets([]);
    } finally {
      setLoading(false);
    }
  };

  // 分类切换
  const handleCategoryChange = (categoryId: number) => {
    setSelectedCategory(categoryId);
    setCurrentPage(1);
    loadPlanets(categoryId, sortType, 1);
  };

  // 排序切换
  const handleSortChange = (sort: SortType) => {
    setSortType(sort);
    setCurrentPage(1);
    loadPlanets(selectedCategory, sort, 1);
  };

  // 搜索
  const handleSearch = () => {
    // TODO: 实现搜索功能
    console.log('搜索关键词:', searchKeyword);
  };

  // 页面加载时获取默认数据
  useEffect(() => {
    loadPlanets(0, 1, 1);
  }, []);

  // 获取星球头像颜色
  const getAvatarColor = (name: string) => {
    const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#feca57', '#ff9ff3', '#54a0ff'];
    const index = name.charCodeAt(0) % colors.length;
    return colors[index];
  };

  // 格式化数字
  const formatNumber = (num: number) => {
    if (num >= 10000) {
      return (num / 10000).toFixed(1) + 'w';
    }
    return num.toString();
  };

  return (
    <div className={styles.discoveryPage}>
      <Header />
      
      <div className={styles.container}>
        {/* 页面头部 */}
        <div className={styles.header}>
          <h1 className={styles.title}>发现星球</h1>
          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="搜索星球..."
              className={styles.searchInput}
              value={searchKeyword}
              onChange={(e) => setSearchKeyword(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <button className={styles.searchButton} onClick={handleSearch}>
              🔍
            </button>
          </div>
        </div>

        <div className={styles.content}>
          {/* 左侧分类栏 */}
          <div className={styles.sidebar}>
            <div className={styles.categoryList}>
              <div className={styles.categoryTitle}>星球分类</div>
              {categories.map((category) => (
                <div
                  key={category.id}
                  className={`${styles.categoryItem} ${
                    selectedCategory === category.id ? styles.active : ''
                  }`}
                  onClick={() => handleCategoryChange(category.id)}
                >
                  {category.categoryName}
                </div>
              ))}
            </div>
          </div>

          {/* 主内容区 */}
          <div className={styles.mainContent}>
            {/* 排序标签 */}
            <div className={styles.sortTabs}>
              <button
                className={`${styles.sortTab} ${sortType === 1 ? styles.active : ''}`}
                onClick={() => handleSortChange(1)}
              >
                按人气
              </button>
              <button
                className={`${styles.sortTab} ${sortType === 2 ? styles.active : ''}`}
                onClick={() => handleSortChange(2)}
              >
                最新创建
              </button>
              <button
                className={`${styles.sortTab} ${sortType === 3 ? styles.active : ''}`}
                onClick={() => handleSortChange(3)}
              >
                内容最多
              </button>
            </div>

            {/* 星球列表 */}
            <div className={styles.planetList}>
              {loading ? (
                <div className={styles.loading}>加载中...</div>
              ) : !planets || planets.length === 0 ? (
                <div className={styles.empty}>
                  <div className={styles.emptyIcon}>🌍</div>
                  <div>暂无星球数据</div>
                </div>
              ) : (
                planets.map((planet) => (
                  <div key={planet.id} className={styles.planetItem}>
                    {/* 星球头像 */}
                    <div 
                      className={styles.planetAvatar}
                      style={{ backgroundColor: getAvatarColor(planet.name) }}
                    >
                      {planet.avatar ? (
                        <img src={planet.avatar} alt={planet.name} style={{ width: '100%', height: '100%', borderRadius: '8px' }} />
                      ) : (
                        planet.name.charAt(0)
                      )}
                    </div>

                    {/* 星球信息 */}
                    <div className={styles.planetInfo}>
                      <div className={styles.planetName}>
                        {planet.name}
                        {planet.isFeatured === 1 && (
                      <span className={styles.hotBadge}>推荐</span>
                    )}
                      </div>
                      <div className={styles.planetDesc}>{planet.description}</div>
                      <div className={styles.planetMeta}>
                        <span>分类: {planet.categoryName || '未分类'}</span>
                        <span>内容: {planet.postCount} 篇</span>
                        <span>
                          {planet.createTime ? (
                            `创建时间: ${new Date(planet.createTime).toLocaleDateString()}`
                          ) : (
                            `最后活跃: ${new Date(planet.lastActiveTime).toLocaleDateString()}`
                          )}
                        </span>
                      </div>
                    </div>

                    {/* 成员数统计 */}
                    <div className={styles.planetStats}>
                      <div className={styles.memberCount}>{formatNumber(planet.memberCount)}</div>
                      <div className={styles.memberLabel}>成员</div>
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* 分页 */}
            {total > pageSize && (
              <div className={styles.pagination}>
                {/* 这里可以添加分页组件 */}
                <div>共 {total} 个星球，第 {currentPage} 页</div>
              </div>
            )}
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};