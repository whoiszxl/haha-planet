<template>
  <a-card class="general-card" :header-style="{ paddingBottom: '0' }" :body-style="{ padding: '17px 20px 21px 20px' }">
    <template #title>
      最近活跃用户
    </template>
    <a-spin :loading="loading" style="width: 100%">
      <div class="users-list">
        <div v-if="usersData.length === 0" class="empty-state">
          暂无活跃用户数据
        </div>
        <div v-else class="user-item" v-for="user in usersData" :key="user.nickname">
          <div class="user-info">
            <div class="user-name">{{ user.nickname }}</div>
            <div class="user-details">
              <span class="access-count">{{ user.accessCount }}次访问</span>
              <span class="ip-address">{{ user.ipAddress }}</span>
            </div>
          </div>
          <div class="last-access">
            {{ formatTime(user.lastAccessTime) }}
          </div>
        </div>
      </div>
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { listDashboardRecentUsers } from '@/apis'
import type { DashboardRecentUsersResp } from '@/apis'

defineOptions({ name: 'RecentUsersCard' })

const loading = ref(false)
const usersData = ref<DashboardRecentUsersResp[]>([])

// 格式化时间
const formatTime = (timeStr: string) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  
  if (diff < 60000) { // 1分钟内
    return '刚刚'
  } else if (diff < 3600000) { // 1小时内
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) { // 24小时内
    return `${Math.floor(diff / 3600000)}小时前`
  } else {
    return date.toLocaleDateString()
  }
}

// 获取最近活跃用户数据
const getUsersData = async () => {
  try {
    loading.value = true
    const { data } = await listDashboardRecentUsers()
    if (Array.isArray(data)) {
      usersData.value = data.map(item => ({
        nickname: item.nickname || '未知用户',
        lastAccessTime: item.lastAccessTime || '',
        accessCount: item.accessCount || 0,
        ipAddress: item.ipAddress || '未知IP'
      }))
    } else {
      usersData.value = []
    }
  } catch (error) {
    console.error('获取最近活跃用户数据失败:', error)
    usersData.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getUsersData()
})
</script>

<style lang="scss" scoped>
.users-list {
  max-height: 200px;
  overflow-y: auto;
}

.empty-state {
  text-align: center;
  color: var(--color-text-3);
  padding: 40px 0;
  font-size: 14px;
}

.user-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border-2);
  
  &:last-child {
    border-bottom: none;
  }
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-1);
  margin-bottom: 4px;
}

.user-details {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: var(--color-text-3);
}

.access-count {
  color: var(--color-primary-6);
}

.ip-address {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.last-access {
  font-size: 12px;
  color: var(--color-text-3);
  white-space: nowrap;
}
</style>
