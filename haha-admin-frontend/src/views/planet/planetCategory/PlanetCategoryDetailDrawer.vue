<template>
  <a-drawer v-model:visible="visible" title="星球分类详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="主键">{{ dataDetail?.id }}</a-descriptions-item>
      <a-descriptions-item label="分类名称">{{ dataDetail?.categoryName }}</a-descriptions-item>
      <a-descriptions-item label="分类图标链接">{{ dataDetail?.iconUrl }}</a-descriptions-item>
      <a-descriptions-item label="父分类ID，0表示顶级分类">{{ dataDetail?.parentId }}</a-descriptions-item>
      <a-descriptions-item label="分类级别:1->1级; 2->2级 3->3级">{{ dataDetail?.level }}</a-descriptions-item>
      <a-descriptions-item label="排序,数值越大越靠前">{{ dataDetail?.sort }}</a-descriptions-item>
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
import { type PlanetCategoryDetailResp, getPlanetCategory } from '@/apis'

const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<PlanetCategoryDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getPlanetCategory(dataId.value)
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
