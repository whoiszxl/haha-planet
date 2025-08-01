<template>
  <a-drawer
    :width="width <= 1000 ? '100%' : '1000px'"
    :visible="visible"
    @cancel="onCancel"
    @ok="onOk"
    unmountOnClose
  >
    <template #title>会员详情</template>

    <!-- 基本信息 -->
    <a-descriptions title="基本信息" bordered>
      <a-descriptions-item label="主键ID">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="用户名">{{ dataDetail?.personaName }}</a-descriptions-item>
      <a-descriptions-item label="真实姓名">{{ dataDetail?.realName }}</a-descriptions-item>
      <a-descriptions-item label="手机">{{ dataDetail?.phone }}</a-descriptions-item>
      <a-descriptions-item label="邮箱">{{ dataDetail?.email }}</a-descriptions-item>
      <a-descriptions-item label="性别">
        <a-tag :color="dataDetail?.gender === 1 ? 'blue' : dataDetail?.gender === 2 ? 'pink' : 'gray'">
          {{ dataDetail?.gender === 1 ? '男' : dataDetail?.gender === 2 ? '女' : '未知' }}
        </a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="生日">{{ dataDetail?.birthday }}</a-descriptions-item>
      <a-descriptions-item label="微信号">{{ dataDetail?.wxCode }}</a-descriptions-item>
      <a-descriptions-item label="个人简介">{{ dataDetail?.summary }}</a-descriptions-item>
    </a-descriptions>

    <!-- 头像与背景 -->
    <a-descriptions title="头像与背景" bordered class="mt-4">
      <a-descriptions-item label="头像">
        <a-image
          :src="dataDetail?.avatar"
          :width="100"
          :height="100"
          fit="cover"
          style="border-radius: 4px;"
        />
      </a-descriptions-item>
      <a-descriptions-item label="头像边框">
        <a-image
          :src="dataDetail?.avatarFrame"
          :width="100"
          :height="100"
          fit="cover"
          style="border-radius: 4px;"
        />
      </a-descriptions-item>
      <a-descriptions-item label="个人资料背景">
        <a-image
          :src="dataDetail?.personaBg"
          :width="200"
          :height="100"
          fit="cover"
          style="border-radius: 4px;"
        />
      </a-descriptions-item>
    </a-descriptions>

    <!-- 地址信息 -->
    <a-descriptions title="地址信息" bordered class="mt-4">
      <a-descriptions-item label="国家">{{ dataDetail?.country }}</a-descriptions-item>
      <a-descriptions-item label="省份">{{ dataDetail?.province }}</a-descriptions-item>
      <a-descriptions-item label="城市">{{ dataDetail?.city }}</a-descriptions-item>
      <a-descriptions-item label="区域">{{ dataDetail?.district }}</a-descriptions-item>
    </a-descriptions>

    <!-- 账户统计 -->
    <a-descriptions title="账户统计" bordered class="mt-4">
      <a-descriptions-item label="会员积分">
        <a-statistic :value="Number(dataDetail?.points) || 0" />
      </a-descriptions-item>
      <a-descriptions-item label="会员等级">
        <a-tag color="gold">LV.{{ dataDetail?.level }}</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="游戏数量">
        <a-statistic :value="Number(dataDetail?.gameCount) || 0" />
      </a-descriptions-item>
      <a-descriptions-item label="截图数量">
        <a-statistic :value="Number(dataDetail?.screenshotCount) || 0" />
      </a-descriptions-item>
      <a-descriptions-item label="好友数量">
        <a-statistic :value="Number(dataDetail?.friendCount) || 0" />
      </a-descriptions-item>
      <a-descriptions-item label="登录次数">
        <a-statistic :value="Number(dataDetail?.loginCount) || 0" />
      </a-descriptions-item>
    </a-descriptions>

    <!-- 账户安全 -->
    <a-descriptions title="账户安全" bordered class="mt-4">
      <a-descriptions-item label="注册IP">{{ dataDetail?.registerIp }}</a-descriptions-item>
      <a-descriptions-item label="最近IP">{{ dataDetail?.lastIp }}</a-descriptions-item>
      <a-descriptions-item label="登录错误次数">
        <a-tag :color="Number(dataDetail?.loginErrorCount) > 5 ? 'red' : 'green'">
          {{ dataDetail?.loginErrorCount }}
        </a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="最后登录">{{ dataDetail?.lastLogin }}</a-descriptions-item>
      <a-descriptions-item label="账户状态">
        <a-tag :color="Number(dataDetail?.status) === 1 ? 'green' : 'red'">
          {{ Number(dataDetail?.status) === 1 ? '有效' : '无效' }}
        </a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="是否热门">
        <a-tag :color="Number(dataDetail?.isHot) === 1 ? 'orange' : ''">
          {{ Number(dataDetail?.isHot) === 1 ? '是' : '否' }}
        </a-tag>
      </a-descriptions-item>
    </a-descriptions>

    <!-- 系统信息 -->
    <a-descriptions title="系统信息" bordered class="mt-4">
      <a-descriptions-item label="创建时间">{{ dataDetail?.createdAt }}</a-descriptions-item>
      <a-descriptions-item label="更新时间">{{ dataDetail?.updatedAt }}</a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script setup lang="ts">
import { useWindowSize } from '@vueuse/core'
import { type MemberDetailResp, getMember } from '@/apis'

const { width } = useWindowSize()

const props = defineProps<{
  visible: boolean
  id?: string | number
}>()

const emit = defineEmits(['update:visible', 'ok'])

const dataDetail = ref<MemberDetailResp>()

const onDetail = async () => {
  const { data } = await getMember(String(props.id))
  dataDetail.value = data
}

const onCancel = () => {
  emit('update:visible', false)
}

const onOk = () => {
  emit('ok')
  emit('update:visible', false)
}

defineExpose({ onDetail })
</script>

<style scoped>
.mt-4 {
  margin-top: 16px;
}

:deep(.arco-descriptions-title) {
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text-1);
  margin-bottom: 16px;
}

:deep(.arco-descriptions-item-label) {
  font-weight: 500;
  color: var(--color-text-2);
}

:deep(.arco-tag) {
  margin-right: 0;
}

:deep(.arco-statistic-value) {
  font-size: 14px;
}
</style>
