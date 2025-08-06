<template>
  <a-drawer v-model:visible="visible" title="用户关注(我关注的人)详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键ID">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="用户ID(关注者)">{{ dataDetail?.userId }}</a-descriptions-item>
      <a-descriptions-item label="被关注者ID">{{ dataDetail?.followingId }}</a-descriptions-item>
      <a-descriptions-item label="关注类型(1:普通关注 2:特别关注)">{{ dataDetail?.followType }}</a-descriptions-item>
      <a-descriptions-item label="是否互相关注(0:否 1:是)">{{ dataDetail?.isMutual }}</a-descriptions-item>
      <a-descriptions-item label="是否取消关注(0:未取消 1:已取消)">{{ dataDetail?.isCancelled }}</a-descriptions-item>
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
import { type UserFollowingDetailResp, getUserFollowing } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<UserFollowingDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getUserFollowing(dataId.value)
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
