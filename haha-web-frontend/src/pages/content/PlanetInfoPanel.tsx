import React from 'react';
import styles from './PlanetContentPage.module.css';
import { Planet } from '../../apis/planet/planet';
import { UserPlanet } from '../../apis/user/userPlanet';
import { getAvatarUrl } from '../../utils/image';
import PlanetContentSkeleton from '../../components/skeleton/PlanetContentSkeleton';

interface AdminUser {
  userId: number;
  nickname: string;
  avatar?: string;
  memberType: number;
  memberTypeName: string;
}

interface PlanetInfoPanelProps {
  selectedPlanet: UserPlanet | null;
  planetDetail: (Planet & { adminUsers?: AdminUser[] }) | null;
  detailLoading: boolean;
  onImageError: (e: React.SyntheticEvent<HTMLImageElement>, key: string, fallback?: string) => void;
  formatNumber: (num: number) => string;
  getMemberBadgeClass: (memberType?: number) => string;
}

export const PlanetInfoPanel: React.FC<PlanetInfoPanelProps> = ({
  selectedPlanet,
  planetDetail,
  detailLoading,
  onImageError,
  formatNumber,
  getMemberBadgeClass
}) => {
  if (!selectedPlanet) {
    return null;
  }

  return (
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
                    onError={(e) => onImageError(e, `planet-large-${selectedPlanet?.id}`, '/default-planet.png')}
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
                            onError={(e) => onImageError(e, `admin-${admin.userId}`, '/default-avatar.png')}
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
  );
};