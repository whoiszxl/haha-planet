<template>
  <a-drawer v-model:visible="visible" title="会员游戏动态详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键ID">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="会员ID">{{ dataDetail?.memberId }}</a-descriptions-item>
      <a-descriptions-item label="游戏ID">{{ dataDetail?.gameId }}</a-descriptions-item>
      <a-descriptions-item label="游戏名称">{{ dataDetail?.gameName }}</a-descriptions-item>
      <a-descriptions-item label="游戏封面">{{ dataDetail?.gameCover }}</a-descriptions-item>
      <a-descriptions-item label="动态类型(1:游戏时长 2:成就解锁 3:截图上传)">{{ dataDetail?.activityType }}</a-descriptions-item>
      <a-descriptions-item label="动态值(比如游戏时长)">{{ dataDetail?.activityValue }}</a-descriptions-item>
      <a-descriptions-item label="单位(小时/个)">{{ dataDetail?.activityUnit }}</a-descriptions-item>
      <a-descriptions-item label="成就计数(比如 7/17)">{{ dataDetail?.achievementCount }}</a-descriptions-item>
      <a-descriptions-item label="成就总数">{{ dataDetail?.achievementTotal }}</a-descriptions-item>
      <a-descriptions-item label="最后活跃时间">{{ dataDetail?.lastActive }}</a-descriptions-item>
      <a-descriptions-item label="状态(0:无效 1:有效)">{{ dataDetail?.status }}</a-descriptions-item>
      <a-descriptions-item label="乐观锁">{{ dataDetail?.version }}</a-descriptions-item>
      <a-descriptions-item label="逻辑删除 1: 已删除, 0: 未删除">{{ dataDetail?.isDeleted }}</a-descriptions-item>
      <a-descriptions-item label="创建者">{{ dataDetail?.createdBy }}</a-descriptions-item>
      <a-descriptions-item label="更新者">{{ dataDetail?.updatedBy }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ dataDetail?.createdAt }}</a-descriptions-item>
      <a-descriptions-item label="更新时间">{{ dataDetail?.updatedAt }}</a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type MemberRecentActivityDetailResp, getMemberRecentActivity } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<MemberRecentActivityDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getMemberRecentActivity(dataId.value)
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

<style lang="scss" scoped></style>
