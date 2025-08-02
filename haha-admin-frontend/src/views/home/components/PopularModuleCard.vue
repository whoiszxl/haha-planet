<template>
  <a-spin :loading="loading" style="width: 100%">
    <a-card title="热门模块" :bordered="false" class="gi_card_title">
      <VCharts
        v-if="!loading && moduleData.length > 0"
        :option="option"
        autoresize
        :style="{ height: '350px', minHeight: '350px' }"
      ></VCharts>
      <div
        v-else-if="!loading"
        :style="{ height: '350px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#999' }"
      >
        暂无数据
      </div>
    </a-card>
  </a-spin>
</template>

<script lang="ts" setup>
import { nextTick, onMounted, ref } from 'vue'
import VCharts from 'vue-echarts'
import { listDashboardPopularModule } from '@/apis'
import type { DashboardPopularModuleResp } from '@/apis/common/type'
import { useChart } from '@/hooks'

defineOptions({ name: 'PopularModuleCard' })

const loading = ref(false)
const moduleData = ref<DashboardPopularModuleResp[]>([])

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

const { option } = useChart((isDark) => {
  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      textStyle: {
        color: isDark ? '#ffffff' : '#4E5969'
      }
    },
    series: [
      {
        name: '访问量',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: isDark ? '#1a1a1a' : '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold',
            color: isDark ? '#ffffff' : '#333333'
          }
        },
        labelLine: {
          show: false
        },
        data: (moduleData.value || []).map((item, index) => ({
          value: item.pvCount,
          name: item.module,
          itemStyle: {
            color: getColor(index)
          }
        }))
      }
    ]
  }
})

// 获取热门模块数据
const getModuleData = async () => {
  try {
    loading.value = true
    const { data } = await listDashboardPopularModule()
    if (Array.isArray(data)) {
      moduleData.value = data.map((item) => ({
        module: item.module || '未知模块',
        pvCount: item.pvCount || 0,
        newPvFromYesterday: item.newPvFromYesterday || 0
      }))
    } else {
      moduleData.value = []
    }
  } catch (error) {
    console.error('获取热门模块数据失败:', error)
    moduleData.value = []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await nextTick()
  getModuleData()
})
</script>

<style lang="scss" scoped>
.gi_card_title {
  :deep(.arco-card-header) {
    border-bottom: 1px solid var(--color-border-2);
  }
}
</style>
