<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '580px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { getPlanetTag, addPlanetTag, updatePlanetTag } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球标签' : '新增星球标签'))
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
    label: '标签名称',
    field: 'name',
    type: 'input',
    props: { placeholder: '请输入标签名称，如：Vue.js' },
    rules: [{ required: true, message: '请输入标签名称' }],
    col: { span: 12 }
  },
  {
    label: '标签颜色',
    field: 'color',
    type: 'color-picker',
    props: { 
      placeholder: '选择标签颜色',
      showText: true,
      presetColors: [
        '#1890ff', '#52c41a', '#faad14', '#f5222d',
        '#722ed1', '#13c2c2', '#eb2f96', '#fa541c'
      ]
    },
    rules: [{ required: true, message: '请选择标签颜色' }],
    col: { span: 12 }
  },
  {
    label: '所属分类',
    field: 'categoryId',
    type: 'select',
    props: {
      placeholder: '请选择分类',
      options: [
        { label: '技术类', value: 1 },
        { label: '行业类', value: 2 },
        { label: '兴趣类', value: 3 },
        { label: '其他', value: 4 }
      ]
    },
    rules: [{ required: true, message: '请选择所属分类' }],
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
    label: '标签描述',
    field: 'description',
    type: 'textarea',
    props: { 
      placeholder: '请输入标签描述，说明该标签的用途和适用场景',
      autoSize: { minRows: 3, maxRows: 5 }
    },
    col: { span: 24 }
  }
]

const { form, resetForm } = useForm({
  name: '',
  color: '#1890ff',
  categoryId: undefined,
  status: 1,
  description: ''
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
  const res = await getPlanetTag(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updatePlanetTag(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetTag(form)
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
