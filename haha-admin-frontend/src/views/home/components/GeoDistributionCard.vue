<template>
  <a-spin :loading="loading" style="width: 100%">
    <a-card title="访客地域分布" :bordered="false" class="gi_card_title">
      <template #extra>
        <a-radio-group v-model="viewType" type="button" size="small" @change="onViewChange">
          <a-radio value="provinces">省份</a-radio>
          <a-radio value="cities">城市</a-radio>
        </a-radio-group>
      </template>
      <VCharts
        v-if="!loading && currentData.length > 0"
        :option="option"
        autoresize
        :style="{ height: '380px', minHeight: '380px' }"
      ></VCharts>
      <div
        v-else-if="!loading"
        :style="{ height: '380px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#999' }"
      >
        暂无数据
      </div>
    </a-card>
  </a-spin>
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, ref } from 'vue'
import VCharts from 'vue-echarts'
import { getDashboardGeoDistribution } from '@/apis'
import type { DashboardGeoDistributionResp } from '@/apis/common/type'
import { useChart } from '@/hooks'

defineOptions({ name: 'GeoDistributionCard' })

// 内部数据结构类型
interface ProcessedGeoData {
  provinces: Array<{ name: string; value: number }>
  cities: Array<{ name: string; value: number }>
}

const loading = ref(false)
const viewType = ref<'provinces' | 'cities'>('provinces')
const geoData = ref<ProcessedGeoData>({ provinces: [], cities: [] })

const currentData = computed(() => {
  if (!geoData.value || !geoData.value.provinces || !geoData.value.cities) {
    return []
  }
  return viewType.value === 'provinces' ? geoData.value.provinces : geoData.value.cities
})

// 获取渐变颜色
const getGradientColor = (ratio: number, isStart: boolean) => {
  const colors = [
    { start: '#ff7875', end: '#ff4d4f' }, // 红色
    { start: '#ffa940', end: '#fa8c16' }, // 橙色
    { start: '#fadb14', end: '#faad14' }, // 黄色
    { start: '#73d13d', end: '#52c41a' }, // 绿色
    { start: '#40a9ff', end: '#1890ff' } // 蓝色
  ]

  const colorIndex = Math.min(Math.floor(ratio * colors.length), colors.length - 1)
  const color = colors[colorIndex]

  return isStart ? color.start : color.end
}

const { option } = useChart((isDark) => {
  const data = currentData.value || []
  const maxValue = data.length > 0 ? Math.max(...data.map(item => item.value)) : 1
  
  return {
    title: {
      text: viewType.value === 'provinces' ? '省份访问量分布' : '城市访问量分布',
      left: 'center',
      textStyle: {
        color: isDark ? '#ffffff' : '#333333',
        fontSize: 16
      }
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      },
      formatter: (params: any) => {
        const data = params[0]
        return `${data.name}: ${data.value} 次访问`
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name),
      axisLabel: {
        color: isDark ? '#ffffff' : '#4E5969',
        interval: 0,
        rotate: data.length > 10 ? 45 : 0
      },
      axisLine: {
        lineStyle: {
          color: isDark ? '#3F3F3F' : '#E5E8EF'
        }
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        color: isDark ? '#ffffff' : '#4E5969',
        formatter: (value: number) => {
          if (value >= 1000) {
            return `${(value / 1000).toFixed(1)}k`
          }
          return value.toString()
        }
      },
      axisLine: {
        show: false
      },
      splitLine: {
        lineStyle: {
          color: isDark ? '#3F3F3F' : '#E5E8EF',
          type: 'dashed'
        }
      }
    },
    series: [
      {
        name: '访问量',
        type: 'bar',
        data: data.map((item) => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: getGradientColor(item.value / maxValue, true)
                },
                {
                  offset: 1,
                  color: getGradientColor(item.value / maxValue, false)
                }
              ]
            },
            borderRadius: [4, 4, 0, 0]
          }
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
})

// 获取地域分布数据
const getGeoData = async () => {
  try {
    loading.value = true
    const { data } = await getDashboardGeoDistribution()
    if (data && typeof data === 'object') {
      // 处理实际 API 返回的数据格式
      const locationStats = Array.isArray(data.locationIpStatistics) ? data.locationIpStatistics : []
      
      // 按省份和城市分组数据
      const provinces: Array<{ name: string, value: number }> = []
      const cities: Array<{ name: string, value: number }> = []

      locationStats.forEach((item) => {
        if (item.name && typeof item.value === 'number') {
          // 如果是内网IP或者不包含|分隔符，归类为其他
          if (item.name === '内网IP' || !item.name.includes('|')) {
            cities.push({ name: item.name, value: item.value })
          } else {
            // 解析地域信息：中国|广东|广州市|移动
            const parts = item.name.split('|')
            if (parts.length >= 3) {
              const province = parts[1] // 省份
              const city = parts[2] // 城市

              // 添加到省份统计
              const existingProvince = provinces.find((p) => p.name === province)
              if (existingProvince) {
                existingProvince.value += item.value
              } else {
                provinces.push({ name: province, value: item.value })
              }

              // 添加到城市统计
              cities.push({ name: city, value: item.value })
            }
          }
        }
      })
      
      geoData.value = { provinces, cities }
    } else {
      geoData.value = { provinces: [], cities: [] }
    }
  } catch (error) {
    console.error('获取地域分布数据失败:', error)
    geoData.value = { provinces: [], cities: [] }
  } finally {
    loading.value = false
  }
}

// 切换视图类型
const onViewChange = (type: 'provinces' | 'cities') => {
  viewType.value = type
}

onMounted(async () => {
  await nextTick()
  getGeoData()
})
</script>

<style lang="scss" scoped>
.gi_card_title {
  :deep(.arco-card-header) {
    border-bottom: 1px solid var(--color-border-2);
  }
}
</style>
