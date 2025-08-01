<template>
    <a-modal
      v-model:visible="visible"
      :title="title"
      :mask-closable="false"
      :esc-to-close="false"
      :modal-style="{ maxWidth: '520px' }"
      width="90%"
      @before-ok="save"
      @close="reset"
    >
      <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
    </a-modal>
  </template>
  
  <script setup lang="ts">
  import { Message } from '@arco-design/web-vue'
  import { getMemberFollower, addMemberFollower, updateMemberFollower } from '@/apis'
  import { type Columns, GiForm } from '@/components/GiForm'
  import { useForm } from '@/hooks'
  
  const emit = defineEmits<{
    (e: 'save-success'): void
  }>()
  
  const dataId = ref('')
  const isUpdate = computed(() => !!dataId.value)
  const title = computed(() => (isUpdate.value ? '修改会员粉丝' : '新增会员粉丝'))
  const formRef = ref<InstanceType<typeof GiForm>>()
  
  const options: Options = {
    form: {},
    col: { xs: 24, sm: 24, md: 24, lg: 24, xl: 24, xxl: 24 },
    btns: { hide: true }
  }
  
  const columns: Columns = [
    {
      label: '用户ID（我）',
      field: 'memberId',
      type: 'input',
      rules: [{ required: true, message: '请输入用户ID（我）' }]
    },
    {
      label: '是否取消关注: 0-未取消 1-取消',
      field: 'cancel',
      type: 'input',
      rules: [{ required: true, message: '请输入是否取消关注: 0-未取消 1-取消' }]
    },
  ]
  
  const { form, resetForm } = useForm({
      // todo 待补充
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
    const res = await getMemberFollower(id)
    Object.assign(form, res.data)
    visible.value = true
  }
  
  // 保存
  const save = async () => {
    try {
      const isInvalid = await formRef.value?.formRef?.validate()
      if (isInvalid) return false
      if (isUpdate.value) {
        await updateMemberFollower(form, dataId.value)
        Message.success('修改成功')
      } else {
        await addMemberFollower(form)
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
  