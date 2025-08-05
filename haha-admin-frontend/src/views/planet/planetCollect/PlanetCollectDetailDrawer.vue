<template>
  <a-drawer 
    v-model:visible="visible" 
    title="收藏记录详情" 
    :width="width >= 600 ? 600 : '100%'" 
    :footer="false"
    class="collect-detail-drawer"
  >
    <div v-if="dataDetail" class="detail-content">
      <!-- 基本信息卡片 -->
      <a-card title="基本信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="medium">
          <a-descriptions-item label="收藏ID">
            <a-tag color="blue">{{ dataDetail.id }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="收藏状态">
            <a-tag :color="getStatusColor(dataDetail.status)">
              <component :is="getStatusIcon(dataDetail.status)" />
              {{ getStatusText(dataDetail.status) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="收藏夹名称">
            <div class="folder-name">
              <icon-folder style="color: #1890ff; margin-right: 4px;" />
              {{ dataDetail.folderName || '默认收藏夹' }}
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="收藏时间">
            {{ formatDateTime(dataDetail.createdAt) }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 关联信息卡片 -->
      <a-card title="关联信息" class="detail-card" :bordered="false">
        <a-descriptions :column="1" size="medium">
          <a-descriptions-item label="星球信息">
            <div class="relation-item">
              <a-tag color="purple">
                <icon-apps /> 星球ID: {{ dataDetail.planetId }}
              </a-tag>
              <a-button type="text" size="small" @click="onViewPlanet">
                <icon-eye /> 查看星球
              </a-button>
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="帖子信息">
            <div class="relation-item">
              <a-tag color="green">
                <icon-file-text /> 帖子ID: {{ dataDetail.postId }}
              </a-tag>
              <a-button type="text" size="small" @click="onViewPost">
                <icon-eye /> 查看帖子
              </a-button>
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="用户信息">
            <div class="relation-item">
              <a-tag color="orange">
                <icon-user /> 用户ID: {{ dataDetail.userId }}
              </a-tag>
              <a-button type="text" size="small" @click="onViewUser">
                <icon-eye /> 查看用户
              </a-button>
            </div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="创建者">{{ dataDetail.createdBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="更新者">{{ dataDetail.updatedBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ formatDateTime(dataDetail.createdAt) }}</a-descriptions-item>
          <a-descriptions-item label="更新时间">{{ formatDateTime(dataDetail.updatedAt) }}</a-descriptions-item>
          <a-descriptions-item label="版本号">{{ dataDetail.version }}</a-descriptions-item>
          <a-descriptions-item label="删除状态">
            <a-tag :color="dataDetail.isDeleted ? 'red' : 'green'">
              {{ dataDetail.isDeleted ? '已删除' : '正常' }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <a-space>
          <a-button type="primary" @click="onViewPost">
            <icon-file-text /> 查看帖子
          </a-button>
          <a-button @click="onViewPlanet">
            <icon-apps /> 查看星球
          </a-button>
          <a-button @click="onViewUser">
            <icon-user /> 查看用户
          </a-button>
        </a-space>
      </div>
    </div>

    <div v-else class="loading-container">
      <a-spin size="large" />
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetCollectDetailResp, getPlanetCollect } from '@/apis'
import { formatDateTime } from '@/utils/format'
import { useRouter } from 'vue-router'
import { Message } from '@arco-design/web-vue'

const { width } = useWindowSize()
const router = useRouter()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetCollectDetailResp>()

// 状态相关方法
const getStatusText = (status: number) => {
  const statusMap = { 1: '正常收藏', 0: '已取消' }
  return statusMap[status] || '未知'
}

const getStatusColor = (status: number) => {
  const colorMap = { 1: 'green', 0: 'gray' }
  return colorMap[status] || 'gray'
}

const getStatusIcon = (status: number) => {
  const iconMap = { 1: 'icon-heart-fill', 0: 'icon-heart' }
  return iconMap[status] || 'icon-question-circle'
}

// 查询详情
const getDataDetail = async () => {
  try {
    const res = await getPlanetCollect(dataId.value)
    dataDetail.value = res.data
  } catch (error) {
    Message.error('获取详情失败')
  }
}

// 查看帖子
const onViewPost = () => {
  if (dataDetail.value?.postId) {
    router.push({
      path: '/planet/planetPost',
      query: { postId: dataDetail.value.postId }
    })
  }
}

// 查看星球
const onViewPlanet = () => {
  if (dataDetail.value?.planetId) {
    router.push({
      path: '/planet/planet',
      query: { planetId: dataDetail.value.planetId }
    })
  }
}

// 查看用户
const onViewUser = () => {
  if (dataDetail.value?.userId) {
    router.push({
      path: '/user/user',
      query: { userId: dataDetail.value.userId }
    })
  }
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  visible.value = true
  await getDataDetail()
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.collect-detail-drawer {
  .detail-content {
    .detail-card {
      margin-bottom: 16px;
      
      :deep(.arco-card-header) {
        background: #fafafa;
        border-bottom: 1px solid #e8e8e8;
        
        .arco-card-header-title {
          font-weight: 600;
          color: #1890ff;
        }
      }
    }

    .folder-name {
      display: flex;
      align-items: center;
      font-weight: 500;
    }

    .relation-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 8px 0;
    }

    .action-buttons {
      margin-top: 24px;
      padding-top: 16px;
      border-top: 1px solid #e8e8e8;
      text-align: center;
    }
  }

  .loading-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 200px;
  }
}
</style>
