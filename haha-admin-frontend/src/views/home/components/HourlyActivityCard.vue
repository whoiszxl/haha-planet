<template>
  <a-card class="general-card" :header-style="{ paddingBottom: '0' }" :body-style="{ padding: '17px 20px 21px 20px' }">
    <template #title>
      24小时活跃分布
    </template>
    <a-spin :loading="loading" style="width: 100%">
      <VChart
        v-if="!loading && activityData.length > 0"
        :option="option"
        autoresize
        :style="{ height: '200px', minHeight: '200px' }"
      ></VChart>
      <div
        v-else
        :style="{ height: '200px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#999' }"
      >
        暂无数据
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { listDashboardHourlyActivity } from '@/apis'
import type { DashboardHourlyActivityResp } from '@/apis'
import { useChart } from '@/hooks'

defineOptions({ name: 'HourlyActivityCard' })

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent])

const loading = ref(false)
const activityData = ref<DashboardHourlyActivityResp[]>([])

const { option } = useChart((_isDark) => {
  return {
    grid: {
      left: '40',
      right: '20',
      top: '20',
      bottom: '40'
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}时: {c}次访问'
    },
    xAxis: {
      type: 'category',
      data: Array.from({ length: 24 }, (_, i) => `${i}:00`),
      axisLabel: {
        interval: 3,
        fontSize: 10
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        fontSize: 10
      }
    },
    series: [
      {
        type: 'bar',
        data: Array.from({ length: 24 }, (_item, hour) => {
          const found = activityData.value.find(item => item.hour === hour)
          return found ? found.count : 0
        }),
        itemStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: '#165DFF' },
              { offset: 1, color: '#722ED1' }
            ]
          }
        },
        barWidth: '60%'
      }
    ]
  }
})

// 获取活跃时段数据
const getActivityData = async () => {
  try {
    loading.value = true
    const { data } = await listDashboardHourlyActivity()
    if (Array.isArray(data)) {
      activityData.value = data.map((item) => ({
        hour: item.hour || 0,
        count: item.count || 0
      }))
    } else {
      activityData.value = []
    }
  } catch (error) {
    console.error('获取活跃时段数据失败:', error)
    activityData.value = []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await nextTick()
  getActivityData()
})
</script>

<style lang="scss" scoped>
.general-card {
  min-height: 280px;
}
</style>
