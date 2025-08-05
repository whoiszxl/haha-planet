<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="帖子评论管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['content']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.postId" placeholder="请输入帖子ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.planetId" placeholder="请输入星球ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.userId" placeholder="请输入用户ID" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.content" placeholder="请输入评论内容" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-select v-model="queryForm.auditStatus" placeholder="审核状态" allow-clear @change="search">
          <a-option :value="1">待审核</a-option>
          <a-option :value="2">审核通过</a-option>
          <a-option :value="3">审核拒绝</a-option>
        </a-select>
        <a-select v-model="queryForm.isAnonymous" placeholder="匿名状态" allow-clear @change="search">
          <a-option :value="0">实名</a-option>
          <a-option :value="1">匿名</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['whoiszxl:planetComment:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-tooltip content="导出">
          <a-button v-permission="['whoiszxl:planetComment:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      <template #content="{ record }">
        <div class="comment-content">
          <a-link @click="onDetail(record)">
            {{ truncateText(record.content, 50) }}
          </a-link>
          <div class="comment-meta" v-if="record.mediaUrls">
            <a-tag color="blue" size="small">
              <icon-image /> 包含媒体
            </a-tag>
          </div>
        </div>
      </template>
      <template #parentId="{ record }">
        <div class="comment-level">
          <a-tag v-if="record.parentId === 0" color="green">顶级评论</a-tag>
          <a-tag v-else color="orange">
            <icon-reply /> 回复 #{{ record.parentId }}
          </a-tag>
        </div>
      </template>
      <template #userId="{ record }">
        <div class="user-info">
          <span class="user-id">{{ record.userId }}</span>
          <a-tag v-if="record.isAnonymous" color="gray" size="small">
            <icon-user /> 匿名
          </a-tag>
        </div>
      </template>
      <template #likeCount="{ record }">
        <a-statistic 
          :value="record.likeCount || 0" 
          :value-style="{ 
            fontSize: '14px', 
            fontWeight: 'bold', 
            color: record.likeCount > 100 ? '#f5222d' : record.likeCount > 10 ? '#fa541c' : '#8c8c8c' 
          }"
        >
          <template #prefix>
            <icon-heart style="color: #f5222d; margin-right: 4px;" />
          </template>
        </a-statistic>
      </template>
      <template #replyCount="{ record }">
        <a-statistic 
          :value="record.replyCount || 0" 
          :value-style="{ 
            fontSize: '14px', 
            fontWeight: 'bold', 
            color: record.replyCount > 50 ? '#52c41a' : record.replyCount > 5 ? '#1890ff' : '#8c8c8c' 
          }"
        >
          <template #prefix>
            <icon-message style="color: #1890ff; margin-right: 4px;" />
          </template>
        </a-statistic>
      </template>
      <template #auditStatus="{ record }">
        <a-tag :color="getAuditStatusColor(record.auditStatus)">
          <component :is="getAuditStatusIcon(record.auditStatus)" />
          {{ getAuditStatusText(record.auditStatus) }}
        </a-tag>
      </template>
      <template #createdAt="{ record }">
        {{ formatDateTime(record.createdAt) }}
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['whoiszxl:planetComment:update']" @click="onUpdate(record)">修改</a-link>
          <a-dropdown v-permission="['whoiszxl:planetComment:audit']">
            <a-link>审核</a-link>
            <template #content>
              <a-doption @click="onAudit(record, 2)">
                <icon-check /> 通过
              </a-doption>
              <a-doption @click="onAudit(record, 3)">
                <icon-close /> 拒绝
              </a-doption>
            </template>
          </a-dropdown>
          <a-link
            v-permission="['whoiszxl:planetComment:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <PlanetCommentAddModal ref="PlanetCommentAddModalRef" @save-success="search" />
    <PlanetCommentDetailDrawer ref="PlanetCommentDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import PlanetCommentAddModal from './PlanetCommentAddModal.vue'
import PlanetCommentDetailDrawer from './PlanetCommentDetailDrawer.vue'
import { type PlanetCommentResp, type PlanetCommentQuery, deletePlanetComment, exportPlanetComment, listPlanetComment, auditPlanetComment } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { formatDateTime } from '@/utils/format'
import { Message } from '@arco-design/web-vue'

import { useRoute } from 'vue-router'

defineOptions({ name: 'PlanetComment' })

const route = useRoute()

const queryForm = reactive<PlanetCommentQuery>({
  postId: route.query.postId as string || undefined,
  planetId: undefined,
  userId: undefined,
  content: undefined,
  auditStatus: undefined,
  isAnonymous: undefined,
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listPlanetComment({ ...queryForm, ...page }), { immediate: true })

// 精简后的列配置
const columns: TableInstanceColumns[] = [
  { title: '帖子ID', dataIndex: 'postId', width: 80 },
  { title: '星球ID', dataIndex: 'planetId', width: 80 },
  { title: '评论内容', dataIndex: 'content', slotName: 'content', width: 250 },
  { title: '评论层级', dataIndex: 'parentId', slotName: 'parentId', width: 120, align: 'center' },
  { title: '评论用户', dataIndex: 'userId', slotName: 'userId', width: 120 },
  { title: '点赞数', dataIndex: 'likeCount', slotName: 'likeCount', width: 80, align: 'center' },
  { title: '回复数', dataIndex: 'replyCount', slotName: 'replyCount', width: 80, align: 'center' },
  { title: '审核状态', dataIndex: 'auditStatus', slotName: 'auditStatus', width: 100, align: 'center' },
  { title: '创建时间', dataIndex: 'createdAt', slotName: 'createdAt', width: 150 },
  {
    title: '操作',
    slotName: 'action',
    width: 150,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['whoiszxl:planetComment:update', 'whoiszxl:planetComment:delete', 'whoiszxl:planetComment:audit'])
  }
]

// 文本截断
const truncateText = (text: string, maxLength: number) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}

// 审核状态相关方法
const getAuditStatusText = (status: number) => {
  const statusMap = { 1: '待审核', 2: '审核通过', 3: '审核拒绝' }
  return statusMap[status] || '未知'
}

const getAuditStatusColor = (status: number) => {
  const colorMap = { 1: 'orange', 2: 'green', 3: 'red' }
  return colorMap[status] || 'gray'
}

const getAuditStatusIcon = (status: number) => {
  const iconMap = { 1: 'icon-clock-circle', 2: 'icon-check-circle', 3: 'icon-close-circle' }
  return iconMap[status] || 'icon-question-circle'
}

// 审核操作
const onAudit = async (record: PlanetCommentResp, status: number) => {
  try {
    await auditPlanetComment(record.id, { auditStatus: status })
    Message.success(status === 2 ? '审核通过' : '审核拒绝')
    search()
  } catch (error) {
    Message.error('审核失败')
  }
}

// 重置
const reset = () => {
  queryForm.postId = undefined
  queryForm.planetId = undefined
  queryForm.userId = undefined
  queryForm.content = undefined
  queryForm.auditStatus = undefined
  queryForm.isAnonymous = undefined
  search()
}

// 删除
const onDelete = (item: PlanetCommentResp) => {
  return handleDelete(() => deletePlanetComment(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportPlanetComment(queryForm))
}

const PlanetCommentAddModalRef = ref<InstanceType<typeof PlanetCommentAddModal>>()
// 新增
const onAdd = () => {
  PlanetCommentAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: PlanetCommentResp) => {
  PlanetCommentAddModalRef.value?.onUpdate(item.id)
}

const PlanetCommentDetailDrawerRef = ref<InstanceType<typeof PlanetCommentDetailDrawer>>()
// 详情
const onDetail = (item: PlanetCommentResp) => {
  PlanetCommentDetailDrawerRef.value?.onDetail(item.id)
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
.comment-content {
  .comment-meta {
    margin-top: 4px;
  }
}

.comment-level {
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  
  .user-id {
    font-weight: 500;
  }
}

:deep(.ant-statistic-content-value) {
  font-weight: bold !important;
}
</style>
