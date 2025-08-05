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
import { getPlanetPost, addPlanetPost, updatePlanetPost } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球帖子' : '新增星球帖子'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { layout: 'vertical' },
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
    rules: [{ required: true, message: '请输入星球ID' }],
    props: {
      placeholder: '请输入星球ID',
      min: 1
    }
  },
  {
    label: '发帖用户ID',
    field: 'userId',
    type: 'input-number',
    rules: [{ required: true, message: '请输入发帖用户ID' }],
    props: {
      placeholder: '请输入发帖用户ID',
      min: 1
    }
  },
  {
    label: '帖子标题',
    field: 'title',
    type: 'input',
    rules: [{ required: true, message: '请输入帖子标题' }],
    props: {
      placeholder: '请输入帖子标题',
      maxLength: 100
    }
  },
  {
    label: '内容类型',
    field: 'contentType',
    type: 'select',
    rules: [{ required: true, message: '请选择内容类型' }],
    props: {
      placeholder: '请选择内容类型',
      options: [
        { label: '文本', value: 1 },
        { label: '图片', value: 2 },
        { label: '视频', value: 3 },
        { label: '音频', value: 4 },
        { label: '文件', value: 5 },
        { label: '链接', value: 6 }
      ]
    }
  },
  {
    label: '帖子内容',
    field: 'content',
    type: 'textarea',
    rules: [{ required: true, message: '请输入帖子内容' }],
    props: {
      placeholder: '请输入帖子内容',
      rows: 4,
      maxLength: 2000,
      showWordLimit: true
    },
    col: { span: 24 }
  },
  {
    label: '媒体文件URLs',
    field: 'mediaUrls',
    type: 'textarea',
    props: {
      placeholder: '请输入媒体文件URLs（JSON格式）',
      rows: 3
    },
    col: { span: 24 }
  },
  {
    label: '设置选项',
    type: 'divider'
  },
  {
    label: '是否匿名发帖',
    field: 'isAnonymous',
    type: 'radio',
    props: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 }
      ]
    }
  },
  {
    label: '是否置顶',
    field: 'isTop',
    type: 'radio',
    props: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 }
      ]
    }
  },
  {
    label: '是否精华',
    field: 'isEssence',
    type: 'radio',
    props: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 }
      ]
    }
  },
  {
    label: '审核状态',
    field: 'auditStatus',
    type: 'select',
    props: {
      placeholder: '请选择审核状态',
      options: [
        { label: '待审核', value: 1 },
        { label: '审核通过', value: 2 },
        { label: '审核拒绝', value: 3 }
      ]
    }
  }
]

const { form, resetForm } = useForm({
  planetId: undefined,
  userId: undefined,
  title: '',
  content: '',
  contentType: 1,
  mediaUrls: '',
  isAnonymous: 0,
  isTop: 0,
  isEssence: 0,
  auditStatus: 1
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
  const res = await getPlanetPost(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    
    // 处理媒体文件URLs
    if (form.mediaUrls) {
      try {
        JSON.parse(form.mediaUrls)
      } catch {
        Message.error('媒体文件URLs格式不正确，请输入有效的JSON格式')
        return false
      }
    }
    
    if (isUpdate.value) {
      await updatePlanetPost(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetPost(form)
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

<style lang="scss" scoped>
:deep(.arco-form-item-label) {
  font-weight: 500;
}

:deep(.arco-divider-text) {
  font-weight: 600;
  color: #1890ff;
}
</style>
