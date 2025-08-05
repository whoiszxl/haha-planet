<template>
  <a-drawer v-model:visible="visible" title="星球详情" :width="width >= 680 ? 680 : '100%'" :footer="false">
    <div v-if="dataDetail" class="planet-detail">
      <!-- 基本信息卡片 -->
      <a-card title="基本信息" class="detail-card">
        <div class="planet-header">
          <a-avatar :src="dataDetail.avatar" :size="80" class="planet-avatar">
            {{ dataDetail.name?.charAt(0) }}
          </a-avatar>
          <div class="planet-info">
            <h2>{{ dataDetail.name }}</h2>
            <p class="planet-code">编码：{{ dataDetail.planetCode }}</p>
            <a-tag :color="dataDetail.priceType === 1 ? 'green' : 'blue'">
              {{ dataDetail.priceType === 1 ? '免费星球' : '付费星球' }}
            </a-tag>
            <a-tag :color="getStatusColor(dataDetail.status)" style="margin-left: 8px">
              {{ getStatusText(dataDetail.status) }}
            </a-tag>
          </div>
        </div>
        <a-descriptions :column="2" class="basic-info">
          <a-descriptions-item label="星球简介" :span="2">
            {{ dataDetail.description || '-' }}
          </a-descriptions-item>
          <a-descriptions-item label="星球主ID">{{ dataDetail.ownerId }}</a-descriptions-item>
          <a-descriptions-item label="分类ID">{{ dataDetail.categoryId || '-' }}</a-descriptions-item>
          <a-descriptions-item label="标签" :span="2">
            <a-space v-if="dataDetail.tags">
              <a-tag v-for="tag in dataDetail.tags.split(',')" :key="tag" size="small">
                {{ tag.trim() }}
              </a-tag>
            </a-space>
            <span v-else>-</span>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 价格信息卡片 -->
      <a-card v-if="dataDetail.priceType === 2" title="价格信息" class="detail-card">
        <a-descriptions :column="2">
          <a-descriptions-item label="加入价格">
            <a-statistic :value="dataDetail.price" prefix="¥" :precision="2" />
          </a-descriptions-item>
          <a-descriptions-item label="原价">
            <a-statistic :value="dataDetail.originalPrice" prefix="¥" :precision="2" />
          </a-descriptions-item>
          <a-descriptions-item label="优惠价">
            <a-statistic :value="dataDetail.discountPrice" prefix="¥" :precision="2" />
          </a-descriptions-item>
          <a-descriptions-item label="总收入">
            <a-statistic :value="dataDetail.totalIncome" prefix="¥" :precision="2" />
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 统计数据卡片 -->
      <a-card title="统计数据" class="detail-card">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-statistic title="成员数量" :value="dataDetail.memberCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="帖子数量" :value="dataDetail.postCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="浏览次数" :value="dataDetail.viewCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="点赞数" :value="dataDetail.likeCount" />
          </a-col>
        </a-row>
        <a-row :gutter="16" style="margin-top: 16px">
          <a-col :span="6">
            <a-statistic title="分享次数" :value="dataDetail.shareCount" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="热度分数" :value="dataDetail.hotScore" :precision="1" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="质量评分" :value="dataDetail.qualityScore" :precision="1" />
          </a-col>
          <a-col :span="6">
            <a-statistic title="推荐权重" :value="dataDetail.recommendWeight" />
          </a-col>
        </a-row>
      </a-card>

      <!-- 设置信息卡片 -->
      <a-card title="设置信息" class="detail-card">
        <a-descriptions :column="2">
          <a-descriptions-item label="加入方式">
            {{ getJoinTypeText(dataDetail.joinType) }}
          </a-descriptions-item>
          <a-descriptions-item label="是否公开">
            <a-tag :color="dataDetail.isPublic ? 'green' : 'red'">
              {{ dataDetail.isPublic ? '公开' : '私密' }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="最大成员数">
            {{ dataDetail.maxMembers || '无限制' }}
          </a-descriptions-item>
          <a-descriptions-item label="自动通过申请">
            <a-tag :color="dataDetail.autoApprove ? 'green' : 'red'">
              {{ dataDetail.autoApprove ? '是' : '否' }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="允许成员发帖">
            <a-tag :color="dataDetail.allowMemberPost ? 'green' : 'red'">
              {{ dataDetail.allowMemberPost ? '是' : '否' }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="发帖需要审核">
            <a-tag :color="dataDetail.postNeedApprove ? 'orange' : 'green'">
              {{ dataDetail.postNeedApprove ? '是' : '否' }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="加入问题" :span="2" v-if="dataDetail.joinQuestion">
            {{ dataDetail.joinQuestion }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 公告规则卡片 -->
      <a-card v-if="dataDetail.notice || dataDetail.rules" title="公告规则" class="detail-card">
        <a-descriptions :column="1">
          <a-descriptions-item v-if="dataDetail.notice" label="星球公告">
            <div class="content-text">{{ dataDetail.notice }}</div>
          </a-descriptions-item>
          <a-descriptions-item v-if="dataDetail.rules" label="星球规则">
            <div class="content-text">{{ dataDetail.rules }}</div>
          </a-descriptions-item>
        </a-descriptions>
      </a-card>

      <!-- 系统信息卡片 -->
      <a-card title="系统信息" class="detail-card">
        <a-descriptions :column="2">
          <a-descriptions-item label="创建者">{{ dataDetail.createdBy }}</a-descriptions-item>
          <a-descriptions-item label="更新者">{{ dataDetail.updatedBy || '-' }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ dataDetail.createdAt }}</a-descriptions-item>
          <a-descriptions-item label="更新时间">{{ dataDetail.updatedAt || '-' }}</a-descriptions-item>
          <a-descriptions-item label="最后活跃时间" :span="2">
            {{ dataDetail.lastActiveTime || '-' }}
          </a-descriptions-item>
        </a-descriptions>
      </a-card>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetDetailResp, getPlanet } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetDetailResp>()

// 状态相关方法
const getStatusColor = (status: number) => {
  const colors = { 0: 'red', 1: 'green', 2: 'orange', 3: 'gray' }
  return colors[status] || 'gray'
}

const getStatusText = (status: number) => {
  const texts = { 0: '禁用', 1: '启用', 2: '审核中', 3: '已关闭' }
  return texts[status] || '未知'
}

const getJoinTypeText = (joinType: number) => {
  const texts = { 1: '直接加入', 2: '申请审核', 3: '邀请制' }
  return texts[joinType] || '未知'
}

// 查询详情
const getDataDetail = async () => {
  const res = await getPlanet(dataId.value)
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
.planet-detail {
  .detail-card {
    margin-bottom: 16px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .planet-header {
    display: flex;
    align-items: center;
    margin-bottom: 24px;
    
    .planet-avatar {
      margin-right: 16px;
    }
    
    .planet-info {
      flex: 1;
      
      h2 {
        margin: 0 0 8px 0;
        font-size: 20px;
        font-weight: 600;
      }
      
      .planet-code {
        margin: 0 0 8px 0;
        color: #666;
        font-size: 14px;
      }
    }
  }
  
  .basic-info {
    margin-top: 16px;
  }
  
  .content-text {
    white-space: pre-wrap;
    line-height: 1.6;
    color: #333;
  }
  
  // 统计数据数字加粗样式
  :deep(.ant-statistic-content-value) {
    font-weight: bold !important;
  }
}
</style>
