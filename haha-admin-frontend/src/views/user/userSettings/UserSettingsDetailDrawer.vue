<template>
  <a-drawer 
    v-model:visible="visible" 
    title="用户设置详情" 
    :width="width >= 680 ? 680 : '100%'" 
    :footer="false"
  >
    <div v-if="dataDetail" class="detail-container">
      <!-- 用户概览卡片 -->
      <a-card class="overview-card" :bordered="false">
        <div class="user-overview">
          <div class="user-info">
            <h3>用户 {{ dataDetail.userId }}</h3>
            <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'" size="large">
              {{ dataDetail.status === 1 ? '有效' : '无效' }}
            </a-tag>
          </div>
          <div class="privacy-level">
            <a-tag :color="getPrivacyLevelColor(dataDetail.privacyLevel)" size="large">
              {{ getPrivacyLevelText(dataDetail.privacyLevel) }}
            </a-tag>
          </div>
        </div>
      </a-card>

      <!-- 标签页内容 -->
      <a-tabs default-active-key="permissions" class="detail-tabs">
        <!-- 权限设置 -->
        <a-tab-pane key="permissions" title="权限设置">
          <a-card title="隐私与权限" :bordered="false">
            <a-row :gutter="[16, 16]">
              <a-col :span="8">
                <div class="permission-item">
                  <icon-search class="permission-icon" />
                  <div>
                    <div class="permission-title">允许被搜索</div>
                    <a-tag :color="dataDetail.allowSearch ? 'green' : 'red'">
                      {{ dataDetail.allowSearch ? '允许' : '不允许' }}
                    </a-tag>
                  </div>
                </div>
              </a-col>
              <a-col :span="8">
                <div class="permission-item">
                  <icon-user-add class="permission-icon" />
                  <div>
                    <div class="permission-title">好友申请</div>
                    <a-tag :color="dataDetail.allowFriendRequest ? 'green' : 'red'">
                      {{ dataDetail.allowFriendRequest ? '允许' : '不允许' }}
                    </a-tag>
                  </div>
                </div>
              </a-col>
              <a-col :span="8">
                <div class="permission-item">
                  <icon-message class="permission-icon" />
                  <div>
                    <div class="permission-title">私信功能</div>
                    <a-tag :color="dataDetail.allowMessage ? 'green' : 'red'">
                      {{ dataDetail.allowMessage ? '允许' : '不允许' }}
                    </a-tag>
                  </div>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </a-tab-pane>

        <!-- 通知设置 -->
        <a-tab-pane key="notifications" title="通知设置">
          <a-card title="通知偏好" :bordered="false">
            <a-row :gutter="[16, 16]">
              <a-col :span="8">
                <div class="notification-item">
                  <icon-email class="notification-icon" />
                  <div>
                    <div class="notification-title">邮件通知</div>
                    <a-switch 
                      :model-value="dataDetail.emailNotification" 
                      disabled 
                      checked-text="开启" 
                      unchecked-text="关闭"
                    />
                  </div>
                </div>
              </a-col>
              <a-col :span="8">
                <div class="notification-item">
                  <icon-phone class="notification-icon" />
                  <div>
                    <div class="notification-title">短信通知</div>
                    <a-switch 
                      :model-value="dataDetail.smsNotification" 
                      disabled 
                      checked-text="开启" 
                      unchecked-text="关闭"
                    />
                  </div>
                </div>
              </a-col>
              <a-col :span="8">
                <div class="notification-item">
                  <icon-notification class="notification-icon" />
                  <div>
                    <div class="notification-title">推送通知</div>
                    <a-switch 
                      :model-value="dataDetail.pushNotification" 
                      disabled 
                      checked-text="开启" 
                      unchecked-text="关闭"
                    />
                  </div>
                </div>
              </a-col>
            </a-row>
          </a-card>
        </a-tab-pane>

        <!-- 个性化设置 -->
        <a-tab-pane key="personalization" title="个性化">
          <a-card title="个性化偏好" :bordered="false">
            <a-descriptions :column="2" size="large">
              <a-descriptions-item label="主题设置">
                <a-tag color="blue">{{ dataDetail.theme || '默认' }}</a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="语言设置">
                <a-tag color="green">{{ getLanguageText(dataDetail.language) }}</a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="时区设置">
                <a-tag color="orange">{{ getTimezoneText(dataDetail.timezone) }}</a-tag>
              </a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-tab-pane>

        <!-- 系统信息 -->
        <a-tab-pane key="system" title="系统信息">
          <a-card title="系统记录" :bordered="false">
            <a-descriptions :column="2" size="large">
              <a-descriptions-item label="主键ID">{{ dataDetail.id }}</a-descriptions-item>
              <a-descriptions-item label="乐观锁">{{ dataDetail.version }}</a-descriptions-item>
              <a-descriptions-item label="逻辑删除">
                <a-tag :color="dataDetail.isDeleted ? 'red' : 'green'">
                  {{ dataDetail.isDeleted ? '已删除' : '未删除' }}
                </a-tag>
              </a-descriptions-item>
              <a-descriptions-item label="创建者">{{ dataDetail.createdBy }}</a-descriptions-item>
              <a-descriptions-item label="更新者">{{ dataDetail.updatedBy }}</a-descriptions-item>
              <a-descriptions-item label="创建时间">{{ dataDetail.createdAt }}</a-descriptions-item>
              <a-descriptions-item label="更新时间">{{ dataDetail.updatedAt }}</a-descriptions-item>
            </a-descriptions>
          </a-card>
        </a-tab-pane>
      </a-tabs>
    </div>

    <!-- 操作按钮 -->
    <template #footer>
      <a-space>
        <a-button @click="visible = false">关闭</a-button>
        <a-button type="primary" @click="onEdit">编辑设置</a-button>
      </a-space>
    </template>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type UserSettingsDetailResp, getUserSettings } from '@/apis'

const emit = defineEmits<{
  (e: 'edit', id: string): void
}>()

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<UserSettingsDetailResp>()

// 工具函数
const getPrivacyLevelText = (level: number) => {
  const map = { 1: '公开', 2: '好友可见', 3: '仅自己' }
  return map[level] || '未知'
}

const getPrivacyLevelColor = (level: number) => {
  const map = { 1: 'green', 2: 'orange', 3: 'red' }
  return map[level] || 'gray'
}

const getLanguageText = (language: string) => {
  const map = {
    'zh-CN': '中文',
    'en-US': 'English',
    'ja-JP': '日本語'
  }
  return map[language] || language || '中文'
}

const getTimezoneText = (timezone: string) => {
  const map = {
    'Asia/Shanghai': 'UTC+8 (北京时间)',
    'UTC': 'UTC+0 (格林威治时间)',
    'America/New_York': 'UTC-5 (纽约时间)'
  }
  return map[timezone] || timezone || 'UTC+8'
}

// 查询详情
const getDataDetail = async () => {
  const res = await getUserSettings(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

// 编辑设置
const onEdit = () => {
  emit('edit', dataId.value)
  visible.value = false
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.detail-container {
  .overview-card {
    margin-bottom: 16px;
    
    .user-overview {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .user-info {
        display: flex;
        align-items: center;
        gap: 12px;
        
        h3 {
          margin: 0;
          color: var(--color-text-1);
        }
      }
    }
  }
  
  .detail-tabs {
    .permission-item,
    .notification-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px;
      border: 1px solid var(--color-border-2);
      border-radius: 6px;
      
      .permission-icon,
      .notification-icon {
        font-size: 20px;
        color: var(--color-text-3);
      }
      
      .permission-title,
      .notification-title {
        font-weight: 500;
        margin-bottom: 4px;
        color: var(--color-text-1);
      }
    }
  }
}
</style>
