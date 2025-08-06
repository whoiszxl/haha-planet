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
import { getUserToken, addUserToken, updateUserToken } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'
import dayjs from 'dayjs'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改用户令牌' : '新增用户令牌'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelAlign: 'left', labelColProps: { span: 6 } },
  col: { xs: 24, sm: 12, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '用户ID',
    field: 'userId',
    type: 'input',
    rules: [{ required: true, message: '请输入用户ID' }],
    props: {
      placeholder: '请输入用户ID'
    }
  },
  {
    label: 'Token',
    field: 'token',
    type: 'textarea',
    rules: [{ required: true, message: '请输入token' }],
    props: {
      placeholder: '请输入token',
      autoSize: { minRows: 2, maxRows: 4 }
    }
  },
  {
    label: 'Token类型',
    field: 'tokenType',
    type: 'radio',
    rules: [{ required: true, message: '请选择token类型' }],
    props: {
      options: [
        { label: '访问令牌', value: 'access' },
        { label: '刷新令牌', value: 'refresh' }
      ]
    }
  },
  {
    label: '来源平台',
    field: 'source',
    type: 'select',
    rules: [{ required: true, message: '请选择来源平台' }],
    props: {
      placeholder: '请选择来源平台',
      options: [
        { label: '网页', value: 'web' },
        { label: '移动端', value: 'app' },
        { label: '接口', value: 'api' }
      ]
    }
  },
  {
    label: '过期时间',
    field: 'expiresTime',
    type: 'datetime',
    props: {
      placeholder: '请选择过期时间',
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  {
    label: '登录IP',
    field: 'loginIp',
    type: 'input',
    props: {
      placeholder: '请输入登录IP'
    }
  },
  {
    label: '登录时间',
    field: 'loginTime',
    type: 'datetime',
    props: {
      placeholder: '请选择登录时间',
      showTime: true,
      format: 'YYYY-MM-DD HH:mm:ss'
    }
  },
  {
    label: '用户代理',
    field: 'userAgent',
    type: 'textarea',
    props: {
      placeholder: '请输入用户代理信息',
      autoSize: { minRows: 2, maxRows: 3 }
    }
  },
  {
    label: '附加信息',
    field: 'metaJson',
    type: 'textarea',
    props: {
      placeholder: '请输入JSON格式的附加信息',
      autoSize: { minRows: 3, maxRows: 5 }
    }
  },
  {
    label: '状态',
    field: 'status',
    type: 'radio',
    rules: [{ required: true, message: '请选择状态' }],
    props: {
      options: [
        { label: '有效', value: 1 },
        { label: '无效', value: 0 }
      ]
    }
  }
]

const { form, resetForm } = useForm({
  userId: '',
  token: '',
  tokenType: 'access',
  source: 'web',
  expiresTime: '',
  loginIp: '',
  loginTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
  userAgent: '',
  metaJson: '',
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
  // 设置默认登录时间为当前时间
  form.loginTime = dayjs().format('YYYY-MM-DD HH:mm:ss')
  visible.value = true
}

// 修改
const onUpdate = async (id: string) => {
  reset()
  dataId.value = id
  const res = await getUserToken(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    
    // 验证JSON格式
    if (form.metaJson) {
      try {
        JSON.parse(form.metaJson)
      } catch (error) {
        Message.error('附加信息必须是有效的JSON格式')
        return false
      }
    }
    
    if (isUpdate.value) {
      await updateUserToken(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addUserToken(form)
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
