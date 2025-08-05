<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1200 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['name']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.planetCode" placeholder="请输入星球编码" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.name" placeholder="请输入星球名称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.status" placeholder="请选择状态" allow-clear @change="search">
          <a-option :value="0">禁用</a-option>
          <a-option :value="1">启用</a-option>
          <a-option :value="2">审核中</a-option>
          <a-option :value="3">已关闭</a-option>
        </a-select>
        <a-select v-model="queryForm.priceType" placeholder="价格类型" allow-clear @change="search">
          <a-option :value="1">免费</a-option>
          <a-option :value="2">付费</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planet:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planet:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #avatar="{ record }">
        <a-avatar v-if="record.avatar" :src="record.avatar" :size="40" />
        <a-avatar v-else :size="40">{{ record.name?.charAt(0) }}</a-avatar>
      </template>
      <template #name="{ record }">
        <a-link @click="onDetail(record)">{{ record.name }}</a-link>
      </template>
      <template #priceType="{ record }">
        <a-tag :color="record.priceType === 1 ? 'green' : 'blue'">
          {{ record.priceType === 1 ? '免费' : '付费' }}
        </a-tag>
      </template>
      <template #status="{ record }">
        <a-tag 
          :color="getStatusColor(record.status)"
        >
          {{ getStatusText(record.status) }}
        </a-tag>
      </template>

      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['whoiszxl:planet:update']" @click="onUpdate(record)">修改</a-link>
          <a-link @click="onViewPosts(record)">
            <icon-file-text /> 查看帖子
          </a-link>
          <a-link
            v-permission="['whoiszxl:planet:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetAddModal ref="PlanetAddModalRef" @save-success="search" />
    <PlanetDetailDrawer ref="PlanetDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetAddModal from './PlanetAddModal.vue'
import PlanetDetailDrawer from './PlanetDetailDrawer.vue'
import { type PlanetResp, type PlanetQuery, deletePlanet, exportPlanet, listPlanet } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { useRouter } from 'vue-router'

defineOptions({ name: 'Planet' })

const router = useRouter()

const queryForm = reactive<PlanetQuery>({
  planetCode: undefined,
  name: undefined,
  status: undefined,
  priceType: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanet({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置 - 只显示核心字段
const columns: TableInstanceColumns[] = [
  { title: 'ID', dataIndex: 'id', width: 80, align: 'center' },
  { title: '头像', dataIndex: 'avatar', slotName: 'avatar', width: 80, align: 'center' },
  { title: '星球名称', dataIndex: 'name', slotName: 'name', width: 150 },
  { title: '简介', dataIndex: 'description', width: 200, ellipsis: true, tooltip: true },
  { title: '星球主', dataIndex: 'ownerId', width: 100, align: 'center' },
  { title: '价格类型', dataIndex: 'priceType', slotName: 'priceType', width: 100, align: 'center' },
  { title: '加入价格', dataIndex: 'price', width: 100, align: 'center' },
  { title: '成员数', dataIndex: 'memberCount', slotName: 'memberCount', width: 100, align: 'center' },
  { title: '帖子数', dataIndex: 'postCount', slotName: 'postCount', width: 100, align: 'center' },
  { title: '总收入', dataIndex: 'totalIncome', slotName: 'totalIncome', width: 120, align: 'center' },
  { title: '状态', dataIndex: 'status', slotName: 'status', width: 100, align: 'center' },
  { title: '创建时间', dataIndex: 'createdAt', width: 180 },
  {
    title: '操作',
    slotName: 'action',
    width: 210,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planet:update', 'whoiszxl:planet:delete'])
  }
]

// 状态相关方法
const getStatusColor = (status: number) => {
  const colors = { 0: 'red', 1: 'green', 2: 'orange', 3: 'gray' }
  return colors[status] || 'gray'
}

const getStatusText = (status: number) => {
  const texts = { 0: '禁用', 1: '启用', 2: '审核中', 3: '已关闭' }
  return texts[status] || '未知'
}

// 重置
const reset = () => {
  queryForm.planetCode = undefined
  queryForm.name = undefined
  queryForm.status = undefined
  queryForm.priceType = undefined
  search()
}

// 删除
const onDelete = (item: PlanetResp) => {
  return handleDelete(() => deletePlanet(item.id), {
    content: `是否确定删除星球"${item.name}"？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanet(queryForm))
}

const PlanetAddModalRef = ref<InstanceType<typeof PlanetAddModal>>()
// 新增
const onAdd = () => {
  PlanetAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetResp) => {
  PlanetAddModalRef.value?.onUpdate(item.id)
}

const PlanetDetailDrawerRef = ref<InstanceType<typeof PlanetDetailDrawer>>()
// 详情
const onDetail = (item: PlanetResp) => {
  PlanetDetailDrawerRef.value?.onDetail(item.id)
}

// 查看帖子
const onViewPosts = (item: PlanetResp) => {
  router.push({
    path: '/planet/planetPost',
    query: {
      planetId: item.id
    }
  })
}
</script>

<style lang="scss" scoped>
// 统计数据数字加粗样式
:deep(.ant-statistic-content-value) {
  font-weight: bold !important;
}
</style>
