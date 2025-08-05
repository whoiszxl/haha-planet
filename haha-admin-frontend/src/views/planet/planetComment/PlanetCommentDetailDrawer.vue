<template>
  <a-drawer v-model:visible="visible" title="帖子评论详情" :width="width >= 800 ? 800 : '100%'" :footer="false">
    <div class="comment-detail">
      <!-- 评论内容卡片 -->
      <a-card title="评论内容" class="detail-card content-card" :bordered="false">
        <div class="comment-content">
          <div class="content-text">
            {{ dataDetail?.content }}
          </div>
          <div class="media-section" v-if="dataDetail?.mediaUrls">
            <a-divider>媒体文件</a-divider>
            <div class="media-grid">
              <div 
                v-for="(url, index) in parseMediaUrls(dataDetail?.mediaUrls)" 
                :key="index" 
                class="media-item"
              >
                <a-image 
                  v-if="isImage(url)" 
                  :src="url" 
                  :width="100" 
                  :height="100" 
                  fit="cover"
                />
                <div v-else class="video-placeholder">
                  <icon-play-circle-fill />
                  <span>视频文件</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </a-card>

      <!-- 基础信息卡片 -->
      <a-card title="基础信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="medium">
          <a-descriptions-item label="帖子ID">
            <a-tag color="blue">{{ dataDetail?.postId }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="星球ID">
            <a-tag color="green">{{ dataDetail?.planetId }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="评论用户">
            <div class="user-display">
              <span class="user-id">{{ dataDetail?.userId }}</span>
              <a-tag v-if="dataDetail?.isAnonymous" color="gray" size="small">
                <icon-user /> 匿名评论
              </a-tag>
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="评论层级">
            <a-tag v-if="dataDetail?.parentId === 0" color="green" size="large">
              <icon-message /> 顶级评论
            </a-tag>
            <a-tag v-else color="orange" size="large">
              <icon-reply /> 回复 #{{ dataDetail?.parentId }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="回复用户" v-if="dataDetail?.replyToUserId">
            <a-tag color="purple">{{ dataDetail?.replyToUserId }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="审核状态">
            <a-tag :color="getAuditStatusColor(dataDetail?.auditStatus)" size="large">
              <component :is="getAuditStatusIcon(dataDetail?.auditStatus)" />
              {{ getAuditStatusText(dataDetail?.auditStatus) }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 互动数据卡片 -->
      <a-card title="互动数据" class="detail-card" :bordered="false">
        <div class="interaction-stats">
          <div class="stat-item">
            <div class="stat-icon like-icon">
              <icon-heart />
            </div>
            <div class="stat-content">
              <div class="stat-label">点赞数</div>
              <a-statistic 
                :value="dataDetail?.likeCount || 0" 
                :value-style="{ 
                  fontSize: '24px', 
                  fontWeight: 'bold', 
                  color: getInteractionColor(dataDetail?.likeCount) 
                }"
              />
            </div>
          </div>
          <div class="stat-item">
            <div class="stat-icon reply-icon">
              <icon-message />
            </div>
            <div class="stat-content">
              <div class="stat-label">回复数</div>
              <a-statistic 
                :value="dataDetail?.replyCount || 0" 
                :value-style="{ 
                  fontSize: '24px', 
                  fontWeight: 'bold', 
                  color: getInteractionColor(dataDetail?.replyCount) 
                }"
              />
            </div>
          </div>
        </div>
        
        <!-- 互动热度 -->
        <a-divider>互动热度</a-divider>
        <div class="heat-analysis">
          <div class="heat-item">
            <span class="heat-label">点赞热度</span>
            <a-progress 
              :percent="getHeatPercent(dataDetail?.likeCount, 'like')" 
              :color="getHeatColor(dataDetail?.likeCount, 'like')"
              size="small"
            />
            <span class="heat-text">{{ getHeatLevel(dataDetail?.likeCount, 'like') }}</span>
          </div>
          <div class="heat-item">
            <span class="heat-label">回复热度</span>
            <a-progress 
              :percent="getHeatPercent(dataDetail?.replyCount, 'reply')" 
              :color="getHeatColor(dataDetail?.replyCount, 'reply')"
              size="small"
            />
            <span class="heat-text">{{ getHeatLevel(dataDetail?.replyCount, 'reply') }}</span>
          </div>
        </div>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="创建者">{{ dataDetail?.createdBy }}</a-descriptions-item>
          <a-descriptions-item label="更新者">{{ dataDetail?.updatedBy }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ formatDateTime(dataDetail?.createdAt) }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间">
            {{ formatDateTime(dataDetail?.updatedAt) }}
          </a-descriptions-item>
          <a-descriptions-item label="版本号">
            <a-tag>v{{ dataDetail?.version }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag :color="dataDetail?.status === 1 ? 'green' : 'red'">
              {{ dataDetail?.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetCommentDetailResp, getPlanetComment } from '@/apis'
import { formatDateTime } from '@/utils/format'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetCommentDetailResp>()

// 审核状态相关方法
const getAuditStatusText = (status: number) => {
  const statusMap = { 1: '待审核', 2: '审核通过', 3: '审核拒绝' }
  return statusMap[status] || '未知'
}

const getAuditStatusColor = (status: number) => {
  const colorMap = { 1: 'orange', 2: 'green', 3: 'red' }
  return colorMap[status] || 'gray'
}

const getAuditStatusIcon = (status: number) => {
  const iconMap = { 1: 'icon-clock-circle', 2: 'icon-check-circle', 3: 'icon-close-circle' }
  return iconMap[status] || 'icon-question-circle'
}

// 媒体文件处理
const parseMediaUrls = (urls: string) => {
  if (!urls) return []
  try {
    return JSON.parse(urls)
  } catch {
    return urls.split(',').filter(url => url.trim())
  }
}

const isImage = (url: string) => {
  return /\.(jpg|jpeg|png|gif|webp)$/i.test(url)
}

// 互动数据相关方法
const getInteractionColor = (count: number) => {
  if (count > 100) return '#f5222d'
  if (count > 50) return '#fa541c'
  if (count > 10) return '#1890ff'
  return '#8c8c8c'
}

const getHeatPercent = (count: number, type: 'like' | 'reply') => {
  const maxValues = { like: 200, reply: 100 }
  const max = maxValues[type]
  return Math.min((count / max) * 100, 100)
}

const getHeatColor = (count: number, type: 'like' | 'reply') => {
  const thresholds = { 
    like: { high: 100, medium: 50, low: 10 },
    reply: { high: 50, medium: 20, low: 5 }
  }
  const threshold = thresholds[type]
  
  if (count > threshold.high) return '#f5222d'
  if (count > threshold.medium) return '#fa541c'
  if (count > threshold.low) return '#1890ff'
  return '#8c8c8c'
}

const getHeatLevel = (count: number, type: 'like' | 'reply') => {
  const thresholds = { 
    like: { high: 100, medium: 50, low: 10 },
    reply: { high: 50, medium: 20, low: 5 }
  }
  const threshold = thresholds[type]
  
  if (count > threshold.high) return '火热'
  if (count > threshold.medium) return '活跃'
  if (count > threshold.low) return '一般'
  if (count > 0) return '较少'
  return '无互动'
}

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetComment(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.comment-detail {
  .detail-card {
    margin-bottom: 16px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    :deep(.ant-card-head) {
      border-bottom: 1px solid var(--color-border-2);
      
      .ant-card-head-title {
        font-weight: 600;
        color: var(--color-text-1);
      }
    }
  }
  
  .content-card {
    .comment-content {
      .content-text {
        font-size: 16px;
        line-height: 1.6;
        color: var(--color-text-1);
        background: var(--color-fill-1);
        padding: 16px;
        border-radius: 8px;
        border-left: 4px solid var(--color-primary);
        margin-bottom: 16px;
      }
      
      .media-section {
        .media-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
          gap: 12px;
          
          .media-item {
            border-radius: 8px;
            overflow: hidden;
            
            .video-placeholder {
              width: 100px;
              height: 100px;
              background: var(--color-fill-2);
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              color: var(--color-text-3);
              font-size: 12px;
              
              svg {
                font-size: 24px;
                margin-bottom: 4px;
              }
            }
          }
        }
      }
    }
  }
  
  .user-display {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .user-id {
      font-weight: 500;
    }
  }
  
  .interaction-stats {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 24px;
    margin-bottom: 16px;
    
    .stat-item {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px;
      background: var(--color-fill-1);
      border-radius: 8px;
      
      .stat-icon {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        
        &.like-icon {
          background: linear-gradient(135deg, #ff6b6b, #ee5a52);
          color: white;
        }
        
        &.reply-icon {
          background: linear-gradient(135deg, #4ecdc4, #44a08d);
          color: white;
        }
      }
      
      .stat-content {
        flex: 1;
        
        .stat-label {
          color: var(--color-text-2);
          font-size: 14px;
          margin-bottom: 4px;
        }
      }
    }
  }
  
  .heat-analysis {
    .heat-item {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 12px;
      
      .heat-label {
        min-width: 80px;
        color: var(--color-text-2);
        font-size: 14px;
      }
      
      .heat-text {
        min-width: 60px;
        font-size: 12px;
        color: var(--color-text-3);
      }
    }
  }
}

:deep(.ant-statistic-content-value) {
  font-weight: bold !important;
}
</style>
