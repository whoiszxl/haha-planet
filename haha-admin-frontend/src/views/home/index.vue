<template>
  <div id="home" class="gi_page home">
    <a-row class="home__content">
      <a-col :span="24">
        <div class="home__item home__item--compact"><WorkCard /></div>
      </a-col>
    </a-row>

        
    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" :xxl="12">
        <div class="home__item"><ClientStatsCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" :xxl="12">
        <div class="home__item"><HourlyActivityCard /></div>
      </a-col>
    </a-row>

    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" :xxl="12">
        <div class="home__item"><GeoDistributionCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" :xxl="12">
        <div class="home__item"><PopularModuleCard /></div>
      </a-col>
    </a-row>

    <a-row class="home__content">
      <a-col :span="24">
        <div class="home__item"><AccessTrendCard /></div>
      </a-col>
    </a-row>







    <a-row class="home__content">
      <a-col :span="12">
        <div class="home__item"><RecentUsersCard /></div>
      </a-col>

      <a-col :span="12">
        <div class="home__item"><PerformanceCard /></div>
      </a-col>
    </a-row>

    <a-back-top :visible-height="100" target-container="#home" />
  </div>
</template>

<script setup lang="ts">
import WorkCard from './components/WorkCard.vue'
import AccessTrendCard from './components/AccessTrendCard.vue'
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
    padding: 4px;
    margin-bottom: 8px;
  }
  &__item {
    padding: 6px;
    box-sizing: border-box;
    
    &--compact {
      padding: 4px;
      
      :deep(.arco-card) {
        margin-bottom: 0;
      }
      
      :deep(.arco-card-body) {
        padding: 16px 20px;
      }
    }
  }
  .backtop-icon {
    cursor: pointer;
  }
}
</style>
