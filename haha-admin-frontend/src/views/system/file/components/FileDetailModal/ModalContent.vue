<template>
  <a-row justify="center" align="center">
    <div style="height: 100px">
      <FileImage :data="data" style="border-radius: 5px" />
    </div>
  </a-row>
  <a-row style="margin-top: 15px">
    <a-descriptions :column="1" layout="inline-vertical">
      <a-descriptions-item label="名称">{{ getFileName(data) }}</a-descriptions-item>
      <a-descriptions-item label="大小">{{ formatFileSize(data.size) }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ data.createdAt }}</a-descriptions-item>
      <a-descriptions-item label="修改时间">{{ data.updatedAt }}</a-descriptions-item>
    </a-descriptions>
  </a-row>
</template>

<script setup lang="ts">
import FileImage from '../../main/FileMain/FileImage.vue'
import type { FileItem } from '@/apis'
import { formatFileSize } from '@/utils'

interface Props {
  data: FileItem
}

withDefaults(defineProps<Props>(), {})

// 文件名称带后缀
const getFileName = (item: FileItem) => {
  return `${item.name}${item.extension ? `.${item.extension}` : ''}`
}
</script>

<style lang="less" scoped>
.label {
  color: var(--color-text-2);
}
:deep(.arco-form-item) {
  margin-bottom: 0;
}
:deep(.arco-form-item-label-col > label) {
  white-space: nowrap;
}
:deep(.arco-descriptions-title) {
  font-size: 14px;
}
:deep(.arco-descriptions-item-label-inline) {
  font-size: 12px;
}
:deep(.arco-descriptions-item-value-inline) {
  font-size: 12px;
  margin-bottom: 10px;
}
</style>
