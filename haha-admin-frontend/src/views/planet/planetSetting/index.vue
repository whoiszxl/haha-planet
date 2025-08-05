<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球设置管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 800 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['settingKey']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.planetId" placeholder="请输入星球ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.settingKey" placeholder="请输入设置键" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.settingType" placeholder="设置类型" allow-clear @change="search">
          <a-option :value="1">字符串</a-option>
          <a-option :value="2">数字</a-option>
          <a-option :value="3">布尔</a-option>
          <a-option :value="4">JSON</a-option>
        </a-select>
        <a-select v-model="queryForm.status" placeholder="状态" allow-clear @change="search">
          <a-option :value="1">启用</a-option>
          <a-option :value="0">禁用</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetSetting:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetSetting:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #settingKey="{ record }">
        <a-link @click="onDetail(record)">{{ record.settingKey }}</a-link>
      </template>
      <template #settingType="{ record }">
        <a-tag :color="getSettingTypeColor(record.settingType)">
          {{ getSettingTypeText(record.settingType) }}
        </a-tag>
      </template>
      <template #settingValue="{ record }">
        <div class="setting-value">
          <span v-if="record.settingType === 3">
            <a-tag :color="record.settingValue === 'true' ? 'green' : 'red'">
              {{ record.settingValue === 'true' ? '是' : '否' }}
            </a-tag>
          </span>
          <span v-else-if="record.settingType === 4" class="json-value">
            <a-tooltip :content="record.settingValue">
              <span>{{ formatJsonValue(record.settingValue) }}</span>
            </a-tooltip>
          </span>
          <span v-else>{{ record.settingValue }}</span>
        </div>
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
          <a-link v-permission="['whoiszxl:planetSetting:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['whoiszxl:planetSetting:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetSettingAddModal ref="PlanetSettingAddModalRef" @save-success="search" />
    <PlanetSettingDetailDrawer ref="PlanetSettingDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetSettingAddModal from './PlanetSettingAddModal.vue'
import PlanetSettingDetailDrawer from './PlanetSettingDetailDrawer.vue'
import { type PlanetSettingResp, type PlanetSettingQuery, deletePlanetSetting, exportPlanetSetting, listPlanetSetting } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { formatDateTime } from '@/utils/format'

defineOptions({ name: 'PlanetSetting' })

const queryForm = reactive<PlanetSettingQuery>({
  planetId: undefined,
  settingKey: undefined,
  settingType: undefined,
  status: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetSetting({ ...queryForm, ...page }), { immediate: true })

// 精简后的列配置
const columns: TableInstanceColumns[] = [
  { title: '星球ID', dataIndex: 'planetId', width: 100 },
  { title: '设置键', dataIndex: 'settingKey', slotName: 'settingKey', width: 150 },
  { title: '设置值', dataIndex: 'settingValue', slotName: 'settingValue', width: 200 },
  { title: '类型', dataIndex: 'settingType', slotName: 'settingType', width: 80, align: 'center' },
  { title: '描述', dataIndex: 'description', width: 150, ellipsis: true, tooltip: true },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80, align: 'center' },
  { title: '创建时间', dataIndex: 'createdAt', slotName: 'createdAt', width: 150 },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetSetting:update', 'whoiszxl:planetSetting:delete'])
  }
]

// 设置类型相关方法
const getSettingTypeText = (type: number) => {
  const typeMap = { 1: '字符串', 2: '数字', 3: '布尔', 4: 'JSON' }
  return typeMap[type] || '未知'
}

const getSettingTypeColor = (type: number) => {
  const colorMap = { 1: 'blue', 2: 'green', 3: 'orange', 4: 'purple' }
  return colorMap[type] || 'gray'
}

// 格式化JSON值显示
const formatJsonValue = (value: string) => {
  if (!value) return ''
  try {
    const obj = JSON.parse(value)
    return JSON.stringify(obj).length > 30 
      ? JSON.stringify(obj).substring(0, 30) + '...' 
      : JSON.stringify(obj)
  } catch {
    return value.length > 30 ? value.substring(0, 30) + '...' : value
  }
}

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.settingKey = undefined
  queryForm.settingType = undefined
  queryForm.status = undefined
  search()
}

// 删除
const onDelete = (item: PlanetSettingResp) => {
  return handleDelete(() => deletePlanetSetting(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetSetting(queryForm))
}

const PlanetSettingAddModalRef = ref<InstanceType<typeof PlanetSettingAddModal>>()
// 新增
const onAdd = () => {
  PlanetSettingAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetSettingResp) => {
  PlanetSettingAddModalRef.value?.onUpdate(item.id)
}

const PlanetSettingDetailDrawerRef = ref<InstanceType<typeof PlanetSettingDetailDrawer>>()
// 详情
const onDetail = (item: PlanetSettingResp) => {
  PlanetSettingDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.setting-value {
  .json-value {
    font-family: 'Courier New', monospace;
    font-size: 12px;
    color: #666;
  }
}
</style>
