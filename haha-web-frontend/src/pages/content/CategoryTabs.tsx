import React from 'react';
import styles from './PlanetContentPage.module.css';

interface CategoryTabsProps {
  sortType: number;
  onSortChange: (sortType: number) => void;
}

export const CategoryTabs: React.FC<CategoryTabsProps> = ({
  sortType,
  onSortChange
}) => {
  const tabs = [
    { id: 1, label: '最新' },
    { id: 2, label: '最多点赞' },
    { id: 3, label: '最多评论' },
    { id: 4, label: '最多浏览' },
    { id: 5, label: '精华' },
    { id: 6, label: '只看星主' },
    { id: 7, label: '问答' },
    { id: 8, label: '文件' }
  ];

  return (
    <div className={styles.categorySection}>
      <div className={styles.categoryContainer}>
        <div className={styles.tabsContainer}>
          {tabs.map((tab) => (
            <div
              key={tab.id}
              className={`${styles.tab} ${sortType === tab.id ? styles.activeTab : ''}`}
              onClick={() => onSortChange(tab.id)}
            >
              {tab.label}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};