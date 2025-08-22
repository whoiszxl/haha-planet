<template>
  <a-drawer v-model:visible="visible" title="星球帖子详情" :width="width >= 680 ? 680 : '100%'" :footer="false">
    <div v-if="dataDetail" class="post-detail">
      <!-- 基本信息卡片 -->
      <a-card title="基本信息" class="detail-card">
        <div class="post-header">
          <div class="post-info">
            <h2>{{ dataDetail.title || '无标题' }}</h2>
            <div class="post-meta">
              <a-tag :color="getContentTypeColor(dataDetail.contentType)">
                {{ getContentTypeText(dataDetail.contentType) }}
              </a-tag>
              <a-tag v-if="dataDetail.isTop" color="red">置顶</a-tag>
              <a-tag v-if="dataDetail.isEssence" color="gold">精华</a-tag>
              <a-tag v-if="dataDetail.isAnonymous" color="purple">匿名</a-tag>
            </div>
          </div>
        </div>
        <a-descriptions :column="2" class="basic-info">
          <a-descriptions-item label="星球ID">{{ dataDetail.planetId }}</a-descriptions-item>
          <a-descriptions-item label="发帖用户ID">{{ dataDetail.userId }}</a-descriptions-item>
          <a-descriptions-item label="帖子内容" :span="2">
            <div class="content-text">{{ dataDetail.content || '-' }}</div>
          </a-descriptions-item>
          <a-descriptions-item v-if="dataDetail.mediaUrls" label="媒体文件" :span="2">
            <div class="media-urls">{{ dataDetail.mediaUrls }}</div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 统计数据卡片 -->
      <a-card title="统计数据" class="detail-card">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-statistic title="浏览次数" :value="dataDetail.viewCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="点赞数" :value="dataDetail.likeCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="评论数" :value="dataDetail.commentCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="分享次数" :value="dataDetail.shareCount" />
          </a-col>
        </a-row>
        <a-row :gutter="16" style="margin-top: 16px">
          <a-col :span="6">
            <a-statistic title="收藏次数" :value="dataDetail.collectCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="热度分数" :value="dataDetail.hotScore" :precision="1" />
          </a-col>
          <a-col :span="12">
            <a-descriptions-item label="最后评论时间">
              {{ dataDetail.lastCommentTime || '-' }}
            </a-descriptions-item>
          </a-col>
        </a-row>
      </a-card>

      <!-- 审核信息卡片 -->
      <a-card title="审核信息" class="detail-card">
        <a-descriptions :column="2">
          <a-descriptions-item label="审核状态">
            <a-tag :color="getAuditStatusColor(dataDetail.auditStatus)">
              {{ getAuditStatusText(dataDetail.auditStatus) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="审核人">{{ dataDetail.auditBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="审核时间" :span="2">
            {{ dataDetail.auditTime || '-' }}
          </a-descriptions-item>
          <a-descriptions-item v-if="dataDetail.auditReason" label="审核原因" :span="2">
            <div class="audit-reason">{{ dataDetail.auditReason }}</div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" class="detail-card">
        <a-descriptions :column="2">
          <a-descriptions-item label="业务状态">
            <a-tag :color="getStatusColor(dataDetail.status)">
              {{ getStatusText(dataDetail.status) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="版本号">{{ dataDetail.version }}</a-descriptions-item>
          <a-descriptions-item label="创建者">{{ dataDetail.createdBy }}</a-descriptions-item>
          <a-descriptions-item label="更新者">{{ dataDetail.updatedBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ dataDetail.createdAt }}</a-descriptions-item>
          <a-descriptions-item label="更新时间">{{ dataDetail.updatedAt || '-' }}</a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetPostDetailResp, getPlanetPost } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetPostDetailResp>()

// 内容类型相关方法
const getContentTypeColor = (type: number) => {
  const colors = { 1: 'blue', 2: 'green', 3: 'purple', 4: 'orange', 5: 'cyan', 6: 'magenta' }
  return colors[type] || 'gray'
}

const getContentTypeText = (type: number) => {
  const texts = { 1: '文本', 2: '图片', 3: '视频', 4: '音频', 5: '文件', 6: '链接' }
  return texts[type] || '未知'
}

// 审核状态相关方法
const getAuditStatusColor = (status: number) => {
  const colors = { 1: 'orange', 2: 'green', 3: 'red' }
  return colors[status] || 'gray'
}

const getAuditStatusText = (status: number) => {
  const texts = { 1: '待审核', 2: '审核通过', 3: '审核拒绝' }
  return texts[status] || '未知'
}

// 业务状态相关方法
const getStatusColor = (status: number) => {
  const colors = { 1: 'green', 2: 'red', 3: 'orange' }
  return colors[status] || 'gray'
}

const getStatusText = (status: number) => {
  const texts = { 1: '正常', 2: '已删除', 3: '已隐藏' }
  return texts[status] || '未知'
}

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetPost(dataId.value)
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
.post-detail {
  .detail-card {
    margin-bottom: 16px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .post-header {
    margin-bottom: 24px;
    
    .post-info {
      h2 {
        margin: 0 0 8px 0;
        font-size: 20px;
        font-weight: 600;
      }
      
      .post-meta {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }
    }
  }
  
  .basic-info {
    margin-top: 16px;
  }
  
  .content-text {
    white-space: pre-wrap;
    line-height: 1.6;
    color: #333;
    max-height: 200px;
    overflow-y: auto;
  }
  
  .media-urls {
    font-family: monospace;
    background: #f4f7fd;
    padding: 8px;
    border-radius: 4px;
    font-size: 12px;
    word-break: break-all;
  }
  
  .audit-reason {
    color: #666;
    font-style: italic;
  }
  
  // 统计数据数字加粗样式
  :deep(.ant-statistic-content-value) {
    font-weight: bold !important;
  }
}
</style>
