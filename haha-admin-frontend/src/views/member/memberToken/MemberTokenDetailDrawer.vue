<template>
  <a-drawer v-model:visible="visible" title="用户令牌详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键ID">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="用户ID">{{ dataDetail?.memberId }}</a-descriptions-item>
      <a-descriptions-item label="token">{{ dataDetail?.token }}</a-descriptions-item>
      <a-descriptions-item label="类型(1:公众号 2:小程序 3:unionid 4:APP)">{{ dataDetail?.type }}</a-descriptions-item>
      <a-descriptions-item label="过期时间">{{ dataDetail?.expiresTime }}</a-descriptions-item>
      <a-descriptions-item label="登录IP">{{ dataDetail?.loginIp }}</a-descriptions-item>
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
import { type MemberTokenDetailResp, getMemberToken } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<MemberTokenDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getMemberToken(dataId.value)
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
