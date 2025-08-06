<template>
  <a-drawer 
    v-model:visible="visible" 
    title="用户客户端详情" 
    :width="width >= 680 ? 680 : '100%'" 
    :footer="false"
  >
    <div v-if="dataDetail" class="client-detail">
      <!-- 客户端头部信息 -->
      <div class="client-header">
        <div class="client-info">
          <h3>{{ dataDetail.clientKey }}</h3>
          <p class="client-id">ID: {{ dataDetail.id }}</p>
          <div class="client-tags">
            <a-tag :color="getAuthTypeColor(dataDetail.authType)">
              {{ getAuthTypeText(dataDetail.authType) }}
            </a-tag>
            <a-tag :color="getClientTypeColor(dataDetail.clientType)">
              {{ getClientTypeText(dataDetail.clientType) }}
            </a-tag>
            <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'">
              {{ dataDetail.status === 1 ? '有效' : '无效' }}
            </a-tag>
          </div>
        </div>
        <div class="client-actions">
          <a-button type="primary" size="small" @click="onEdit">
            <template #icon><icon-edit /></template>
            编辑
          </a-button>
        </div>
      </div>

      <!-- 详细信息 -->
      <a-tabs default-active-key="basic">
        <a-tab-pane key="basic" title="基本信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="客户端Key">{{ dataDetail.clientKey }}</a-descriptions-item>
            <a-descriptions-item label="客户端密钥">
              <span class="secret-text">{{ maskSecret(dataDetail.clientSecret) }}</span>
              <a-button size="mini" @click="copySecret(dataDetail.clientSecret)">复制</a-button>
            </a-descriptions-item>
            <a-descriptions-item label="授权类型">
              <a-tag :color="getAuthTypeColor(dataDetail.authType)">
                {{ getAuthTypeText(dataDetail.authType) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="客户端类型">
              <a-tag :color="getClientTypeColor(dataDetail.clientType)">
                {{ getClientTypeText(dataDetail.clientType) }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="状态">
              <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'">
                {{ dataDetail.status === 1 ? '有效' : '无效' }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="config" title="配置信息">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-statistic 
                title="Token有效期" 
                :value="dataDetail.timeout === -1 ? '永不过期' : formatDuration(dataDetail.timeout)"
                :value-style="{ color: dataDetail.timeout === -1 ? '#00b42a' : '#1d2129' }"
              />
            </a-col>
            <a-col :span="12">
              <a-statistic 
                title="活跃超时时间" 
                :value="dataDetail.activeTimeout === -1 ? '不限制' : formatDuration(dataDetail.activeTimeout)"
                :value-style="{ color: dataDetail.activeTimeout === -1 ? '#00b42a' : '#1d2129' }"
              />
            </a-col>
          </a-row>
          
          <a-descriptions :column="2" size="large" style="margin-top: 20px;">
            <a-descriptions-item label="Token有效期（秒）">
              {{ dataDetail.timeout === -1 ? '永不过期' : dataDetail.timeout }}
            </a-descriptions-item>
            <a-descriptions-item label="活跃超时（秒）">
              {{ dataDetail.activeTimeout === -1 ? '不限制' : dataDetail.activeTimeout }}
            </a-descriptions-item>
            <a-descriptions-item label="版本号">{{ dataDetail.version }}</a-descriptions-item>
            <a-descriptions-item label="逻辑删除">
              <a-tag :color="dataDetail.isDeleted === 0 ? 'green' : 'red'">
                {{ dataDetail.isDeleted === 0 ? '未删除' : '已删除' }}
              </a-tag>
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="system" title="系统信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="创建者">{{ dataDetail.createdBy || '-' }}</a-descriptions-item>
            <a-descriptions-item label="更新者">{{ dataDetail.updatedBy || '-' }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(dataDetail.createdAt) }}
            </a-descriptions-item>
            <a-descriptions-item label="更新时间">
              {{ formatDateTime(dataDetail.updatedAt) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type UserClientDetailResp, getUserClient } from '@/apis'
import { formatDateTime } from '@/utils'
import { Message } from '@arco-design/web-vue'

const emit = defineEmits<{
  (e: 'edit', id: string): void
}>()

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<UserClientDetailResp>()

// 查询详情
const getDataDetail = async () => {
  try {
    const res = await getUserClient(dataId.value)
    dataDetail.value = res.data
  } catch (error) {
    Message.error('获取客户端详情失败')
  }
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

// 工具函数
const getAuthTypeText = (type: string) => {
  const typeMap = {
    'authorization_code': '授权码模式',
    'client_credentials': '客户端模式',
    'password': '密码模式',
    'refresh_token': '刷新令牌'
  }
  return typeMap[type] || type
}

const getAuthTypeColor = (type: string) => {
  const colorMap = {
    'authorization_code': 'blue',
    'client_credentials': 'green',
    'password': 'orange',
    'refresh_token': 'purple'
  }
  return colorMap[type] || 'gray'
}

const getClientTypeText = (type: string) => {
  const typeMap = {
    'web': 'Web应用',
    'mobile': '移动应用',
    'desktop': '桌面应用',
    'server': '服务端应用'
  }
  return typeMap[type] || type
}

const getClientTypeColor = (type: string) => {
  const colorMap = {
    'web': 'blue',
    'mobile': 'green',
    'desktop': 'orange',
    'server': 'purple'
  }
  return colorMap[type] || 'gray'
}

const maskSecret = (secret: string) => {
  if (!secret) return '-'
  return secret.substring(0, 8) + '****' + secret.substring(secret.length - 4)
}

const formatDuration = (seconds: number) => {
  if (seconds < 60) return `${seconds}秒`
  if (seconds < 3600) return `${Math.floor(seconds / 60)}分钟`
  if (seconds < 86400) return `${Math.floor(seconds / 3600)}小时`
  return `${Math.floor(seconds / 86400)}天`
}

const copySecret = async (secret: string) => {
  try {
    await navigator.clipboard.writeText(secret)
    Message.success('密钥已复制到剪贴板')
  } catch {
    Message.error('复制失败')
  }
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.client-detail {
  .client-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px 0;
    border-bottom: 1px solid var(--color-border-2);
    margin-bottom: 20px;
    
    .client-info {
      flex: 1;
      
      h3 {
        margin: 0 0 8px 0;
        font-size: 20px;
        font-weight: 600;
        font-family: 'Courier New', monospace;
      }
      
      .client-id {
        margin: 0 0 12px 0;
        color: var(--color-text-3);
        font-size: 14px;
      }
      
      .client-tags {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }
    }
    
    .client-actions {
      margin-left: 16px;
    }
  }
}

.secret-text {
  font-family: 'Courier New', monospace;
  margin-right: 8px;
}
</style>
