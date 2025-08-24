import React from 'react';
import styles from './PlanetContentPage.module.css';
import { UserPlanet } from '../../apis/user/userPlanet';
import { getAvatarUrl, getDefaultAvatarUrl } from '../../utils/image';

interface PlanetSidebarProps {
  createdPlanets: UserPlanet[];
  joinedPlanets: UserPlanet[];
  managedPlanets?: UserPlanet[];
  selectedPlanet: UserPlanet | null;
  collapsedSections: { [key: string]: boolean };
  error: string | null;
  onPlanetSelect: (planet: UserPlanet) => void;
  onToggleSection: (sectionKey: string) => void;
  onImageError: (e: React.SyntheticEvent<HTMLImageElement>, key: string, fallback?: string) => void;
  formatNumber: (num: number) => string;
}

export const PlanetSidebar: React.FC<PlanetSidebarProps> = ({
  createdPlanets,
  joinedPlanets,
  managedPlanets = [],
  selectedPlanet,
  collapsedSections,
  error,
  onPlanetSelect,
  onToggleSection,
  onImageError,
  formatNumber
}) => {
  // 获取星球头像
  const getPlanetAvatar = (avatar?: string) => {
    if (!avatar) return getDefaultAvatarUrl();
    return getAvatarUrl(avatar);
  };

  // 渲染星球分组
  const renderPlanetSection = (title: string, planets: UserPlanet[], sectionKey: string) => {
    const isCollapsed = collapsedSections[sectionKey];
    
    return (
      <div className={styles.planetSection}>
        <div 
          className={styles.sectionHeader}
          onClick={() => onToggleSection(sectionKey)}
        >
          <span className={styles.sectionTitle}>{title}</span>
          <span className={styles.sectionCount}>{planets.length}</span>
          <span className={`${styles.collapseIcon} ${isCollapsed ? styles.collapsed : ''}`}>
            ▼
          </span>
        </div>
        {!isCollapsed && (
          <div className={styles.sectionList}>
            <div className={styles.planetList}>
              {planets.map(planet => (
                <div 
                  key={planet.id} 
                  className={`${styles.planetItem} ${selectedPlanet?.id === planet.id ? styles.selected : ''}`}
                  onClick={() => onPlanetSelect(planet)}
                >
                  <div className={styles.planetAvatar}>
                    <img 
                      src={getPlanetAvatar(planet.avatar)} 
                      alt={planet.name}
                    />
                  </div>
                  <div className={styles.planetInfo}>
                    <div className={styles.planetName}>{planet.name}</div>
                    <div className={styles.planetMeta}>
                      <span className={styles.memberCount}>{planet.memberCount || 0}人</span>
                      <span className={styles.postCount}>{planet.postCount || 0}帖</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    );
  };

  return (
    <div className={styles.sidebar}>
      <div className={styles.sidebarHeader}>
        <h3 className={styles.sidebarTitle}>我的星球</h3>
      </div>
      
      {/* 我创建的星球 */}
      {createdPlanets.length > 0 && renderPlanetSection('我创建的', createdPlanets, 'created')}
      
      {/* 我加入的星球 */}
      {joinedPlanets.length > 0 && renderPlanetSection('我加入的', joinedPlanets, 'joined')}
    </div>
  );
};