<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="收藏记录管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['folderName']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.planetId" placeholder="请输入星球ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.userId" placeholder="请输入用户ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.postId" placeholder="请输入帖子ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.folderName" placeholder="请输入收藏夹名称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.status" placeholder="收藏状态" allow-clear @change="search">
          <a-option :value="1">正常</a-option>
          <a-option :value="0">已取消</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetCollect:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetCollect:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #folderName="{ record }">
        <div class="folder-info">
          <a-link @click="onDetail(record)">
            <icon-folder /> {{ record.folderName || '默认收藏夹' }}
          </a-link>
        </div>
      </template>
      <template #userId="{ record }">
        <div class="user-info">
          <span class="user-id">{{ record.userId }}</span>
        </div>
      </template>
      <template #status="{ record }">
        <a-tag :color="getStatusColor(record.status)">
          <component :is="getStatusIcon(record.status)" />
          {{ getStatusText(record.status) }}
        </a-tag>
      </template>
      <template #createdAt="{ record }">
        {{ formatDateTime(record.createdAt) }}
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link @click="onViewPost(record)">
            <icon-eye /> 查看帖子
          </a-link>
          <a-link @click="onViewPlanet(record)">
            <icon-apps /> 查看星球
          </a-link>
          <a-link v-permission="['whoiszxl:planetCollect:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['whoiszxl:planetCollect:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetCollectAddModal ref="PlanetCollectAddModalRef" @save-success="search" />
    <PlanetCollectDetailDrawer ref="PlanetCollectDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetCollectAddModal from './PlanetCollectAddModal.vue'
import PlanetCollectDetailDrawer from './PlanetCollectDetailDrawer.vue'
import { type PlanetCollectResp, type PlanetCollectQuery, deletePlanetCollect, exportPlanetCollect, listPlanetCollect } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { formatDateTime } from '@/utils/format'
import { useRouter } from 'vue-router'

defineOptions({ name: 'PlanetCollect' })

const router = useRouter()
const route = useRoute()

const queryForm = reactive<PlanetCollectQuery>({
  planetId: undefined,
  userId: undefined,
  postId: route.query.postId as string || undefined,
  folderName: undefined,
  status: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetCollect({ ...queryForm, ...page }), { immediate: true })

// 精简后的列配置
const columns: TableInstanceColumns[] = [
  { title: 'ID', dataIndex: 'id', width: 80, align: 'center' },
  { title: '星球ID', dataIndex: 'planetId', width: 100, align: 'center' },
  { title: '帖子ID', dataIndex: 'postId', width: 100, align: 'center' },
  { title: '用户ID', dataIndex: 'userId', slotName: 'userId', width: 120 },
  { title: '收藏夹', dataIndex: 'folderName', slotName: 'folderName', width: 150 },
  { title: '收藏状态', dataIndex: 'status', slotName: 'status', width: 100, align: 'center' },
  { title: '收藏时间', dataIndex: 'createdAt', slotName: 'createdAt', width: 150 },
  {
    title: '操作',
    slotName: 'action',
    width: 200,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetCollect:update', 'whoiszxl:planetCollect:delete'])
  }
]

// 状态相关方法
const getStatusText = (status: number) => {
  const statusMap = { 1: '正常收藏', 0: '已取消' }
  return statusMap[status] || '未知'
}

const getStatusColor = (status: number) => {
  const colorMap = { 1: 'green', 0: 'gray' }
  return colorMap[status] || 'gray'
}

const getStatusIcon = (status: number) => {
  const iconMap = { 1: 'icon-heart-fill', 0: 'icon-heart' }
  return iconMap[status] || 'icon-question-circle'
}

// 查看帖子
const onViewPost = (record: PlanetCollectResp) => {
  router.push({
    path: '/planet/planetPost',
    query: { postId: record.postId }
  })
}

// 查看星球
const onViewPlanet = (record: PlanetCollectResp) => {
  router.push({
    path: '/planet/planet',
    query: { planetId: record.planetId }
  })
}

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.userId = undefined
  queryForm.postId = undefined
  queryForm.folderName = undefined
  queryForm.status = undefined
  search()
}

// 删除
const onDelete = (item: PlanetCollectResp) => {
  return handleDelete(() => deletePlanetCollect(item.id), {
    content: `是否确定删除该收藏记录？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetCollect(queryForm))
}

const PlanetCollectAddModalRef = ref<InstanceType<typeof PlanetCollectAddModal>>()
// 新增
const onAdd = () => {
  PlanetCollectAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetCollectResp) => {
  PlanetCollectAddModalRef.value?.onUpdate(item.id)
}

const PlanetCollectDetailDrawerRef = ref<InstanceType<typeof PlanetCollectDetailDrawer>>()
// 详情
const onDetail = (item: PlanetCollectResp) => {
  PlanetCollectDetailDrawerRef.value?.onDetail(item.id)
}

// 监听路由参数变化
watch(() => route.query.postId, (newPostId) => {
  if (newPostId) {
    queryForm.postId = newPostId as string
    search()
  }
}, { immediate: true })
</script>

<style lang="scss" scoped>
.folder-info {
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-info {
  .user-id {
    font-weight: 500;
  }
}
</style>
