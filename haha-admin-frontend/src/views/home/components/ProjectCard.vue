<template>
  <a-card title="项目" :bordered="false" size="medium" class="gi_card_title" style="overflow: hidden">
    <a-row align="stretch">
      <a-col v-for="(item, index) in list" :key="item.name" :xs="12" :sm="8" :md="8">
        <a-card-grid class="w-full h-full">
          <a-card :bordered="false" hoverable :class="`animated-fade-up-${index}`">
            <a :href="item.url" target="_blank">
              <section class="item">
                <div class="item__header">
                  <img :src="item.logo" width="30px" alt="logo" />
                  <span class="item__name gi_line_1">{{ item.name }}</span>
                </div>
                <div class="item__middle">
                  <p class="item__desc gi_line_2">{{ item.desc }}</p>
                </div>
              </section>
            </a>
          </a-card>
        </a-card-grid>
      </a-col>
    </a-row>
  </a-card>
</template>

<script setup lang="ts">
const list = [
  {
    name: 'haha Admin',
    desc: '游戏平台后台管理系统',
    logo: 'https://haha.com/logo.svg',
    url: 'https://haha.com'
  },
]
</script>

<style lang="scss" scoped>
:deep(.arco-card) {
  height: 100%;
  .arco-card-body {
    height: 100%;
    box-sizing: border-box;
  }
}

:deep(.arco-card-header) {
  border: none;
}

.item {
  height: 100%;
  display: flex;
  flex-direction: column;
  &__header {
    display: flex;
    align-items: center;
  }
  &__name {
    margin-left: 10px;
    font-size: 1.125rem;
    line-height: 1.75rem;
    color: var(--color-text-1);
    &:hover {
      color: rgb(var(--arcoblue-6));
    }
  }
  &__middle {
    flex: 1;
  }
  &__desc {
    flex: 1;
    margin-top: 10px;
    line-height: 1.5;
    color: var(--color-text-2);
  }
  &__footer {
    font-size: 12px;
    color: var(--color-text-3);
    margin-top: 8px;
  }
}
</style>
