<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球通知管理"
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
        <a-input v-model="queryForm.userId" placeholder="请输入接收通知的用户ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.notificationType" placeholder="请输入通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.title" placeholder="请输入通知标题" allow-clear @change="search">
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
        <a-button v-permission="['whoiszxl:planetNotification:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetNotification:export']" class="gi_hover_btn-border" @click="onExport">
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
          <a-link v-permission="['whoiszxl:planetNotification:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['whoiszxl:planetNotification:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetNotificationAddModal ref="PlanetNotificationAddModalRef" @save-success="search" />
    <PlanetNotificationDetailDrawer ref="PlanetNotificationDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetNotificationAddModal from './PlanetNotificationAddModal.vue'
import PlanetNotificationDetailDrawer from './PlanetNotificationDetailDrawer.vue'
import { type PlanetNotificationResp, type PlanetNotificationQuery, deletePlanetNotification, exportPlanetNotification, listPlanetNotification } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'PlanetNotification' })

const queryForm = reactive<PlanetNotificationQuery>({
  planetId: undefined,
  userId: undefined,
  notificationType: undefined,
  title: undefined,
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
} = useTable((page) => listPlanetNotification({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键', dataIndex: 'id', slotName: 'id' },
  { title: '星球ID', dataIndex: 'planetId', slotName: 'planetId' },
  { title: '接收通知的用户ID', dataIndex: 'userId', slotName: 'userId' },
  { title: '发送者ID', dataIndex: 'senderId', slotName: 'senderId' },
  { title: '通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告', dataIndex: 'notificationType', slotName: 'notificationType' },
  { title: '通知标题', dataIndex: 'title', slotName: 'title' },
  { title: '通知内容', dataIndex: 'content', slotName: 'content' },
  { title: '关联目标类型: 1-帖子 2-评论 3-星球', dataIndex: 'targetType', slotName: 'targetType' },
  { title: '关联目标ID', dataIndex: 'targetId', slotName: 'targetId' },
  { title: '是否已读: 0-未读 1-已读', dataIndex: 'isRead', slotName: 'isRead' },
  { title: '阅读时间', dataIndex: 'readTime', slotName: 'readTime' },
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
    show: has.hasPermOr(['whoiszxl:planetNotification:update', 'whoiszxl:planetNotification:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.userId = undefined
  queryForm.notificationType = undefined
  queryForm.title = undefined
  queryForm.version = undefined
  queryForm.createdBy = undefined
  search()
}

// 删除
const onDelete = (item: PlanetNotificationResp) => {
  return handleDelete(() => deletePlanetNotification(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetNotification(queryForm))
}

const PlanetNotificationAddModalRef = ref<InstanceType<typeof PlanetNotificationAddModal>>()
// 新增
const onAdd = () => {
  PlanetNotificationAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetNotificationResp) => {
  PlanetNotificationAddModalRef.value?.onUpdate(item.id)
}

const PlanetNotificationDetailDrawerRef = ref<InstanceType<typeof PlanetNotificationDetailDrawer>>()
// 详情
const onDetail = (item: PlanetNotificationResp) => {
  PlanetNotificationDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped></style>
