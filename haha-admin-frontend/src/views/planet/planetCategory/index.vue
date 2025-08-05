<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球分类管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['categoryName']"
      @refresh="search"
    >
      <template #custom-left>
        <a-space :size="16">
          <a-input v-model="queryForm.categoryName" placeholder="请输入分类名称" allow-clear @change="search">
            <template #prefix><icon-search /></template>
          </a-input>
          <a-select
            v-model="queryForm.level"
            :options="levelOptions"
            placeholder="请选择分类级别"
            allow-clear
            style="width: 150px"
            @change="search"
          />
          <a-select
            v-model="queryForm.status"
            :options="[
              { label: '无效', value: 0 },
              { label: '有效', value: 1 }
            ]"
            placeholder="请选择状态"
            allow-clear
            style="width: 150px"
            @change="search"
          />
          <a-button @click="reset">重置</a-button>
        </a-space>
      </template>

      <template #custom-right>
        <a-space>
          <a-button v-permission="['planet:planetCategory:add']" type="primary" @click="onAdd">
            <template #icon><icon-plus /></template>
            <span>新增</span>
          </a-button>
          <a-tooltip content="导出">
            <a-button v-permission="['planet:planetCategory:export']" class="gi_hover_btn-border" @click="onExport">
              <template #icon><icon-download /></template>
            </a-button>
          </a-tooltip>
        </a-space>
      </template>

      <template #categoryName="{ record }">
        <a-space>
          <a-image
            v-if="record.iconUrl"
            :src="record.iconUrl"
            :width="24"
            height="24"
            fit="contain"
            style="border-radius: 4px"
          />
          <span>{{ record.categoryName }}</span>
        </a-space>
      </template>

      <template #level="{ record }">
        <a-tag :color="getLevelColor(record.level)">
          {{ getLevelLabel(record.level) }}
        </a-tag>
      </template>

      <template #status="{ record }">
        <a-tag :color="record.status === 1 ? 'green' : 'red'">
          {{ record.status === 1 ? '有效' : '无效' }}
        </a-tag>
      </template>

      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['planet:planetCategory:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['planet:planetCategory:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetCategoryAddModal ref="PlanetCategoryAddModalRef" @save-success="search" />
    <PlanetCategoryDetailDrawer ref="PlanetCategoryDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetCategoryAddModal from './PlanetCategoryAddModal.vue'
import PlanetCategoryDetailDrawer from './PlanetCategoryDetailDrawer.vue'
import { type PlanetCategoryResp, type PlanetCategoryQuery, deletePlanetCategory, exportPlanetCategory, listPlanetCategory } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'PlanetCategory' })

const levelOptions = [
  { label: '一级分类', value: 1 },
  { label: '二级分类', value: 2 },
  { label: '三级分类', value: 3 }
]

const getLevelLabel = (level: number) => {
  return levelOptions.find(item => item.value === level)?.label || '未知'
}

const getLevelColor = (level: number) => {
  const colors = ['blue', 'green', 'orange']
  return colors[level - 1] || 'default'
}

const queryForm = reactive<PlanetCategoryQuery>({
  categoryName: undefined,
  level: undefined,
  status: undefined,
  sort: ['sort,desc', 'createdAt,desc', 'parentId,asc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetCategory({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '分类名称', dataIndex: 'categoryName', slotName: 'categoryName', width: 220 },
  { title: '分类级别', dataIndex: 'level', slotName: 'level', width: 100 },
  { title: '排序', dataIndex: 'sort', width: 80, align: 'center' },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 80 },
  { title: '创建人', dataIndex: 'createdBy', width: 120 },
  { title: '创建时间', dataIndex: 'createdAt', width: 180 },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['planet:planetCategory:update', 'planet:planetCategory:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.categoryName = undefined
  queryForm.level = undefined
  queryForm.status = undefined
  search()
}

// 删除
const onDelete = (item: PlanetCategoryResp) => {
  return handleDelete(() => deletePlanetCategory(item.id), {
    content: `是否确定删除分类 [${item.categoryName}]？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetCategory(queryForm))
}

const PlanetCategoryAddModalRef = ref<InstanceType<typeof PlanetCategoryAddModal>>()
// 新增
const onAdd = () => {
  PlanetCategoryAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetCategoryResp) => {
  PlanetCategoryAddModalRef.value?.onUpdate(item.id)
}

const PlanetCategoryDetailDrawerRef = ref<InstanceType<typeof PlanetCategoryDetailDrawer>>()
// 详情
const onDetail = (item: PlanetCategoryResp) => {
  PlanetCategoryDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
:deep(.arco-table-td) {
  .arco-tag {
    margin-right: 0;
  }
}
</style>