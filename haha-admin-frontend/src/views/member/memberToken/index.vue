<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户令牌管理"
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
        <a-input v-model="queryForm.memberId" placeholder="请输入用户ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.token" placeholder="请输入token" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.type" placeholder="请输入类型(1:公众号 2:小程序 3:unionid 4:APP)" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['member:memberToken:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['member:memberToken:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['member:memberToken:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['member:memberToken:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <MemberTokenAddModal ref="MemberTokenAddModalRef" @save-success="search" />
    <MemberTokenDetailDrawer ref="MemberTokenDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import MemberTokenAddModal from './MemberTokenAddModal.vue'
import MemberTokenDetailDrawer from './MemberTokenDetailDrawer.vue'
import { type MemberTokenResp, type MemberTokenQuery, deleteMemberToken, exportMemberToken, listMemberToken } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'MemberToken' })

const queryForm = reactive<MemberTokenQuery>({
  memberId: undefined,
  token: undefined,
  type: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listMemberToken({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键ID', dataIndex: 'id', slotName: id },
  { title: '用户ID', dataIndex: 'memberId', slotName: memberId },
  { title: 'token', dataIndex: 'token', slotName: token },
  { title: '类型(1:公众号 2:小程序 3:unionid 4:APP)', dataIndex: 'type', slotName: type },
  { title: '过期时间', dataIndex: 'expiresTime', slotName: expiresTime },
  { title: '登录IP', dataIndex: 'loginIp', slotName: loginIp },
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
    show: has.hasPermOr(['member:memberToken:update', 'member:memberToken:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.memberId = undefined
  queryForm.token = undefined
  queryForm.type = undefined
  search()
}

// 删除
const onDelete = (item: MemberTokenResp) => {
  return handleDelete(() => deleteMemberToken(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportMemberToken(queryForm))
}

const MemberTokenAddModalRef = ref<InstanceType<typeof MemberTokenAddModal>>()
// 新增
const onAdd = () => {
  MemberTokenAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: MemberTokenResp) => {
  MemberTokenAddModalRef.value?.onUpdate(item.id)
}

const MemberTokenDetailDrawerRef = ref<InstanceType<typeof MemberTokenDetailDrawer>>()
// 详情
const onDetail = (item: MemberTokenResp) => {
  MemberTokenDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
