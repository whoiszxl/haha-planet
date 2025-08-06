<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="积分记录管理"
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
        <a-input v-model="queryForm.userId" placeholder="请输入用户ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.pointsChange" placeholder="请输入积分变化(正数增加，负数减少)" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.pointsBefore" placeholder="请输入变化前积分" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.pointsAfter" placeholder="请输入变化后积分" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.changeType" placeholder="请输入变化类型(sign:签到 post:发帖 like:点赞 comment:评论)" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.version" placeholder="请输入乐观锁" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['user:pointsRecord:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['user:pointsRecord:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['user:pointsRecord:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['user:pointsRecord:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PointsRecordAddModal ref="PointsRecordAddModalRef" @save-success="search" />
    <PointsRecordDetailDrawer ref="PointsRecordDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PointsRecordAddModal from './PointsRecordAddModal.vue'
import PointsRecordDetailDrawer from './PointsRecordDetailDrawer.vue'
import { type PointsRecordResp, type PointsRecordQuery, deletePointsRecord, exportPointsRecord, listPointsRecord } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'PointsRecord' })

const queryForm = reactive<PointsRecordQuery>({
  userId: undefined,
  pointsChange: undefined,
  pointsBefore: undefined,
  pointsAfter: undefined,
  changeType: undefined,
  version: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPointsRecord({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键ID', dataIndex: 'id', slotName: 'id' },
  { title: '用户ID', dataIndex: 'userId', slotName: 'userId' },
  { title: '积分变化(正数增加，负数减少)', dataIndex: 'pointsChange', slotName: 'pointsChange' },
  { title: '变化前积分', dataIndex: 'pointsBefore', slotName: 'pointsBefore' },
  { title: '变化后积分', dataIndex: 'pointsAfter', slotName: 'pointsAfter' },
  { title: '变化类型(sign:签到 post:发帖 like:点赞 comment:评论)', dataIndex: 'changeType', slotName: 'changeType' },
  { title: '变化原因', dataIndex: 'changeReason', slotName: 'changeReason' },
  { title: '关联ID(如帖子ID、评论ID等)', dataIndex: 'relatedId', slotName: 'relatedId' },
  { title: '状态(0:无效 1:有效)', dataIndex: 'status', slotName: 'status' },
  { title: '乐观锁', dataIndex: 'version', slotName: 'version' },
  { title: '逻辑删除 1: 已删除, 0: 未删除', dataIndex: 'isDeleted', slotName: 'isDeleted' },
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
    show: has.hasPermOr(['user:pointsRecord:update', 'user:pointsRecord:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.userId = undefined
  queryForm.pointsChange = undefined
  queryForm.pointsBefore = undefined
  queryForm.pointsAfter = undefined
  queryForm.changeType = undefined
  queryForm.version = undefined
  search()
}

// 删除
const onDelete = (item: PointsRecordResp) => {
  return handleDelete(() => deletePointsRecord(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPointsRecord(queryForm))
}

const PointsRecordAddModalRef = ref<InstanceType<typeof PointsRecordAddModal>>()
// 新增
const onAdd = () => {
  PointsRecordAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PointsRecordResp) => {
  PointsRecordAddModalRef.value?.onUpdate(item.id)
}

const PointsRecordDetailDrawerRef = ref<InstanceType<typeof PointsRecordDetailDrawer>>()
// 详情
const onDetail = (item: PointsRecordResp) => {
  PointsRecordDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
