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
import { getUserClient, addUserClient, updateUserClient } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改用户客户端' : '新增用户客户端'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelAlign: 'left', labelColProps: { span: 6 }, wrapperColProps: { span: 18 } },
  col: { xs: 24, sm: 24, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  // 基本信息分组
  {
    label: '基本信息',
    field: 'basic',
    type: 'divider'
  },
  {
    label: '客户端Key',
    field: 'clientKey',
    type: 'input',
    rules: [
      { required: true, message: '请输入客户端Key' },
      { min: 6, max: 50, message: '长度在6-50个字符' },
      { pattern: /^[a-zA-Z0-9_-]+$/, message: '只能包含字母、数字、下划线和横线' }
    ],
    props: {
      placeholder: '请输入客户端Key，用于标识客户端'
    }
  },
  {
    label: '客户端密钥',
    field: 'clientSecret',
    type: 'input',
    rules: [
      { required: true, message: '请输入客户端密钥' },
      { min: 16, max: 100, message: '长度在16-100个字符' }
    ],
    props: {
      placeholder: '请输入客户端密钥',
      type: 'password',
      showPassword: true
    }
  },
  {
    label: '授权类型',
    field: 'authType',
    type: 'select',
    rules: [{ required: true, message: '请选择授权类型' }],
    options: [
      { label: '授权码模式', value: 'authorization_code' },
      { label: '客户端模式', value: 'client_credentials' },
      { label: '密码模式', value: 'password' },
      { label: '刷新令牌', value: 'refresh_token' }
    ],
    props: {
      placeholder: '请选择OAuth2授权类型'
    }
  },
  {
    label: '客户端类型',
    field: 'clientType',
    type: 'select',
    rules: [{ required: true, message: '请选择客户端类型' }],
    options: [
      { label: 'Web应用', value: 'web' },
      { label: '移动应用', value: 'mobile' },
      { label: '桌面应用', value: 'desktop' },
      { label: '服务端应用', value: 'server' }
    ],
    props: {
      placeholder: '请选择客户端应用类型'
    }
  },
  // 配置信息分组
  {
    label: '配置信息',
    field: 'config',
    type: 'divider'
  },
  {
    label: 'Token有效期',
    field: 'timeout',
    type: 'input-number',
    rules: [{ required: true, message: '请输入Token有效期' }],
    props: {
      placeholder: '单位：秒，-1表示永不过期',
      min: -1,
      max: 86400 * 365, // 最大1年
      precision: 0
    },
    extra: '单位：秒，-1表示永不过期，建议设置为7200（2小时）'
  },
  {
    label: '活跃超时时间',
    field: 'activeTimeout',
    type: 'input-number',
    rules: [{ required: true, message: '请输入活跃超时时间' }],
    props: {
      placeholder: '单位：秒，-1表示不限制',
      min: -1,
      max: 86400 * 30, // 最大30天
      precision: 0
    },
    extra: '单位：秒，-1表示不限制，超过此时间未活跃将冻结Token'
  },
  {
    label: '状态',
    field: 'status',
    type: 'radio',
    rules: [{ required: true, message: '请选择状态' }],
    options: [
      { label: '有效', value: 1 },
      { label: '无效', value: 0 }
    ],
    props: {
      type: 'button'
    }
  },
  // 其他信息（编辑时显示）
  {
    label: '其他信息',
    field: 'other',
    type: 'divider',
    show: () => isUpdate.value
  },
  {
    label: '版本号',
    field: 'version',
    type: 'input-number',
    show: () => isUpdate.value,
    props: {
      disabled: true,
      placeholder: '系统自动维护'
    },
    extra: '乐观锁版本号，系统自动维护'
  }
]

const { form, resetForm } = useForm({
  clientKey: '',
  clientSecret: '',
  authType: 'authorization_code',
  clientType: 'web',
  timeout: 7200, // 默认2小时
  activeTimeout: 1800, // 默认30分钟
  status: 1,
  version: 0
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
  try {
    const res = await getUserClient(id)
    Object.assign(form, res.data)
    visible.value = true
  } catch (error) {
    Message.error('获取客户端信息失败')
  }
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    
    if (isUpdate.value) {
      await updateUserClient(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addUserClient(form)
      Message.success('新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    Message.error(isUpdate.value ? '修改失败' : '新增失败')
    return false
  }
}

defineExpose({ onAdd, onUpdate })
</script>
