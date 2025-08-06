<template>
  <div class="table-page">
    <GiTable
      ref="tableRef"
      row-key="id"
      title="用户等级管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['levelName']"
      @refresh="search"
    >
      <template #custom-left>
        <a-input 
          v-model="queryForm.level" 
          placeholder="请输入等级" 
          allow-clear 
          style="width: 120px"
          @change="search"
        >
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input 
          v-model="queryForm.levelName" 
          placeholder="请输入等级名称" 
          allow-clear 
          style="width: 150px"
          @change="search"
        >
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input-group style="width: 280px">
          <a-input 
            v-model="queryForm.minExperience" 
            placeholder="最小经验值" 
            allow-clear 
            @change="search"
          />
          <a-input 
            v-model="queryForm.maxExperience" 
            placeholder="最大经验值" 
            allow-clear 
            @change="search"
          />
        </a-input-group>
        <a-select 
          v-model="queryForm.status" 
          placeholder="选择状态" 
          allow-clear 
          style="width: 120px"
          @change="search"
        >
          <a-option :value="1">有效</a-option>
          <a-option :value="0">无效</a-option>
        </a-select>
        <a-button @click="reset">重置</a-button>
      </template>
      <template #custom-right>
        <a-button v-permission="['user:userLevel:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <span>新增</span>
        </a-button>
        <a-dropdown @select="onBatchAction">
          <a-button>批量操作</a-button>
          <template #content>
            <a-doption value="enable">批量启用</a-doption>
            <a-doption value="disable">批量禁用</a-doption>
            <a-doption value="delete">批量删除</a-doption>
          </template>
        </a-dropdown>
        <a-tooltip content="导出">
          <a-button v-permission="['user:userLevel:export']" class="gi_hover_btn-border" @click="onExport">
            <template #icon>
              <icon-download />
            </template>
          </a-button>
        </a-tooltip>
      </template>
      
      <!-- 等级显示优化 -->
      <template #level="{ record }">
        <div class="level-display">
          <a-tag :color="getLevelColor(record.level)" class="level-tag">
            {{ record.level }}
          </a-tag>
          <span class="level-name">{{ record.levelName }}</span>
        </div>
      </template>
      
      <!-- 经验值范围显示 -->
      <template #experienceRange="{ record }">
        <div class="experience-range">
          <span class="range-text">{{ record.minExperience }} - {{ record.maxExperience }}</span>
          <a-progress 
            :percent="getExperiencePercent(record)" 
            size="mini" 
            :show-text="false"
            :color="getLevelColor(record.level)"
          />
        </div>
      </template>
      
      <!-- 等级图标和颜色显示 -->
      <template #levelIcon="{ record }">
        <div class="level-icon-display">
          <div 
            v-if="record.levelIcon" 
            class="icon-preview"
            :style="{ backgroundColor: record.levelColor || '#f0f0f0' }"
          >
            <img :src="record.levelIcon" alt="等级图标" class="icon-img" />
          </div>
          <div v-else class="icon-placeholder">无图标</div>
        </div>
      </template>
      
      <!-- 权益显示 -->
      <template #privileges="{ record }">
        <div class="privileges-display">
          <a-tooltip :content="record.privileges">
            <a-tag v-if="record.privileges" color="blue" class="privileges-tag">
              {{ truncateText(record.privileges, 10) }}
            </a-tag>
            <span v-else class="no-privileges">暂无权益</span>
          </a-tooltip>
        </div>
      </template>
      
      <!-- 状态显示优化 -->
      <template #status="{ record }">
        <a-switch 
          :model-value="record.status === 1"
          :disabled="!has.hasPerm('user:userLevel:update')"
          @change="(value) => onToggleStatus(record, value)"
        >
          <template #checked>有效</template>
          <template #unchecked>无效</template>
        </a-switch>
      </template>
      
      <!-- 操作列优化 -->
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['user:userLevel:update']" @click="onUpdate(record)">修改</a-link>
          <a-link @click="onDetail(record)">详情</a-link>
          <a-dropdown>
            <a-link>更多</a-link>
            <template #content>
              <a-doption @click="onCopy(record)">复制</a-doption>
              <a-doption 
                v-permission="['user:userLevel:delete']"
                class="danger-option"
                @click="onDelete(record)"
              >
                删除
              </a-doption>
            </template>
          </a-dropdown>
        </a-space>
      </template>
    </GiTable>

    <UserLevelAddModal ref="UserLevelAddModalRef" @save-success="search" />
    <UserLevelDetailDrawer ref="UserLevelDetailDrawerRef" />
  </div>
</template>

<script setup lang="ts">
import UserLevelAddModal from './UserLevelAddModal.vue'
import UserLevelDetailDrawer from './UserLevelDetailDrawer.vue'
import { type UserLevelResp, type UserLevelQuery, deleteUserLevel, exportUserLevel, listUserLevel } from '@/apis'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { Message } from '@arco-design/web-vue'

defineOptions({ name: 'UserLevel' })

const queryForm = reactive<UserLevelQuery>({
  level: undefined,
  levelName: undefined,
  minExperience: undefined,
  maxExperience: undefined,
  status: undefined,
  sort: ['level,asc']
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete
} = useTable((page) => listUserLevel({ ...queryForm, ...page }), { immediate: true })

// 优化后的列配置
const columns: TableInstanceColumns[] = [
  { 
    title: '等级信息', 
    dataIndex: 'level', 
    slotName: 'level',
    width: 180,
    fixed: 'left'
  },
  { 
    title: '经验值范围', 
    slotName: 'experienceRange',
    width: 200
  },
  { 
    title: '等级图标', 
    dataIndex: 'levelIcon', 
    slotName: 'levelIcon',
    width: 100,
    align: 'center'
  },
  { 
    title: '等级权益', 
    dataIndex: 'privileges', 
    slotName: 'privileges',
    width: 150
  },
  { 
    title: '等级描述', 
    dataIndex: 'description', 
    ellipsis: true,
    tooltip: true,
    width: 200
  },
  { 
    title: '状态', 
    dataIndex: 'status', 
    slotName: 'status',
    width: 100,
    align: 'center'
  },
  { 
    title: '创建时间', 
    dataIndex: 'createdAt',
    width: 180
  },
  {
    title: '操作',
    slotName: 'action',
    width: 150,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['user:userLevel:update', 'user:userLevel:delete'])
  }
]

// 工具函数
const getLevelColor = (level: number) => {
  const colors = ['#f56565', '#ed8936', '#ecc94b', '#48bb78', '#38b2ac', '#4299e1', '#9f7aea', '#ed64a6']
  return colors[level % colors.length] || '#718096'
}

const getExperiencePercent = (record: UserLevelResp) => {
  const range = record.maxExperience - record.minExperience
  return range > 0 ? Math.min(100, (record.minExperience / record.maxExperience) * 100) : 0
}

const truncateText = (text: string, maxLength: number) => {
  return text && text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}

// 重置
const reset = () => {
  Object.assign(queryForm, {
    level: undefined,
    levelName: undefined,
    minExperience: undefined,
    maxExperience: undefined,
    status: undefined
  })
  search()
}

// 切换状态
// 修改导入语句，移除 updateUserLevelStatus
import { type UserLevelResp, type UserLevelQuery, deleteUserLevel, exportUserLevel, listUserLevel } from '@/apis'

const onToggleStatus = async (record: UserLevelResp, status: boolean) => {
  try {
    // 使用现有的 updateUserLevel 函数
    await updateUserLevel({ status: status ? 1 : 0 }, record.id)
    record.status = status ? 1 : 0
    Message.success('状态更新成功')
    // 刷新列表
    search()
  } catch (error) {
    Message.error('状态更新失败')
  }
}

// 批量操作
const onBatchAction = (action: string) => {
  // 实现批量操作逻辑
  Message.info(`批量${action}功能待实现`)
}

// 复制等级
const onCopy = (record: UserLevelResp) => {
  UserLevelAddModalRef.value?.onCopy(record)
}

// 删除
const onDelete = (item: UserLevelResp) => {
  return handleDelete(() => deleteUserLevel(item.id), {
    content: `是否确定删除该条数据？`,
    showModal: true
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportUserLevel(queryForm))
}

const UserLevelAddModalRef = ref<InstanceType<typeof UserLevelAddModal>>()
// 新增
const onAdd = () => {
  UserLevelAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (item: UserLevelResp) => {
  UserLevelAddModalRef.value?.onUpdate(item.id)
}

const UserLevelDetailDrawerRef = ref<InstanceType<typeof UserLevelDetailDrawer>>()
// 详情
const onDetail = (item: UserLevelResp) => {
  UserLevelDetailDrawerRef.value?.onDetail(item.id)
}
</script>

<style lang="scss" scoped>
.level-display {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .level-tag {
    font-weight: bold;
    min-width: 40px;
    text-align: center;
  }
  
  .level-name {
    font-size: 14px;
    color: #666;
  }
}

.experience-range {
  .range-text {
    font-size: 12px;
    color: #666;
    margin-bottom: 4px;
    display: block;
  }
}

.level-icon-display {
  .icon-preview {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto;
    
    .icon-img {
      width: 24px;
      height: 24px;
      border-radius: 50%;
    }
  }
  
  .icon-placeholder {
    font-size: 12px;
    color: #ccc;
    text-align: center;
  }
}

.privileges-display {
  .privileges-tag {
    cursor: pointer;
  }
  
  .no-privileges {
    font-size: 12px;
    color: #ccc;
  }
}

.danger-option {
  color: #f56565 !important;
}
</style>
