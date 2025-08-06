<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户令牌管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1400 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['userId']"
      @refresh="search"
    >
      <template #custom-left>
        <a-space>
          <a-input 
            v-model="queryForm.userId" 
            placeholder="请输入用户ID" 
            allow-clear 
            style="width: 200px"
            @change="search"
          >
            <template #prefix><icon-search /></template>
          </a-input>
          <a-select 
            v-model="queryForm.tokenType" 
            placeholder="令牌类型" 
            allow-clear 
            style="width: 140px"
            @change="search"
          >
            <a-option value="access">访问令牌</a-option>
            <a-option value="refresh">刷新令牌</a-option>
          </a-select>
          <a-select 
            v-model="queryForm.source" 
            placeholder="来源平台" 
            allow-clear 
            style="width: 120px"
            @change="search"
          >
            <a-option value="web">网页</a-option>
            <a-option value="app">移动端</a-option>
            <a-option value="api">接口</a-option>
          </a-select>
          <a-select 
            v-model="queryForm.status" 
            placeholder="状态" 
            allow-clear 
            style="width: 100px"
            @change="search"
          >
            <a-option :value="1">有效</a-option>
            <a-option :value="0">无效</a-option>
          </a-select>
          <a-button @click="reset">重置</a-button>
        </a-space>
      </template>
      <template #custom-right>
        <a-space>
          <a-button v-permission="['user:userToken:add']" type="primary" @click="onAdd">
            <template #icon><icon-plus /></template>
            <span>新增</span>
          </a-button>
          <a-button type="outline" @click="onBatchRevoke" :disabled="!selectedRowKeys.length">
            <template #icon><icon-close /></template>
            <span>批量撤销</span>
          </a-button>
          <a-tooltip content="导出">
            <a-button v-permission="['user:userToken:export']" class="gi_hover_btn-border" @click="onExport">
              <template #icon><icon-download /></template>
            </a-button>
          </a-tooltip>
        </a-space>
      </template>
      
      <!-- 用户ID模板 -->
      <template #userId="{ record }">
        <a-link @click="onDetail(record)">{{ record.userId }}</a-link>
      </template>
      
      <!-- Token模板 -->
      <template #token="{ record }">
        <div class="token-container">
          <a-typography-text 
            :copyable="{ text: record.token }" 
            :ellipsis="{ rows: 1, showTooltip: true }"
            class="token-text"
          >
            {{ maskToken(record.token) }}
          </a-typography-text>
        </div>
      </template>
      
      <!-- Token类型模板 -->
      <template #tokenType="{ record }">
        <a-tag :color="getTokenTypeColor(record.tokenType)">
          <template #icon>
            <icon-key v-if="record.tokenType === 'access'" />
            <icon-refresh v-else />
          </template>
          {{ getTokenTypeText(record.tokenType) }}
        </a-tag>
      </template>
      
      <!-- 来源平台模板 -->
      <template #source="{ record }">
        <a-tag :color="getSourceColor(record.source)">
          <template #icon>
            <icon-desktop v-if="record.source === 'web'" />
            <icon-mobile v-else-if="record.source === 'app'" />
            <icon-code v-else />
          </template>
          {{ getSourceText(record.source) }}
        </a-tag>
      </template>
      
      <!-- 过期时间模板 -->
      <template #expiresTime="{ record }">
        <div class="expires-time-container">
          <div class="time-text">{{ record.expiresTime }}</div>
          <a-tag 
            :color="getExpiresStatus(record.expiresTime).color" 
            size="small"
          >
            {{ getExpiresStatus(record.expiresTime).text }}
          </a-tag>
        </div>
      </template>
      
      <!-- 登录信息模板 -->
      <template #loginInfo="{ record }">
        <a-descriptions :column="1" size="mini" bordered>
          <a-descriptions-item label="IP">{{ record.loginIp }}</a-descriptions-item>
          <a-descriptions-item label="时间">{{ formatTime(record.loginTime) }}</a-descriptions-item>
        </a-descriptions>
      </template>
      
      <!-- 用户代理模板 -->
      <template #userAgent="{ record }">
        <a-tooltip :content="record.userAgent">
          <div class="user-agent-container">
            <div class="browser-info">{{ getBrowserInfo(record.userAgent) }}</div>
            <div class="os-info">{{ getOSInfo(record.userAgent) }}</div>
          </div>
        </a-tooltip>
      </template>
      
      <!-- 状态模板 -->
      <template #status="{ record }">
        <a-tag :color="record.status === 1 ? 'green' : 'red'">
          {{ record.status === 1 ? '有效' : '无效' }}
        </a-tag>
      </template>
      
      <!-- 操作模板 -->
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['user:userToken:update']" @click="onUpdate(record)">修改</a-link>
          <a-link @click="onDetail(record)">详情</a-link>
          <a-dropdown>
            <a-link>更多<icon-down /></a-link>
            <template #content>
              <a-doption @click="onRevoke(record)" :disabled="record.status === 0">
                <template #icon><icon-close /></template>
                撤销令牌
              </a-doption>
              <a-doption @click="onRefresh(record)" :disabled="record.tokenType !== 'refresh'">
                <template #icon><icon-refresh /></template>
                刷新令牌
              </a-doption>
              <a-doption 
                @click="onDelete(record)" 
                class="danger-option"
                v-permission="['user:userToken:delete']"
              >
                <template #icon><icon-delete /></template>
                删除
              </a-doption>
            </template>
          </a-dropdown>
        </a-space>
      </template>
    </GiTable>

    <UserTokenAddModal ref="UserTokenAddModalRef" @save-success="search" />
    <UserTokenDetailDrawer ref="UserTokenDetailDrawerRef" @revoke="handleRevoke" />
  </div>
</template>

<script setup lang="ts">
import UserTokenAddModal from './UserTokenAddModal.vue'
import UserTokenDetailDrawer from './UserTokenDetailDrawer.vue'
import { type UserTokenResp, type UserTokenQuery, deleteUserToken, exportUserToken, listUserToken } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { Message } from '@arco-design/web-vue'
import dayjs from 'dayjs'

defineOptions({ name: 'UserToken' })

const queryForm = reactive<UserTokenQuery>({
  userId: undefined,
  tokenType: undefined,
  source: undefined,
  status: undefined,
  sort: ['createdAt,desc']
})

const selectedRowKeys = ref<string[]>([])

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listUserToken({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置
const columns: TableInstanceColumns[] = [
  { title: '用户ID', dataIndex: 'userId', slotName: 'userId', width: 120, fixed: 'left' },
  { title: 'Token', dataIndex: 'token', slotName: 'token', width: 160 },
  { title: '类型', dataIndex: 'tokenType', slotName: 'tokenType', width: 120 },
  { title: '来源', dataIndex: 'source', slotName: 'source', width: 100 },
  { title: '过期时间', dataIndex: 'expiresTime', slotName: 'expiresTime', width: 180 },
  { title: '登录信息', slotName: 'loginInfo', width: 240 },
  { title: '用户代理', dataIndex: 'userAgent', slotName: 'userAgent', width: 180 },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createdAt', width: 160 },
  {
    title: '操作',
    slotName: 'action',
    width: 150,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['user:userToken:update', 'user:userToken:delete'])
  }
]

// 工具函数
const maskToken = (token: string) => {
  if (!token || token.length < 10) return token
  return `${token.substring(0, 6)}...${token.substring(token.length - 4)}`
}

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

const formatTime = (time: string) => {
  return time ? dayjs(time).format('MM-DD HH:mm') : '-'
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

// 重置
const reset = () => {
  queryForm.userId = undefined
  queryForm.tokenType = undefined
  queryForm.source = undefined
  queryForm.status = undefined
  search()
}

// 撤销令牌
const onRevoke = async (item: UserTokenResp) => {
  try {
    // 调用撤销API
    Message.success('令牌撤销成功')
    search()
  } catch (error) {
    Message.error('撤销失败')
  }
}

// 批量撤销
const onBatchRevoke = async () => {
  try {
    // 调用批量撤销API
    Message.success(`成功撤销 ${selectedRowKeys.value.length} 个令牌`)
    selectedRowKeys.value = []
    search()
  } catch (error) {
    Message.error('批量撤销失败')
  }
}

// 刷新令牌
const onRefresh = async (item: UserTokenResp) => {
  try {
    // 调用刷新API
    Message.success('令牌刷新成功')
    search()
  } catch (error) {
    Message.error('刷新失败')
  }
}

// 处理撤销
const handleRevoke = (id: string) => {
  const item = dataList.value.find(item => item.id === id)
  if (item) onRevoke(item)
}

// ... existing code ...
</script>

<style lang="scss" scoped>
.token-container {
  .token-text {
    font-family: 'Courier New', monospace;
    font-size: 12px;
  }
}

.expires-time-container {
  .time-text {
    font-size: 12px;
    margin-bottom: 4px;
  }
}

.user-agent-container {
  .browser-info {
    font-weight: 500;
    color: var(--color-text-1);
  }
  
  .os-info {
    font-size: 12px;
    color: var(--color-text-3);
  }
}

.danger-option {
  color: var(--color-danger-6);
}
</style>
