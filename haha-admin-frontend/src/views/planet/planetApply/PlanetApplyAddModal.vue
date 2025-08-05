<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '600px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { getPlanetApply, addPlanetApply, updatePlanetApply } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球申请' : '新增星球申请'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelColProps: { span: 6 }, wrapperColProps: { span: 18 } },
  col: { xs: 24, sm: 24, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '星球ID',
    field: 'planetId',
    type: 'input',
    rules: [{ required: true, message: '请输入星球ID' }],
    props: {
      placeholder: '请输入星球ID'
    }
  },
  {
    label: '申请用户ID',
    field: 'userId', 
    type: 'input',
    rules: [{ required: true, message: '请输入申请用户ID' }],
    props: {
      placeholder: '请输入申请用户ID'
    }
  },
  {
    label: '申请理由',
    field: 'applyReason',
    type: 'textarea',
    rules: [{ required: true, message: '请输入申请理由' }],
    props: {
      placeholder: '请输入申请理由',
      maxLength: 500,
      showWordLimit: true,
      autoSize: { minRows: 3, maxRows: 6 }
    },
    col: { span: 24 }
  },
  {
    label: '问题答案',
    field: 'answer',
    type: 'textarea',
    props: {
      placeholder: '请输入问题答案',
      maxLength: 1000,
      showWordLimit: true,
      autoSize: { minRows: 3, maxRows: 6 }
    },
    col: { span: 24 }
  },
  {
    label: '申请状态',
    field: 'applyStatus',
    type: 'select',
    rules: [{ required: true, message: '请选择申请状态' }],
    props: {
      placeholder: '请选择申请状态',
      options: [
        { label: '待审核', value: '1' },
        { label: '已通过', value: '2' },
        { label: '已拒绝', value: '3' }
      ]
    }
  },
  {
    label: '审核原因',
    field: 'auditReason',
    type: 'textarea',
    show: ({ model }) => model.applyStatus === '3',
    rules: [
      { 
        required: true, 
        message: '拒绝时必须填写审核原因',
        validator: (value, callback) => {
          if (form.applyStatus === '3' && !value) {
            callback('拒绝时必须填写审核原因')
          } else {
            callback()
          }
        }
      }
    ],
    props: {
      placeholder: '请输入审核原因',
      maxLength: 200,
      showWordLimit: true,
      autoSize: { minRows: 2, maxRows: 4 }
    },
    col: { span: 24 }
  }
]

const { form, resetForm } = useForm({
  planetId: '',
  userId: '',
  applyReason: '',
  answer: '',
  applyStatus: '1',
  auditReason: ''
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
  const res = await getPlanetApply(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    
    // 如果是通过状态，自动设置审核时间和审核人
    if (form.applyStatus === '2') {
      form.auditTime = new Date().toISOString()
      form.auditBy = 'current_user' // 这里应该从用户上下文获取
    }
    
    if (isUpdate.value) {
      await updatePlanetApply(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetApply(form)
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
