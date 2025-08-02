<template>
  <a-avatar 
    :size="size" 
    :shape="shape"
    :style="{ opacity: isLoading ? 0.6 : 1, ...customStyle }"
  >
    <img v-if="displayUrl" :src="displayUrl" :alt="alt" />
    <span v-else-if="fallbackText">{{ fallbackText }}</span>
    <a-spin v-if="isLoading" :size="Math.min(size / 4, 20)" style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%)" />
  </a-avatar>
</template>

<script setup lang="ts">
import { avatarUrlCache } from '@/utils/avatar-cache'

interface Props {
  avatarKey?: string
  size?: number
  shape?: 'circle' | 'square'
  alt?: string
  fallbackText?: string
  customStyle?: Record<string, any>
}

const props = withDefaults(defineProps<Props>(), {
  size: 40,
  shape: 'circle',
  alt: 'avatar',
  fallbackText: '',
  customStyle: () => ({})
})

const displayUrl = ref('')
const isLoading = ref(false)

const loadAvatar = async () => {
  if (!props.avatarKey) {
    displayUrl.value = ''
    return
  }

  isLoading.value = true
  
  try {
    const url = await avatarUrlCache.getPresignedUrl(props.avatarKey)
    displayUrl.value = url || ''
  } catch (error) {
    console.error('加载头像失败:', error)
    displayUrl.value = ''
  } finally {
    isLoading.value = false
  }
}

watch(
  () => props.avatarKey,
  () => {
    loadAvatar()
  },
  { immediate: true }
)

onMounted(() => {
  loadAvatar()
})
</script>