<template>
  <a-drawer v-model:visible="visible" title="星球通知详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="星球ID">{{ dataDetail?.planetId }}</a-descriptions-item>
      <a-descriptions-item label="接收通知的用户ID">{{ dataDetail?.userId }}</a-descriptions-item>
      <a-descriptions-item label="发送者ID">{{ dataDetail?.senderId }}</a-descriptions-item>
      <a-descriptions-item label="通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告">{{ dataDetail?.notificationType }}</a-descriptions-item>
      <a-descriptions-item label="通知标题">{{ dataDetail?.title }}</a-descriptions-item>
      <a-descriptions-item label="通知内容">{{ dataDetail?.content }}</a-descriptions-item>
      <a-descriptions-item label="关联目标类型: 1-帖子 2-评论 3-星球">{{ dataDetail?.targetType }}</a-descriptions-item>
      <a-descriptions-item label="关联目标ID">{{ dataDetail?.targetId }}</a-descriptions-item>
      <a-descriptions-item label="是否已读: 0-未读 1-已读">{{ dataDetail?.isRead }}</a-descriptions-item>
      <a-descriptions-item label="阅读时间">{{ dataDetail?.readTime }}</a-descriptions-item>
      <a-descriptions-item label="乐观锁">{{ dataDetail?.version }}</a-descriptions-item>
      <a-descriptions-item label="业务状态">{{ dataDetail?.status }}</a-descriptions-item>
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
import { type PlanetNotificationDetailResp, getPlanetNotification } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetNotificationDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetNotification(dataId.value)
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
