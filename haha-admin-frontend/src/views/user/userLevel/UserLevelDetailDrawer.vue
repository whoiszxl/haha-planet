<template>
  <a-drawer 
    v-model:visible="visible" 
    title="用户等级详情" 
    :width="width >= 680 ? 680 : '100%'" 
    :footer="false"
  >
    <div v-if="dataDetail" class="detail-container">
      <!-- 等级概览卡片 -->
      <a-card class="level-overview" :bordered="false">
        <div class="level-header">
          <div class="level-icon">
            <img 
              v-if="dataDetail.levelIcon" 
              :src="dataDetail.levelIcon" 
              alt="等级图标"
              class="icon-img"
            />
            <div 
              v-else 
              class="icon-placeholder"
              :style="{ backgroundColor: dataDetail.levelColor || '#f0f0f0' }"
            >
              {{ dataDetail.level }}
            </div>
          </div>
          <div class="level-info">
            <h2 class="level-title">{{ dataDetail.levelName }}</h2>
            <div class="level-meta">
              <a-tag :color="dataDetail.levelColor" size="large">等级 {{ dataDetail.level }}</a-tag>
              <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'">
                {{ dataDetail.status === 1 ? '有效' : '无效' }}
              </a-tag>
            </div>
          </div>
        </div>
        
        <!-- 经验值进度 -->
        <div class="experience-section">
          <div class="experience-info">
            <span>经验值范围：{{ dataDetail.minExperience }} - {{ dataDetail.maxExperience }}</span>
            <span>经验值跨度：{{ dataDetail.maxExperience - dataDetail.minExperience }}</span>
          </div>
          <a-progress 
            :percent="getExperiencePercent(dataDetail)" 
            :color="dataDetail.levelColor"
            :track-color="dataDetail.levelColor + '20'"
            size="large"
          />
        </div>
      </a-card>
      
      <!-- 详细信息标签页 -->
      <a-tabs default-active-key="basic" class="detail-tabs">
        <a-tab-pane key="basic" title="基本信息">
          <a-descriptions :column="2" size="large" class="general-description">
            <a-descriptions-item label="主键ID">{{ dataDetail.id }}</a-descriptions-item>
            <a-descriptions-item label="等级">{{ dataDetail.level }}</a-descriptions-item>
            <a-descriptions-item label="等级名称">{{ dataDetail.levelName }}</a-descriptions-item>
            <a-descriptions-item label="最小经验值">{{ dataDetail.minExperience }}</a-descriptions-item>
            <a-descriptions-item label="最大经验值">{{ dataDetail.maxExperience }}</a-descriptions-item>
            <a-descriptions-item label="等级颜色">
              <div class="color-display">
                <div 
                  class="color-block" 
                  :style="{ backgroundColor: dataDetail.levelColor }"
                ></div>
                <span>{{ dataDetail.levelColor }}</span>
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="状态">
              <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'">
                {{ dataDetail.status === 1 ? '有效' : '无效' }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="乐观锁">{{ dataDetail.version }}</a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="privileges" title="等级权益">
          <div class="privileges-content">
            <a-empty v-if="!dataDetail.privileges" description="暂无等级权益" />
            <div v-else class="privileges-text">
              {{ dataDetail.privileges }}
            </div>
          </div>
        </a-tab-pane>
        
        <a-tab-pane key="description" title="等级描述">
          <div class="description-content">
            <a-empty v-if="!dataDetail.description" description="暂无等级描述" />
            <div v-else class="description-text">
              {{ dataDetail.description }}
            </div>
          </div>
        </a-tab-pane>
        
        <a-tab-pane key="system" title="系统信息">
          <a-descriptions :column="1" size="large">
            <a-descriptions-item label="创建者">{{ dataDetail.createdBy || '系统' }}</a-descriptions-item>
            <a-descriptions-item label="更新者">{{ dataDetail.updatedBy || '系统' }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ dataDetail.createdAt }}</a-descriptions-item>
            <a-descriptions-item label="更新时间">{{ dataDetail.updatedAt }}</a-descriptions-item>
            <a-descriptions-item label="逻辑删除">
              <a-tag :color="dataDetail.isDeleted === 1 ? 'red' : 'green'">
                {{ dataDetail.isDeleted === 1 ? '已删除' : '未删除' }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
      </a-tabs>
    </div>
    
    <!-- 操作按钮 -->
    <template #footer>
      <a-space>
        <a-button @click="visible = false">关闭</a-button>
        <a-button 
          v-permission="['user:userLevel:update']" 
          type="primary" 
          @click="onEdit"
        >
          编辑
        </a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type UserLevelDetailResp, getUserLevel } from '@/apis'

const emit = defineEmits<{
  (e: 'edit', id: string): void
}>()

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<UserLevelDetailResp>()

// 计算经验值百分比
const getExperiencePercent = (data: UserLevelDetailResp) => {
  const range = data.maxExperience - data.minExperience
  return range > 0 ? Math.min(100, (data.minExperience / data.maxExperience) * 100) : 0
}

// 查询详情
const getDataDetail = async () => {
  const res = await getUserLevel(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

// 编辑
const onEdit = () => {
  emit('edit', dataId.value)
  visible.value = false
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.detail-container {
  .level-overview {
    margin-bottom: 16px;
    
    .level-header {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 16px;
      
      .level-icon {
        .icon-img {
          width: 64px;
          height: 64px;
          border-radius: 50%;
          object-fit: cover;
        }
        
        .icon-placeholder {
          width: 64px;
          height: 64px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24px;
          font-weight: bold;
          color: white;
        }
      }
      
      .level-info {
        flex: 1;
        
        .level-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: bold;
        }
        
        .level-meta {
          display: flex;
          gap: 8px;
        }
      }
    }
    
    .experience-section {
      .experience-info {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
        font-size: 14px;
        color: #666;
      }
    }
  }
  
  .detail-tabs {
    .privileges-content,
    .description-content {
      min-height: 200px;
      
      .privileges-text,
      .description-text {
        padding: 16px;
        background: #f8f9fa;
        border-radius: 6px;
        line-height: 1.6;
        white-space: pre-wrap;
      }
    }
  }
}

.color-display {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .color-block {
    width: 20px;
    height: 20px;
    border-radius: 4px;
    border: 1px solid #d9d9d9;
  }
}
</style>
