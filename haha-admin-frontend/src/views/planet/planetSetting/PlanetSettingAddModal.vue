<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '680px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { getPlanetSetting, addPlanetSetting, updatePlanetSetting } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球设置' : '新增星球设置'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelColProps: { span: 6 }, wrapperColProps: { span: 18 } },
  col: { xs: 24, sm: 24, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '基础信息',
    field: 'basic',
    type: 'divider'
  },
  {
    label: '星球ID',
    field: 'planetId',
    type: 'input-number',
    props: { placeholder: '请输入星球ID', precision: 0 },
    rules: [{ required: true, message: '请输入星球ID' }],
    col: { span: 12 }
  },
  {
    label: '设置键',
    field: 'settingKey',
    type: 'input',
    props: { placeholder: '请输入设置键，如：max_members' },
    rules: [{ required: true, message: '请输入设置键' }],
    col: { span: 12 }
  },
  {
    label: '设置类型',
    field: 'settingType',
    type: 'select',
    props: {
      placeholder: '请选择设置类型',
      options: [
        { label: '字符串', value: 1 },
        { label: '数字', value: 2 },
        { label: '布尔', value: 3 },
        { label: 'JSON', value: 4 }
      ]
    },
    rules: [{ required: true, message: '请选择设置类型' }],
    col: { span: 12 }
  },
  {
    label: '状态',
    field: 'status',
    type: 'radio',
    props: {
      options: [
        { label: '启用', value: 1 },
        { label: '禁用', value: 0 }
      ]
    },
    rules: [{ required: true, message: '请选择状态' }],
    col: { span: 12 }
  },
  {
    label: '设置内容',
    field: 'content',
    type: 'divider'
  },
  {
    label: '设置值',
    field: 'settingValue',
    type: 'textarea',
    props: { 
      placeholder: '请输入设置值',
      autoSize: { minRows: 3, maxRows: 6 }
    },
    rules: [{ required: true, message: '请输入设置值' }],
    col: { span: 24 }
  },
  {
    label: '设置描述',
    field: 'description',
    type: 'textarea',
    props: { 
      placeholder: '请输入设置描述，说明该设置的作用',
      autoSize: { minRows: 2, maxRows: 4 }
    },
    col: { span: 24 }
  }
]

const { form, resetForm } = useForm({
  planetId: undefined,
  settingKey: '',
  settingValue: '',
  settingType: 1,
  description: '',
  status: 1
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
  const res = await getPlanetSetting(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updatePlanetSetting(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetSetting(form)
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
