<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="会员积分变动日志管理"
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
        <a-input v-model="queryForm.memberPointId" placeholder="请输入会员积分ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['member:memberPointsLog:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['member:memberPointsLog:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['member:memberPointsLog:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['member:memberPointsLog:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <MemberPointsLogAddModal ref="MemberPointsLogAddModalRef" @save-success="search" />
    <MemberPointsLogDetailDrawer ref="MemberPointsLogDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import MemberPointsLogAddModal from './MemberPointsLogAddModal.vue'
import MemberPointsLogDetailDrawer from './MemberPointsLogDetailDrawer.vue'
import { type MemberPointsLogResp, type MemberPointsLogQuery, deleteMemberPointsLog, exportMemberPointsLog, listMemberPointsLog } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'MemberPointsLog' })

const queryForm = reactive<MemberPointsLogQuery>({
  memberPointId: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listMemberPointsLog({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键', dataIndex: 'id', slotName: id },
  { title: '会员积分ID', dataIndex: 'memberPointId', slotName: memberPointId },
  { title: '变动前的会员积分', dataIndex: 'beforePoint', slotName: beforePoint },
  { title: '变动的会员积分', dataIndex: 'changePoint', slotName: changePoint },
  { title: '变动后的会员积分', dataIndex: 'afterPoint', slotName: afterPoint },
  { title: '本次积分变动的原因', dataIndex: 'updateReason', slotName: updateReason },
  { title: '乐观锁', dataIndex: 'version', slotName: version },
  { title: '逻辑删除 1: 已删除, 0: 未删除', dataIndex: 'isDeleted', slotName: isDeleted },
  { title: '创建者', dataIndex: 'createdBy', slotName: createdBy },
  { title: '更新者', dataIndex: 'updatedBy', slotName: updatedBy },
  { title: '创建时间', dataIndex: 'createdAt', slotName: createdAt },
  { title: '更新时间', dataIndex: 'updatedAt', slotName: updatedAt },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['member:memberPointsLog:update', 'member:memberPointsLog:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.memberPointId = undefined
  search()
}

// 删除
const onDelete = (item: MemberPointsLogResp) => {
  return handleDelete(() => deleteMemberPointsLog(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportMemberPointsLog(queryForm))
}

const MemberPointsLogAddModalRef = ref<InstanceType<typeof MemberPointsLogAddModal>>()
// 新增
const onAdd = () => {
  MemberPointsLogAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: MemberPointsLogResp) => {
  MemberPointsLogAddModalRef.value?.onUpdate(item.id)
}

const MemberPointsLogDetailDrawerRef = ref<InstanceType<typeof MemberPointsLogDetailDrawer>>()
// 详情
const onDetail = (item: MemberPointsLogResp) => {
  MemberPointsLogDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
