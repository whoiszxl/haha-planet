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
import { getPlanetNotification, addPlanetNotification, updatePlanetNotification } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球通知' : '新增星球通知'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: {},
  col: { xs: 24, sm: 24, md: 24, lg: 24, xl: 24, xxl: 24 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '星球ID',
    field: 'planetId',
    type: 'input',
    rules: [{ required: true, message: '请输入星球ID' }]
  },
  {
    label: '接收通知的用户ID',
    field: 'userId',
    type: 'input',
    rules: [{ required: true, message: '请输入接收通知的用户ID' }]
  },
  {
    label: '通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告',
    field: 'notificationType',
    type: 'input',
    rules: [{ required: true, message: '请输入通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告' }]
  },
  {
    label: '通知标题',
    field: 'title',
    type: 'input',
    rules: [{ required: true, message: '请输入通知标题' }]
  },
  {
    label: '乐观锁',
    field: 'version',
    type: 'input',
    rules: [{ required: true, message: '请输入乐观锁' }]
  },
  {
    label: '创建者',
    field: 'createdBy',
    type: 'input',
    rules: [{ required: true, message: '请输入创建者' }]
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
  const res = await getPlanetNotification(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updatePlanetNotification(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetNotification(form)
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
