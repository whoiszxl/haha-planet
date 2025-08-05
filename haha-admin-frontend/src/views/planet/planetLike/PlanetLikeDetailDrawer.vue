<template>
  <a-drawer v-model:visible="visible" title="点赞记录详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="星球ID">{{ dataDetail?.planetId }}</a-descriptions-item>
      <a-descriptions-item label="用户ID">{{ dataDetail?.userId }}</a-descriptions-item>
      <a-descriptions-item label="点赞目标类型: 1-帖子 2-评论">{{ dataDetail?.targetType }}</a-descriptions-item>
      <a-descriptions-item label="目标ID">{{ dataDetail?.targetId }}</a-descriptions-item>
      <a-descriptions-item label="被点赞的用户ID">{{ dataDetail?.targetUserId }}</a-descriptions-item>
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
import { type PlanetLikeDetailResp, getPlanetLike } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetLikeDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetLike(dataId.value)
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
