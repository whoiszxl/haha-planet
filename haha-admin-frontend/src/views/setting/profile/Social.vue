<template>
  <a-card title="第三方账号" bordered class="gradient-card">
    <div class="empty-state">
      <div class="empty-icon">
        <icon-exclamation-circle :size="48" class="text-gray-400" />
      </div>
      <div class="empty-text">
        <p class="text-lg font-medium text-gray-600 mb-2">暂无第三方账号配置</p>
        <p class="text-sm text-gray-400">当前系统未配置第三方登录服务</p>
      </div>
    </div>
  </a-card>
</template>

<script setup lang="ts">
// 已移除所有第三方登录相关功能
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.empty-icon {
  margin-bottom: 16px;
}

.empty-text {
  max-width: 300px;
}
</style>
