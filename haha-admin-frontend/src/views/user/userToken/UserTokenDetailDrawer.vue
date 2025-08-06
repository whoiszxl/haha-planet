<template>
  <a-drawer 
    v-model:visible="visible" 
    title="用户令牌详情" 
    :width="width >= 720 ? 720 : '100%'" 
    :footer="false"
  >
    <div v-if="dataDetail" class="detail-container">
      <!-- 令牌概览卡片 -->
      <a-card class="overview-card" :bordered="false">
        <div class="token-overview">
          <div class="token-info">
            <h3>{{ getTokenTypeText(dataDetail.tokenType) }}</h3>
            <a-space>
              <a-tag :color="getTokenTypeColor(dataDetail.tokenType)" size="large">
                {{ getTokenTypeText(dataDetail.tokenType) }}
              </a-tag>
              <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'" size="large">
                {{ dataDetail.status === 1 ? '有效' : '无效' }}
              </a-tag>
              <a-tag 
                :color="getExpiresStatus(dataDetail.expiresTime).color" 
                size="large"
              >
                {{ getExpiresStatus(dataDetail.expiresTime).text }}
              </a-tag>
            </a-space>
          </div>
          <div class="token-actions">
            <a-space>
              <a-button 
                type="outline" 
                size="small" 
                @click="onCopyToken"
              >
                <template #icon><icon-copy /></template>
                复制Token
              </a-button>
              <a-button 
                type="outline" 
                status="warning" 
                size="small" 
                @click="onRevokeToken"
                :disabled="dataDetail.status === 0"
              >
                <template #icon><icon-close /></template>
                撤销
              </a-button>
            </a-space>
          </div>
        </div>
      </a-card>

      <!-- 标签页内容 -->
      <a-tabs default-active-key="basic" class="detail-tabs">
        <!-- 基本信息 -->
        <a-tab-pane key="basic" title="基本信息">
          <a-card title="令牌信息" :bordered="false">
            <a-descriptions :column="2" size="large">
              <a-descriptions-item label="用户ID">
                <a-tag color="blue">{{ dataDetail.userId }}</a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="Token">
                <div class="token-display">
                  <a-typography-text 
                    :copyable="{ text: dataDetail.token }" 
                    :ellipsis="{ rows: 2, showTooltip: true }"
                    class="token-text"
                  >
                    {{ dataDetail.token }}
                  </a-typography-text>
                </div>
              </a-descriptions-item>
              <a-descriptions-item label="来源平台">
                <a-tag :color="getSourceColor(dataDetail.source)">
                  <template #icon>
                    <icon-desktop v-if="dataDetail.source === 'web'" />
                    <icon-mobile v-else-if="dataDetail.source === 'app'" />
                    <icon-code v-else />
                  </template>
                  {{ getSourceText(dataDetail.source) }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="过期时间">
                <div class="expires-info">
                  <div>{{ dataDetail.expiresTime || '永不过期' }}</div>
                  <a-tag 
                    :color="getExpiresStatus(dataDetail.expiresTime).color" 
                    size="small"
                  >
                    {{ getExpiresStatus(dataDetail.expiresTime).text }}
                  </a-tag>
                </div>
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-tab-pane>

        <!-- 登录信息 -->
        <a-tab-pane key="login" title="登录信息">
          <a-card title="登录详情" :bordered="false">
            <a-descriptions :column="2" size="large">
              <a-descriptions-item label="登录IP">
                <a-tag color="orange">{{ dataDetail.loginIp }}</a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="登录时间">
                {{ dataDetail.loginTime }}
              </a-descriptions-item>
              <a-descriptions-item label="用户代理" :span="2">
                <div class="user-agent-detail">
                  <div class="agent-info">
                    <a-space>
                      <a-tag color="blue">{{ getBrowserInfo(dataDetail.userAgent) }}</a-tag>
                      <a-tag color="green">{{ getOSInfo(dataDetail.userAgent) }}</a-tag>
                    </a-space>
                  </div>
                  <a-typography-text 
                    :copyable="{ text: dataDetail.userAgent }" 
                    :ellipsis="{ rows: 3, showTooltip: true }"
                    class="agent-text"
                  >
                    {{ dataDetail.userAgent }}
                  </a-typography-text>
                </div>
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-tab-pane>

        <!-- 附加信息 -->
        <a-tab-pane key="meta" title="附加信息">
          <a-card title="元数据" :bordered="false">
            <div v-if="dataDetail.metaJson" class="meta-json-container">
              <a-typography-text 
                :copyable="{ text: dataDetail.metaJson }" 
                class="json-text"
              >
                <pre>{{ formatJson(dataDetail.metaJson) }}</pre>
              </a-typography-text>
            </div>
            <a-empty v-else description="暂无附加信息" />
          </a-card>
        </a-tab-pane>

        <!-- 系统信息 -->
        <a-tab-pane key="system" title="系统信息">
          <a-card title="系统记录" :bordered="false">
            <a-descriptions :column="2" size="large">
              <a-descriptions-item label="主键ID">{{ dataDetail.id }}</a-descriptions-item>
              <a-descriptions-item label="乐观锁">{{ dataDetail.version }}</a-descriptions-item>
              <a-descriptions-item label="逻辑删除">
                <a-tag :color="dataDetail.isDeleted ? 'red' : 'green'">
                  {{ dataDetail.isDeleted ? '已删除' : '未删除' }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="创建者">{{ dataDetail.createdBy }}</a-descriptions-item>
              <a-descriptions-item label="更新者">{{ dataDetail.updatedBy }}</a-descriptions-item>
              <a-descriptions-item label="创建时间">{{ dataDetail.createdAt }}</a-descriptions-item>
              <a-descriptions-item label="更新时间">{{ dataDetail.updatedAt }}</a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-tab-pane>
      </a-tabs>
    </div>

    <!-- 操作按钮 -->
    <template #footer>
      <a-space>
        <a-button @click="visible = false">关闭</a-button>
        <a-button type="primary" @click="onEdit">编辑令牌</a-button>
        <a-button 
          type="outline" 
          status="warning" 
          @click="onRevokeToken"
          :disabled="dataDetail?.status === 0"
        >
          撤销令牌
        </a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type UserTokenDetailResp, getUserToken } from '@/apis'
import { Message } from '@arco-design/web-vue'
import dayjs from 'dayjs'

const emit = defineEmits<{
  (e: 'edit', id: string): void
  (e: 'revoke', id: string): void
}>()

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<UserTokenDetailResp>()

// 工具函数
const getTokenTypeText = (type: string) => {
  const map = { access: '访问令牌', refresh: '刷新令牌' }
  return map[type] || type
}

const getTokenTypeColor = (type: string) => {
  const map = { access: 'blue', refresh: 'green' }
  return map[type] || 'gray'
}

const getSourceText = (source: string) => {
  const map = { web: '网页', app: '移动端', api: '接口' }
  return map[source] || source
}

const getSourceColor = (source: string) => {
  const map = { web: 'blue', app: 'green', api: 'orange' }
  return map[source] || 'gray'
}

const getExpiresStatus = (expiresTime: string) => {
  if (!expiresTime) return { text: '永久', color: 'blue' }
  
  const now = dayjs()
  const expires = dayjs(expiresTime)
  const diff = expires.diff(now, 'hour')
  
  if (diff < 0) return { text: '已过期', color: 'red' }
  if (diff < 24) return { text: '即将过期', color: 'orange' }
  return { text: '正常', color: 'green' }
}

const getBrowserInfo = (userAgent: string) => {
  if (!userAgent) return '未知浏览器'
  if (userAgent.includes('Chrome')) return 'Chrome'
  if (userAgent.includes('Firefox')) return 'Firefox'
  if (userAgent.includes('Safari')) return 'Safari'
  if (userAgent.includes('Edge')) return 'Edge'
  return '其他浏览器'
}

const getOSInfo = (userAgent: string) => {
  if (!userAgent) return '未知系统'
  if (userAgent.includes('Windows')) return 'Windows'
  if (userAgent.includes('Mac')) return 'macOS'
  if (userAgent.includes('Linux')) return 'Linux'
  if (userAgent.includes('Android')) return 'Android'
  if (userAgent.includes('iOS')) return 'iOS'
  return '其他系统'
}

const formatJson = (jsonStr: string) => {
  try {
    return JSON.stringify(JSON.parse(jsonStr), null, 2)
  } catch (error) {
    return jsonStr
  }
}

// 复制Token
const onCopyToken = async () => {
  try {
    await navigator.clipboard.writeText(dataDetail.value?.token || '')
    Message.success('Token已复制到剪贴板')
  } catch (error) {
    Message.error('复制失败')
  }
}

// 撤销令牌
const onRevokeToken = () => {
  emit('revoke', dataId.value)
}

// 编辑令牌
const onEdit = () => {
  emit('edit', dataId.value)
  visible.value = false
}

// 查询详情
const getDataDetail = async () => {
  const res = await getUserToken(dataId.value)
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
.detail-container {
  .overview-card {
    margin-bottom: 16px;
    
    .token-overview {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .token-info {
        h3 {
          margin: 0 0 8px 0;
          color: var(--color-text-1);
        }
      }
    }
  }
  
  .detail-tabs {
    .token-display {
      .token-text {
        font-family: 'Courier New', monospace;
        font-size: 12px;
        word-break: break-all;
      }
    }
    
    .expires-info {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }
    
    .user-agent-detail {
      .agent-info {
        margin-bottom: 8px;
      }
      
      .agent-text {
        font-family: 'Courier New', monospace;
        font-size: 12px;
        color: var(--color-text-3);
      }
    }
    
    .meta-json-container {
      .json-text {
        pre {
          background: var(--color-fill-2);
          padding: 12px;
          border-radius: 4px;
          font-family: 'Courier New', monospace;
          font-size: 12px;
          overflow-x: auto;
        }
      }
    }
  }
}
</style>
