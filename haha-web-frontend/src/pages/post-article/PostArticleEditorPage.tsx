import React, { useState, useCallback, useEffect } from "react";
import { Button, Input, message, Tag, Dropdown, Modal, Image, Spin, Empty } from "antd";
import { SaveOutlined, SendOutlined, TagOutlined, DeleteOutlined, PictureOutlined } from "@ant-design/icons";
import MDEditor from '@uiw/react-md-editor';
import { useNavigate } from "react-router-dom";
import { createPost } from "../../apis/post/post";
import { getGalleryList, GalleryItem, GalleryListResponse } from "../../apis/gallery/gallery";
import { getImageUrl } from "../../utils/image";
import styles from "./PostArticleEditorPage.module.css";

const { TextArea } = Input;

interface ArticleData {
  title: string;
  content: string;
  tags: string[];
  summary: string;
  coverImage?: string;
}

export const PostArticleEditorPage: React.FC = () => {
  const navigate = useNavigate();
  const [articleData, setArticleData] = useState<ArticleData>({
    title: "",
    content: "",
    tags: [],
    summary: "",
    coverImage: ""
  });

  const [isPublishing, setIsPublishing] = useState(false);
  const [availableTags] = useState(["技术", "生活", "随笔", "教程", "分享", "思考"]);
  const [isTagModalVisible, setIsTagModalVisible] = useState(false);
  const [newTag, setNewTag] = useState("");
  const [isGalleryModalVisible, setIsGalleryModalVisible] = useState(false);
  const [galleryData, setGalleryData] = useState<GalleryListResponse | null>(null);
  const [isLoadingGallery, setIsLoadingGallery] = useState(false);

  // 组件加载时自动恢复草稿
  useEffect(() => {
    const draft = localStorage.getItem('article_draft');
    if (draft) {
      try {
        const parsedDraft = JSON.parse(draft);
        setArticleData(parsedDraft);
        message.info('已自动恢复草稿内容');
      } catch (error) {
        console.error('恢复草稿失败:', error);
        localStorage.removeItem('article_draft');
      }
    }
  }, []);

  // 打开画廊模态框
  const handleOpenGalleryModal = useCallback(async () => {
    setIsGalleryModalVisible(true);
    setIsLoadingGallery(true);
    
    try {
      const response = await getGalleryList();
      if (response.code === 'SUCCESS' && response.data) {
        setGalleryData(response.data.data);
      } else {
        message.error(response.message || '获取画廊数据失败');
      }
    } catch (error) {
      console.error('获取画廊数据失败:', error);
      message.error('获取画廊数据失败，请重试');
    } finally {
      setIsLoadingGallery(false);
    }
  }, []);

  const handleInputChange = useCallback((field: keyof ArticleData, value: any) => {
    const newData = {
      ...articleData,
      [field]: value
    };
    setArticleData(newData);
    
    // 自动保存草稿到本地存储
    localStorage.setItem('article_draft', JSON.stringify(newData));
  }, [articleData]);

  // 选择封面图
  const handleSelectCoverImage = useCallback((imageUrl: string) => {
    handleInputChange('coverImage', imageUrl);
    setIsGalleryModalVisible(false);
    message.success('封面图选择成功');
  }, [handleInputChange]);

  // 移除封面图
  const handleRemoveCoverImage = useCallback(() => {
    handleInputChange('coverImage', '');
    message.success('封面图已移除');
  }, [handleInputChange]);

  const handleTagChange = useCallback((tags: string[]) => {
    const newData = {
      ...articleData,
      tags
    };
    setArticleData(newData);
    
    // 自动保存草稿到本地存储
    localStorage.setItem('article_draft', JSON.stringify(newData));
  }, [articleData]);



  const handleAddNewTag = useCallback(() => {
    if (newTag.trim() && !availableTags.includes(newTag.trim())) {
      const updatedTags = [...articleData.tags, newTag.trim()];
      handleTagChange(updatedTags);
      setNewTag("");
    }
  }, [newTag, availableTags, articleData.tags, handleTagChange]);

  const handleRemoveTag = useCallback((tag: string) => {
    const updatedTags = articleData.tags.filter(t => t !== tag);
    handleTagChange(updatedTags);
  }, [articleData.tags, handleTagChange]);

  const handleAddTag = useCallback((tag: string) => {
    if (!articleData.tags.includes(tag)) {
      const updatedTags = [...articleData.tags, tag];
      handleTagChange(updatedTags);
    }
  }, [articleData.tags, handleTagChange]);

  const handleClearDraft = useCallback(() => {
    localStorage.removeItem('article_draft');
    setArticleData({
      title: "",
      content: "",
      tags: [],
      summary: "",
      coverImage: ""
    });
    message.success('草稿已清空');
  }, []);

  const handleLoadDraft = useCallback(() => {
    const draft = localStorage.getItem('article_draft');
    if (draft) {
      try {
        const parsedDraft = JSON.parse(draft);
        setArticleData(parsedDraft);
        message.success('草稿已重新加载');
      } catch (error) {
        console.error('加载草稿失败:', error);
        localStorage.removeItem('article_draft');
        message.error('草稿数据损坏，已清除');
      }
    } else {
      message.info('暂无草稿');
    }
  }, []);



  const handlePublish = useCallback(async () => {
    if (!articleData.title.trim()) {
      message.error('请输入文章标题');
      return;
    }
    if (!articleData.content.trim()) {
      message.error('请输入文章内容');
      return;
    }

    setIsPublishing(true);
    try {
      // 调用真实的发布文章API
        const response = await createPost({
          planetId: 1, // 默认星球ID，实际应该从用户选择或上下文获取
          title: articleData.title,
          summary: articleData.summary || '',
          contentType: 2, // 2表示文章类型
          content: articleData.content,
          tags: articleData.tags,
          isAnonymous: false,
          isTop: false,
          isEssence: false,
          isOriginal: true,
          coverImage: articleData.coverImage
        });
      
      if (response.code === 'SUCCESS') {
        message.success('文章发布成功！');
        localStorage.removeItem('article_draft'); // 清除草稿
        navigate('/'); // 返回首页
      } else {
        message.error(response.message || '发布失败，请重试');
      }
    } catch (error) {
      console.error('发布文章失败:', error);
      message.error('发布失败，请重试');
    } finally {
      setIsPublishing(false);
    }
  }, [articleData, navigate]);



  return (
    <div className={styles.postArticleEditorPage}>
      <div className={styles.editorContainer}>
        {/* 文章信息区域 */}
        <div className={styles.articleInfo}>
          <div className={styles.compactForm}>
            <div className={styles.titleRow}>
              <Input
                placeholder="请输入文章标题..."
                value={articleData.title}
                onChange={(e) => handleInputChange('title', e.target.value)}
                className={styles.titleInputBorderless}
                size="large"
                bordered={false}
              />
            </div>

            <div className={styles.summaryRow}>
              <TextArea
                placeholder="请输入文章摘要（可选）..."
                value={articleData.summary}
                onChange={(e) => handleInputChange('summary', e.target.value)}
                rows={2}
                maxLength={200}
                className={styles.summaryInputBorderless}
                bordered={false}
              />
            </div>

            {/* 封面图选择区域 */}
            <div className={styles.coverImageRow}>
              <div className={styles.coverImageLabel}>封面图片：</div>
              <div className={styles.coverImageContent}>
                {articleData.coverImage ? (
                  <div className={styles.coverImagePreview}>
                    <Image
                      src={getImageUrl(articleData.coverImage)}
                      alt="封面图预览"
                      width={120}
                      height={80}
                      style={{ objectFit: 'cover', borderRadius: '6px' }}
                    />
                    <div className={styles.coverImageActions}>
                      <Button 
                        size="small" 
                        onClick={handleOpenGalleryModal}
                        type="text"
                      >
                        更换
                      </Button>
                      <Button 
                        size="small" 
                        onClick={handleRemoveCoverImage}
                        type="text"
                        danger
                      >
                        移除
                      </Button>
                    </div>
                  </div>
                ) : (
                  <Button 
                    icon={<PictureOutlined />}
                    onClick={handleOpenGalleryModal}
                    type="dashed"
                    className={styles.selectCoverButton}
                  >
                    选择封面图片
                  </Button>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* 编辑器区域 */}
        <div className={styles.editorSection}>
          <div className={styles.editorHeader}>
            <span className={styles.editorTitle}>Markdown 编辑器</span>
            <div className={styles.editorTips}>
              <span>支持 Markdown 语法，代码高亮，表格等功能</span>
            </div>
          </div>
          
          <MDEditor
            value={articleData.content}
            onChange={(value) => handleInputChange('content', value || '')}
            preview="edit"
            hideToolbar={false}
            data-color-mode="light"
            textareaProps={{
              placeholder: '请输入文章内容，支持 Markdown 语法...'
            }}
          />
        </div>

        {/* 底部操作区域 */}
        <div className={styles.bottomActions}>
          <div className={styles.actionButtons}>
            <div className={styles.leftSection}>
              <Dropdown
                open={isTagModalVisible}
                onOpenChange={setIsTagModalVisible}
                trigger={['click']}
                placement="topLeft"
                dropdownRender={() => (
                  <div className={styles.tagModal}>
                    {/* 已选标签 */}
                    {articleData.tags.length > 0 && (
                      <div className={styles.selectedTagsSection}>
                        {articleData.tags.map((tag, index) => (
                          <Tag
                            key={index}
                            closable
                            onClose={() => handleRemoveTag(tag)}
                            className={styles.selectedTag}
                          >
                            {tag}
                          </Tag>
                        ))}
                      </div>
                    )}
                    
                    {/* 可选标签 */}
                    <div className={styles.availableTagsSection}>
                      {availableTags.filter(tag => !articleData.tags.includes(tag)).map((tag, index) => (
                        <Tag
                          key={index}
                          onClick={() => handleAddTag(tag)}
                          className={styles.availableTag}
                        >
                          {tag}
                        </Tag>
                      ))}
                    </div>
                    
                    {/* 添加新标签 */}
                    <div className={styles.addNewTagSection}>
                      <Input
                        placeholder="添加新标签"
                        value={newTag}
                        onChange={(e) => setNewTag(e.target.value)}
                        onPressEnter={handleAddNewTag}
                        suffix={
                          <Button 
                            type="text" 
                            size="small" 
                            onClick={handleAddNewTag}
                            style={{ padding: 0, minWidth: 'auto' }}
                          >
                            +
                          </Button>
                        }
                      />
                    </div>
                  </div>
                )}
              >
                <Button 
                  icon={<TagOutlined />} 
                  type="text"
                  className={styles.tagButton}
                >
                  标签 ({articleData.tags.length})
                </Button>
              </Dropdown>
              
              {/* 已选标签展示区域 */}
              {articleData.tags.length > 0 && (
                <div className={styles.displayedTagsSection}>
                  {articleData.tags.map((tag, index) => (
                    <Tag
                      key={index}
                      closable
                      onClose={() => handleRemoveTag(tag)}
                      className={styles.displayedTag}
                    >
                      {tag}
                    </Tag>
                  ))}
                </div>
              )}
            </div>
            <div className={styles.rightButtons}>
              <Button 
                icon={<SaveOutlined />} 
                onClick={handleLoadDraft}
                type="text"
              >
                重新加载
              </Button>
              <Button 
                icon={<DeleteOutlined />} 
                onClick={handleClearDraft}
                type="text"
                danger
              >
                清空草稿
              </Button>
              <Button 
                icon={<SendOutlined />} 
                onClick={handlePublish}
                type="primary"
                loading={isPublishing}
              >
                发布文章
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* 画廊选择模态框 */}
      <Modal
        title="选择封面图片"
        open={isGalleryModalVisible}
        onCancel={() => setIsGalleryModalVisible(false)}
        footer={null}
        width={800}
        className={styles.galleryModal}
      >
        {isLoadingGallery ? (
          <div className={styles.galleryLoading}>
            <Spin size="large" />
            <p>正在加载画廊数据...</p>
          </div>
        ) : galleryData && Object.keys(galleryData).length > 0 ? (
          <div className={styles.galleryContent}>
            {Object.entries(galleryData).map(([category, images]) => (
              <div key={category} className={styles.galleryCategory}>
                <h3 className={styles.categoryTitle}>{category}</h3>
                <div className={styles.galleryGrid}>
                  {images.map((image: GalleryItem) => (
                    <div
                      key={image.id}
                      className={styles.galleryImageCard}
                      onClick={() => handleSelectCoverImage(image.fileUrl)}
                    >
                      <Image
                        src={getImageUrl(image.thumbnailUrl || image.fileUrl)}
                        alt={image.description || image.fileName}
                        className={styles.galleryImage}
                        preview={false}
                      />
                      <div className={styles.galleryImageOverlay}>
                        <h4>{image.fileName}</h4>
                        <p>{image.description || '暂无描述'}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <Empty description="暂无画廊数据" />
        )}
      </Modal>
    </div>
  );
};