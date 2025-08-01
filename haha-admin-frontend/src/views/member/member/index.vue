<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="会员管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['name']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input v-model="queryForm.phone" placeholder="请输入手机" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.email" placeholder="请输入邮箱" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.personaName" placeholder="请输入个人资料名称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.realName" placeholder="请输入真实姓名" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.country" placeholder="请输入国家" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">重置</a-button>
      </template>

      <template #custom-right>
        <a-button v-permission="['member:member:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>

        <a-tooltip content="导出">
          <a-button v-permission="['member:member:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>


      <template #avatar="{ record }">
        <a-image
          :src="record.avatar"
          :width="40"
          height="40"
          fit="cover"
          style="border-radius: 4px;"
        />
      </template>

      <template #personaName="{ record }">
        <a-link @click="onDetail(record)">{{ record.personaName }}</a-link>
      </template>


      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['member:member:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['member:member:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>

    <MemberAddModal ref="MemberAddModalRef" @save-success="search" />
    <MemberDetailDrawer 
      ref="MemberDetailDrawerRef" 
      :visible="detailVisible" 
      :id="currentId"
      @update:visible="detailVisible = $event"
      @ok="search"
    />
  </div>
</template>

<script setup lang="ts">
import MemberAddModal from './MemberAddModal.vue'
import MemberDetailDrawer from './MemberDetailDrawer.vue'
import { type MemberResp, type MemberQuery, deleteMember, exportMember, listMember } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'

defineOptions({ name: 'Member' })

const queryForm = reactive<MemberQuery>({
  phone: '',
  email: '',
  personaName: '',
  realName: '',
  country: '',
  sort: ['createdAt,desc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listMember({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '主键ID', dataIndex: 'id', slotName: 'id' },
  { title: '头像', dataIndex: 'avatar', slotName: 'avatar' },
  { title: '个人资料名称', dataIndex: 'personaName', slotName: 'personaName' },
  { title: '手机', dataIndex: 'phone', slotName: 'phone', ellipsis: true, tooltip: true },
  { title: '真实姓名', dataIndex: 'realName', slotName: 'realName' },
  { title: '国家', dataIndex: 'country', slotName: 'country' },
  { title: '最近使用时的IP地址', dataIndex: 'lastIp', slotName: 'lastIp' },
  { title: '最后登录', dataIndex: 'lastLogin', slotName: 'lastLogin' },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['member:member:update', 'member:member:delete'])
  }
]

// 重置
const reset = () => {
  queryForm.phone = ''
  queryForm.email = ''
  queryForm.personaName = ''
  queryForm.realName = ''
  queryForm.country = ''
  search()
}

// 删除
const onDelete = (item: MemberResp) => {
  return handleDelete(() => deleteMember(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportMember(queryForm))
}

const detailVisible = ref(false)
const currentId = ref<string | number>()

const MemberAddModalRef = ref<InstanceType<typeof MemberAddModal>>()
// 新增
const onAdd = () => {
  MemberAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: MemberResp) => {
  MemberAddModalRef.value?.onUpdate(item.id)
}

const MemberDetailDrawerRef = ref<InstanceType<typeof MemberDetailDrawer>>()
// 详情
const onDetail = async (item: MemberResp) => {
  currentId.value = item.id
  detailVisible.value = true
  await nextTick()
  MemberDetailDrawerRef.value?.onDetail()
}
</script>

<style lang="scss" scoped></style>
