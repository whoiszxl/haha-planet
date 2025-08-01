<template>
    <a-drawer v-model:visible="visible" title="会员粉丝详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
      <a-descriptions :column="2" size="large" class="general-description">
        <a-descriptions-item label="主键ID">{{ dataDetail?.id }}</a-descriptions-item>
        <a-descriptions-item label="用户ID（我）">{{ dataDetail?.memberId }}</a-descriptions-item>
        <a-descriptions-item label="粉丝ID（别人）">{{ dataDetail?.targetId }}</a-descriptions-item>
        <a-descriptions-item label="是否取消关注: 0-未取消 1-取消">{{ dataDetail?.cancel }}</a-descriptions-item>
        <a-descriptions-item label="创建者">{{ dataDetail?.createdBy }}</a-descriptions-item>
        <a-descriptions-item label="更新者">{{ dataDetail?.updatedBy }}</a-descriptions-item>
        <a-descriptions-item label="创建时间">{{ dataDetail?.createdAt }}</a-descriptions-item>
        <a-descriptions-item label="更新时间">{{ dataDetail?.updatedAt }}</a-descriptions-item>
      </a-descriptions>
    </a-drawer>
  </template>
  
  <script lang="ts" setup>
  import { useWindowSize } from '@vueuse/core'
  import { type MemberFollowerDetailResp, getMemberFollower } from '@/apis'
  
  const { width } = useWindowSize()
  
  const visible = ref(false)
  const dataId = ref('')
  const dataDetail = ref<MemberFollowerDetailResp>()
  // 查询详情
  const getDataDetail = async () => {
    const res = await getMemberFollower(dataId.value)
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
  