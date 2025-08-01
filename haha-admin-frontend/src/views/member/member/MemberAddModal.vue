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
import { getMember, addMember, updateMember } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改会员' : '新增会员'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: {},
  col: { xs: 24, sm: 24, md: 24, lg: 24, xl: 24, xxl: 24 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '手机',
    field: 'phone',
    type: 'input',
    rules: [{ required: true, message: '请输入手机' }]
  },
  {
    label: '邮箱',
    field: 'email',
    type: 'input',
    rules: [{ required: true, message: '请输入邮箱' }]
  },
  {
    label: '登录密码',
    field: 'password',
    type: 'input',
    rules: [{ required: true, message: '请输入登录密码' }]
  },
  {
    label: '会员登录次数',
    field: 'loginCount',
    type: 'input',
    rules: [{ required: true, message: '请输入会员登录次数' }]
  },
  {
    label: '会员登录错误次数',
    field: 'loginErrorCount',
    type: 'input',
    rules: [{ required: true, message: '请输入会员登录错误次数' }]
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
  const res = await getMember(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updateMember(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addMember(form)
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
