<template>
  <a-drawer 
    v-model:visible="visible" 
    title="用户详情" 
    :width="width >= 680 ? 680 : '100%'" 
    :footer="false"
  >
    <div v-if="dataDetail" class="user-detail">
      <!-- 用户头像和基本信息 -->
      <div class="user-header">
        <div class="avatar-container">
          <img 
            v-if="getImageUrl(dataDetail.avatar)" 
            :src="getImageUrl(dataDetail.avatar)" 
            :alt="dataDetail.nickname || dataDetail.username"
            class="user-avatar"
            @error="handleImageError"
          />
          <div v-else class="avatar-placeholder">
            {{ dataDetail.nickname?.charAt(0) || dataDetail.username?.charAt(0) }}
          </div>
        </div>
        <div class="user-info">
          <h3>{{ dataDetail.nickname || dataDetail.username }}</h3>
          <p class="user-code">ID: {{ dataDetail.userCode }}</p>
          <div class="user-tags">
            <a-tag :color="getUserTypeColor(dataDetail.userType)">
              {{ getUserTypeText(dataDetail.userType) }}
            </a-tag>
            <a-tag :color="dataDetail.status === 1 ? 'green' : 'red'">
              {{ dataDetail.status === 1 ? '有效' : '无效' }}
            </a-tag>
            <a-tag v-if="dataDetail.isVerified === 1" color="blue">
              已实名认证
            </a-tag>
          </div>
        </div>
      </div>

      <!-- 详细信息 -->
      <a-tabs default-active-key="basic">
        <a-tab-pane key="basic" title="基本信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="用户名">{{ dataDetail.username }}</a-descriptions-item>
            <a-descriptions-item label="昵称">{{ dataDetail.nickname }}</a-descriptions-item>
            <a-descriptions-item label="性别">
              {{ getGenderText(dataDetail.gender) }}
            </a-descriptions-item>
            <a-descriptions-item label="生日">{{ dataDetail.birthday }}</a-descriptions-item>
            <a-descriptions-item label="手机号">{{ dataDetail.phone }}</a-descriptions-item>
            <a-descriptions-item label="邮箱">{{ dataDetail.email }}</a-descriptions-item>
            <a-descriptions-item label="真实姓名">{{ dataDetail.realName }}</a-descriptions-item>
            <a-descriptions-item label="身份证号">{{ maskIdCard(dataDetail.idCard) }}</a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="address" title="地址信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="省份">{{ dataDetail.province }}</a-descriptions-item>
            <a-descriptions-item label="城市">{{ dataDetail.city }}</a-descriptions-item>
            <a-descriptions-item label="区县">{{ dataDetail.district }}</a-descriptions-item>
            <a-descriptions-item label="详细地址" :span="2">{{ dataDetail.address }}</a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="career" title="职业信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="职业">{{ dataDetail.profession }}</a-descriptions-item>
            <a-descriptions-item label="公司">{{ dataDetail.company }}</a-descriptions-item>
            <a-descriptions-item label="学历">{{ dataDetail.education }}</a-descriptions-item>
            <a-descriptions-item label="学校">{{ dataDetail.school }}</a-descriptions-item>
            <a-descriptions-item label="个人简介" :span="2">
              <div class="bio-content">{{ dataDetail.bio }}</div>
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="stats" title="统计信息">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-statistic title="用户等级" :value="dataDetail.level" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="经验值" :value="dataDetail.experience" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="积分" :value="dataDetail.points" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="账户余额" :value="dataDetail.balance" :precision="2" prefix="¥" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="粉丝数" :value="dataDetail.followerCount" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="关注数" :value="dataDetail.followingCount" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="发帖数" :value="dataDetail.postCount" />
            </a-col>
            <a-col :span="12">
              <a-statistic title="获赞数" :value="dataDetail.likeCount" />
            </a-col>
          </a-row>
        </a-tab-pane>
        
        <a-tab-pane key="login" title="登录信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="最后登录时间">
              {{ formatDateTime(dataDetail.lastLoginTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="最后登录IP">{{ dataDetail.lastLoginIp }}</a-descriptions-item>
            <a-descriptions-item label="登录次数">{{ dataDetail.loginCount }}</a-descriptions-item>
            <a-descriptions-item label="注册来源">{{ dataDetail.registerSource }}</a-descriptions-item>
            <a-descriptions-item label="注册IP">{{ dataDetail.registerIp }}</a-descriptions-item>
            <a-descriptions-item label="认证时间">
              {{ formatDateTime(dataDetail.verifiedTime) }}
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        
        <a-tab-pane key="system" title="系统信息">
          <a-descriptions :column="2" size="large">
            <a-descriptions-item label="创建者">{{ dataDetail.createdBy }}</a-descriptions-item>
            <a-descriptions-item label="更新者">{{ dataDetail.updatedBy }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">
              {{ formatDateTime(dataDetail.createdAt) }}
            </a-descriptions-item>
            <a-descriptions-item label="更新时间">
              {{ formatDateTime(dataDetail.updatedAt) }}
            </a-descriptions-item>
            <a-descriptions-item label="版本号">{{ dataDetail.version }}</a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type UserInfoDetailResp, getUserInfo } from '@/apis'
import { formatDateTime } from '@/utils'

const { width } = useWindowSize()

// 获取环境变量
const imageBaseUrl = import.meta.env.VITE_IMAGE_BASE_URL

// 图片URL处理函数
const getImageUrl = (imagePath: string) => {
  if (!imagePath) return ''
  // 如果已经是完整URL（包含http或https），直接返回
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath
  }
  // 否则拼接基础URL
  return `${imageBaseUrl}${imagePath.startsWith('/') ? imagePath.slice(1) : imagePath}`
}

// 图片加载错误处理
const handleImageError = (event: Event) => {
  const target = event.target as HTMLImageElement
  target.style.display = 'none'
  // 显示占位符
  const placeholder = target.nextElementSibling as HTMLElement
  if (placeholder && placeholder.classList.contains('avatar-placeholder')) {
    placeholder.style.display = 'flex'
  }
}

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<UserInfoDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getUserInfo(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

defineExpose({ onDetail })

// 工具函数
const getGenderText = (gender: number) => {
  const genderMap = {
    0: '未知',
    1: '男',
    2: '女'
  }
  return genderMap[gender] || '未知'
}

const getUserTypeText = (type: number) => {
  const typeMap = {
    1: '普通用户',
    2: '认证用户',
    3: 'VIP用户', 
    4: '企业用户'
  }
  return typeMap[type] || '未知'
}

const getUserTypeColor = (type: number) => {
  const colorMap = {
    1: 'gray',
    2: 'blue',
    3: 'gold',
    4: 'purple'
  }
  return colorMap[type] || 'gray'
}

const maskIdCard = (idCard: string) => {
  if (!idCard) return '-'
  return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2')
}
</script>

<style lang="scss" scoped>
.user-detail {
  .user-header {
    display: flex;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid var(--color-border-2);
    margin-bottom: 20px;
    
    .avatar-container {
      position: relative;
      width: 80px;
      height: 80px;
      flex-shrink: 0;
      
      .user-avatar {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid var(--color-border-2);
      }
      
      .avatar-placeholder {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background: var(--color-primary-light-1);
        color: var(--color-primary-6);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 32px;
        font-weight: 600;
        border: 2px solid var(--color-border-2);
      }
    }
    
    .user-info {
      margin-left: 16px;
      flex: 1;
      
      h3 {
        margin: 0 0 8px 0;
        font-size: 20px;
        font-weight: 600;
      }
      
      .user-code {
        margin: 0 0 12px 0;
        color: var(--color-text-3);
        font-size: 14px;
      }
      
      .user-tags {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
      }
    }
  }
  
  .bio-content {
    white-space: pre-wrap;
    line-height: 1.6;
  }
}
</style>
