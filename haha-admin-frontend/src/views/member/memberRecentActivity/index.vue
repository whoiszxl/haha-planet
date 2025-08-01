<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="会员游戏动态管理"
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
        <a-input v-model="queryForm.memberId" placeholder="请输入会员ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.gameId" placeholder="请输入游戏ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.gameName" placeholder="请输入游戏名称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['member:memberRecentActivity:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['member:memberRecentActivity:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['member:memberRecentActivity:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['member:memberRecentActivity:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <MemberRecentActivityAddModal ref="MemberRecentActivityAddModalRef" @save-success="search" />
    <MemberRecentActivityDetailDrawer ref="MemberRecentActivityDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import MemberRecentActivityAddModal from './MemberRecentActivityAddModal.vue'
import MemberRecentActivityDetailDrawer from './MemberRecentActivityDetailDrawer.vue'
import { type MemberRecentActivityResp, type MemberRecentActivityQuery, deleteMemberRecentActivity, exportMemberRecentActivity, listMemberRecentActivity } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'MemberRecentActivity' })

const queryForm = reactive<MemberRecentActivityQuery>({
  memberId: undefined,
  gameId: undefined,
  gameName: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listMemberRecentActivity({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键ID', dataIndex: 'id', slotName: id },
  { title: '会员ID', dataIndex: 'memberId', slotName: memberId },
  { title: '游戏ID', dataIndex: 'gameId', slotName: gameId },
  { title: '游戏名称', dataIndex: 'gameName', slotName: gameName },
  { title: '游戏封面', dataIndex: 'gameCover', slotName: gameCover },
  { title: '动态类型(1:游戏时长 2:成就解锁 3:截图上传)', dataIndex: 'activityType', slotName: activityType },
  { title: '动态值(比如游戏时长)', dataIndex: 'activityValue', slotName: activityValue },
  { title: '单位(小时/个)', dataIndex: 'activityUnit', slotName: activityUnit },
  { title: '成就计数(比如 7/17)', dataIndex: 'achievementCount', slotName: achievementCount },
  { title: '成就总数', dataIndex: 'achievementTotal', slotName: achievementTotal },
  { title: '最后活跃时间', dataIndex: 'lastActive', slotName: lastActive },
  { title: '状态(0:无效 1:有效)', dataIndex: 'status', slotName: status },
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
    show: has.hasPermOr(['member:memberRecentActivity:update', 'member:memberRecentActivity:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.memberId = undefined
  queryForm.gameId = undefined
  queryForm.gameName = undefined
  search()
}

// 删除
const onDelete = (item: MemberRecentActivityResp) => {
  return handleDelete(() => deleteMemberRecentActivity(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportMemberRecentActivity(queryForm))
}

const MemberRecentActivityAddModalRef = ref<InstanceType<typeof MemberRecentActivityAddModal>>()
// 新增
const onAdd = () => {
  MemberRecentActivityAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: MemberRecentActivityResp) => {
  MemberRecentActivityAddModalRef.value?.onUpdate(item.id)
}

const MemberRecentActivityDetailDrawerRef = ref<InstanceType<typeof MemberRecentActivityDetailDrawer>>()
// 详情
const onDetail = (item: MemberRecentActivityResp) => {
  MemberRecentActivityDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
