<template>
  <a-dropdown trigger="click" position="bottom" @select="handleThemeSelect">
    <a-button size="mini" class="gi_hover_btn theme-btn">
      <template #icon>
        <component :is="currentThemeIcon" :size="18" />
      </template>
      <span class="theme-text">{{ currentThemeName }}</span>
    </a-button>
    <template #content>
      <a-doption 
        v-for="theme in themeOptions" 
        :key="theme.value" 
        :value="theme.value"
        :class="{ 'active-theme': appStore.theme === theme.value }"
      >
        <template #icon>
          <component :is="theme.icon" :size="16" />
        </template>
        <span>{{ theme.label }}</span>
        <icon-check v-if="appStore.theme === theme.value" class="check-icon" />
      </a-doption>
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAppStore } from '@/stores'

defineOptions({ name: 'GiThemeBtn' })

const appStore = useAppStore()

// 主题配置选项
const themeOptions = [
  {
    value: 'light',
    label: '明亮主题',
    icon: 'icon-sun-fill',
    description: '经典明亮主题'
  },
  {
    value: 'dark',
    label: '暗黑主题', 
    icon: 'icon-moon-fill',
    description: '护眼暗黑主题'
  },
  {
    value: 'auto',
    label: '跟随系统',
    icon: 'icon-computer',
    description: '跟随系统主题设置'
  },
  {
    value: 'blue',
    label: '海洋蓝',
    icon: 'icon-palette',
    description: '清新海洋蓝主题'
  },
  {
    value: 'green',
    label: '森林绿',
    icon: 'icon-moon-fill',
    description: '自然森林绿主题'
  }
]

// 当前主题图标
const currentThemeIcon = computed(() => {
  const theme = themeOptions.find(t => t.value === appStore.theme)
  return theme?.icon || 'icon-sun-fill'
})

// 当前主题名称
const currentThemeName = computed(() => {
  const theme = themeOptions.find(t => t.value === appStore.theme)
  return theme?.label || '明亮主题'
})

// 处理主题选择
const handleThemeSelect = (value: string) => {
  appStore.setTheme(value)
}
</script>

<style lang="scss" scoped>
.theme-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 80px;
  
  .theme-text {
    font-size: 12px;
    white-space: nowrap;
  }
}

:deep(.arco-dropdown-option) {
  &.active-theme {
    background-color: var(--color-primary-light-1);
    color: var(--color-primary-6);
  }
  
  .check-icon {
    margin-left: auto;
    color: var(--color-primary-6);
  }
}
</style>
