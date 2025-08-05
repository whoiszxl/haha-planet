<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球标签管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 800 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['name']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.name" placeholder="请输入标签名称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.categoryId" placeholder="选择分类" allow-clear @change="search">
          <a-option value="">全部分类</a-option>
          <a-option :value="1">技术类</a-option>
          <a-option :value="2">行业类</a-option>
          <a-option :value="3">兴趣类</a-option>
          <a-option :value="4">其他</a-option>
        </a-select>
        <a-select v-model="queryForm.status" placeholder="状态" allow-clear @change="search">
          <a-option :value="1">启用</a-option>
          <a-option :value="0">禁用</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetTag:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetTag:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #name="{ record }">
        <a-link @click="onDetail(record)">{{ record.name }}</a-link>
      </template>
      <template #color="{ record }">
        <div class="tag-color-display">
          <div 
            class="color-circle" 
            :style="{ backgroundColor: record.color || '#1890ff' }"
          ></div>
          <span class="color-text">{{ record.color || '#1890ff' }}</span>
        </div>
      </template>
      <template #useCount="{ record }">
        <a-statistic 
          :value="record.useCount || 0" 
          :value-style="{ fontSize: '14px', fontWeight: 'bold', color: record.useCount > 100 ? '#52c41a' : record.useCount > 10 ? '#1890ff' : '#8c8c8c' }"
        />
      </template>
      <template #categoryId="{ record }">
        <a-tag :color="getCategoryColor(record.categoryId)">
          {{ getCategoryName(record.categoryId) }}
        </a-tag>
      </template>
      <template #status="{ record }">
        <a-tag :color="record.status === 1 ? 'green' : 'red'">
          {{ record.status === 1 ? '启用' : '禁用' }}
        </a-tag>
      </template>
      <template #createdAt="{ record }">
        {{ formatDateTime(record.createdAt) }}
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['whoiszxl:planetTag:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['whoiszxl:planetTag:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetTagAddModal ref="PlanetTagAddModalRef" @save-success="search" />
    <PlanetTagDetailDrawer ref="PlanetTagDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetTagAddModal from './PlanetTagAddModal.vue'
import PlanetTagDetailDrawer from './PlanetTagDetailDrawer.vue'
import { type PlanetTagResp, type PlanetTagQuery, deletePlanetTag, exportPlanetTag, listPlanetTag } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { formatDateTime } from '@/utils/format'

defineOptions({ name: 'PlanetTag' })

const queryForm = reactive<PlanetTagQuery>({
  name: undefined,
  categoryId: undefined,
  status: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetTag({ ...queryForm, ...page }), { immediate: true })

// 精简后的列配置
const columns: TableInstanceColumns[] = [
  { title: '标签名称', dataIndex: 'name', slotName: 'name', width: 120 },
  { title: '标签颜色', dataIndex: 'color', slotName: 'color', width: 120, align: 'center' },
  { title: '使用次数', dataIndex: 'useCount', slotName: 'useCount', width: 100, align: 'center' },
  { title: '所属分类', dataIndex: 'categoryId', slotName: 'categoryId', width: 100, align: 'center' },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80, align: 'center' },
  { title: '创建时间', dataIndex: 'createdAt', slotName: 'createdAt', width: 150 },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetTag:update', 'whoiszxl:planetTag:delete'])
  }
]

// 分类相关方法
const getCategoryName = (categoryId: number) => {
  const categoryMap = { 1: '技术类', 2: '行业类', 3: '兴趣类', 4: '其他' }
  return categoryMap[categoryId] || '未分类'
}

const getCategoryColor = (categoryId: number) => {
  const colorMap = { 1: 'blue', 2: 'green', 3: 'orange', 4: 'gray' }
  return colorMap[categoryId] || 'gray'
}

// 重置
const reset = () => {
  queryForm.name = undefined
  queryForm.categoryId = undefined
  queryForm.status = undefined
  search()
}

// 删除
const onDelete = (item: PlanetTagResp) => {
  return handleDelete(() => deletePlanetTag(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetTag(queryForm))
}

const PlanetTagAddModalRef = ref<InstanceType<typeof PlanetTagAddModal>>()
// 新增
const onAdd = () => {
  PlanetTagAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetTagResp) => {
  PlanetTagAddModalRef.value?.onUpdate(item.id)
}

const PlanetTagDetailDrawerRef = ref<InstanceType<typeof PlanetTagDetailDrawer>>()
// 详情
const onDetail = (item: PlanetTagResp) => {
  PlanetTagDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.tag-color-display {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .color-circle {
    width: 16px;
    height: 16px;
    border-radius: 50%;
    border: 1px solid var(--color-border-2);
    flex-shrink: 0;
  }
  
  .color-text {
    font-family: 'Courier New', monospace;
    font-size: 12px;
    color: var(--color-text-2);
  }
}

:deep(.ant-statistic-content-value) {
  font-weight: bold !important;
}
</style>
