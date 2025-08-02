<template>
  <a-card class="general-card" :header-style="{ paddingBottom: '0' }" :body-style="{ padding: '17px 20px 21px 20px' }">
    <template #title>
      系统性能指标
    </template>
    <a-spin :loading="loading" style="width: 100%">
      <div class="performance-stats">
        <div class="stat-item">
          <div class="stat-value">{{ performanceData.avgResponseTime }}ms</div>
          <div class="stat-label">平均响应时间</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ performanceData.slowRequestCount }}</div>
          <div class="stat-label">慢请求数量</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ (performanceData.errorRate * 100).toFixed(1) }}%</div>
          <div class="stat-label">错误率</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ performanceData.totalRequests }}</div>
          <div class="stat-label">总请求数</div>
        </div>
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getDashboardPerformance } from '@/apis'
import type { DashboardPerformanceResp } from '@/apis'

defineOptions({ name: 'PerformanceCard' })

const loading = ref(false)
const performanceData = ref<DashboardPerformanceResp>({
  avgResponseTime: 0,
  slowRequestCount: 0,
  errorRate: 0,
  totalRequests: 0
})

// 获取性能数据
const getPerformanceData = async () => {
  try {
    loading.value = true
    const { data } = await getDashboardPerformance()
    if (data && typeof data === 'object') {
      performanceData.value = {
        avgResponseTime: data.avgResponseTime || 0,
        slowRequestCount: data.slowRequestCount || 0,
        errorRate: data.errorRate || 0,
        totalRequests: data.totalRequests || 0
      }
    }
  } catch (error) {
    console.error('获取性能数据失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getPerformanceData()
})
</script>

<style lang="scss" scoped>
.performance-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 12px;
  background: var(--color-fill-2);
  border-radius: 6px;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-1);
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: var(--color-text-3);
}
</style>
