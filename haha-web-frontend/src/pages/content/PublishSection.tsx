import React from 'react';
import styles from './PlanetContentPage.module.css';
import { UserPlanet } from '../../apis/user/userPlanet';
import { getAvatarUrl, getDefaultAvatarUrl } from '../../utils/image';
import {
  EmojiIcon,
  ImageIcon,
  LinkIcon,
  BoldIcon,
  HeadingIcon,
  WriteIcon
} from '../../components/icons/SocialIcons';

interface PublishSectionProps {
  selectedPlanet: UserPlanet | null;
  userAvatar?: string;
  onPublishClick: () => void;
  onWriteArticleClick: () => void;
  onImageUploadClick: () => void;
  uploadingImage: boolean;
  fileInputRef: React.RefObject<HTMLInputElement>;
  onFileSelect: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onImageError: (e: React.SyntheticEvent<HTMLImageElement>, key: string, fallback?: string) => void;
}

export const PublishSection: React.FC<PublishSectionProps> = ({
  selectedPlanet,
  userAvatar,
  onPublishClick,
  onWriteArticleClick,
  onImageUploadClick,
  uploadingImage,
  fileInputRef,
  onFileSelect,
  onImageError
}) => {
  // 获取用户头像
  const getUserAvatar = (avatar?: string) => {
    if (!avatar) return getDefaultAvatarUrl();
    return getAvatarUrl(avatar);
  };

  return (
    <div className={styles.publishSection}>
      <div className={styles.publishContainer}>
        {/* 发布输入框 */}
        <div className={styles.publishBox}>
          <div 
            className={styles.inputSection}
            onClick={onPublishClick}
          >
            <div className={styles.publishInputArea}>
              <div className={styles.userAvatarSection}>
                <img 
                  src={selectedPlanet ? getAvatarUrl(selectedPlanet.avatar) : getDefaultAvatarUrl()} 
                  alt="用户头像" 
                  className={styles.modalUserAvatar}
                  onError={(e) => onImageError(e, 'publish-avatar', getDefaultAvatarUrl())}
                />
              </div>
              <div className={styles.inputPlaceholder}>
                {selectedPlanet ? `在 ${selectedPlanet.name} 发表主题...` : '请先选择一个星球...'}
              </div>
            </div>
          </div>
        </div>
        
        {/* 工具栏 */}
        <div className={styles.toolbar}>
          <div className={styles.toolIcon}>
            <EmojiIcon />
          </div>
          <div 
            className={styles.toolIcon}
            onClick={onImageUploadClick}
            style={{ opacity: uploadingImage ? 0.5 : 1 }}
          >
            <ImageIcon />
          </div>
          <div className={styles.toolIcon}>
            <LinkIcon />
          </div>
          <div className={styles.toolIcon}>
            <BoldIcon />
          </div>
          <div className={styles.toolIcon}>
            <HeadingIcon />
          </div>
          <div 
            className={styles.toolIcon}
            onClick={onWriteArticleClick}
          >
            <WriteIcon />
            写文章
          </div>
        </div>
        
        {/* 隐藏的文件输入框 */}
        <input
          type="file"
          ref={fileInputRef}
          onChange={onFileSelect}
          accept="image/*"
          style={{ display: 'none' }}
        />
      </div>
    </div>
  );
};