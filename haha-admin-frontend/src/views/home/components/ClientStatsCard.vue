<template>
  <a-card class="general-card" :header-style="{ paddingBottom: '0' }" :body-style="{ padding: '17px 20px 21px 20px' }">
    <template #title>
      客户端分布
      <a-radio-group v-model="viewType" type="button" size="small" style="margin-left: 16px;">
        <a-radio value="browsers">浏览器</a-radio>
        <a-radio value="os">操作系统</a-radio>
      </a-radio-group>
    </template>
    <a-spin :loading="loading" style="width: 100%">
      <VChart
        v-if="!loading && currentData.length > 0"
        :option="option"
        autoresize
        :style="{ height: '200px', minHeight: '200px' }"
      ></VChart>
      <div v-else-if="!loading && currentData.length === 0" class="no-data">
        <a-empty description="暂无数据" />
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, ref } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart } from 'echarts/charts'
import { LegendComponent, TooltipComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { getDashboardClientStats } from '@/apis'
import type { DashboardClientStatsResp } from '@/apis'
import { useChart } from '@/hooks'

defineOptions({ name: 'ClientStatsCard' })

use([CanvasRenderer, PieChart, TooltipComponent, LegendComponent])

const loading = ref(false)
const viewType = ref<'browsers' | 'os'>('browsers')
const clientData = ref<DashboardClientStatsResp>({
  browsers: [],
  operatingSystems: []
})

const currentData = computed(() => {
  if (!clientData.value) return []
  return viewType.value === 'browsers'
    ? clientData.value.browsers
    : clientData.value.operatingSystems
})

// 获取颜色
const getColor = (index: number) => {
  const colors = [
    '#5470c6',
    '#91cc75',
    '#fac858',
    '#ee6666',
    '#73c0de',
    '#3ba272',
    '#fc8452',
    '#9a60b4',
    '#ea7ccc',
    '#ffb347'
  ]
  return colors[index % colors.length]
}

const { option } = useChart((_isDark) => {
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: '0',
      left: 'center',
      itemWidth: 10,
      itemHeight: 10,
      textStyle: {
        fontSize: 10
      }
    },
    series: [
      {
        name: viewType.value === 'browsers' ? '浏览器分布' : '操作系统分布',
        type: 'pie',
        radius: ['30%', '70%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        label: {
          show: false
        },
        labelLine: {
          show: false
        },
        data: (currentData.value || []).map((item, index) => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: getColor(index)
          }
        }))
      }
    ]
  }
})

// 获取客户端统计数据
const getClientData = async () => {
  try {
    loading.value = true
    const { data } = await getDashboardClientStats()
    if (data && typeof data === 'object') {
      clientData.value = {
        browsers: Array.isArray(data.browsers)
          ? data.browsers.map((item) => ({
              name: item.name || '未知浏览器',
              value: item.value || 0
            }))
          : [],
        operatingSystems: Array.isArray(data.operatingSystems)
          ? data.operatingSystems.map((item) => ({
              name: item.name || '未知系统',
              value: item.value || 0
            }))
          : []
      }
    } else {
      clientData.value = { browsers: [], operatingSystems: [] }
    }
  } catch (error) {
    console.error('获取客户端统计数据失败:', error)
    clientData.value = { browsers: [], operatingSystems: [] }
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await nextTick()
  getClientData()
})
</script>

<style lang="scss" scoped>
.general-card {
  min-height: 280px;
}
</style>
