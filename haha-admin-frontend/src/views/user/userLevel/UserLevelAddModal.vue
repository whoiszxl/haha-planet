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
import { getUserLevel, addUserLevel, updateUserLevel } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const isCopy = ref(false)
const title = computed(() => {
  if (isCopy.value) return '复制用户等级'
  return isUpdate.value ? '修改用户等级' : '新增用户等级'
})
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelAlign: 'left' },
  col: { xs: 24, sm: 12, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '等级',
    field: 'level',
    type: 'input-number',
    props: {
      min: 1,
      max: 999,
      placeholder: '请输入等级数字'
    },
    rules: [{ required: true, message: '请输入等级' }]
  },
  {
    label: '等级名称',
    field: 'levelName',
    type: 'input',
    props: {
      placeholder: '请输入等级名称，如：青铜、白银等'
    },
    rules: [{ required: true, message: '请输入等级名称' }]
  },
  {
    label: '最小经验值',
    field: 'minExperience',
    type: 'input-number',
    props: {
      min: 0,
      placeholder: '请输入最小经验值'
    },
    rules: [{ required: true, message: '请输入最小经验值' }]
  },
  {
    label: '最大经验值',
    field: 'maxExperience',
    type: 'input-number',
    props: {
      min: 0,
      placeholder: '请输入最大经验值'
    },
    rules: [
      { required: true, message: '请输入最大经验值' },
      {
        validator: (value, callback) => {
          if (form.minExperience && value <= form.minExperience) {
            callback('最大经验值必须大于最小经验值')
          } else {
            callback()
          }
        }
      }
    ]
  },
  {
    label: '等级图标',
    field: 'levelIcon',
    type: 'upload',
    props: {
      accept: 'image/*',
      listType: 'picture-card',
      limit: 1,
      autoUpload: true,
      action: '/api/upload/image'
    }
  },
  {
    label: '等级颜色',
    field: 'levelColor',
    type: 'color-picker',
    props: {
      showText: true,
      size: 'large'
    }
  },
  {
    label: '等级权益',
    field: 'privileges',
    type: 'textarea',
    props: {
      placeholder: '请输入等级权益描述',
      rows: 3,
      maxLength: 500,
      showWordLimit: true
    },
    span: 24
  },
  {
    label: '等级描述',
    field: 'description',
    type: 'textarea',
    props: {
      placeholder: '请输入等级描述',
      rows: 3,
      maxLength: 200,
      showWordLimit: true
    },
    span: 24
  },
  {
    label: '状态',
    field: 'status',
    type: 'radio',
    options: [
      { label: '有效', value: 1 },
      { label: '无效', value: 0 }
    ],
    props: {
      type: 'button'
    },
    span: 24
  }
]

const { form, resetForm } = useForm({
  level: undefined,
  levelName: '',
  minExperience: 0,
  maxExperience: 0,
  levelIcon: '',
  levelColor: '#1890ff',
  privileges: '',
  description: '',
  status: 1
})

// 重置
const reset = () => {
  formRef.value?.formRef?.resetFields()
  resetForm()
  isCopy.value = false
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
  const res = await getUserLevel(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 复制
const onCopy = async (record: any) => {
  reset()
  dataId.value = ''
  isCopy.value = true
  Object.assign(form, {
    ...record,
    level: record.level + 1, // 自动递增等级
    levelName: `${record.levelName}_副本`
  })
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    
    if (isUpdate.value) {
      await updateUserLevel(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addUserLevel(form)
      Message.success(isCopy.value ? '复制成功' : '新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    return false
  }
}

defineExpose({ onAdd, onUpdate, onCopy })
</script>
