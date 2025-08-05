<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球标签关联管理"
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
        <a-input v-model="queryForm.tagId" placeholder="请输入标签ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.version" placeholder="请输入乐观锁" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.createdBy" placeholder="请输入创建者" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetTagRelation:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetTagRelation:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #name="{ record }">
        <a-link @click="onDetail(record)">{{ record.name }}</a-link>
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['whoiszxl:planetTagRelation:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['whoiszxl:planetTagRelation:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetTagRelationAddModal ref="PlanetTagRelationAddModalRef" @save-success="search" />
    <PlanetTagRelationDetailDrawer ref="PlanetTagRelationDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetTagRelationAddModal from './PlanetTagRelationAddModal.vue'
import PlanetTagRelationDetailDrawer from './PlanetTagRelationDetailDrawer.vue'
import { type PlanetTagRelationResp, type PlanetTagRelationQuery, deletePlanetTagRelation, exportPlanetTagRelation, listPlanetTagRelation } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'PlanetTagRelation' })

const queryForm = reactive<PlanetTagRelationQuery>({
  planetId: undefined,
  tagId: undefined,
  version: undefined,
  createdBy: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetTagRelation({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键', dataIndex: 'id', slotName: 'id' },
  { title: '星球ID', dataIndex: 'planetId', slotName: 'planetId' },
  { title: '标签ID', dataIndex: 'tagId', slotName: 'tagId' },
  { title: '乐观锁', dataIndex: 'version', slotName: 'version' },
  { title: '业务状态', dataIndex: 'status', slotName: 'status' },
  { title: '逻辑删除 1: 已删除， 0: 未删除', dataIndex: 'isDeleted', slotName: 'isDeleted' },
  { title: '创建者', dataIndex: 'createdBy', slotName: 'createdBy' },
  { title: '更新者', dataIndex: 'updatedBy', slotName: 'updatedBy' },
  { title: '创建时间', dataIndex: 'createdAt', slotName: 'createdAt' },
  { title: '更新时间', dataIndex: 'updatedAt', slotName: 'updatedAt' },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetTagRelation:update', 'whoiszxl:planetTagRelation:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.tagId = undefined
  queryForm.version = undefined
  queryForm.createdBy = undefined
  search()
}

// 删除
const onDelete = (item: PlanetTagRelationResp) => {
  return handleDelete(() => deletePlanetTagRelation(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetTagRelation(queryForm))
}

const PlanetTagRelationAddModalRef = ref<InstanceType<typeof PlanetTagRelationAddModal>>()
// 新增
const onAdd = () => {
  PlanetTagRelationAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetTagRelationResp) => {
  PlanetTagRelationAddModalRef.value?.onUpdate(item.id)
}

const PlanetTagRelationDetailDrawerRef = ref<InstanceType<typeof PlanetTagRelationDetailDrawer>>()
// 详情
const onDetail = (item: PlanetTagRelationResp) => {
  PlanetTagRelationDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
