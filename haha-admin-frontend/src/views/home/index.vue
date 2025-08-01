<template>
  <div id="home" class="gi_page home">
    <WorkCard />


    <a-row class="home__content">
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="18" :xxl="18">
        <div class="home__item"><AccessTrendCard /></div>
      </a-col>
      <a-col :xs="24" :sm="24" :md="24" :lg="12" :xl="6" :xxl="6">
        <div class="home__item"><FastCard /></div>
      </a-col>
    </a-row>

    <a-back-top :visible-height="100" target-container="#home" />
  </div>
</template>

<script setup lang="ts">
import WorkCard from './components/WorkCard.vue'
import AccessTrendCard from './components/AccessTrendCard.vue'
import FastCard from './components/FastCard.vue'

defineOptions({ name: 'Home' })

const open = (url: string) => {
  window.open(url, '_blank')
}
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
