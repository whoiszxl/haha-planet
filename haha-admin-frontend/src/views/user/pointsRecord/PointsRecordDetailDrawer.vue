<template>
  <a-drawer v-model:visible="visible" title="积分记录详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键ID">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="用户ID">{{ dataDetail?.userId }}</a-descriptions-item>
      <a-descriptions-item label="积分变化(正数增加，负数减少)">{{ dataDetail?.pointsChange }}</a-descriptions-item>
      <a-descriptions-item label="变化前积分">{{ dataDetail?.pointsBefore }}</a-descriptions-item>
      <a-descriptions-item label="变化后积分">{{ dataDetail?.pointsAfter }}</a-descriptions-item>
      <a-descriptions-item label="变化类型(sign:签到 post:发帖 like:点赞 comment:评论)">{{ dataDetail?.changeType }}</a-descriptions-item>
      <a-descriptions-item label="变化原因">{{ dataDetail?.changeReason }}</a-descriptions-item>
      <a-descriptions-item label="关联ID(如帖子ID、评论ID等)">{{ dataDetail?.relatedId }}</a-descriptions-item>
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
import { type PointsRecordDetailResp, getPointsRecord } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PointsRecordDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getPointsRecord(dataId.value)
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
