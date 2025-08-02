<template>
  <a-card title="工作台" :bordered="false" class="card">
    <template #extra>
      <NowTime />
    </template>
    <a-row align="center" wrap :gutter="[{ xs: 0, sm: 14, md: 14, lg: 14, xl: 14, xxl: 14 }, 16]" class="content">
      <a-col :xs="24" :sm="24" :md="14" :lg="16" :xl="16" :xxl="18">
        <a-space size="medium">
          <a-avatar :size="68">
            <img :src="userStore.avatar" alt="avatar" />
          </a-avatar>
          <div class="welcome">
            <p class="hello">{{ goodTimeText() }}！{{ userStore.name }}</p>
            <p>今日数据概览</p>
          </div>
        </a-space>
      </a-col>
      <a-col :xs="24" :sm="24" :md="10" :lg="8" :xl="8" :xxl="6" style="margin: -8px -7px">
        <a-row justify="end">
        </a-row>
      </a-col>
    </a-row>
    
    <!-- 统计数据展示 -->
    <a-spin :loading="loading" style="width: 100%">
      <a-row :gutter="16" class="statistics">
        <a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" :xxl="6">
          <a-statistic
            title="总浏览量"
            :value="totalData.pvCount"
            :value-style="{ color: '#3f8600' }"
          >
            <template #prefix>
              <icon-eye />
            </template>
          </a-statistic>
        </a-col>
        <a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" :xxl="6">
          <a-statistic
            title="访客数量"
            :value="totalData.ipCount"
            :value-style="{ color: '#cf1322' }"
          >
            <template #prefix>
              <icon-user />
            </template>
          </a-statistic>
        </a-col>
        <a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" :xxl="6">
          <a-statistic
            title="今日浏览量"
            :value="totalData.todayPvCount"
            :value-style="{ color: '#1890ff' }"
          >
            <template #prefix>
              <icon-calendar />
            </template>
          </a-statistic>
        </a-col>
        <a-col :xs="12" :sm="12" :md="6" :lg="6" :xl="6" :xxl="6">
          <a-statistic
            title="较昨日增长"
            :value="totalData.newPvFromYesterday"
            :value-style="{ color: '#722ed1' }"
            :precision="1"
            suffix="%"
          >
            <template #prefix>
              <icon-arrow-up />
            </template>
          </a-statistic>
        </a-col>
      </a-row>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { IconEye, IconUser, IconCalendar, IconArrowUp } from '@arco-design/web-vue/es/icon'
import NowTime from './NowTime/index.vue'
import { useDevice } from '@/hooks'
import { useUserStore } from '@/stores'
import { goodTimeText } from '@/utils'
import { getDashboardTotal } from '@/apis'
import type { DashboardTotalResp } from '@/apis/common/type'

defineOptions({ name: 'WorkCard' })

const { isDesktop } = useDevice()
const userStore = useUserStore()

const loading = ref(false)
const totalData = ref<DashboardTotalResp>({
  pvCount: 0,
  ipCount: 0,
  todayPvCount: 0,
  newPvFromYesterday: 0
})

// 获取总计数据
const getTotalData = async () => {
  try {
    loading.value = true
    const { data } = await getDashboardTotal()
    if (data && typeof data === 'object') {
      totalData.value = {
        pvCount: data.pvCount || 0,
        ipCount: data.ipCount || 0,
        todayPvCount: data.todayPvCount || 0,
        newPvFromYesterday: data.newPvFromYesterday || 0
      }
    }
  } catch (error) {
    console.error('获取总计数据失败:', error)
    // 保持默认值
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getTotalData()
})
</script>

<style lang="scss" scoped>
:deep(.arco-statistic-title) {
  margin-bottom: 0;
  font-size: 14px;
  color: var(--color-text-2);
}

:deep(.arco-statistic-value) {
  font-size: 24px;
  font-weight: 600;
}

.card {
  // 设置固定高度与AccessTrendCard保持一致
  :deep(.arco-card) {
    height: 410px;
    display: flex;
    flex-direction: column;
  }
  
  :deep(.arco-card-body) {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .content {
    padding: 8px 20px;
    .welcome {
      margin: 8px 0;
      color: $color-text-3;
      .hello {
        font-size: 1.25rem;
        color: $color-text-1;
        margin-bottom: 10px;
      }
    }
  }
  
  .statistics {
    flex: 1;
    display: flex;
    align-items: center;
    margin-top: 24px;
    padding: 0 20px 20px;
    
    .arco-row {
      width: 100%;
    }
    
    .arco-col {
      text-align: center;
      padding: 16px 0;
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &:hover {
        background-color: var(--color-fill-2);
        transform: translateY(-2px);
      }
    }
  }
}
</style>
