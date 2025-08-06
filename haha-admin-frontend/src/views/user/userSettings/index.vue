<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户设置管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1200 }"
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
            v-model="queryForm.status" 
            placeholder="状态" 
            allow-clear 
            style="width: 120px"
            @change="search"
          >
            <a-option :value="1">有效</a-option>
            <a-option :value="0">无效</a-option>
          </a-select>
          <a-select 
            v-model="queryForm.privacyLevel" 
            placeholder="隐私级别" 
            allow-clear 
            style="width: 140px"
            @change="search"
          >
            <a-option :value="1">公开</a-option>
            <a-option :value="2">好友可见</a-option>
            <a-option :value="3">仅自己</a-option>
          </a-select>
          <a-button @click="reset">重置</a-button>
        </a-space>
      </template>
      <template #custom-right>
        <a-space>
          <a-button v-permission="['user:userSettings:add']" type="primary" @click="onAdd">
            <template #icon><icon-plus /></template>
            <span>新增</span>
          </a-button>
          <a-tooltip content="导出">
            <a-button v-permission="['user:userSettings:export']" class="gi_hover_btn-border" @click="onExport">
              <template #icon><icon-download /></template>
            </a-button>
          </a-tooltip>
        </a-space>
      </template>
      
      <!-- 用户ID模板 -->
      <template #userId="{ record }">
        <a-link @click="onDetail(record)">{{ record.userId }}</a-link>
      </template>
      
      <!-- 隐私级别模板 -->
      <template #privacyLevel="{ record }">
        <a-tag :color="getPrivacyLevelColor(record.privacyLevel)">
          {{ getPrivacyLevelText(record.privacyLevel) }}
        </a-tag>
      </template>
      
      <!-- 权限设置模板 -->
      <template #permissions="{ record }">
        <div class="permissions-container">
          <a-tooltip content="允许被搜索">
            <a-tag :color="record.allowSearch ? 'green' : 'red'" size="small">
              <icon-search />{{ record.allowSearch ? '可搜索' : '不可搜索' }}
            </a-tag>
          </a-tooltip>
          <a-tooltip content="允许好友申请">
            <a-tag :color="record.allowFriendRequest ? 'green' : 'red'" size="small">
              <icon-user-add />{{ record.allowFriendRequest ? '可申请' : '不可申请' }}
            </a-tag>
          </a-tooltip>
          <a-tooltip content="允许私信">
            <a-tag :color="record.allowMessage ? 'green' : 'red'" size="small">
              <icon-message />{{ record.allowMessage ? '可私信' : '不可私信' }}
            </a-tag>
          </a-tooltip>
        </div>
      </template>
      
      <!-- 通知设置模板 -->
      <template #notifications="{ record }">
        <div class="notifications-container">
          <a-tooltip content="邮件通知">
            <a-tag :color="record.emailNotification ? 'blue' : 'gray'" size="small">
              <icon-email />{{ record.emailNotification ? '开启' : '关闭' }}
            </a-tag>
          </a-tooltip>
          <a-tooltip content="短信通知">
            <a-tag :color="record.smsNotification ? 'blue' : 'gray'" size="small">
              <icon-phone />{{ record.smsNotification ? '开启' : '关闭' }}
            </a-tag>
          </a-tooltip>
          <a-tooltip content="推送通知">
            <a-tag :color="record.pushNotification ? 'blue' : 'gray'" size="small">
              <icon-notification />{{ record.pushNotification ? '开启' : '关闭' }}
            </a-tag>
          </a-tooltip>
        </div>
      </template>
      
      <!-- 个性化设置模板 -->
      <template #personalization="{ record }">
        <a-descriptions :column="1" size="mini" bordered>
          <a-descriptions-item label="主题">{{ record.theme || '默认' }}</a-descriptions-item>
          <a-descriptions-item label="语言">{{ record.language || '中文' }}</a-descriptions-item>
          <a-descriptions-item label="时区">{{ record.timezone || 'UTC+8' }}</a-descriptions-item>
        </a-descriptions>
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
          <a-link v-permission="['user:userSettings:update']" @click="onUpdate(record)">修改</a-link>
          <a-link @click="onDetail(record)">详情</a-link>
          <a-popconfirm
            content="确定要删除这条用户设置吗？"
            @ok="onDelete(record)"
          >
            <a-link
              v-permission="['user:userSettings:delete']"
              status="danger"
              :disabled="record.disabled"
            >
              删除
            </a-link>
          </a-popconfirm>
        </a-space>
      </template>
    </GiTable>

    <UserSettingsAddModal ref="UserSettingsAddModalRef" @save-success="search" />
    <UserSettingsDetailDrawer ref="UserSettingsDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import UserSettingsAddModal from './UserSettingsAddModal.vue'
import UserSettingsDetailDrawer from './UserSettingsDetailDrawer.vue'
import { type UserSettingsResp, type UserSettingsQuery, deleteUserSettings, exportUserSettings, listUserSettings } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'UserSettings' })

const queryForm = reactive<UserSettingsQuery>({
  userId: undefined,
  status: undefined,
  privacyLevel: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listUserSettings({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置
const columns: TableInstanceColumns[] = [
  { title: '用户ID', dataIndex: 'userId', slotName: 'userId', width: 120, fixed: 'left' },
  { title: '隐私级别', dataIndex: 'privacyLevel', slotName: 'privacyLevel', width: 100 },
  { title: '权限设置', slotName: 'permissions', width: 200 },
  { title: '通知设置', slotName: 'notifications', width: 180 },
  { title: '个性化', slotName: 'personalization', width: 150 },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80 },
  { title: '创建时间', dataIndex: 'createdAt', width: 160 },
  { title: '更新时间', dataIndex: 'updatedAt', width: 160 },
  {
    title: '操作',
    slotName: 'action',
    width: 150,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['user:userSettings:update', 'user:userSettings:delete'])
  }
]

// 工具函数
const getPrivacyLevelText = (level: number) => {
  const map = { 1: '公开', 2: '好友可见', 3: '仅自己' }
  return map[level] || '未知'
}

const getPrivacyLevelColor = (level: number) => {
  const map = { 1: 'green', 2: 'orange', 3: 'red' }
  return map[level] || 'gray'
}

// 重置
const reset = () => {
  queryForm.userId = undefined
  queryForm.status = undefined
  queryForm.privacyLevel = undefined
  search()
}

// 删除
const onDelete = (item: UserSettingsResp) => {
  return handleDelete(() => deleteUserSettings(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportUserSettings(queryForm))
}

const UserSettingsAddModalRef = ref<InstanceType<typeof UserSettingsAddModal>>()
// 新增
const onAdd = () => {
  UserSettingsAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: UserSettingsResp) => {
  UserSettingsAddModalRef.value?.onUpdate(item.id)
}

const UserSettingsDetailDrawerRef = ref<InstanceType<typeof UserSettingsDetailDrawer>>()
// 详情
const onDetail = (item: UserSettingsResp) => {
  UserSettingsDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.permissions-container,
.notifications-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
  
  .arco-tag {
    margin: 0;
    display: flex;
    align-items: center;
    gap: 4px;
  }
}
</style>
