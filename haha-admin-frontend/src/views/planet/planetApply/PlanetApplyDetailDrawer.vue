<template>
  <a-drawer v-model:visible="visible" title="星球申请详情" :width="width >= 600 ? 600 : '100%'" :footer="false">
    <div class="apply-detail">
      <!-- 基本信息卡片 -->
      <a-card title="基本信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="large">
          <a-descriptions-item label="申请ID">
            <a-tag color="blue">{{ dataDetail?.id }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="星球ID">
            <a-tag color="purple">{{ dataDetail?.planetId }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="申请用户">
            <a-tag color="green">{{ dataDetail?.userId }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="申请状态">
            <a-tag :color="getStatusColor(dataDetail?.applyStatus)">
              <icon-check-circle v-if="dataDetail?.applyStatus === '2'" />
              <icon-clock-circle v-else-if="dataDetail?.applyStatus === '1'" />
              <icon-close-circle v-else />
              {{ getStatusText(dataDetail?.applyStatus) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="申请时间" :span="2">
            <icon-calendar />
            {{ formatDateTime(dataDetail?.createdAt) }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 申请内容卡片 -->
      <a-card title="申请内容" class="detail-card" :bordered="false">
        <a-descriptions :column="1" size="large">
          <a-descriptions-item label="申请理由">
            <div class="content-text">
              {{ dataDetail?.applyReason || '暂无' }}
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="问题答案">
            <div class="content-text">
              {{ dataDetail?.answer || '暂无' }}
            </div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 审核信息卡片 -->
      <a-card v-if="dataDetail?.applyStatus !== '1'" title="审核信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="large">
          <a-descriptions-item label="审核人">
            <a-tag v-if="dataDetail?.auditBy" color="orange">
              <icon-user />
              {{ dataDetail.auditBy }}
            </a-tag>
            <span v-else class="text-gray">暂无</span>
          </a-descriptions-item>
          <a-descriptions-item label="审核时间">
            <span v-if="dataDetail?.auditTime">
              <icon-calendar />
              {{ formatDateTime(dataDetail.auditTime) }}
            </span>
            <span v-else class="text-gray">暂无</span>
          </a-descriptions-item>
          <a-descriptions-item label="审核原因" :span="2">
            <div class="content-text">
              {{ dataDetail?.auditReason || '暂无' }}
            </div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="large">
          <a-descriptions-item label="创建时间">
            <icon-calendar />
            {{ formatDateTime(dataDetail?.createdAt) }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间">
            <icon-calendar />
            {{ formatDateTime(dataDetail?.updatedAt) }}
          </a-descriptions-item>
          <a-descriptions-item label="创建者">
            {{ dataDetail?.createdBy || '系统' }}
          </a-descriptions-item>
          <a-descriptions-item label="更新者">
            {{ dataDetail?.updatedBy || '系统' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 操作按钮 -->
      <div class="action-buttons" v-if="dataDetail?.applyStatus === '1'">
        <a-space>
          <a-button type="primary" status="success" @click="onAuditPass">
            <template #icon><icon-check /></template>
            通过申请
          </a-button>
          <a-button type="primary" status="danger" @click="onAuditReject">
            <template #icon><icon-close /></template>
            拒绝申请
          </a-button>
        </a-space>
      </div>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetApplyDetailResp, getPlanetApply } from '@/apis'
import { formatDateTime } from '@/utils'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetApplyDetailResp>()

// 状态相关方法
const getStatusColor = (status: string) => {
  const colorMap = {
    '1': 'orange',
    '2': 'green',
    '3': 'red'
  }
  return colorMap[status] || 'gray'
}

const getStatusText = (status: string) => {
  const textMap = {
    '1': '待审核',
    '2': '已通过', 
    '3': '已拒绝'
  }
  return textMap[status] || '未知'
}

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetApply(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

// 审核操作
const onAuditPass = () => {
  // 这里应该调用审核通过的API
  console.log('审核通过')
}

const onAuditReject = () => {
  // 这里应该调用审核拒绝的API
  console.log('审核拒绝')
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.apply-detail {
  .detail-card {
    margin-bottom: 16px;
    
    :deep(.arco-card-header) {
      border-bottom: 1px solid var(--color-border-2);
      padding: 16px 20px;
      
      .arco-card-header-title {
        font-weight: 600;
        font-size: 16px;
      }
    }
    
    :deep(.arco-card-body) {
      padding: 20px;
    }
  }
  
  .content-text {
    line-height: 1.6;
    padding: 8px 12px;
    background-color: var(--color-fill-1);
    border-radius: 4px;
    border-left: 3px solid var(--color-primary-light-4);
    white-space: pre-wrap;
    word-break: break-word;
  }
  
  .text-gray {
    color: var(--color-text-3);
  }
  
  .action-buttons {
    margin-top: 24px;
    padding: 16px;
    background-color: var(--color-fill-1);
    border-radius: 6px;
    text-align: center;
  }
}

:deep(.arco-descriptions-item-label) {
  font-weight: 500;
  color: var(--color-text-2);
}

:deep(.arco-descriptions-item-value) {
  color: var(--color-text-1);
}
</style>
