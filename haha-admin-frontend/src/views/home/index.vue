<template>
  <div id="home" class="gi_page home">
    <WorkCard />

    <!-- 第一行：访问趋势和快捷操作 -->
    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="18" :xxl="18">
        <div class="home__item"><AccessTrendCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="6" :xxl="6">
        <div class="home__item"><FastCard /></div>
      </a-col>
    </a-row>

    <!-- 第二行：地域分布和热门模块 -->
    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" :xxl="12">
        <div class="home__item"><GeoDistributionCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="12" :xxl="12">
        <div class="home__item"><PopularModuleCard /></div>
      </a-col>
    </a-row>

    <!-- 第三行：客户端统计、24小时活跃分布和系统性能 -->
    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8" :xxl="8">
        <div class="home__item"><ClientStatsCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8" :xxl="8">
        <div class="home__item"><HourlyActivityCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="24" :lg="8" :xl="8" :xxl="8">
        <div class="home__item"><PerformanceCard /></div>
      </a-col>
    </a-row>

    <!-- 第四行：最近活跃用户 -->
    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" :xxl="24">
        <div class="home__item"><RecentUsersCard /></div>
      </a-col>
    </a-row>

    <a-back-top :visible-height="100" target-container="#home" />
  </div>
</template>

<script setup lang="ts">
import WorkCard from './components/WorkCard.vue'
import AccessTrendCard from './components/AccessTrendCard.vue'
import FastCard from './components/FastCard.vue'
import GeoDistributionCard from './components/GeoDistributionCard.vue'
import PopularModuleCard from './components/PopularModuleCard.vue'
import ClientStatsCard from './components/ClientStatsCard.vue'
import HourlyActivityCard from './components/HourlyActivityCard.vue'
import PerformanceCard from './components/PerformanceCard.vue'
import RecentUsersCard from './components/RecentUsersCard.vue'

defineOptions({ name: 'Home' })
</script>

<style lang="scss" scoped>
.home {
  padding: 0;
  span.link {
    cursor: pointer;
    color: rgb(var(--arcoblue-6));
    font-weight: bold;
    &:hover {
      opacity: 0.7;
    }
  }
  &__content {
    padding: 6px;
  }
  &__item {
    padding: 6px;
    box-sizing: border-box;
  }
  .backtop-icon {
    cursor: pointer;
  }
}
</style>
