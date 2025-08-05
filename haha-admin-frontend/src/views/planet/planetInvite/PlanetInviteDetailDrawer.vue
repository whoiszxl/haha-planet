<template>
  <a-drawer v-model:visible="visible" title="星球邀请详情" :width="width >= 680 ? 680 : '100%'" :footer="false">
    <div v-if="dataDetail" class="space-y-6">
      <!-- 基本信息卡片 -->
      <a-card title="基本信息" :bordered="false" class="shadow-sm">
        <div class="grid grid-cols-2 gap-4">
          <div class="space-y-3">
            <div class="flex items-center space-x-2">
              <span class="text-gray-600 w-20">邀请码:</span>
              <a-tag color="blue" class="font-mono">{{ dataDetail.inviteCode }}</a-tag>
            </div>
            <div class="flex items-center space-x-2">
              <span class="text-gray-600 w-20">星球ID:</span>
              <span class="font-medium">{{ dataDetail.planetId }}</span>
            </div>
            <div class="flex items-center space-x-2">
              <span class="text-gray-600 w-20">邀请人:</span>
              <span class="font-medium">{{ dataDetail.inviterId }}</span>
            </div>
          </div>
          <div class="space-y-3">
            <div class="flex items-center space-x-2">
              <span class="text-gray-600 w-20">邀请类型:</span>
              <a-tag :color="getInviteTypeColor(dataDetail.inviteType)">
                {{ getInviteTypeText(dataDetail.inviteType) }}
              </a-tag>
            </div>
            <div class="flex items-center space-x-2">
              <span class="text-gray-600 w-20">当前状态:</span>
              <a-tag :color="getInviteStatusColor(dataDetail.inviteStatus)">
                {{ getInviteStatusText(dataDetail.inviteStatus) }}
              </a-tag>
            </div>
            <div v-if="dataDetail.inviteeId" class="flex items-center space-x-2">
              <span class="text-gray-600 w-20">被邀请人:</span>
              <span class="font-medium">{{ dataDetail.inviteeId }}</span>
            </div>
          </div>
        </div>
        
        <div v-if="dataDetail.inviteMessage" class="mt-4">
          <div class="text-gray-600 mb-2">邀请消息:</div>
          <div class="bg-gray-50 p-3 rounded-md text-sm">
            {{ dataDetail.inviteMessage }}
          </div>
        </div>
      </a-card>

      <!-- 使用情况卡片 -->
      <a-card title="使用情况" :bordered="false" class="shadow-sm">
        <div class="grid grid-cols-2 gap-6">
          <div class="text-center">
            <div class="text-2xl font-bold text-blue-600 mb-1">{{ dataDetail.usedCount }}</div>
            <div class="text-gray-600 text-sm">已使用次数</div>
          </div>
          <div class="text-center">
            <div class="text-2xl font-bold text-green-600 mb-1">{{ dataDetail.maxUseCount }}</div>
            <div class="text-gray-600 text-sm">最大使用次数</div>
          </div>
        </div>
        
        <div class="mt-4">
          <div class="flex justify-between items-center mb-2">
            <span class="text-gray-600">使用进度</span>
            <span class="text-sm text-gray-500">
              {{ getUsagePercent(dataDetail.usedCount, dataDetail.maxUseCount) }}%
            </span>
          </div>
          <a-progress 
            :percent="getUsagePercent(dataDetail.usedCount, dataDetail.maxUseCount)"
            :color="getUsageColor(dataDetail.usedCount, dataDetail.maxUseCount)"
            :stroke-width="8"
          />
        </div>
      </a-card>

      <!-- 时间信息卡片 -->
      <a-card title="时间信息" :bordered="false" class="shadow-sm">
        <div class="space-y-3">
          <div class="flex justify-between items-center">
            <span class="text-gray-600">过期时间:</span>
            <span :class="{ 'text-red-500 font-medium': isExpired(dataDetail.expireTime) }">
              {{ formatDateTime(dataDetail.expireTime) }}
              <a-tag v-if="isExpired(dataDetail.expireTime)" color="red" size="small" class="ml-2">
                已过期
              </a-tag>
            </span>
          </div>
          <div class="flex justify-between items-center">
            <span class="text-gray-600">创建时间:</span>
            <span>{{ formatDateTime(dataDetail.createdAt) }}</span>
          </div>
          <div v-if="dataDetail.updatedAt !== dataDetail.createdAt" class="flex justify-between items-center">
            <span class="text-gray-600">更新时间:</span>
            <span>{{ formatDateTime(dataDetail.updatedAt) }}</span>
          </div>
        </div>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" :bordered="false" class="shadow-sm">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="记录ID">{{ dataDetail.id }}</a-descriptions-item>
          <a-descriptions-item label="版本号">{{ dataDetail.version }}</a-descriptions-item>
          <a-descriptions-item label="创建者">{{ dataDetail.createdBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="更新者">{{ dataDetail.updatedBy || '-' }}</a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetInviteDetailResp, getPlanetInvite } from '@/apis'
import { formatDateTime } from '@/utils/format'  // 直接从 format.ts 导入
import dayjs from 'dayjs'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetInviteDetailResp>()

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetInvite(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

// 邀请类型相关
const getInviteTypeText = (type: number) => {
  const typeMap = { 1: '邀请码', 2: '链接邀请' }
  return typeMap[type] || '未知'
}

const getInviteTypeColor = (type: number) => {
  const colorMap = { 1: 'blue', 2: 'green' }
  return colorMap[type] || 'gray'
}

// 邀请状态相关
const getInviteStatusText = (status: number) => {
  const statusMap = { 1: '有效', 2: '已使用', 3: '已过期', 4: '已取消' }
  return statusMap[status] || '未知'
}

const getInviteStatusColor = (status: number) => {
  const colorMap = { 1: 'green', 2: 'blue', 3: 'red', 4: 'gray' }
  return colorMap[status] || 'gray'
}

// 使用进度相关
const getUsagePercent = (used: number, max: number) => {
  return max > 0 ? Math.round((used / max) * 100) : 0
}

const getUsageColor = (used: number, max: number) => {
  const percent = getUsagePercent(used, max)
  if (percent >= 90) return '#f53f3f'
  if (percent >= 70) return '#ff7d00'
  return '#00b42a'
}

// 是否过期
const isExpired = (expireTime: string) => {
  return dayjs(expireTime).isBefore(dayjs())
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.text-red-500 {
  color: #f53f3f;
}

.shadow-sm {
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
}

.space-y-6 > * + * {
  margin-top: 1.5rem;
}

.space-y-3 > * + * {
  margin-top: 0.75rem;
}

.space-x-2 > * + * {
  margin-left: 0.5rem;
}

.grid {
  display: grid;
}

.grid-cols-2 {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.gap-4 {
  gap: 1rem;
}

.gap-6 {
  gap: 1.5rem;
}

.text-center {
  text-align: center;
}

.font-mono {
  font-family: ui-monospace, SFMono-Regular, "SF Mono", Consolas, "Liberation Mono", Menlo, monospace;
}

.font-medium {
  font-weight: 500;
}

.font-bold {
  font-weight: 700;
}

.text-2xl {
  font-size: 1.5rem;
  line-height: 2rem;
}

.text-sm {
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.text-gray-600 {
  color: #4b5563;
}

.text-gray-500 {
  color: #6b7280;
}

.text-blue-600 {
  color: #2563eb;
}

.text-green-600 {
  color: #16a34a;
}

.bg-gray-50 {
  background-color: #f9fafb;
}

.p-3 {
  padding: 0.75rem;
}

.rounded-md {
  border-radius: 0.375rem;
}

.mb-1 {
  margin-bottom: 0.25rem;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.mt-4 {
  margin-top: 1rem;
}

.ml-2 {
  margin-left: 0.5rem;
}

.w-20 {
  width: 5rem;
}

.flex {
  display: flex;
}

.items-center {
  align-items: center;
}

.justify-between {
  justify-content: space-between;
}
</style>
