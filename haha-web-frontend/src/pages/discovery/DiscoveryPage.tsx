import React, { useState, useEffect } from 'react';
import { SearchIcon } from '../../components/icons/SocialIcons';
import styles from './DiscoveryPage.module.css';
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
        page,
        pageSize,
        sortType: sort
      };
      
      // 只有选择具体分类时才传入categoryId参数，全部分类(categoryId=0)时不传
      if (categoryId !== 0) {
        params.categoryId = categoryId;
      }
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

  // 处理分页变化
  const handlePageChange = (page: number) => {
    if (page < 1 || page > Math.ceil(total / pageSize)) return;
    loadPlanets(selectedCategory, sortType, page);
    // 翻页后滚动到页面顶部
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  // 生成页码数组
  const generatePageNumbers = () => {
    const totalPages = Math.ceil(total / pageSize);
    const pages: (number | string)[] = [];
    
    if (totalPages <= 7) {
      // 总页数少于等于7页，显示所有页码
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i);
      }
    } else {
      // 总页数大于7页，显示省略号
      if (currentPage <= 4) {
        // 当前页在前4页
        for (let i = 1; i <= 5; i++) {
          pages.push(i);
        }
        pages.push('...');
        pages.push(totalPages);
      } else if (currentPage >= totalPages - 3) {
        // 当前页在后4页
        pages.push(1);
        pages.push('...');
        for (let i = totalPages - 4; i <= totalPages; i++) {
          pages.push(i);
        }
      } else {
        // 当前页在中间
        pages.push(1);
        pages.push('...');
        for (let i = currentPage - 1; i <= currentPage + 1; i++) {
          pages.push(i);
        }
        pages.push('...');
        pages.push(totalPages);
      }
    }
    
    return pages;
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

  // 格式化价格显示
  const formatPrice = (planet: Planet) => {
    // priceType: 1=免费, 2=付费
    if (planet.priceType === 1) {
      return { type: 'free', display: '免费' };
    } else if (planet.priceType === 2) {
      // 如果有优惠价且不等于原价，显示为限时优惠
      if (planet.discountPrice && planet.originalPrice && planet.discountPrice < planet.originalPrice) {
        return { 
          type: 'discount', 
          display: `¥${planet.discountPrice}`,
          originalPrice: `¥${planet.originalPrice}`
        };
      } else {
        // 正常付费价格
        return { type: 'paid', display: `¥${planet.price}` };
      }
    }
    return { type: 'free', display: '免费' };
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
              <SearchIcon className={styles.searchIcon} />
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
                        <span>成员: {formatNumber(planet.memberCount)} 人</span>
                        <span>
                          {planet.createTime ? (
                            `创建时间: ${new Date(planet.createTime).toLocaleDateString()}`
                          ) : (
                            `最后活跃: ${new Date(planet.lastActiveTime).toLocaleDateString()}`
                          )}
                        </span>
                      </div>
                    </div>

                    {/* 价格信息 */}
                    <div className={styles.priceInfo}>
                      {(() => {
                        const priceInfo = formatPrice(planet);
                        return (
                          <div className={`${styles.price} ${styles[priceInfo.type]}`}>
                            {priceInfo.type === 'discount' ? (
                              <>
                                <span className={styles.discountPrice}>{priceInfo.display}</span>
                                <span className={styles.originalPrice}>{priceInfo.originalPrice}</span>
                              </>
                            ) : (
                              <span>{priceInfo.display}</span>
                            )}
                          </div>
                        );
                      })()}
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* 分页 */}
            {total > pageSize && (
              <div className={styles.pagination}>
                <div className={styles.paginationInfo}>
                  共 {total} 个星球，第 {currentPage} 页，共 {Math.ceil(total / pageSize)} 页
                </div>
                <div className={styles.paginationButtons}>
                  {/* 上一页按钮 */}
                  <button
                    className={`${styles.pageButton} ${currentPage === 1 ? styles.disabled : ''}`}
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 1}
                  >
                    上一页
                  </button>

                  {/* 页码按钮 */}
                  {generatePageNumbers().map((page, index) => (
                    <button
                      key={index}
                      className={`${styles.pageButton} ${
                        page === currentPage ? styles.active : ''
                      } ${page === '...' ? styles.ellipsis : ''}`}
                      onClick={() => typeof page === 'number' && handlePageChange(page)}
                      disabled={page === '...'}
                    >
                      {page}
                    </button>
                  ))}

                  {/* 下一页按钮 */}
                  <button
                    className={`${styles.pageButton} ${
                      currentPage === Math.ceil(total / pageSize) ? styles.disabled : ''
                    }`}
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === Math.ceil(total / pageSize)}
                  >
                    下一页
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};