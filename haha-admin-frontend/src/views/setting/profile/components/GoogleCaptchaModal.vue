<template>
  <a-modal
    v-model:visible="visible"
    title="Google验证器绑定"
    :width="600"
    :footer="false"
    @cancel="handleCancel"
  >
    <div class="google-captcha-modal">
      <!-- 步骤指示器 -->
      <a-steps :current="currentStep" class="mb-6">
        <a-step title="扫描二维码" />
        <a-step title="输入验证码" />
        <a-step title="保存备用码" />
      </a-steps>

      <!-- 步骤1: 扫描二维码 -->
      <div v-if="currentStep === 0" class="step-content">
        <div class="qr-section">
          <div class="qr-code-container">
            <img v-if="qrCodeImage" :src="qrCodeImage" alt="Google验证器二维码" class="qr-code" />
            <a-spin v-else size="large" />
          </div>
          <div class="qr-instructions">
            <h3>使用Google验证器扫描二维码</h3>
            <ol>
              <li>在手机上下载并安装 Google Authenticator 应用</li>
              <li>打开应用，点击"+"按钮</li>
              <li>选择"扫描二维码"</li>
              <li>扫描左侧的二维码</li>
            </ol>
            <div class="secret-key">
              <p>如果无法扫描，请手动输入密钥：</p>
              <a-input-group compact>
                <a-input
                  :value="secretKey"
                  readonly
                  class="secret-input"
                />
                <a-button @click="copySecretKey">
                  <template #icon>
                    <icon-copy />
                  </template>
                  复制
                </a-button>
              </a-input-group>
            </div>
          </div>
        </div>
        <div class="step-actions">
          <a-button type="primary" @click="nextStep" :disabled="!qrCodeImage">
            下一步
          </a-button>
        </div>
      </div>

      <!-- 步骤2: 输入验证码 -->
      <div v-if="currentStep === 1" class="step-content">
        <div class="verify-section">
          <h3>输入验证码</h3>
          <p>请输入Google验证器中显示的6位数字验证码：</p>
          <a-input
            v-model:value="verifyCode"
            placeholder="请输入6位验证码"
            size="large"
            class="verify-input"
            type="text"
            :maxlength="6"
            @input="onCodeInput"
            @keyup.enter="verifyCode.length === 6 && handleVerify()"
          />
          <!-- 调试信息 -->
          <div style="margin-top: 10px; font-size: 12px; color: #666;">
            当前输入: {{ verifyCode }} | 长度: {{ verifyCode.length }} | 有效: {{ isCodeValid }}
          </div>
          <div class="step-actions">
            <a-button @click="prevStep">上一步</a-button>
            <a-button
              type="primary"
              @click="handleVerify"
              :loading="verifying"
              :disabled="verifyCode.length !== 6"
            >
              验证并绑定
            </a-button>
            <!-- 测试按钮 -->
            <a-button @click="testButton" type="outline">
              测试按钮
            </a-button>
          </div>
        </div>
      </div>

      <!-- 步骤3: 保存备用码 -->
      <div v-if="currentStep === 2" class="step-content">
        <div class="backup-section">
          <h3>备用恢复码</h3>
          <a-alert
            type="warning"
            message="重要提示"
            description="请将这些备用恢复码保存在安全的地方。当您无法使用Google验证器时，可以使用这些代码进行身份验证。每个代码只能使用一次。"
            show-icon
            class="mb-4"
          />
          <div class="backup-codes">
            <div v-for="(code, index) in backupCodes" :key="index" class="backup-code">
              {{ code }}
            </div>
          </div>
          <div class="step-actions">
            <a-button @click="copyBackupCodes">
              <template #icon>
                <icon-copy />
              </template>
              复制所有备用码
            </a-button>
            <a-button type="primary" @click="handleComplete">
              我已保存，完成绑定
            </a-button>
          </div>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { ref, computed, watch, nextTick } from 'vue'
import { Message } from '@arco-design/web-vue'
import { generateGoogleCaptcha, validateGoogleCaptcha } from '@/apis'

interface Props {
  modelValue: boolean
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'success'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 状态管理
const currentStep = ref(0)
const verifying = ref(false)

// 数据
const qrCodeImage = ref('')
const secretKey = ref('')
const backupCodes = ref<string[]>([])
const verifyCode = ref('')

// 计算属性：验证码是否有效
const isCodeValid = computed(() => {
  return verifyCode.value && verifyCode.value.length === 6
})

// 生成Google验证码
const generateCaptcha = async () => {
  try {
    const { data } = await generateGoogleCaptcha()
    qrCodeImage.value = data.qrCodeImage
    secretKey.value = data.secretKey
    backupCodes.value = data.backupCodes
  } catch (error) {
    Message.error('生成Google验证码失败')
    console.error(error)
  }
}

// 步骤控制
const nextStep = () => {
  if (currentStep.value < 2) {
    currentStep.value++
  }
}

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

// 输入监听函数
const onCodeInput = (value: string) => {
  console.log('输入事件:', value, '长度:', value.length)
  verifyCode.value = value
  // 强制触发响应式更新
  nextTick(() => {
    console.log('更新后的值:', verifyCode.value, '长度:', verifyCode.value.length)
  })
}

// 测试按钮
const testButton = () => {
  console.log('测试按钮被点击')
  console.log('当前验证码:', verifyCode.value)
  console.log('验证码长度:', verifyCode.value.length)
  console.log('按钮是否应该被禁用:', verifyCode.value.length !== 6)
  Message.info(`当前验证码: ${verifyCode.value}, 长度: ${verifyCode.value.length}`)
}

// 验证码验证
const handleVerify = async () => {
  if (verifyCode.value.length !== 6) {
    Message.warning('请输入6位验证码')
    return
  }

  verifying.value = true
  try {
    console.log('开始验证验证码:', verifyCode.value)
    const response = await validateGoogleCaptcha({ code: verifyCode.value })
    console.log('完整响应:', response)
    const { data } = response
    console.log('验证结果:', data)
    
    if (data.valid) {
      Message.success('验证成功！')
      nextStep()
    } else {
      const errorMsg = data.message || '验证失败，请检查验证码是否正确'
      console.error('验证失败:', errorMsg)
      Message.error(errorMsg)
    }
  } catch (error: any) {
    console.error('验证请求失败:', error)
    
    let errorMessage = '验证失败，请重试'
    
    if (error.response) {
      // 服务器响应错误
      console.error('服务器响应错误:', error.response.data)
      errorMessage = error.response.data?.message || error.response.data || '服务器错误'
    } else if (error.request) {
      // 网络请求错误
      console.error('网络请求错误:', error.request)
      errorMessage = '网络连接失败，请检查网络连接'
    } else {
      // 其他错误
      console.error('其他错误:', error.message)
      errorMessage = error.message || '未知错误'
    }
    
    Message.error(errorMessage)
  } finally {
    verifying.value = false
  }
}

// 复制功能
const copySecretKey = async () => {
  try {
    await navigator.clipboard.writeText(secretKey.value)
    Message.success('密钥已复制到剪贴板')
  } catch (error) {
    Message.error('复制失败，请手动复制')
  }
}

const copyBackupCodes = async () => {
  try {
    const codesText = backupCodes.value.join('\n')
    await navigator.clipboard.writeText(codesText)
    Message.success('备用码已复制到剪贴板')
  } catch (error) {
    Message.error('复制失败，请手动复制')
  }
}

// 完成绑定
const handleComplete = () => {
  Message.success('Google验证器绑定成功！')
  emit('success')
  handleCancel()
}

// 取消操作
const handleCancel = () => {
  visible.value = false
  // 重置状态
  currentStep.value = 0
  qrCodeImage.value = ''
  secretKey.value = ''
  backupCodes.value = []
  verifyCode.value = ''
  verifying.value = false
}

// 打开模态框时生成验证码
watch(visible, (newVal) => {
  if (newVal) {
    generateCaptcha()
  }
})

// 调试：监听验证码变化
watch(verifyCode, (newVal) => {
  console.log('验证码变化:', newVal, '长度:', newVal.length, '有效:', isCodeValid.value)
})
</script>

<style lang="scss" scoped>
.google-captcha-modal {
  .step-content {
    min-height: 400px;
    padding: 20px 0;
  }

  .qr-section {
    display: flex;
    gap: 30px;
    align-items: flex-start;

    .qr-code-container {
      flex-shrink: 0;
      width: 200px;
      height: 200px;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 1px solid var(--color-border-2);
      border-radius: 8px;
      background: #fff;

      .qr-code {
        width: 180px;
        height: 180px;
      }
    }

    .qr-instructions {
      flex: 1;

      h3 {
        margin-bottom: 16px;
        color: var(--color-text-1);
      }

      ol {
        margin-bottom: 20px;
        padding-left: 20px;

        li {
          margin-bottom: 8px;
          color: var(--color-text-2);
        }
      }

      .secret-key {
        p {
          margin-bottom: 8px;
          color: var(--color-text-2);
          font-size: 14px;
        }

        .secret-input {
          font-family: 'Courier New', monospace;
          font-size: 12px;
        }
      }
    }
  }

  .verify-section {
    text-align: center;

    h3 {
      margin-bottom: 16px;
      color: var(--color-text-1);
    }

    p {
      margin-bottom: 24px;
      color: var(--color-text-2);
    }

    .verify-input {
      max-width: 300px;
      margin: 0 auto 30px;
      text-align: center;
      font-size: 18px;
      letter-spacing: 4px;
    }
  }

  .backup-section {
    h3 {
      margin-bottom: 16px;
      color: var(--color-text-1);
    }

    .backup-codes {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;
      margin: 20px 0 30px;
      padding: 20px;
      background: var(--color-fill-2);
      border-radius: 8px;

      .backup-code {
        padding: 8px 12px;
        background: #fff;
        border-radius: 4px;
        font-family: 'Courier New', monospace;
        font-size: 14px;
        text-align: center;
        border: 1px solid var(--color-border-2);
      }
    }
  }

  .step-actions {
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-top: 30px;
  }
}
</style>
