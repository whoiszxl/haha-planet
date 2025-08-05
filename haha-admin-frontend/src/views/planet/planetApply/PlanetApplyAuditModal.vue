<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '500px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <div class="audit-form">
      <a-alert v-if="isBatch" type="info" :message="`即将审核 ${auditIds.length} 条申请记录`" show-icon style="margin-bottom: 16px" />
      
      <a-form ref="formRef" :model="form" layout="vertical">
        <a-form-item label="审核结果" field="auditResult" :rules="[{ required: true, message: '请选择审核结果' }]">
          <a-radio-group v-model="form.auditResult">
            <a-radio value="2">
              <a-tag color="green">
                <template #icon><icon-check-circle /></template>
                通过
              </a-tag>
            </a-radio>
            <a-radio value="3">
              <a-tag color="red">
                <template #icon><icon-close-circle /></template>
                拒绝
              </a-tag>
            </a-radio>
          </a-radio-group>
        </a-form-item>
        
        <a-form-item 
          label="审核原因" 
          field="auditReason" 
          :rules="form.auditResult === '3' ? [{ required: true, message: '拒绝时必须填写审核原因' }] : []"
        >
          <a-textarea 
            v-model="form.auditReason" 
            :placeholder="form.auditResult === '3' ? '请输入拒绝原因' : '请输入审核备注（可选）'"
            :max-length="200"
            show-word-limit
            :auto-size="{ minRows: 3, maxRows: 6 }"
          />
        </a-form-item>
      </a-form>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { updatePlanetApply, getPlanetApply } from '@/apis'

const emit = defineEmits<{
  (e: 'audit-success'): void
}>()

const visible = ref(false)
const auditIds = ref<string[]>([])
const isBatch = computed(() => auditIds.value.length > 1)
const title = computed(() => isBatch.value ? '批量审核申请' : '审核申请')
const formRef = ref()

const form = reactive({
  auditResult: '2',
  auditReason: ''
})

// 重置表单
const reset = () => {
  form.auditResult = '2'
  form.auditReason = ''
  formRef.value?.resetFields()
}

// 单个审核
const onAudit = async (id: string) => {
  reset()
  auditIds.value = [id]
  visible.value = true
}

// 批量审核
const onBatchAudit = (ids: string[]) => {
  reset()
  auditIds.value = ids
  visible.value = true
}

// 保存审核结果
const save = async () => {
  try {
    const isInvalid = await formRef.value?.validate()
    if (isInvalid) return false
    
    // 准备审核数据
    const auditData = {
      applyStatus: form.auditResult,
      auditReason: form.auditReason,
      auditTime: new Date().toISOString(),
      auditBy: 'current_user' // 这里应该从用户上下文获取
    }
    
    if (isBatch.value) {
      // 批量审核
      const promises = auditIds.value.map(id => {
        return updatePlanetApply(auditData, id)
      })
      await Promise.all(promises)
      Message.success(`批量审核成功，共处理 ${auditIds.value.length} 条记录`)
    } else {
      // 单个审核
      await updatePlanetApply(auditData, auditIds.value[0])
      Message.success('审核成功')
    }
    
    emit('audit-success')
    return true
  } catch (error) {
    console.error('审核失败:', error)
    Message.error('审核失败，请重试')
    return false
  }
}

defineExpose({ onAudit, onBatchAudit })
</script>

<style lang="scss" scoped>
.audit-form {
  :deep(.arco-radio) {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    
    .arco-radio-button {
      margin-right: 8px;
    }
  }
}
</style>