<template>
  <a-drawer v-model:visible="visible" title="星球设置详情" :width="width >= 680 ? 680 : '100%'" :footer="false">
    <div class="setting-detail">
      <!-- 基础信息卡片 -->
      <a-card title="基础信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="medium">
          <a-descriptions-item label="星球ID">
            <a-tag color="blue">{{ dataDetail?.planetId }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="设置键">
            <code class="setting-key">{{ dataDetail?.settingKey }}</code>
          </a-descriptions-item>
          <a-descriptions-item label="设置类型">
            <a-tag :color="getSettingTypeColor(dataDetail?.settingType)">
              {{ getSettingTypeText(dataDetail?.settingType) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag :color="dataDetail?.status === 1 ? 'green' : 'red'">
              {{ dataDetail?.status === 1 ? '启用' : '禁用' }}
            </a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 设置内容卡片 -->
      <a-card title="设置内容" class="detail-card" :bordered="false">
        <a-descriptions :column="1" size="medium">
          <a-descriptions-item label="设置值">
            <div class="setting-value-display">
              <template v-if="dataDetail?.settingType === 3">
                <a-tag :color="dataDetail?.settingValue === 'true' ? 'green' : 'red'" size="large">
                  <icon-check v-if="dataDetail?.settingValue === 'true'" />
                  <icon-close v-else />
                  {{ dataDetail?.settingValue === 'true' ? '是' : '否' }}
                </a-tag>
              </template>
              <template v-else-if="dataDetail?.settingType === 4">
                <div class="json-display">
                  <pre>{{ formatJsonDisplay(dataDetail?.settingValue) }}</pre>
                </div>
              </template>
              <template v-else>
                <span class="value-text">{{ dataDetail?.settingValue }}</span>
              </template>
            </div>
          </a-descriptions-item>
          <a-descriptions-item label="设置描述" v-if="dataDetail?.description">
            <div class="description-text">{{ dataDetail?.description }}</div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" class="detail-card" :bordered="false">
        <a-descriptions :column="2" size="small">
          <a-descriptions-item label="创建者">{{ dataDetail?.createdBy }}</a-descriptions-item>
          <a-descriptions-item label="更新者">{{ dataDetail?.updatedBy }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ formatDateTime(dataDetail?.createdAt) }}
          </a-descriptions-item>
          <a-descriptions-item label="更新时间">
            {{ formatDateTime(dataDetail?.updatedAt) }}
          </a-descriptions-item>
          <a-descriptions-item label="版本号">
            <a-tag>v{{ dataDetail?.version }}</a-tag>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetSettingDetailResp, getPlanetSetting } from '@/apis'
import { formatDateTime } from '@/utils/format'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetSettingDetailResp>()

// 设置类型相关方法
const getSettingTypeText = (type: number) => {
  const typeMap = { 1: '字符串', 2: '数字', 3: '布尔', 4: 'JSON' }
  return typeMap[type] || '未知'
}

const getSettingTypeColor = (type: number) => {
  const colorMap = { 1: 'blue', 2: 'green', 3: 'orange', 4: 'purple' }
  return colorMap[type] || 'gray'
}

// 格式化JSON显示
const formatJsonDisplay = (value: string) => {
  if (!value) return ''
  try {
    return JSON.stringify(JSON.parse(value), null, 2)
  } catch {
    return value
  }
}

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetSetting(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped>
.setting-detail {
  .detail-card {
    margin-bottom: 16px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    :deep(.ant-card-head) {
      border-bottom: 1px solid var(--color-border-2);
      
      .ant-card-head-title {
        font-weight: 600;
        color: var(--color-text-1);
      }
    }
  }
  
  .setting-key {
    background: var(--color-fill-2);
    padding: 2px 6px;
    border-radius: 4px;
    font-family: 'Courier New', monospace;
    font-size: 12px;
  }
  
  .setting-value-display {
    .value-text {
      font-size: 14px;
      font-weight: 500;
      color: var(--color-text-1);
    }
    
    .json-display {
      background: var(--color-fill-1);
      border: 1px solid var(--color-border-2);
      border-radius: 6px;
      padding: 12px;
      
      pre {
        margin: 0;
        font-family: 'Courier New', monospace;
        font-size: 12px;
        line-height: 1.5;
        color: var(--color-text-2);
        white-space: pre-wrap;
        word-break: break-all;
      }
    }
  }
  
  .description-text {
    color: var(--color-text-2);
    line-height: 1.6;
  }
}
</style>
