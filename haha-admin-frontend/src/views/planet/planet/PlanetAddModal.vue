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
import { getPlanet, addPlanet, updatePlanet } from '@/apis'
import { type Columns, GiForm } from '@/components/GiForm'
import { useForm } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改星球' : '新增星球'))
const formRef = ref<InstanceType<typeof GiForm>>()

const options: Options = {
  form: { labelColProps: { span: 6 }, wrapperColProps: { span: 18 } },
  col: { xs: 24, sm: 24, md: 12, lg: 12, xl: 12, xxl: 12 },
  btns: { hide: true }
}

const columns: Columns = [
  {
    label: '星球编码',
    field: 'planetCode',
    type: 'input',
    rules: [{ required: true, message: '请输入星球编码' }],
    props: { placeholder: '请输入唯一的星球编码' }
  },
  {
    label: '星球名称',
    field: 'name',
    type: 'input',
    rules: [{ required: true, message: '请输入星球名称' }],
    props: { placeholder: '请输入星球名称' }
  },
  {
    label: '星球简介',
    field: 'description',
    type: 'textarea',
    rules: [{ required: true, message: '请输入星球简介' }],
    props: { placeholder: '请输入星球简介', rows: 3 },
    col: { span: 24 }
  },
  {
    label: '星球头像',
    field: 'avatar',
    type: 'input',
    props: { placeholder: '请输入头像URL' }
  },
  {
    label: '封面图片',
    field: 'coverImage',
    type: 'input',
    props: { placeholder: '请输入封面图片URL' }
  },
  {
    label: '分类ID',
    field: 'categoryId',
    type: 'input-number',
    props: { placeholder: '请选择分类' }
  },
  {
    label: '标签',
    field: 'tags',
    type: 'input',
    props: { placeholder: '多个标签用逗号分隔' }
  },
  {
    label: '价格类型',
    field: 'priceType',
    type: 'select',
    rules: [{ required: true, message: '请选择价格类型' }],
    options: [
      { label: '免费', value: 1 },
      { label: '付费', value: 2 }
    ]
  },
  {
    label: '加入价格',
    field: 'price',
    type: 'input-number',
    props: { placeholder: '请输入价格', min: 0, precision: 2 },
    show: ({ formModel }) => formModel.priceType === 2
  },
  {
    label: '原价',
    field: 'originalPrice',
    type: 'input-number',
    props: { placeholder: '请输入原价', min: 0, precision: 2 },
    show: ({ formModel }) => formModel.priceType === 2
  },
  {
    label: '优惠价',
    field: 'discountPrice',
    type: 'input-number',
    props: { placeholder: '请输入优惠价', min: 0, precision: 2 },
    show: ({ formModel }) => formModel.priceType === 2
  },
  {
    label: '加入方式',
    field: 'joinType',
    type: 'select',
    rules: [{ required: true, message: '请选择加入方式' }],
    options: [
      { label: '直接加入', value: 1 },
      { label: '申请审核', value: 2 },
      { label: '邀请制', value: 3 }
    ]
  },
  {
    label: '是否公开',
    field: 'isPublic',
    type: 'radio',
    rules: [{ required: true, message: '请选择是否公开' }],
    options: [
      { label: '私密', value: 0 },
      { label: '公开', value: 1 }
    ]
  },
  {
    label: '最大成员数',
    field: 'maxMembers',
    type: 'input-number',
    props: { placeholder: '0表示无限制', min: 0 }
  },
  {
    label: '加入问题',
    field: 'joinQuestion',
    type: 'textarea',
    props: { placeholder: '申请加入时需要回答的问题', rows: 2 },
    col: { span: 24 },
    show: ({ formModel }) => formModel.joinType === 2
  },
  {
    label: '星球公告',
    field: 'notice',
    type: 'textarea',
    props: { placeholder: '请输入星球公告', rows: 3 },
    col: { span: 24 }
  },
  {
    label: '星球规则',
    field: 'rules',
    type: 'textarea',
    props: { placeholder: '请输入星球规则', rows: 3 },
    col: { span: 24 }
  }
]

const { form, resetForm } = useForm({
  priceType: 1,
  joinType: 1,
  isPublic: 1,
  maxMembers: 0,
  autoApprove: 0,
  allowMemberPost: 1,
  postNeedApprove: 0,
  allowAnonymous: 0,
  watermarkEnabled: 0
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
  const res = await getPlanet(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updatePlanet(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addPlanet(form)
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
