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
import { getUserSettings, addUserSettings, updateUserSettings } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改用户设置' : '新增用户设置'))
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
    label: '隐私级别',
    field: 'privacyLevel',
    type: 'select',
    rules: [{ required: true, message: '请选择隐私级别' }],
    props: {
      placeholder: '请选择隐私级别',
      options: [
        { label: '公开', value: 1 },
        { label: '好友可见', value: 2 },
        { label: '仅自己', value: 3 }
      ]
    }
  },
  {
    label: '允许被搜索',
    field: 'allowSearch',
    type: 'switch',
    props: {
      checkedText: '允许',
      uncheckedText: '不允许'
    }
  },
  {
    label: '允许好友申请',
    field: 'allowFriendRequest',
    type: 'switch',
    props: {
      checkedText: '允许',
      uncheckedText: '不允许'
    }
  },
  {
    label: '允许私信',
    field: 'allowMessage',
    type: 'switch',
    props: {
      checkedText: '允许',
      uncheckedText: '不允许'
    }
  },
  {
    label: '邮件通知',
    field: 'emailNotification',
    type: 'switch',
    props: {
      checkedText: '开启',
      uncheckedText: '关闭'
    }
  },
  {
    label: '短信通知',
    field: 'smsNotification',
    type: 'switch',
    props: {
      checkedText: '开启',
      uncheckedText: '关闭'
    }
  },
  {
    label: '推送通知',
    field: 'pushNotification',
    type: 'switch',
    props: {
      checkedText: '开启',
      uncheckedText: '关闭'
    }
  },
  {
    label: '主题设置',
    field: 'theme',
    type: 'select',
    props: {
      placeholder: '请选择主题',
      options: [
        { label: '默认主题', value: 'default' },
        { label: '深色主题', value: 'dark' },
        { label: '浅色主题', value: 'light' }
      ]
    }
  },
  {
    label: '语言设置',
    field: 'language',
    type: 'select',
    props: {
      placeholder: '请选择语言',
      options: [
        { label: '中文', value: 'zh-CN' },
        { label: 'English', value: 'en-US' },
        { label: '日本語', value: 'ja-JP' }
      ]
    }
  },
  {
    label: '时区设置',
    field: 'timezone',
    type: 'select',
    props: {
      placeholder: '请选择时区',
      options: [
        { label: 'UTC+8 (北京时间)', value: 'Asia/Shanghai' },
        { label: 'UTC+0 (格林威治时间)', value: 'UTC' },
        { label: 'UTC-5 (纽约时间)', value: 'America/New_York' }
      ]
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
  privacyLevel: 1,
  allowSearch: true,
  allowFriendRequest: true,
  allowMessage: true,
  emailNotification: true,
  smsNotification: false,
  pushNotification: true,
  theme: 'default',
  language: 'zh-CN',
  timezone: 'Asia/Shanghai',
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
  const res = await getUserSettings(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updateUserSettings(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addUserSettings(form)
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
