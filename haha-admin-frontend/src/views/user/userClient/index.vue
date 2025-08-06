<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户客户端管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1200 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['clientKey']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.clientKey" placeholder="请输入客户端Key" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select 
          v-model="queryForm.authType" 
          placeholder="授权类型" 
          allow-clear 
          multiple
          @change="search"
        >
          <a-option value="ACCOUNT">账号授权</a-option>
          <a-option value="PHONE">手机号授权</a-option>
          <a-option value="EMAIL">邮箱授权</a-option>
          <a-option value="SOCIAL">社交授权</a-option>
          <a-option value="client_credentials">客户端授权</a-option>
        </a-select>
        <a-select v-model="queryForm.clientType" placeholder="客户端类型" allow-clear @change="search">
          <a-option value="web">Web应用</a-option>
          <a-option value="mobile">移动应用</a-option>
          <a-option value="desktop">桌面应用</a-option>
          <a-option value="server">服务端应用</a-option>
        </a-select>
        <a-select v-model="queryForm.status" placeholder="状态" allow-clear @change="search">
          <a-option :value="1">有效</a-option>
          <a-option :value="0">无效</a-option>
        </a-select>
        <a-range-picker v-model="queryForm.dateRange" @change="search" />
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['user:userClient:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增客户端</span>
        </a-button>
        <a-button v-permission="['user:userClient:batch']" @click="onBatchOperation">
          <template #icon><icon-settings /></template>
          <span>批量操作</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['user:userClient:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon><icon-download /></template>
          </a-button>
        </a-tooltip>
      </template>
      
      <!-- 优化后的列显示 -->
      <template #clientKey="{ record }">
        <a-link @click="onDetail(record)">{{ record.clientKey }}</a-link>
      </template>
      
      <template #clientSecret="{ record }">
        <span class="secret-text">{{ maskSecret(record.clientSecret) }}</span>
        <a-button size="mini" @click="copySecret(record.clientSecret)">复制</a-button>
      </template>
      
      <template #authType="{ record }">
        <div class="auth-types-container">
          <template v-if="Array.isArray(record.authType)">
            <a-tag 
              v-for="(type, index) in record.authType" 
              :key="index"
              :color="getAuthTypeColor(type)"
              class="auth-type-tag"
            >
              {{ getAuthTypeText(type) }}
            </a-tag>
          </template>
          <template v-else>
            <a-tag :color="getAuthTypeColor(record.authType)">
              {{ getAuthTypeText(record.authType) }}
            </a-tag>
          </template>
        </div>
      </template>
      
      <template #clientType="{ record }">
        <a-tag :color="getClientTypeColor(record.clientType)">{{ getClientTypeText(record.clientType) }}</a-tag>
      </template>
      
      <template #timeout="{ record }">
        <span v-if="record.timeout === -1" class="text-success">永不过期</span>
        <span v-else>{{ formatDuration(record.timeout) }}</span>
      </template>
      
      <template #activeTimeout="{ record }">
        <span v-if="record.activeTimeout === -1" class="text-success">不限制</span>
        <span v-else>{{ formatDuration(record.activeTimeout) }}</span>
      </template>
      
      <template #status="{ record }">
        <a-tag :color="record.status === 1 ? 'green' : 'red'">
          {{ record.status === 1 ? '有效' : '无效' }}
        </a-tag>
      </template>
      
      <template #createdAt="{ record }">
        {{ formatDateTime(record.createdAt) }}
      </template>
      
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['user:userClient:update']" @click="onUpdate(record)">编辑</a-link>
          <a-link v-permission="['user:userClient:view']" @click="onDetail(record)">详情</a-link>
          <a-dropdown>
            <a-link>更多</a-link>
            <template #content>
              <a-doption @click="onToggleStatus(record)">
                {{ record.status === 1 ? '禁用' : '启用' }}
              </a-doption>
              <a-doption @click="onResetSecret(record)">重置密钥</a-doption>
              <a-doption 
                v-permission="['user:userClient:delete']"
                class="text-danger"
                @click="onDelete(record)"
              >
                删除
              </a-doption>
            </template>
          </a-dropdown>
        </a-space>
      </template>
    </GiTable>

    <UserClientAddModal ref="UserClientAddModalRef" @save-success="search" />
    <UserClientDetailDrawer ref="UserClientDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import UserClientAddModal from './UserClientAddModal.vue'
import UserClientDetailDrawer from './UserClientDetailDrawer.vue'
import { type UserClientResp, type UserClientQuery, deleteUserClient, exportUserClient, listUserClient } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile, formatDateTime } from '@/utils'
import has from '@/utils/has'
import { Message } from '@arco-design/web-vue'

defineOptions({ name: 'UserClient' })

// 优化后的查询表单
const queryForm = reactive<UserClientQuery>({
  clientKey: undefined,
  authType: undefined,
  clientType: undefined,
  status: undefined,
  dateRange: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listUserClient({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置 - 只显示核心字段
const columns: TableInstanceColumns[] = [
  { title: 'ID', dataIndex: 'id', width: 80, fixed: 'left' },
  { title: '客户端Key', dataIndex: 'clientKey', slotName: 'clientKey', width: 120, fixed: 'left' },
  { title: '客户端密钥', dataIndex: 'clientSecret', slotName: 'clientSecret', width: 180 },
  { title: '授权类型', dataIndex: 'authType', slotName: 'authType', width: 160, ellipsis: false },
  { title: '客户端类型', dataIndex: 'clientType', slotName: 'clientType', width: 120 },
  { title: 'Token有效期', dataIndex: 'timeout', slotName: 'timeout', width: 100 },
  { title: '活跃超时', dataIndex: 'activeTimeout', slotName: 'activeTimeout', width: 100 },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createdAt', slotName: 'createdAt', width: 120 },
  {
    title: '操作',
    slotName: 'action',
    width: 150,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['user:userClient:update', 'user:userClient:delete'])
  }
]

// 工具函数更新
const getAuthTypeText = (type: string) => {
  const typeMap = {
    'ACCOUNT': '账号',
    'PHONE': '手机',
    'EMAIL': '邮箱',
    'SOCIAL': '社交',
    'client_credentials': '客户端'
  }
  return typeMap[type] || type
}

const getAuthTypeColor = (type: string) => {
  const colorMap = {
    'ACCOUNT': 'blue',
    'PHONE': 'green',
    'EMAIL': 'orange',
    'SOCIAL': 'purple',
    'client_credentials': 'cyan'
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

// 新增功能函数
const onBatchOperation = () => {
  // 批量操作逻辑
  Message.info('批量操作功能开发中')
}

const onToggleStatus = async (record: UserClientResp) => {
  // 切换状态逻辑
  Message.info('状态切换功能开发中')
}

const onResetSecret = async (record: UserClientResp) => {
  // 重置密钥逻辑
  Message.info('重置密钥功能开发中')
}

// 重置
const reset = () => {
  queryForm.clientKey = undefined
  queryForm.authType = undefined
  queryForm.clientType = undefined
  queryForm.status = undefined
  queryForm.dateRange = undefined
  search()
}

// 删除
const onDelete = (item: UserClientResp) => {
  return handleDelete(() => deleteUserClient(item.id), {
    content: `是否确定删除客户端 "${item.clientKey}"？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportUserClient(queryForm))
}

const UserClientAddModalRef = ref<InstanceType<typeof UserClientAddModal>>()
// 新增
const onAdd = () => {
  UserClientAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: UserClientResp) => {
  UserClientAddModalRef.value?.onUpdate(item.id)
}

const UserClientDetailDrawerRef = ref<InstanceType<typeof UserClientDetailDrawer>>()
// 详情
const onDetail = (item: UserClientResp) => {
  UserClientDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.secret-text {
  font-family: 'Courier New', monospace;
  margin-right: 8px;
}

.text-success {
  color: var(--color-success-6);
}

.text-danger {
  color: var(--color-danger-6);
}

.auth-types-container {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  max-width: 150px; // 增加宽度
  line-height: 1.4;
  
  .auth-type-tag {
    margin: 0;
    font-size: 11px; // 稍微减小字体
    padding: 1px 4px; // 减小内边距
    line-height: 1.3;
    border-radius: 3px;
    white-space: nowrap; // 防止标签内文字换行
  }
}
</style>
