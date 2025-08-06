<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户基础信息管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1200 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['username']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.username" placeholder="请输入用户名/昵称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.phone" placeholder="请输入手机号" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.userType" placeholder="用户类型" allow-clear @change="search">
          <a-option :value="1">普通用户</a-option>
          <a-option :value="2">认证用户</a-option>
          <a-option :value="3">VIP用户</a-option>
          <a-option :value="4">企业用户</a-option>
        </a-select>
        <a-select v-model="queryForm.status" placeholder="状态" allow-clear @change="search">
          <a-option :value="1">有效</a-option>
          <a-option :value="0">无效</a-option>
        </a-select>
        <a-range-picker v-model="queryForm.dateRange" @change="search" />
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['user:userInfo:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增用户</span>
        </a-button>
        <a-button v-permission="['user:userInfo:batch']" @click="onBatchOperation">
          <template #icon><icon-settings /></template>
          <span>批量操作</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['user:userInfo:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon><icon-download /></template>
          </a-button>
        </a-tooltip>
      </template>
      
      <!-- 优化后的列显示 -->
      <template #username="{ record }">
        <a-link @click="onDetail(record)">{{ record.username }}</a-link>
      </template>
      
      <template #avatar="{ record }">
        <div class="avatar-wrapper">
          <img 
            v-if="record.avatar" 
            :src="getImageUrl(record.avatar)" 
            class="avatar-img"
            @error="handleImageError"
          />
          <div v-else class="avatar-placeholder">
            {{ record.nickname?.charAt(0) || record.username?.charAt(0) }}
          </div>
        </div>
      </template>
      
      <template #userType="{ record }">
        <a-tag :color="getUserTypeColor(record.userType)">{{ getUserTypeText(record.userType) }}</a-tag>
      </template>
      
      <template #status="{ record }">
        <a-tag :color="record.status === 1 ? 'green' : 'red'">
          {{ record.status === 1 ? '有效' : '无效' }}
        </a-tag>
      </template>
      
      <template #isVerified="{ record }">
        <a-tag :color="record.isVerified === 1 ? 'blue' : 'gray'">
          {{ record.isVerified === 1 ? '已认证' : '未认证' }}
        </a-tag>
      </template>
      
      <template #balance="{ record }">
        <span class="text-primary">¥{{ formatAmount(record.balance) }}</span>
      </template>
      
      <template #lastLoginTime="{ record }">
        {{ formatDateTime(record.lastLoginTime) }}
      </template>
      
      <template #createdAt="{ record }">
        {{ formatDateTime(record.createdAt) }}
      </template>
      
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['user:userInfo:update']" @click="onUpdate(record)">编辑</a-link>
          <a-link v-permission="['user:userInfo:view']" @click="onDetail(record)">详情</a-link>
          <a-dropdown>
            <a-link>更多</a-link>
            <template #content>
              <a-doption @click="onResetPassword(record)">重置密码</a-doption>
              <a-doption @click="onToggleStatus(record)">
                {{ record.status === 1 ? '禁用' : '启用' }}
              </a-doption>
              <a-doption 
                v-permission="['user:userInfo:delete']"
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

    <UserInfoAddModal ref="UserInfoAddModalRef" @save-success="search" />
    <UserInfoDetailDrawer ref="UserInfoDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import UserInfoAddModal from './UserInfoAddModal.vue'
import UserInfoDetailDrawer from './UserInfoDetailDrawer.vue'
import { type UserInfoResp, type UserInfoQuery, deleteUserInfo, exportUserInfo, listUserInfo } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile, formatDateTime, formatAmount } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'UserInfo' })

// 获取环境变量
const imageBaseUrl = import.meta.env.VITE_IMAGE_BASE_URL

// 图片URL处理函数
const getImageUrl = (imagePath: string) => {
  if (!imagePath) return ''
  // 如果已经是完整URL（包含http或https），直接返回
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath
  }
  // 否则拼接基础URL
  return `${imageBaseUrl}${imagePath.startsWith('/') ? imagePath.slice(1) : imagePath}`
}

// 优化后的查询表单
const queryForm = reactive<UserInfoQuery>({
  username: undefined,
  phone: undefined,
  userType: undefined,
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
} = useTable((page) => listUserInfo({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置 - 只显示核心字段
const columns: TableInstanceColumns[] = [
  { title: 'ID', dataIndex: 'id', width: 50, fixed: 'left' },
  { title: '头像', dataIndex: 'avatar', slotName: 'avatar', width: 70 },
  { title: '用户名', dataIndex: 'username', slotName: 'username', width: 120, fixed: 'left' },
  { title: '昵称', dataIndex: 'nickname', width: 90 },
  { title: '手机号', dataIndex: 'phone', width: 120 },
  { title: '邮箱', dataIndex: 'email', width: 180 },
  { title: '用户类型', dataIndex: 'userType', slotName: 'userType', width: 100 },
  { title: '等级', dataIndex: 'level', width: 70 },
  { title: '积分', dataIndex: 'points', width: 100 },
  { title: '余额', dataIndex: 'balance', slotName: 'balance', width: 120 },
  { title: '粉丝数', dataIndex: 'followerCount', width: 90 },
  { title: '实名认证', dataIndex: 'isVerified', slotName: 'isVerified', width: 100 },
  { title: '最后登录', dataIndex: 'lastLoginTime', slotName: 'lastLoginTime', width: 160 },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80 },
  {
    title: '操作',
    slotName: 'action',
    width: 180,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['user:userInfo:update', 'user:userInfo:delete'])
  }
]

// 工具函数
const getUserTypeText = (type: number) => {
  const typeMap = {
    1: '普通用户',
    2: '认证用户', 
    3: 'VIP用户',
    4: '企业用户'
  }
  return typeMap[type] || '未知'
}

const getUserTypeColor = (type: number) => {
  const colorMap = {
    1: 'gray',
    2: 'blue',
    3: 'gold', 
    4: 'purple'
  }
  return colorMap[type] || 'gray'
}

// 新增功能函数
const onBatchOperation = () => {
  // 批量操作逻辑
}

const onResetPassword = (record: UserInfoResp) => {
  // 重置密码逻辑
}

const onToggleStatus = (record: UserInfoResp) => {
  // 切换状态逻辑
}

// 重置
const reset = () => {
  queryForm.username = undefined
  queryForm.phone = undefined
  queryForm.userType = undefined
  queryForm.status = undefined
  queryForm.dateRange = undefined
  search()
}

// 删除
const onDelete = (item: UserInfoResp) => {
  return handleDelete(() => deleteUserInfo(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportUserInfo(queryForm))
}

const UserInfoAddModalRef = ref<InstanceType<typeof UserInfoAddModal>>()
// 新增
const onAdd = () => {
  UserInfoAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: UserInfoResp) => {
  UserInfoAddModalRef.value?.onUpdate(item.id)
}

const UserInfoDetailDrawerRef = ref<InstanceType<typeof UserInfoDetailDrawer>>()
// 详情
const onDetail = (item: UserInfoResp) => {
  UserInfoDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.avatar-wrapper {
  .avatar-img {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    object-fit: cover;
  }
  
  .avatar-placeholder {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background-color: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }
}
</style>
