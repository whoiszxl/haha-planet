<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球成员管理"
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
        <a-input v-model="queryForm.userId" placeholder="请输入用户ID" allow-clear @change="search">
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
        <a-button v-permission="['whoiszxl:planetMember:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetMember:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['whoiszxl:planetMember:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['whoiszxl:planetMember:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetMemberAddModal ref="PlanetMemberAddModalRef" @save-success="search" />
    <PlanetMemberDetailDrawer ref="PlanetMemberDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetMemberAddModal from './PlanetMemberAddModal.vue'
import PlanetMemberDetailDrawer from './PlanetMemberDetailDrawer.vue'
import { type PlanetMemberResp, type PlanetMemberQuery, deletePlanetMember, exportPlanetMember, listPlanetMember } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'PlanetMember' })

const queryForm = reactive<PlanetMemberQuery>({
  planetId: undefined,
  userId: undefined,
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
} = useTable((page) => listPlanetMember({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键', dataIndex: 'id', slotName: 'id' },
  { title: '星球ID', dataIndex: 'planetId', slotName: 'planetId' },
  { title: '用户ID', dataIndex: 'userId', slotName: 'userId' },
  { title: '成员类型: 1-普通成员 2-管理员 3-星球主', dataIndex: 'memberType', slotName: 'memberType' },
  { title: '加入时间', dataIndex: 'joinTime', slotName: 'joinTime' },
  { title: '到期时间', dataIndex: 'expireTime', slotName: 'expireTime' },
  { title: '加入来源: 1-直接加入 2-邀请 3-分享', dataIndex: 'joinSource', slotName: 'joinSource' },
  { title: '邀请人ID', dataIndex: 'inviterId', slotName: 'inviterId' },
  { title: '订单ID', dataIndex: 'orderId', slotName: 'orderId' },
  { title: '在星球中的昵称', dataIndex: 'nickname', slotName: 'nickname' },
  { title: '是否被禁言: 0-否 1-是', dataIndex: 'isMuted', slotName: 'isMuted' },
  { title: '禁言结束时间', dataIndex: 'muteEndTime', slotName: 'muteEndTime' },
  { title: '最后阅读时间', dataIndex: 'lastReadTime', slotName: 'lastReadTime' },
  { title: '总发帖数', dataIndex: 'totalPosts', slotName: 'totalPosts' },
  { title: '总获赞数', dataIndex: 'totalLikes', slotName: 'totalLikes' },
  { title: '乐观锁', dataIndex: 'version', slotName: 'version' },
  { title: '业务状态: 1-正常 2-已退出', dataIndex: 'status', slotName: 'status' },
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
    show: has.hasPermOr(['whoiszxl:planetMember:update', 'whoiszxl:planetMember:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.userId = undefined
  queryForm.version = undefined
  queryForm.createdBy = undefined
  search()
}

// 删除
const onDelete = (item: PlanetMemberResp) => {
  return handleDelete(() => deletePlanetMember(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetMember(queryForm))
}

const PlanetMemberAddModalRef = ref<InstanceType<typeof PlanetMemberAddModal>>()
// 新增
const onAdd = () => {
  PlanetMemberAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetMemberResp) => {
  PlanetMemberAddModalRef.value?.onUpdate(item.id)
}

const PlanetMemberDetailDrawerRef = ref<InstanceType<typeof PlanetMemberDetailDrawer>>()
// 详情
const onDetail = (item: PlanetMemberResp) => {
  PlanetMemberDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
