import React from 'react';
import styles from './PlanetContentSkeleton.module.css';

interface PlanetContentSkeletonProps {
  type?: 'sidebar' | 'main' | 'rightPanel' | 'postList' | 'fullPage';
}

export const PlanetContentSkeleton: React.FC<PlanetContentSkeletonProps> = ({ type = 'main' }) => {
  // 侧边栏骨架屏
  const renderSidebarSkeleton = () => (
    <div className={styles.sidebarSkeleton}>
      <div className={styles.sidebarHeader}>
        <div className={styles.skeletonTitle}></div>
      </div>
      
      <div className={styles.planetSections}>
        {/* 创建的星球 */}
        <div className={styles.sectionSkeleton}>
          <div className={styles.sectionHeaderSkeleton}>
            <div className={styles.skeletonText}></div>
            <div className={styles.skeletonCount}></div>
          </div>
          <div className={styles.planetListSkeleton}>
            {[1, 2, 3].map(i => (
              <div key={i} className={styles.planetItemSkeleton}>
                <div className={styles.planetAvatarSkeleton}></div>
                <div className={styles.planetInfoSkeleton}>
                  <div className={styles.planetNameSkeleton}></div>
                  <div className={styles.planetMetaSkeleton}>
                    <div className={styles.skeletonMeta}></div>
                    <div className={styles.skeletonMeta}></div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
        
        {/* 加入的星球 */}
        <div className={styles.sectionSkeleton}>
          <div className={styles.sectionHeaderSkeleton}>
            <div className={styles.skeletonText}></div>
            <div className={styles.skeletonCount}></div>
          </div>
          <div className={styles.planetListSkeleton}>
            {[1, 2].map(i => (
              <div key={i} className={styles.planetItemSkeleton}>
                <div className={styles.planetAvatarSkeleton}></div>
                <div className={styles.planetInfoSkeleton}>
                  <div className={styles.planetNameSkeleton}></div>
                  <div className={styles.planetMetaSkeleton}>
                    <div className={styles.skeletonMeta}></div>
                    <div className={styles.skeletonMeta}></div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );

  // 主内容区骨架屏
  const renderMainSkeleton = () => (
    <div className={styles.mainSkeleton}>
      {/* 发布区域骨架屏 */}
      <div className={styles.publishSkeleton}>
        <div className={styles.publishBoxSkeleton}>
          <div className={styles.avatarSkeleton}></div>
          <div className={styles.inputSkeleton}></div>
        </div>
        <div className={styles.toolbarSkeleton}>
          {[1, 2, 3, 4, 5, 6].map(i => (
            <div key={i} className={styles.toolIconSkeleton}></div>
          ))}
        </div>
      </div>

      {/* 分类标签区域骨架屏 */}
      <div className={styles.categorySkeleton}>
        <div className={styles.tabsSkeleton}>
          {[1, 2, 3, 4, 5, 6, 7, 8].map(i => (
            <div key={i} className={styles.tabSkeleton}></div>
          ))}
        </div>
      </div>
    </div>
  );

  // 帖子列表骨架屏
  const renderPostListSkeleton = () => (
    <div className={styles.postListSkeleton}>
      {[1, 2, 3, 4, 5].map(i => (
        <div key={i} className={styles.postItemSkeleton}>
          <div className={styles.postAvatarSkeleton}></div>
          <div className={styles.postContentSkeleton}>
            <div className={styles.postHeaderSkeleton}>
              <div className={styles.userNameSkeleton}></div>
              <div className={styles.postTimeSkeleton}></div>
            </div>
            <div className={styles.postBodySkeleton}>
              <div className={styles.postTitleSkeleton}></div>
              <div className={styles.postTextSkeleton}>
                <div className={styles.textLineSkeleton}></div>
                <div className={styles.textLineSkeleton}></div>
                <div className={styles.textLineShortSkeleton}></div>
              </div>
            </div>
            <div className={styles.postFooterSkeleton}>
              {[1, 2, 3, 4].map(j => (
                <div key={j} className={styles.actionSkeleton}>
                  <div className={styles.actionIconSkeleton}></div>
                  <div className={styles.actionTextSkeleton}></div>
                </div>
              ))}
            </div>
          </div>
        </div>
      ))}
    </div>
  );

  // 右侧面板骨架屏
  const renderRightPanelSkeleton = () => (
    <div className={styles.rightPanelSkeleton}>
      <div className={styles.planetInfoSkeleton}>
        <div className={styles.planetHeaderSkeleton}>
          <div className={styles.planetAvatarLargeSkeleton}></div>
          <div className={styles.planetBasicInfoSkeleton}>
            <div className={styles.planetTitleSkeleton}></div>
            <div className={styles.planetStatsSkeleton}>
              <div className={styles.statItemSkeleton}>
                <div className={styles.statNumberSkeleton}></div>
                <div className={styles.statLabelSkeleton}></div>
              </div>
              <div className={styles.statItemSkeleton}>
                <div className={styles.statNumberSkeleton}></div>
                <div className={styles.statLabelSkeleton}></div>
              </div>
            </div>
          </div>
        </div>

        <div className={styles.planetDetailsSkeleton}>
          <div className={styles.detailSectionSkeleton}>
            <div className={styles.sectionTitleSkeleton}></div>
            <div className={styles.descriptionSkeleton}>
              <div className={styles.textLineSkeleton}></div>
              <div className={styles.textLineSkeleton}></div>
              <div className={styles.textLineShortSkeleton}></div>
            </div>
          </div>

          <div className={styles.detailSectionSkeleton}>
            <div className={styles.sectionTitleSkeleton}></div>
            <div className={styles.infoListSkeleton}>
              {[1, 2, 3, 4].map(i => (
                <div key={i} className={styles.infoRowSkeleton}>
                  <div className={styles.infoLabelSkeleton}></div>
                  <div className={styles.infoValueSkeleton}></div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );

  // 全页面骨架屏
  const renderFullPageSkeleton = () => (
    <div className={styles.fullPageSkeleton}>
      <div className={styles.fullPageContent}>
        <div className={styles.sidebarContainer}>
          <PlanetContentSkeleton type="sidebar" />
        </div>
        <div className={styles.mainContainer}>
          <PlanetContentSkeleton type="main" />
          <PlanetContentSkeleton type="postList" />
        </div>
        <div className={styles.rightContainer}>
          <PlanetContentSkeleton type="rightPanel" />
        </div>
      </div>
    </div>
  );

  // 根据类型渲染对应的骨架屏
  switch (type) {
    case 'sidebar':
      return renderSidebarSkeleton();
    case 'main':
      return renderMainSkeleton();
    case 'rightPanel':
      return renderRightPanelSkeleton();
    case 'postList':
      return renderPostListSkeleton();
    case 'fullPage':
      return renderFullPageSkeleton();
    default:
      return renderMainSkeleton();
  }
};

export default PlanetContentSkeleton;
