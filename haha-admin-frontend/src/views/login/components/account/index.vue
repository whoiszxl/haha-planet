<template>
  <a-form
    ref="formRef"
    :model="form"
    :rules="rules"
    :label-col-style="{ display: 'none' }"
    :wrapper-col-style="{ flex: 1 }"
    size="large"
    @submit="handleLogin"
  >
    <a-form-item field="username" hide-label>
      <a-input v-model="form.username" placeholder="请输入用户名" allow-clear />
    </a-form-item>
    <a-form-item field="password" hide-label>
      <a-input-password v-model="form.password" placeholder="请输入密码" />
    </a-form-item>
    <a-form-item field="captcha" hide-label>
      <a-input v-model="form.captcha" placeholder="请输入验证码" :max-length="4" allow-clear style="flex: 1 1" />
      <div class="captcha-container" @click="getCaptcha">
        <img :src="captchaImgBase64" alt="验证码" class="captcha" />
        <div v-if="form.expired" class="overlay">
          <p>已过期，请刷新</p>
        </div>
      </div>
    </a-form-item>
    <!-- Google验证码输入框 -->
    <a-form-item v-if="needGoogleCode" field="googleCode" hide-label>
      <a-input
        v-model="form.googleCode"
        placeholder="请输入Google验证码"
        :max-length="6"
        allow-clear
      />
    </a-form-item>
    <a-form-item>
      <a-row justify="space-between" align="center" class="w-full">
        <a-checkbox v-model="loginConfig.rememberMe">记住我</a-checkbox>
        <a-link>忘记密码</a-link>
      </a-row>
    </a-form-item>
    <a-form-item>
      <a-space direction="vertical" fill class="w-full">
        <a-button class="btn" type="primary" :loading="loading" html-type="submit" size="large" long>立即登录</a-button>
      </a-space>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { type FormInstance, Message } from '@arco-design/web-vue'
import { useStorage } from '@vueuse/core'
import { checkGoogleCaptchaStatusByUsername, getImageCaptcha } from '@/apis'
import { useUserStore } from '@/stores'
import { encryptByRsa } from '@/utils/encrypt'

const loginConfig = useStorage('login-config', {
  rememberMe: true,
  username: 'banana', // 演示默认值
  password: '123456' // 演示默认值
  // username: debug ? 'admin' : '', // 演示默认值
  // password: debug ? 'admin123' : '', // 演示默认值
})

const formRef = ref<FormInstance>()
const form = reactive({
  username: loginConfig.value.username,
  password: loginConfig.value.password,
  captcha: '',
  uuid: '',
  expired: false,
  googleCode: '' // Google验证码
})
// 是否需要Google验证码
const needGoogleCode = ref(false)

const rules: FormInstance['rules'] = {
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码' }],
  captcha: [{ required: true, message: '请输入验证码' }],
  googleCode: [{
    validator: (value, callback) => {
      if (needGoogleCode.value && (!value || value.length !== 6)) {
        callback('请输入6位Google验证码')
        return
      }
      callback()
    }
  }]
}

// 验证码过期定时器
let timer
const startTimer = (expireTime: number) => {
  if (timer) {
    clearTimeout(timer)
  }
  const remainingTime = expireTime - Date.now()
  if (remainingTime <= 0) {
    form.expired = true
    return
  }
  timer = setTimeout(() => {
    form.expired = true
  }, remainingTime)
}

const captchaImgBase64 = ref()
// 获取验证码
const getCaptcha = () => {
  getImageCaptcha().then((res) => {
    const { uuid, img, expireTime } = res.data
    form.uuid = uuid
    captchaImgBase64.value = img
    form.expired = false
    startTimer(expireTime)
  })
}

// 检查Google验证码绑定状态
const checkGoogleBindStatus = async () => {
  try {
    // 只有在用户名输入后才检查
    if (!form.username || form.username.length < 2) {
      needGoogleCode.value = false
      return
    }
    
    // 通过公开接口检查用户是否绑定Google验证码
    const response = await checkGoogleCaptchaStatusByUsername(form.username)
    needGoogleCode.value = response.data.bound || false
    
    console.log(`用户 ${form.username} Google验证码绑定状态:`, needGoogleCode.value)
    
    // 如果不需要Google验证码，清空输入框
    if (!needGoogleCode.value) {
      form.googleCode = ''
    }
  } catch {
    needGoogleCode.value = false
    form.googleCode = ''
  }
}

const userStore = useUserStore()
const router = useRouter()
const loading = ref(false)
// 登录
const handleLogin = async () => {
  try {
    const isInvalid = await formRef.value?.validate()
    if (isInvalid) return
    loading.value = true
    await userStore.accountLogin({
      username: form.username,
      password: encryptByRsa(form.password) || '',
      captcha: form.captcha,
      uuid: form.uuid,
      googleCode: form.googleCode // 传递Google验证码
    })
    const { redirect, ...othersQuery } = router.currentRoute.value.query
    router.push({
      path: (redirect as string) || '/',
      query: {
        ...othersQuery
      }
    })
    const { rememberMe } = loginConfig.value
    loginConfig.value.username = rememberMe ? form.username : ''
    Message.success('欢迎使用')
  } catch {
    getCaptcha()
    form.captcha = ''
    form.googleCode = '' // 清空Google验证码
  } finally {
    loading.value = false
  }
}

// 监听用户名变化
watch(
  () => form.username,
  () => {
    // 防抖处理，用户停止输入500ms后再检查
    clearTimeout(checkTimer)
    checkTimer = setTimeout(() => {
      checkGoogleBindStatus()
    }, 500)
  },
  { immediate: false }
)

// 检查定时器
let checkTimer: ReturnType<typeof setTimeout>

onMounted(() => {
  getCaptcha()
})

// 组件销毁时清理定时器
onBeforeUnmount(() => {
  if (timer) {
    clearTimeout(timer)
  }
  if (checkTimer) {
    clearTimeout(checkTimer)
  }
})
</script>

<style lang="scss" scoped>
.arco-input-wrapper,
:deep(.arco-select-view-single) {
  height: 40px;
  border-radius: 4px;
  font-size: 13px;
  background-color: rgba(30, 41, 59, 0.8);
  border: 1px solid rgba(71, 85, 105, 0.3);
  transition: all 0.3s ease;
}

.arco-input-wrapper:hover {
  border-color: rgba(96, 165, 250, 0.5);
  background-color: rgba(30, 41, 59, 0.9);
}

.arco-input-wrapper:focus-within {
  border-color: #60a5fa;
  background-color: rgba(30, 41, 59, 0.95);
  box-shadow: 0 0 0 2px rgba(96, 165, 250, 0.1);
}

.arco-input-wrapper.arco-input-error {
  background-color: rgba(127, 29, 29, 0.8);
  border-color: rgba(239, 68, 68, 0.5);
}

.arco-input-wrapper.arco-input-error:hover {
  background-color: rgba(127, 29, 29, 0.9);
  border-color: rgba(239, 68, 68, 0.7);
}

.arco-input-wrapper :deep(.arco-input) {
  font-size: 13px;
  color: #f1f5f9;
  background-color: transparent;
}

.arco-input-wrapper :deep(.arco-input::placeholder) {
  color: #94a3b8;
}

:deep(.arco-checkbox-label) {
  color: #f1f5f9;
}

:deep(.arco-link) {
  color: #60a5fa;
}

.captcha {
  width: 111px;
  height: 36px;
  margin: 0 0 0 5px;
}

.btn {
  height: 40px;
}

.captcha-container {
  position: relative;
  display: inline-block;
  cursor: pointer;
}

.captcha-container {
  position: relative;
  display: inline-block;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(51, 51, 51, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
}

.overlay p {
  font-size: 12px;
  color: white;
}
</style>
