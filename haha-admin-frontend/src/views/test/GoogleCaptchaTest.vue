<template>
  <div class="google-captcha-test">
    <a-card title="Google验证码功能测试" class="mb-4">
      <a-space direction="vertical" size="large" class="w-full">
        <!-- 状态显示 -->
        <a-alert
          :type="googleStatus.bound ? 'success' : 'info'"
          :message="googleStatus.bound ? '已绑定Google验证器' : '未绑定Google验证器'"
          :description="googleStatus.message"
          show-icon
        />

        <!-- 操作按钮 -->
        <a-space>
          <a-button type="primary" @click="checkStatus" :loading="checking">
            检查绑定状态
          </a-button>
          <a-button 
            v-if="!googleStatus.bound" 
            type="primary" 
            @click="showBindModal"
          >
            绑定Google验证器
          </a-button>
          <a-button 
            v-if="googleStatus.bound" 
            type="outline" 
            status="danger"
            @click="handleUnbind"
            :loading="unbinding"
          >
            解绑Google验证器
          </a-button>
        </a-space>

        <!-- 验证测试 -->
        <div v-if="googleStatus.bound" class="verify-section">
          <h3>验证码测试</h3>
          <a-input-group compact>
            <a-input
              v-model:value="testCode"
              placeholder="输入6位验证码"
              :maxlength="6"
              style="width: 200px"
            />
            <a-button 
              type="primary" 
              @click="testValidate"
              :loading="validating"
              :disabled="testCode.length !== 6"
            >
              验证
            </a-button>
          </a-input-group>
        </div>

        <!-- 备用码测试 -->
        <div v-if="googleStatus.bound" class="backup-section">
          <h3>备用恢复码</h3>
          <a-space>
            <a-button @click="generateBackup" :loading="generatingBackup">
              生成备用码
            </a-button>
            <a-input-group compact v-if="backupCodes.length > 0">
              <a-input
                v-model:value="testBackupCode"
                placeholder="输入备用码测试"
                style="width: 200px"
              />
              <a-button 
                type="primary" 
                @click="testBackupValidate"
                :loading="validatingBackup"
              >
                验证备用码
              </a-button>
            </a-input-group>
          </a-space>
          
          <div v-if="backupCodes.length > 0" class="backup-codes-display">
            <h4>生成的备用码：</h4>
            <div class="codes-grid">
              <div v-for="code in backupCodes" :key="code" class="backup-code">
                {{ code }}
              </div>
            </div>
          </div>
        </div>
      </a-space>
    </a-card>

    <!-- Google验证码绑定模态框 -->
    <GoogleCaptchaModal v-model="modalVisible" @success="onBindSuccess" />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import { Message } from '@arco-design/web-vue'
import { 
  checkGoogleCaptchaStatus, 
  validateGoogleCaptcha, 
  generateBackupCodes,
  validateBackupCode,
  unbindGoogleCaptcha
} from '@/apis'
import GoogleCaptchaModal from '@/views/setting/profile/components/GoogleCaptchaModal.vue'

// 状态管理
const googleStatus = ref({ bound: false, message: '' })
const modalVisible = ref(false)
const checking = ref(false)
const validating = ref(false)
const unbinding = ref(false)
const generatingBackup = ref(false)
const validatingBackup = ref(false)

// 测试数据
const testCode = ref('')
const testBackupCode = ref('')
const backupCodes = ref<string[]>([])

// 检查绑定状态
const checkStatus = async () => {
  checking.value = true
  try {
    const { data } = await checkGoogleCaptchaStatus()
    googleStatus.value = data
    Message.success('状态检查完成')
  } catch (error) {
    Message.error('检查状态失败')
    console.error(error)
  } finally {
    checking.value = false
  }
}

// 显示绑定模态框
const showBindModal = () => {
  modalVisible.value = true
}

// 绑定成功回调
const onBindSuccess = () => {
  googleStatus.value = { bound: true, message: '已绑定' }
  Message.success('Google验证器绑定成功！')
}

// 验证测试
const testValidate = async () => {
  if (testCode.value.length !== 6) {
    Message.warning('请输入6位验证码')
    return
  }

  validating.value = true
  try {
    const { data } = await validateGoogleCaptcha({ code: testCode.value })
    if (data.valid) {
      Message.success('验证成功！')
    } else {
      Message.error(data.message || '验证失败')
    }
  } catch (error) {
    Message.error('验证请求失败')
    console.error(error)
  } finally {
    validating.value = false
    testCode.value = ''
  }
}

// 生成备用码
const generateBackup = async () => {
  generatingBackup.value = true
  try {
    const { data } = await generateBackupCodes({ count: 10 })
    backupCodes.value = data.backupCodes
    Message.success(`成功生成${data.count}个备用码`)
  } catch (error) {
    Message.error('生成备用码失败')
    console.error(error)
  } finally {
    generatingBackup.value = false
  }
}

// 验证备用码
const testBackupValidate = async () => {
  if (!testBackupCode.value.trim()) {
    Message.warning('请输入备用码')
    return
  }

  validatingBackup.value = true
  try {
    const { data } = await validateBackupCode({ backupCode: testBackupCode.value })
    if (data.valid) {
      Message.success('备用码验证成功！')
    } else {
      Message.error(data.message || '备用码验证失败')
    }
  } catch (error) {
    Message.error('备用码验证请求失败')
    console.error(error)
  } finally {
    validatingBackup.value = false
    testBackupCode.value = ''
  }
}

// 解绑
const handleUnbind = async () => {
  unbinding.value = true
  try {
    await unbindGoogleCaptcha()
    googleStatus.value = { bound: false, message: '未绑定' }
    backupCodes.value = []
    Message.success('Google验证器解绑成功！')
  } catch (error) {
    Message.error('解绑失败')
    console.error(error)
  } finally {
    unbinding.value = false
  }
}

// 初始化
onMounted(() => {
  checkStatus()
})
</script>

<style lang="scss" scoped>
.google-captcha-test {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;

  .verify-section,
  .backup-section {
    padding: 16px;
    background: var(--color-fill-1);
    border-radius: 8px;

    h3, h4 {
      margin-bottom: 12px;
      color: var(--color-text-1);
    }
  }

  .backup-codes-display {
    margin-top: 16px;

    .codes-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
      gap: 8px;
      margin-top: 8px;

      .backup-code {
        padding: 8px 12px;
        background: #fff;
        border: 1px solid var(--color-border-2);
        border-radius: 4px;
        font-family: 'Courier New', monospace;
        font-size: 12px;
        text-align: center;
      }
    }
  }
}
</style>
