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
import { getPlanetInvite, addPlanetInvite, updatePlanetInvite } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'
import dayjs from 'dayjs'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球邀请' : '新增星球邀请'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelColProps: { span: 6 }, wrapperColProps: { span: 18 } },
  col: { xs: 24, sm: 24, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '基本信息',
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
    label: '邀请人ID',
    field: 'inviterId',
    type: 'input-number',
    props: { placeholder: '请输入邀请人ID', precision: 0 },
    rules: [{ required: true, message: '请输入邀请人ID' }],
    col: { span: 12 }
  },
  {
    label: '邀请类型',
    field: 'inviteType',
    type: 'radio',
    props: {
      options: [
        { label: '邀请码', value: 1 },
        { label: '链接邀请', value: 2 }
      ]
    },
    rules: [{ required: true, message: '请选择邀请类型' }],
    col: { span: 24 }
  },
  {
    label: '邀请码',
    field: 'inviteCode',
    type: 'input',
    props: { 
      placeholder: '请输入邀请码（留空自动生成）',
      maxLength: 20
    },
    col: { span: 12 }
  },
  {
    label: '邀请消息',
    field: 'inviteMessage',
    type: 'textarea',
    props: { 
      placeholder: '请输入邀请消息',
      maxLength: 200,
      showWordLimit: true,
      autoSize: { minRows: 2, maxRows: 4 }
    },
    col: { span: 24 }
  },
  {
    label: '使用设置',
    type: 'divider'
  },
  {
    label: '过期时间',
    field: 'expireTime',
    type: 'date-picker',
    props: {
      placeholder: '请选择过期时间',
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss',
      disabledDate: (date: Date) => dayjs(date).isBefore(dayjs(), 'day')
    },
    rules: [{ required: true, message: '请选择过期时间' }],
    col: { span: 12 }
  },
  {
    label: '最大使用次数',
    field: 'maxUseCount',
    type: 'input-number',
    props: { 
      placeholder: '请输入最大使用次数',
      min: 1,
      max: 1000,
      precision: 0
    },
    rules: [{ required: true, message: '请输入最大使用次数' }],
    col: { span: 12 }
  }
]

const { form, resetForm } = useForm({
  planetId: undefined,
  inviterId: undefined,
  inviteType: 1,
  inviteCode: '',
  inviteMessage: '',
  expireTime: dayjs().add(7, 'day').format('YYYY-MM-DD HH:mm:ss'),
  maxUseCount: 1
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
  const res = await getPlanetInvite(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    
    // 如果邀请码为空，生成随机邀请码
    if (!form.inviteCode) {
      form.inviteCode = generateInviteCode()
    }
    
    if (isUpdate.value) {
      await updatePlanetInvite(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetInvite(form)
      Message.success('新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    return false
  }
}

// 生成邀请码
const generateInviteCode = () => {
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
  let result = ''
  for (let i = 0; i < 8; i++) {
    result += chars.charAt(Math.floor(Math.random() * chars.length))
  }
  return result
}

defineExpose({ onAdd, onUpdate })
</script>
