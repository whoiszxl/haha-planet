<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="星球帖子管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1200 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['title']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.planetId" placeholder="请输入星球ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.title" placeholder="请输入帖子标题" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.contentType" placeholder="内容类型" allow-clear @change="search">
          <a-option :value="1">文本</a-option>
          <a-option :value="2">图片</a-option>
          <a-option :value="3">视频</a-option>
          <a-option :value="4">音频</a-option>
          <a-option :value="5">文件</a-option>
          <a-option :value="6">链接</a-option>
        </a-select>
        <a-select v-model="queryForm.auditStatus" placeholder="审核状态" allow-clear @change="search">
          <a-option :value="1">待审核</a-option>
          <a-option :value="2">审核通过</a-option>
          <a-option :value="3">审核拒绝</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetPost:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetPost:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #title="{ record }">
        <a-link @click="onDetail(record)">{{ record.title || '无标题' }}</a-link>
      </template>
      <template #content="{ record }">
        <a-tooltip :content="record.content">
          <span class="content-preview">{{ record.content?.substring(0, 30) }}{{ record.content?.length > 30 ? '...' : '' }}</span>
        </a-tooltip>
      </template>
      <template #contentType="{ record }">
        <a-tag :color="getContentTypeColor(record.contentType)">
          {{ getContentTypeText(record.contentType) }}
        </a-tag>
      </template>
      <template #isTop="{ record }">
        <a-tag :color="record.isTop ? 'red' : 'gray'">
          {{ record.isTop ? '置顶' : '普通' }}
        </a-tag>
      </template>
      <template #isEssence="{ record }">
        <a-tag :color="record.isEssence ? 'gold' : 'gray'">
          {{ record.isEssence ? '精华' : '普通' }}
        </a-tag>
      </template>
      <template #viewCount="{ record }">
        <a-statistic :value="record.viewCount" :precision="0" />
      </template>
      <template #likeCount="{ record }">
        <a-statistic :value="record.likeCount" :precision="0" />
      </template>
      <template #commentCount="{ record }">
        <a-statistic :value="record.commentCount" :precision="0" />
      </template>
      <template #auditStatus="{ record }">
        <a-tag :color="getAuditStatusColor(record.auditStatus)">
          {{ getAuditStatusText(record.auditStatus) }}
        </a-tag>
      </template>
      <template #hotScore="{ record }">
        <a-statistic :value="record.hotScore" :precision="1" />
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['whoiszxl:planetPost:update']" @click="onUpdate(record)">修改</a-link>
          <a-link @click="onViewComments(record)">
            <icon-message /> 查看评论
          </a-link>
          <a-link @click="onViewCollectors(record)">
            <icon-heart /> 查看收藏者
          </a-link>
          <a-link
            v-permission="['whoiszxl:planetPost:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetPostAddModal ref="PlanetPostAddModalRef" @save-success="search" />
    <PlanetPostDetailDrawer ref="PlanetPostDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetPostAddModal from './PlanetPostAddModal.vue'
import PlanetPostDetailDrawer from './PlanetPostDetailDrawer.vue'
import { type PlanetPostResp, type PlanetPostQuery, deletePlanetPost, exportPlanetPost, listPlanetPost } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { useRoute, useRouter } from 'vue-router'

defineOptions({ name: 'PlanetPost' })

const route = useRoute()
const router = useRouter()

const queryForm = reactive<PlanetPostQuery>({
  planetId: route.query.planetId as string || undefined,
  title: undefined,
  contentType: undefined,
  auditStatus: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetPost({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置 - 只显示核心字段
const columns: TableInstanceColumns[] = [
  { title: 'ID', dataIndex: 'id', width: 80, align: 'center' },
  { title: '星球ID', dataIndex: 'planetId', width: 100, align: 'center' },
  { title: '帖子标题', dataIndex: 'title', slotName: 'title', width: 200 },
  { title: '内容预览', dataIndex: 'content', slotName: 'content', width: 200, ellipsis: true },
  { title: '内容类型', dataIndex: 'contentType', slotName: 'contentType', width: 100, align: 'center' },
  { title: '置顶', dataIndex: 'isTop', slotName: 'isTop', width: 80, align: 'center' },
  { title: '精华', dataIndex: 'isEssence', slotName: 'isEssence', width: 80, align: 'center' },
  { title: '浏览数', dataIndex: 'viewCount', slotName: 'viewCount', width: 100, align: 'center' },
  { title: '点赞数', dataIndex: 'likeCount', slotName: 'likeCount', width: 100, align: 'center' },
  { title: '评论数', dataIndex: 'commentCount', slotName: 'commentCount', width: 100, align: 'center' },
  { title: '审核状态', dataIndex: 'auditStatus', slotName: 'auditStatus', width: 100, align: 'center' },
  { title: '热度分数', dataIndex: 'hotScore', slotName: 'hotScore', width: 100, align: 'center' },
  { title: '创建时间', dataIndex: 'createdAt', width: 180 },
  {
    title: '操作',
    slotName: 'action',
    width: 310, 
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetPost:update', 'whoiszxl:planetPost:delete'])
  }
]

// 内容类型相关方法
const getContentTypeColor = (type: number) => {
  const colors = { 1: 'blue', 2: 'green', 3: 'purple', 4: 'orange', 5: 'cyan', 6: 'magenta' }
  return colors[type] || 'gray'
}

const getContentTypeText = (type: number) => {
  const texts = { 1: '文本', 2: '图片', 3: '视频', 4: '音频', 5: '文件', 6: '链接' }
  return texts[type] || '未知'
}

// 审核状态相关方法
const getAuditStatusColor = (status: number) => {
  const colors = { 1: 'orange', 2: 'green', 3: 'red' }
  return colors[status] || 'gray'
}

const getAuditStatusText = (status: number) => {
  const texts = { 1: '待审核', 2: '审核通过', 3: '审核拒绝' }
  return texts[status] || '未知'
}

// 重置
const reset = () => {
  queryForm.planetId = undefined
  queryForm.title = undefined
  queryForm.contentType = undefined
  queryForm.auditStatus = undefined
  search()
}

// 删除
const onDelete = (item: PlanetPostResp) => {
  return handleDelete(() => deletePlanetPost(item.id), {
    content: `是否确定删除帖子"${item.title || '无标题'}"？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetPost(queryForm))
}

const PlanetPostAddModalRef = ref<InstanceType<typeof PlanetPostAddModal>>()
// 新增
const onAdd = () => {
  PlanetPostAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetPostResp) => {
  PlanetPostAddModalRef.value?.onUpdate(item.id)
}

const PlanetPostDetailDrawerRef = ref<InstanceType<typeof PlanetPostDetailDrawer>>()
// 详情
const onDetail = (item: PlanetPostResp) => {
  PlanetPostDetailDrawerRef.value?.onDetail(item.id)
}

// 监听路由参数变化
watch(() => route.query.planetId, (newPlanetId) => {
  if (newPlanetId) {
    queryForm.planetId = newPlanetId as string
    search()
  }
}, { immediate: true })

// 查看评论
const onViewComments = (item: PlanetPostResp) => {
  router.push({
    path: '/planet/planetComment',
    query: {
      postId: item.id
    }
  })
}

// 查看收藏者
const onViewCollectors = (item: PlanetPostResp) => {
  router.push({
    path: '/planet/planetCollect',
    query: {
      postId: item.id
    }
  })
}
</script>

<style lang="scss" scoped>
.content-preview {
  color: #666;
  font-size: 13px;
}

// 统计数据数字加粗样式
:deep(.ant-statistic-content-value) {
  font-weight: bold !important;
}
</style>
