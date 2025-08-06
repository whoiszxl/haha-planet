<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '520px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { getPointsRecord, addPointsRecord, updatePointsRecord } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改积分记录' : '新增积分记录'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: {},
  col: { xs: 24, sm: 24, md: 24, lg: 24, xl: 24, xxl: 24 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '用户ID',
    field: 'userId',
    type: 'input',
    rules: [{ required: true, message: '请输入用户ID' }]
  },
  {
    label: '积分变化(正数增加，负数减少)',
    field: 'pointsChange',
    type: 'input',
    rules: [{ required: true, message: '请输入积分变化(正数增加，负数减少)' }]
  },
  {
    label: '变化前积分',
    field: 'pointsBefore',
    type: 'input',
    rules: [{ required: true, message: '请输入变化前积分' }]
  },
  {
    label: '变化后积分',
    field: 'pointsAfter',
    type: 'input',
    rules: [{ required: true, message: '请输入变化后积分' }]
  },
  {
    label: '变化类型(sign:签到 post:发帖 like:点赞 comment:评论)',
    field: 'changeType',
    type: 'input',
    rules: [{ required: true, message: '请输入变化类型(sign:签到 post:发帖 like:点赞 comment:评论)' }]
  },
  {
    label: '乐观锁',
    field: 'version',
    type: 'input',
    rules: [{ required: true, message: '请输入乐观锁' }]
  },
]

const { form, resetForm } = useForm({
    // todo 待补充
})

// 重置
const reset = () => {
  formRef.value?.formRef?.resetFields()
  resetForm()
}

const visible = ref(false)
// 新增
const onAdd = () => {
  reset()
  dataId.value = ''
  visible.value = true
}

// 修改
const onUpdate = async (id: string) => {
  reset()
  dataId.value = id
  const res = await getPointsRecord(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updatePointsRecord(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPointsRecord(form)
      Message.success('新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    return false
  }
}

defineExpose({ onAdd, onUpdate })
</script>
