<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球邀请管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['inviteCode']"
      @refresh="search"
    >
      <template #custom-left>
        <a-space wrap>
          <a-input v-model="queryForm.planetId" placeholder="星球ID" allow-clear style="width: 120px" @change="search">
            <template #prefix><icon-search /></template>
          </a-input>
          <a-input v-model="queryForm.inviteCode" placeholder="邀请码" allow-clear style="width: 150px" @change="search">
            <template #prefix><icon-search /></template>
          </a-input>
          <a-select v-model="queryForm.inviteType" placeholder="邀请类型" allow-clear style="width: 120px" @change="search">
            <a-option :value="1">邀请码</a-option>
            <a-option :value="2">链接邀请</a-option>
          </a-select>
          <a-select v-model="queryForm.inviteStatus" placeholder="邀请状态" allow-clear style="width: 120px" @change="search">
            <a-option :value="1">有效</a-option>
            <a-option :value="2">已使用</a-option>
            <a-option :value="3">已过期</a-option>
            <a-option :value="4">已取消</a-option>
          </a-select>
          <a-range-picker v-model="queryForm.expireTimeRange" placeholder="过期时间范围" @change="search" />
          <a-button @click="reset">重置</a-button>
        </a-space>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetInvite:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增邀请</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetInvite:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #inviteCode="{ record }">
        <a-link @click="onDetail(record)">{{ record.inviteCode }}</a-link>
      </template>
      <template #inviteType="{ record }">
        <a-tag :color="getInviteTypeColor(record.inviteType)">
          {{ getInviteTypeText(record.inviteType) }}
        </a-tag>
      </template>
      <template #inviteStatus="{ record }">
        <a-tag :color="getInviteStatusColor(record.inviteStatus)">
          {{ getInviteStatusText(record.inviteStatus) }}
        </a-tag>
      </template>
      <template #expireTime="{ record }">
        <span :class="{ 'text-red-500': isExpired(record.expireTime) }">
          {{ formatDateTime(record.expireTime) }}
        </span>
      </template>
      <template #usageProgress="{ record }">
        <div class="flex items-center space-x-2">
          <a-progress 
            :percent="getUsagePercent(record.usedCount, record.maxUseCount)" 
            :width="60" 
            :stroke-width="6"
            :color="getUsageColor(record.usedCount, record.maxUseCount)"
          />
          <span class="text-sm text-gray-600">
            {{ record.usedCount }}/{{ record.maxUseCount }}
          </span>
        </div>
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['whoiszxl:planetInvite:update']" @click="onUpdate(record)">编辑</a-link>
          <a-link 
            v-if="record.inviteStatus === 1" 
            v-permission="['whoiszxl:planetInvite:update']" 
            @click="onCancel(record)"
          >
            取消
          </a-link>
          <a-link
            v-permission="['whoiszxl:planetInvite:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetInviteAddModal ref="PlanetInviteAddModalRef" @save-success="search" />
    <PlanetInviteDetailDrawer ref="PlanetInviteDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import PlanetInviteAddModal from './PlanetInviteAddModal.vue'
import PlanetInviteDetailDrawer from './PlanetInviteDetailDrawer.vue'
import { type PlanetInviteResp, type PlanetInviteQuery, deletePlanetInvite, exportPlanetInvite, listPlanetInvite, updatePlanetInvite } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile, formatDateTime } from '@/utils'
import has from '@/utils/has'
import dayjs from 'dayjs'

defineOptions({ name: 'PlanetInvite' })

const queryForm = reactive<PlanetInviteQuery & { expireTimeRange?: [string, string] }>({
  planetId: undefined,
  inviteCode: undefined,
  inviteType: undefined,
  inviteStatus: undefined,
  expireTimeRange: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => {
  const params = { ...queryForm, ...page }
  // 处理时间范围
  if (queryForm.expireTimeRange) {
    params.expireTimeStart = queryForm.expireTimeRange[0]
    params.expireTimeEnd = queryForm.expireTimeRange[1]
  }
  delete params.expireTimeRange
  return listPlanetInvite(params)
}, { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '邀请码', dataIndex: 'inviteCode', slotName: 'inviteCode', width: 120 },
  { title: '星球ID', dataIndex: 'planetId', width: 80 },
  { title: '邀请人ID', dataIndex: 'inviterId', width: 100 },
  { title: '邀请类型', dataIndex: 'inviteType', slotName: 'inviteType', width: 100 },
  { title: '邀请消息', dataIndex: 'inviteMessage', width: 150, ellipsis: true, tooltip: true },
  { title: '过期时间', dataIndex: 'expireTime', slotName: 'expireTime', width: 150 },
  { title: '使用进度', slotName: 'usageProgress', width: 120 },
  { title: '邀请状态', dataIndex: 'inviteStatus', slotName: 'inviteStatus', width: 100 },
  { title: '创建时间', dataIndex: 'createdAt', width: 150, render: ({ record }) => formatDateTime(record.createdAt) },
  {
    title: '操作',
    slotName: 'action',
    width: 150,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetInvite:update', 'whoiszxl:planetInvite:delete'])
  }
]

// 邀请类型相关
const getInviteTypeText = (type: number) => {
  const typeMap = { 1: '邀请码', 2: '链接邀请' }
  return typeMap[type] || '未知'
}

const getInviteTypeColor = (type: number) => {
  const colorMap = { 1: 'blue', 2: 'green' }
  return colorMap[type] || 'gray'
}

// 邀请状态相关
const getInviteStatusText = (status: number) => {
  const statusMap = { 1: '有效', 2: '已使用', 3: '已过期', 4: '已取消' }
  return statusMap[status] || '未知'
}

const getInviteStatusColor = (status: number) => {
  const colorMap = { 1: 'green', 2: 'blue', 3: 'red', 4: 'gray' }
  return colorMap[status] || 'gray'
}

// 使用进度相关
const getUsagePercent = (used: number, max: number) => {
  return max > 0 ? Math.round((used / max)) : 0
}

const getUsageColor = (used: number, max: number) => {
  const percent = getUsagePercent(used, max)
  if (percent >= 90) return '#f53f3f'
  if (percent >= 70) return '#ff7d00'
  return '#00b42a'
}

// 是否过期
const isExpired = (expireTime: string) => {
  return dayjs(expireTime).isBefore(dayjs())
}

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.inviteCode = undefined
  queryForm.inviteType = undefined
  queryForm.inviteStatus = undefined
  queryForm.expireTimeRange = undefined
  search()
}

// 取消邀请
const onCancel = async (item: PlanetInviteResp) => {
  try {
    await updatePlanetInvite({ inviteStatus: 4 }, item.id)
    Message.success('邀请已取消')
    search()
  } catch (error) {
    Message.error('取消失败')
  }
}

// 删除
const onDelete = (item: PlanetInviteResp) => {
  return handleDelete(() => deletePlanetInvite(item.id), {
    content: `是否确定删除邀请码 "${item.inviteCode}"？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetInvite(queryForm))
}

const PlanetInviteAddModalRef = ref<InstanceType<typeof PlanetInviteAddModal>>()
// 新增
const onAdd = () => {
  PlanetInviteAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetInviteResp) => {
  PlanetInviteAddModalRef.value?.onUpdate(item.id)
}

const PlanetInviteDetailDrawerRef = ref<InstanceType<typeof PlanetInviteDetailDrawer>>()
// 详情
const onDetail = (item: PlanetInviteResp) => {
  PlanetInviteDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.text-red-500 {
  color: #f53f3f;
}
</style>
