<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '720px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { getPlanetComment, addPlanetComment, updatePlanetComment } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改帖子评论' : '新增帖子评论'))
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
    label: '帖子ID',
    field: 'postId',
    type: 'input-number',
    props: { placeholder: '请输入帖子ID', precision: 0 },
    rules: [{ required: true, message: '请输入帖子ID' }],
    col: { span: 12 }
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
    label: '评论用户ID',
    field: 'userId',
    type: 'input-number',
    props: { placeholder: '请输入评论用户ID', precision: 0 },
    rules: [{ required: true, message: '请输入评论用户ID' }],
    col: { span: 12 }
  },
  {
    label: '父评论ID',
    field: 'parentId',
    type: 'input-number',
    props: { placeholder: '0表示顶级评论', precision: 0, min: 0 },
    col: { span: 12 }
  },
  {
    label: '回复用户ID',
    field: 'replyToUserId',
    type: 'input-number',
    props: { placeholder: '回复的用户ID（可选）', precision: 0 },
    col: { span: 12 }
  },
  {
    label: '匿名评论',
    field: 'isAnonymous',
    type: 'radio',
    props: {
      options: [
        { label: '实名评论', value: 0 },
        { label: '匿名评论', value: 1 }
      ]
    },
    col: { span: 12 }
  },
  {
    label: '评论内容',
    field: 'content_section',
    type: 'divider'
  },
  {
    label: '评论内容',
    field: 'content',
    type: 'textarea',
    props: { 
      placeholder: '请输入评论内容',
      autoSize: { minRows: 4, maxRows: 8 },
      maxLength: 1000,
      showWordLimit: true
    },
    rules: [{ required: true, message: '请输入评论内容' }],
    col: { span: 24 }
  },
  {
    label: '媒体文件',
    field: 'mediaUrls',
    type: 'upload',
    props: {
      multiple: true,
      accept: 'image/*,video/*',
      listType: 'picture-card',
      limit: 9,
      tip: '支持上传图片和视频，最多9个文件'
    },
    col: { span: 24 }
  },
  {
    label: '审核设置',
    field: 'audit_section',
    type: 'divider'
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
    },
    rules: [{ required: true, message: '请选择审核状态' }],
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
  }
]

const { form, resetForm } = useForm({
  postId: undefined,
  planetId: undefined,
  userId: undefined,
  parentId: 0,
  replyToUserId: undefined,
  content: '',
  mediaUrls: [],
  isAnonymous: 0,
  auditStatus: 1,
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
  const res = await getPlanetComment(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updatePlanetComment(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanetComment(form)
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
