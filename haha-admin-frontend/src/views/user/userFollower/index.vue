<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户粉丝(关注我的人)管理"
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
        <a-input v-model="queryForm.userId" placeholder="请输入用户ID(被关注者)" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.followerId" placeholder="请输入关注者ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.version" placeholder="请输入乐观锁" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['user:userFollower:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['user:userFollower:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['user:userFollower:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['user:userFollower:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <UserFollowerAddModal ref="UserFollowerAddModalRef" @save-success="search" />
    <UserFollowerDetailDrawer ref="UserFollowerDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import UserFollowerAddModal from './UserFollowerAddModal.vue'
import UserFollowerDetailDrawer from './UserFollowerDetailDrawer.vue'
import { type UserFollowerResp, type UserFollowerQuery, deleteUserFollower, exportUserFollower, listUserFollower } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'UserFollower' })

const queryForm = reactive<UserFollowerQuery>({
  userId: undefined,
  followerId: undefined,
  version: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listUserFollower({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键ID', dataIndex: 'id', slotName: 'id' },
  { title: '用户ID(被关注者)', dataIndex: 'userId', slotName: 'userId' },
  { title: '关注者ID', dataIndex: 'followerId', slotName: 'followerId' },
  { title: '关注类型(1:普通关注 2:特别关注)', dataIndex: 'followType', slotName: 'followType' },
  { title: '是否互相关注(0:否 1:是)', dataIndex: 'isMutual', slotName: 'isMutual' },
  { title: '是否取消关注(0:未取消 1:已取消)', dataIndex: 'isCancelled', slotName: 'isCancelled' },
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
    show: has.hasPermOr(['user:userFollower:update', 'user:userFollower:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.userId = undefined
  queryForm.followerId = undefined
  queryForm.version = undefined
  search()
}

// 删除
const onDelete = (item: UserFollowerResp) => {
  return handleDelete(() => deleteUserFollower(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportUserFollower(queryForm))
}

const UserFollowerAddModalRef = ref<InstanceType<typeof UserFollowerAddModal>>()
// 新增
const onAdd = () => {
  UserFollowerAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: UserFollowerResp) => {
  UserFollowerAddModalRef.value?.onUpdate(item.id)
}

const UserFollowerDetailDrawerRef = ref<InstanceType<typeof UserFollowerDetailDrawer>>()
// 详情
const onDetail = (item: UserFollowerResp) => {
  UserFollowerDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
