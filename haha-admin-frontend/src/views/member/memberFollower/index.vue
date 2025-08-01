<template>
    <div class="table-page">
      <GiTable
        ref="tableRef"
        row-key="id"
        title="会员粉丝管理"
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
          <a-button @click="reset">重置</a-button>
        </template>
        <template #custom-right>
          <a-button v-permission="['member:memberFollower:add']" type="primary" @click="onAdd">
            <template #icon><icon-plus /></template>
            <span>新增</span>
          </a-button>
          <a-tooltip content="导出">
            <a-button v-permission="['member:memberFollower:export']" class="gi_hover_btn-border" @click="onExport">
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
            <a-link v-permission="['member:memberFollower:update']" @click="onUpdate(record)">修改</a-link>
            <a-link
              v-permission="['member:memberFollower:delete']"
              status="danger"
              :disabled="record.disabled"
              @click="onDelete(record)"
            >
              删除
            </a-link>
          </a-space>
        </template>
      </GiTable>
  
      <MemberFollowerAddModal ref="MemberFollowerAddModalRef" @save-success="search" />
      <MemberFollowerDetailDrawer ref="MemberFollowerDetailDrawerRef" />
    </div>
  </template>
  
  <script setup lang="ts">
  import MemberFollowerAddModal from './MemberFollowerAddModal.vue'
  import MemberFollowerDetailDrawer from './MemberFollowerDetailDrawer.vue'
  import { type MemberFollowerResp, type MemberFollowerQuery, deleteMemberFollower, exportMemberFollower, listMemberFollower } from '@/apis'
  import type { TableInstanceColumns } from '@/components/GiTable/type'
  import { useDownload, useTable } from '@/hooks'
  import { isMobile } from '@/utils'
  import has from '@/utils/has'
  
  defineOptions({ name: 'MemberFollower' })
  
  const queryForm = reactive<MemberFollowerQuery>({
    sort: ['createdAt,desc']
  })
  
  const {
    tableData: dataList,
    loading,
    pagination,
    search,
    handleDelete
  } = useTable((page) => listMemberFollower({ ...queryForm, ...page }), { immediate: true })
  
  const columns: TableInstanceColumns[] = [
    { title: '主键ID', dataIndex: 'id', slotName: id },
    { title: '用户ID（我）', dataIndex: 'memberId', slotName: memberId },
    { title: '粉丝ID（别人）', dataIndex: 'targetId', slotName: targetId },
    { title: '是否取消关注: 0-未取消 1-取消', dataIndex: 'cancel', slotName: cancel },
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
      show: has.hasPermOr(['member:memberFollower:update', 'member:memberFollower:delete'])
    }
  ]
  
  // 重置
  const reset = () => {
    search()
  }
  
  // 删除
  const onDelete = (item: MemberFollowerResp) => {
    return handleDelete(() => deleteMemberFollower(item.id), {
      content: `是否确定删除该条数据？`,
      showModal: true
    })
  }
  
  // 导出
  const onExport = () => {
    useDownload(() => exportMemberFollower(queryForm))
  }
  
  const MemberFollowerAddModalRef = ref<InstanceType<typeof MemberFollowerAddModal>>()
  // 新增
  const onAdd = () => {
    MemberFollowerAddModalRef.value?.onAdd()
  }
  
  // 修改
  const onUpdate = (item: MemberFollowerResp) => {
    MemberFollowerAddModalRef.value?.onUpdate(item.id)
  }
  
  const MemberFollowerDetailDrawerRef = ref<InstanceType<typeof MemberFollowerDetailDrawer>>()
  // 详情
  const onDetail = (item: MemberFollowerResp) => {
    MemberFollowerDetailDrawerRef.value?.onDetail(item.id)
  }
  </script>
  
  <style lang="scss" scoped></style>
  