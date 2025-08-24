import React, { useRef } from 'react';
import styles from './PlanetContentPage.module.css';
import { UserPlanet } from '../../apis/user/userPlanet';
import { getAvatarUrl, getDefaultAvatarUrl, getImageUrl } from '../../utils/image';
import {
  EmojiIcon,
  ImageIcon,
  DocumentIcon,
  BoldIcon,
  HeadingIcon
} from '../../components/icons/SocialIcons';

interface PublishForm {
  title: string;
  summary: string;
}

interface PublishModalProps {
  show: boolean;
  selectedPlanet: UserPlanet | null;
  publishForm: PublishForm;
  publishLoading: boolean;
  uploadingImage: boolean;
  uploadedImages: {url: string, fileName: string}[];
  onClose: () => void;
  onPublishFormChange: (field: keyof PublishForm, value: string) => void;
  onPublishPost: () => void;
  onImageUploadClick: () => void;
  onFileSelect: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onRemoveImage: (imageUrl: string) => void;
}

export const PublishModal: React.FC<PublishModalProps> = ({
  show,
  selectedPlanet,
  publishForm,
  publishLoading,
  uploadingImage,
  uploadedImages,
  onClose,
  onPublishFormChange,
  onPublishPost,
  onImageUploadClick,
  onFileSelect,
  onRemoveImage
}) => {
  const fileInputRef = useRef<HTMLInputElement>(null);

  if (!show) {
    return null;
  }

  return (
    <div className={styles.modalOverlay} onClick={onClose}>
      <div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
        <div className={styles.modalHeader}>
          <h3 className={styles.modalTitle}>发布内容</h3>
          <button
            className={styles.closeButton}
            onClick={onClose}
          >
            ×
          </button>
        </div>
        
        <div className={styles.modalBody}>
          {/* 用户头像和输入区域 */}
          <div className={styles.publishInputArea}>
            <div className={styles.userAvatarSection}>
              <img 
                src={selectedPlanet ? getAvatarUrl(selectedPlanet.avatar) : getDefaultAvatarUrl()} 
                alt="用户头像" 
                className={styles.modalUserAvatar}
              />
            </div>
            <div className={styles.inputSection}>
              <input
                type="text"
                value={publishForm.title}
                onChange={(e) => onPublishFormChange('title', e.target.value)}
                placeholder="点击发表主题..."
                className={styles.modalTitleInput}
                maxLength={100}
              />
            </div>
          </div>
          
          {/* 内容输入区域 */}
          <div className={styles.contentInputArea}>
            <textarea
              value={publishForm.summary}
              onChange={(e) => onPublishFormChange('summary', e.target.value)}
              placeholder=""
              className={styles.modalContentTextarea}
              rows={8}
              maxLength={10000}
            />
          </div>
          
          {/* 图片展示区域 */}
          {uploadedImages.length > 0 && (
             <div className={styles.imagePreviewArea}>
               {uploadedImages.map((item, index) => (
                 <div key={index} className={styles.imagePreviewItem}>
                   <img 
                     src={getImageUrl(item.url)} 
                     alt={item.fileName}
                     className={styles.previewImage}
                   />
                   <button 
                     className={styles.removeImageButton}
                     onClick={() => onRemoveImage(item.url)}
                     title="删除图片"
                   >
                     ×
                   </button>
                 </div>
               ))}
             </div>
           )}
        </div>
        
        <div className={styles.modalFooter}>
          {/* 工具栏 */}
          <div className={styles.modalToolbar}>
            <div className={styles.toolbarLeft}>
              <button className={styles.toolButton}>
                <EmojiIcon className={styles.toolIcon} />
              </button>
              <button 
                className={styles.toolButton}
                onClick={onImageUploadClick}
                disabled={uploadingImage}
                title={uploadingImage ? '图片上传中...' : '上传图片'}
              >
                <ImageIcon className={styles.toolIcon} />
                {uploadingImage && <span style={{fontSize: '12px', marginLeft: '4px'}}>上传中...</span>}
              </button>
              <button className={styles.toolButton}>
                <DocumentIcon className={styles.toolIcon} />
              </button>
              <button className={styles.toolButton}>
                <BoldIcon className={styles.toolIcon} />
              </button>
              <button className={styles.toolButton}>
                <HeadingIcon className={styles.toolIcon} />
              </button>
            </div>
            
            {/* 隐藏的文件输入框 */}
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              style={{ display: 'none' }}
              onChange={onFileSelect}
            />
            
            <div className={styles.toolbarRight}>
              <span className={styles.charCount}>({publishForm.summary.length}/10000)</span>
              <button 
                className={styles.modalPublishButton}
                onClick={onPublishPost}
                disabled={publishLoading || !publishForm.title.trim() || !publishForm.summary.trim()}
              >
                {publishLoading ? '发布中...' : '发布'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};