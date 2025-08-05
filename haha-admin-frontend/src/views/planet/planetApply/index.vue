<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球申请管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['name']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.planetId" placeholder="请输入星球ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.userId" placeholder="请输入申请用户ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.applyStatus" placeholder="申请状态" allow-clear @change="search">
          <a-option value="1">待审核</a-option>
          <a-option value="2">已通过</a-option>
          <a-option value="3">已拒绝</a-option>
        </a-select>
        <a-range-picker v-model="queryForm.auditTimeRange" @change="search" />
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['planet:planetApply:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-button v-permission="['planet:planetApply:audit']" type="outline" @click="onBatchAudit" :disabled="!selectedRowKeys.length">
          <template #icon><icon-check /></template>
          <span>批量审核</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['planet:planetApply:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon><icon-download /></template>
          </a-button>
        </a-tooltip>
      </template>
      
      <!-- 申请状态标签 -->
      <template #applyStatus="{ record }">
        <a-tag :color="getStatusColor(record.applyStatus)">
          {{ getStatusText(record.applyStatus) }}
        </a-tag>
      </template>
      
      <!-- 申请理由显示 -->
      <template #applyReason="{ record }">
        <a-tooltip :content="record.applyReason" position="top">
          <span class="text-ellipsis">{{ record.applyReason?.slice(0, 20) }}{{ record.applyReason?.length > 20 ? '...' : '' }}</span>
        </a-tooltip>
      </template>
      
      <!-- 审核时间格式化 -->
      <template #auditTime="{ record }">
        {{ record.auditTime ? formatDateTime(record.auditTime) : '-' }}
      </template>
      
      <!-- 创建时间格式化 -->
      <template #createdAt="{ record }">
        {{ formatDateTime(record.createdAt) }}
      </template>
      
      <template #action="{ record }">
        <a-space>
          <a-link @click="onDetail(record)">详情</a-link>
          <a-link v-if="record.applyStatus === '1'" v-permission="['planet:planetApply:audit']" @click="onAudit(record)" status="success">
            审核
          </a-link>
          <a-link v-permission="['planet:planetApply:update']" @click="onUpdate(record)">修改</a-link>
          <a-link v-permission="['planet:planetApply:delete']" status="danger" @click="onDelete(record)">
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetApplyAddModal ref="PlanetApplyAddModalRef" @save-success="search" />
    <PlanetApplyDetailDrawer ref="PlanetApplyDetailDrawerRef" />
    <PlanetApplyAuditModal ref="PlanetApplyAuditModalRef" @audit-success="search" />
  </div>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import PlanetApplyAddModal from './PlanetApplyAddModal.vue'
import PlanetApplyDetailDrawer from './PlanetApplyDetailDrawer.vue'
import PlanetApplyAuditModal from './PlanetApplyAuditModal.vue'
import { type PlanetApplyResp, type PlanetApplyQuery, deletePlanetApply, exportPlanetApply, listPlanetApply } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile, formatDateTime } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'PlanetApply' })

const queryForm = reactive<PlanetApplyQuery & { applyStatus?: string; auditTimeRange?: string[] }>({
  planetId: undefined,
  userId: undefined,
  applyStatus: undefined,
  auditTimeRange: undefined,
  sort: ['createdAt,desc']
})

const selectedRowKeys = ref<string[]>([])

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => {
  const params = { ...queryForm, ...page }
  // 处理时间范围
  if (queryForm.auditTimeRange?.length === 2) {
    params.auditTimeStart = queryForm.auditTimeRange[0]
    params.auditTimeEnd = queryForm.auditTimeRange[1]
  }
  delete params.auditTimeRange
  return listPlanetApply(params)
}, { immediate: true })

// 精简后的列配置
const columns: TableInstanceColumns[] = [
  { 
    title: '星球ID', 
    dataIndex: 'planetId', 
    width: 100,
    fixed: 'left'
  },
  { 
    title: '申请用户', 
    dataIndex: 'userId', 
    width: 120
  },
  { 
    title: '申请理由', 
    dataIndex: 'applyReason', 
    slotName: 'applyReason',
    width: 200
  },
  { 
    title: '申请状态', 
    dataIndex: 'applyStatus', 
    slotName: 'applyStatus',
    width: 100,
    align: 'center'
  },
  { 
    title: '审核时间', 
    dataIndex: 'auditTime', 
    slotName: 'auditTime',
    width: 150
  },
  { 
    title: '审核人', 
    dataIndex: 'auditBy', 
    width: 100
  },
  { 
    title: '创建时间', 
    dataIndex: 'createdAt', 
    slotName: 'createdAt',
    width: 150
  },
  {
    title: '操作',
    slotName: 'action',
    width: 200,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined
  }
]

// 状态相关方法
const getStatusColor = (status: string) => {
  const colorMap = {
    '1': 'orange',
    '2': 'green', 
    '3': 'red'
  }
  return colorMap[status] || 'gray'
}

const getStatusText = (status: string) => {
  const textMap = {
    '1': '待审核',
    '2': '已通过',
    '3': '已拒绝'
  }
  return textMap[status] || '未知'
}

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.userId = undefined
  queryForm.applyStatus = undefined
  queryForm.auditTimeRange = undefined
  search()
}

// 删除
const onDelete = (item: PlanetApplyResp) => {
  return handleDelete(() => deletePlanetApply(item.id), {
    content: `是否确定删除该申请记录？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetApply(queryForm))
}

// 组件引用
const PlanetApplyAddModalRef = ref<InstanceType<typeof PlanetApplyAddModal>>()
const PlanetApplyDetailDrawerRef = ref<InstanceType<typeof PlanetApplyDetailDrawer>>()
const PlanetApplyAuditModalRef = ref<InstanceType<typeof PlanetApplyAuditModal>>()

// 新增
const onAdd = () => {
  PlanetApplyAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetApplyResp) => {
  PlanetApplyAddModalRef.value?.onUpdate(item.id)
}

// 详情
const onDetail = (item: PlanetApplyResp) => {
  PlanetApplyDetailDrawerRef.value?.onDetail(item.id)
}

// 审核
const onAudit = (item: PlanetApplyResp) => {
  PlanetApplyAuditModalRef.value?.onAudit(item.id)
}

// 批量审核
const onBatchAudit = () => {
  if (selectedRowKeys.value.length === 0) {
    Message.warning('请选择要审核的申请')
    return
  }
  PlanetApplyAuditModalRef.value?.onBatchAudit(selectedRowKeys.value)
}
</script>

<style lang="scss" scoped>
.text-ellipsis {
  display: inline-block;
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}
</style>
