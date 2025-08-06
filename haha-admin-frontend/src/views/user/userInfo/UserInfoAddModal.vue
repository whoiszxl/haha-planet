<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '800px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { getUserInfo, addUserInfo, updateUserInfo } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改用户基础信息' : '新增用户基础信息'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { layout: 'vertical' },
  col: { xs: 24, sm: 12, md: 12, lg: 8, xl: 8, xxl: 8 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '用户唯一标识',
    field: 'userCode',
    type: 'input',
    rules: [{ required: true, message: '请输入用户唯一标识' }]
  },
  {
    label: '用户名',
    field: 'username',
    type: 'input',
    rules: [{ required: true, message: '请输入用户名' }]
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
  const res = await getUserInfo(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updateUserInfo(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addUserInfo(form)
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
