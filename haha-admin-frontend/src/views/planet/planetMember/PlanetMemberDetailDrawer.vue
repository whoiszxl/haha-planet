<template>
  <a-drawer v-model:visible="visible" title="星球成员详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="星球ID">{{ dataDetail?.planetId }}</a-descriptions-item>
      <a-descriptions-item label="用户ID">{{ dataDetail?.userId }}</a-descriptions-item>
      <a-descriptions-item label="成员类型: 1-普通成员 2-管理员 3-星球主">{{ dataDetail?.memberType }}</a-descriptions-item>
      <a-descriptions-item label="加入时间">{{ dataDetail?.joinTime }}</a-descriptions-item>
      <a-descriptions-item label="到期时间">{{ dataDetail?.expireTime }}</a-descriptions-item>
      <a-descriptions-item label="加入来源: 1-直接加入 2-邀请 3-分享">{{ dataDetail?.joinSource }}</a-descriptions-item>
      <a-descriptions-item label="邀请人ID">{{ dataDetail?.inviterId }}</a-descriptions-item>
      <a-descriptions-item label="订单ID">{{ dataDetail?.orderId }}</a-descriptions-item>
      <a-descriptions-item label="在星球中的昵称">{{ dataDetail?.nickname }}</a-descriptions-item>
      <a-descriptions-item label="是否被禁言: 0-否 1-是">{{ dataDetail?.isMuted }}</a-descriptions-item>
      <a-descriptions-item label="禁言结束时间">{{ dataDetail?.muteEndTime }}</a-descriptions-item>
      <a-descriptions-item label="最后阅读时间">{{ dataDetail?.lastReadTime }}</a-descriptions-item>
      <a-descriptions-item label="总发帖数">{{ dataDetail?.totalPosts }}</a-descriptions-item>
      <a-descriptions-item label="总获赞数">{{ dataDetail?.totalLikes }}</a-descriptions-item>
      <a-descriptions-item label="乐观锁">{{ dataDetail?.version }}</a-descriptions-item>
      <a-descriptions-item label="业务状态: 1-正常 2-已退出">{{ dataDetail?.status }}</a-descriptions-item>
      <a-descriptions-item label="逻辑删除 1: 已删除， 0: 未删除">{{ dataDetail?.isDeleted }}</a-descriptions-item>
      <a-descriptions-item label="创建者">{{ dataDetail?.createdBy }}</a-descriptions-item>
      <a-descriptions-item label="更新者">{{ dataDetail?.updatedBy }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ dataDetail?.createdAt }}</a-descriptions-item>
      <a-descriptions-item label="更新时间">{{ dataDetail?.updatedAt }}</a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type PlanetMemberDetailResp, getPlanetMember } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetMemberDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetMember(dataId.value)
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
