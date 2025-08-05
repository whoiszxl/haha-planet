<template>
  <a-drawer v-model:visible="visible" title="星球标签详情" :width="width >= 680 ? 680 : '100%'" :footer="false">
    <div class="tag-detail">
      <!-- 标签预览卡片 -->
      <a-card title="标签预览" class="detail-card preview-card" :bordered="false">
        <div class="tag-preview">
          <a-tag 
            :color="dataDetail?.color" 
            size="large" 
            class="preview-tag"
          >
            {{ dataDetail?.name }}
          </a-tag>
          <div class="preview-info">
            <div class="color-info">
              <span class="label">颜色值：</span>
              <code>{{ dataDetail?.color }}</code>
              <div 
                class="color-sample" 
                :style="{ backgroundColor: dataDetail?.color }"
              ></div>
            </div>
          </div>
        </div>
      </a-card>

      <!-- 基础信息卡片 -->
      <a-card title="基础信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="medium">
          <a-descriptions-item label="标签名称">
            <span class="tag-name">{{ dataDetail?.name }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="所属分类">
            <a-tag :color="getCategoryColor(dataDetail?.categoryId)">
              {{ getCategoryName(dataDetail?.categoryId) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="使用次数">
            <a-statistic 
              :value="dataDetail?.useCount || 0" 
              :value-style="{ 
                fontSize: '16px', 
                fontWeight: 'bold', 
                color: getUsageColor(dataDetail?.useCount) 
              }"
            >
              <template #suffix>
                <span style="font-size: 12px; color: #8c8c8c;">次</span>
              </template>
            </a-statistic>
          </a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag :color="dataDetail?.status === 1 ? 'green' : 'red'" size="large">
              <icon-check v-if="dataDetail?.status === 1" />
              <icon-close v-else />
              {{ dataDetail?.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 描述信息卡片 -->
      <a-card title="描述信息" class="detail-card" :bordered="false" v-if="dataDetail?.description">
        <div class="description-content">
          {{ dataDetail?.description }}
        </div>
      </a-card>

      <!-- 使用统计卡片 -->
      <a-card title="使用统计" class="detail-card" :bordered="false">
        <div class="usage-stats">
          <div class="stat-item">
            <div class="stat-label">使用频率</div>
            <div class="stat-value">
              <a-progress 
                :percent="getUsagePercent(dataDetail?.useCount)" 
                :color="getUsageColor(dataDetail?.useCount)"
                :show-text="false"
                size="small"
              />
              <span class="percent-text">{{ getUsageLevel(dataDetail?.useCount) }}</span>
            </div>
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
        </a-descriptions>
      </a-card>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetTagDetailResp, getPlanetTag } from '@/apis'
import { formatDateTime } from '@/utils/format'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetTagDetailResp>()

// 分类相关方法
const getCategoryName = (categoryId: number) => {
  const categoryMap = { 1: '技术类', 2: '行业类', 3: '兴趣类', 4: '其他' }
  return categoryMap[categoryId] || '未分类'
}

const getCategoryColor = (categoryId: number) => {
  const colorMap = { 1: 'blue', 2: 'green', 3: 'orange', 4: 'gray' }
  return colorMap[categoryId] || 'gray'
}

// 使用次数相关方法
const getUsageColor = (count: number) => {
  if (count > 100) return '#52c41a'
  if (count > 50) return '#1890ff'
  if (count > 10) return '#faad14'
  return '#8c8c8c'
}

const getUsagePercent = (count: number) => {
  if (count > 100) return 100
  if (count > 50) return 80
  if (count > 10) return 60
  if (count > 0) return 30
  return 0
}

const getUsageLevel = (count: number) => {
  if (count > 100) return '热门'
  if (count > 50) return '常用'
  if (count > 10) return '一般'
  if (count > 0) return '较少'
  return '未使用'
}

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetTag(dataId.value)
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
.tag-detail {
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
  
  .preview-card {
    background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    
    .tag-preview {
      text-align: center;
      padding: 20px;
      
      .preview-tag {
        font-size: 16px;
        padding: 8px 16px;
        border-radius: 20px;
        margin-bottom: 16px;
      }
      
      .preview-info {
        .color-info {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 8px;
          
          .label {
            color: var(--color-text-2);
            font-size: 12px;
          }
          
          code {
            background: var(--color-fill-2);
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Courier New', monospace;
            font-size: 12px;
          }
          
          .color-sample {
            width: 20px;
            height: 20px;
            border-radius: 4px;
            border: 1px solid var(--color-border-2);
          }
        }
      }
    }
  }
  
  .tag-name {
    font-size: 16px;
    font-weight: 600;
    color: var(--color-text-1);
  }
  
  .description-content {
    color: var(--color-text-2);
    line-height: 1.6;
    padding: 12px;
    background: var(--color-fill-1);
    border-radius: 6px;
    border-left: 4px solid var(--color-primary);
  }
  
  .usage-stats {
    .stat-item {
      display: flex;
      align-items: center;
      gap: 16px;
      
      .stat-label {
        min-width: 80px;
        color: var(--color-text-2);
        font-size: 14px;
      }
      
      .stat-value {
        flex: 1;
        display: flex;
        align-items: center;
        gap: 12px;
        
        .percent-text {
          font-size: 12px;
          color: var(--color-text-3);
          min-width: 40px;
        }
      }
    }
  }
}

:deep(.ant-statistic-content-value) {
  font-weight: bold !important;
}
</style>
