<template>
  <a-drawer v-model:visible="visible" title="会员积分变动日志详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="会员积分ID">{{ dataDetail?.memberPointId }}</a-descriptions-item>
      <a-descriptions-item label="变动前的会员积分">{{ dataDetail?.beforePoint }}</a-descriptions-item>
      <a-descriptions-item label="变动的会员积分">{{ dataDetail?.changePoint }}</a-descriptions-item>
      <a-descriptions-item label="变动后的会员积分">{{ dataDetail?.afterPoint }}</a-descriptions-item>
      <a-descriptions-item label="本次积分变动的原因">{{ dataDetail?.updateReason }}</a-descriptions-item>
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
import { type MemberPointsLogDetailResp, getMemberPointsLog } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<MemberPointsLogDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getMemberPointsLog(dataId.value)
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
