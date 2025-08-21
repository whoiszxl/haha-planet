import React, { useState, useCallback } from "react";
import { Button, Input, message, Select, Tag } from "antd";
import { SaveOutlined, SendOutlined, ArrowLeftOutlined } from "@ant-design/icons";
import MDEditor from '@uiw/react-md-editor';
import { useNavigate } from "react-router-dom";
import styles from "./PostArticleEditorPage.module.css";
import { Header } from "../../components";

const { TextArea } = Input;
const { Option } = Select;

interface ArticleData {
  title: string;
  content: string;
  tags: string[];
  summary: string;
}

export const PostArticleEditorPage: React.FC = () => {
  const navigate = useNavigate();
  const [articleData, setArticleData] = useState<ArticleData>({
    title: "",
    content: "",
    tags: [],
    summary: ""
  });

  const [isPublishing, setIsPublishing] = useState(false);
  const [availableTags] = useState(["技术", "生活", "随笔", "教程", "分享", "思考"]);


  const handleInputChange = useCallback((field: keyof ArticleData, value: any) => {
    setArticleData(prev => ({
      ...prev,
      [field]: value
    }));
  }, []);

  const handleTagChange = useCallback((tags: string[]) => {
    setArticleData(prev => ({
      ...prev,
      tags
    }));
  }, []);

  const handleSaveDraft = useCallback(() => {
    localStorage.setItem('article_draft', JSON.stringify(articleData));
    message.success('草稿已保存');
  }, [articleData]);

  const handleLoadDraft = useCallback(() => {
    const draft = localStorage.getItem('article_draft');
    if (draft) {
      setArticleData(JSON.parse(draft));
      message.success('草稿已加载');
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
      // 这里应该调用发布文章的API
      await new Promise(resolve => setTimeout(resolve, 2000)); // 模拟API调用
      message.success('文章发布成功！');
      localStorage.removeItem('article_draft'); // 清除草稿
      navigate('/'); // 返回首页
    } catch (error) {
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
                className={styles.titleInput}
                size="large"
              />
            </div>
            
            <div className={styles.metaRow}>
              <div className={styles.tagsGroup}>
                <Select
                  mode="multiple"
                  placeholder="选择标签"
                  value={articleData.tags}
                  onChange={handleTagChange}
                  className={styles.tagsSelect}
                  maxTagCount={3}
                  size="small"
                >
                  {availableTags.map(tag => (
                    <Option key={tag} value={tag}>{tag}</Option>
                  ))}
                </Select>
              </div>
            </div>

            <div className={styles.summaryRow}>
              <TextArea
                placeholder="请输入文章摘要（可选）..."
                value={articleData.summary}
                onChange={(e) => handleInputChange('summary', e.target.value)}
                rows={2}
                maxLength={200}
                showCount
                className={styles.summaryInput}
              />
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

            textareaProps={{
              placeholder: '请输入文章内容，支持 Markdown 语法...',
              style: {
                fontSize: 14,
                lineHeight: 1.6,
                fontFamily: 'Monaco, Menlo, "Ubuntu Mono", monospace'
              }
            }}
            height={500}
          />
        </div>

        {/* 底部操作区域 */}
        <div className={styles.bottomActions}>
          <div className={styles.actionButtons}>
            <Button 
              icon={<SaveOutlined />} 
              onClick={handleLoadDraft}
              type="text"
            >
              加载草稿
            </Button>
            <Button 
              icon={<SaveOutlined />} 
              onClick={handleSaveDraft}
              type="text"
            >
              保存草稿
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
  );
};