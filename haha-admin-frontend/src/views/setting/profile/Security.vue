<template>
  <a-card title="安全设置" bordered class="gradient-card">
    <div v-for="item in modeList" :key="item.title">
      <div class="item">
        <div class="icon-wrapper"><GiSvgIcon :name="item.icon" :size="26" /></div>
        <div class="info">
          <div class="info-top">
            <span class="label">{{ item.title }}</span>
            <span class="bind">
              <icon-check-circle-fill v-if="item.status" :size="14" class="success" />
              <icon-exclamation-circle-fill v-else :size="14" class="warning" />
              <span style="font-size: 12px" :class="item.status ? 'success' : 'warning'">{{ item.statusString }}</span>
            </span>
          </div>
          <div class="info-desc">
            <span class="value">{{ item.value }}</span>
            {{ item.subtitle }}
          </div>
        </div>
        <div class="btn-wrapper">
          <a-button
            v-if="item.jumpMode === 'modal'"
            class="btn"
            :type="item.status ? 'secondary' : 'primary'"
            @click="onUpdate(item.type, item.status)"
          >
            {{ ['password'].includes(item.type) || item.status ? '修改' : '绑定' }}
          </a-button>
        </div>
      </div>
    </div>
  </a-card>

  <VerifyModel ref="verifyModelRef" />
  <GoogleCaptchaModal v-model="googleModalVisible" @success="onGoogleBindSuccess" />
</template>

<script lang="ts" setup>
import { ref, computed, watch, onMounted } from 'vue'
import { Modal } from '@arco-design/web-vue'
import type { ModeItem } from '../type'
import VerifyModel from '../components/VerifyModel.vue'
import GoogleCaptchaModal from './components/GoogleCaptchaModal.vue'
import { checkGoogleCaptchaStatus } from '@/apis'
import { useUserStore } from '@/stores'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)

// Google验证码状态
const googleCaptchaStatus = ref(false)
const googleModalVisible = ref(false)

const modeList = ref<ModeItem[]>([])

// 初始化列表数据
const initModeList = () => {
  const user = userInfo.value || {}
  
  modeList.value = [
    {
      title: '安全手机',
      icon: 'phone-color',
      value: user.phone || '手机号',
      subtitle: '可用于登录、身份验证、密码找回、通知接收',
      type: 'phone',
      jumpMode: 'modal',
      status: !!user.phone,
      statusString: user.phone ? '已绑定' : '未绑定'
    },
    {
      title: '安全邮箱',
      icon: 'email-color',
      value: user.email || '邮箱',
      subtitle: '可用于登录、身份验证、密码找回、通知接收',
      type: 'email',
      jumpMode: 'modal',
      status: !!user.email,
      statusString: user.email ? '已绑定' : '未绑定'
    },
    {
      title: '登录密码',
      icon: 'password-color',
      value: user.pwdResetTime ? '已设置' : '未设置',
      subtitle: user.pwdResetTime ? '为了您的账号安全，建议定期修改密码' : '请设置密码，可通过账号+密码登录',
      type: 'password',
      jumpMode: 'modal',
      status: !!user.pwdResetTime,
      statusString: user.pwdResetTime ? '已设置' : '未设置'
    },
    {
      title: 'Google验证器',
      icon: 'google-color',
      value: googleCaptchaStatus.value ? 'Google Authenticator' : '未绑定',
      subtitle: '使用Google验证器提供额外的安全保障，防止账号被盗用',
      type: 'google',
      jumpMode: 'modal',
      status: googleCaptchaStatus.value,
      statusString: googleCaptchaStatus.value ? '已绑定' : '未绑定'
    }
  ]
}

const verifyModelRef = ref<InstanceType<typeof VerifyModel>>()

// 修改
const onUpdate = (type: string, isAlreadyBound?: boolean) => {
  if (type === 'google') {
    // 如果已经绑定Google验证器，需要二次确认
    if (isAlreadyBound && googleCaptchaStatus.value) {
      Modal.warning({
        title: '重新绑定Google验证器',
        content: '重新绑定将会解除当前的Google验证器绑定，您需要使用新的密钥重新配置验证器应用。请确保您已保存好备用恢复码，以防无法访问验证器应用。确定要继续吗？',
        okText: '确定重新绑定',
        cancelText: '取消',
        onOk: () => {
          googleModalVisible.value = true
        }
      })
    } else {
      googleModalVisible.value = true
    }
  } else {
    verifyModelRef.value?.open(type)
  }
}

// Google验证码绑定成功回调
const onGoogleBindSuccess = () => {
  googleCaptchaStatus.value = true
  initModeList() // 重新初始化列表
}

// 检查Google验证码绑定状态
const checkGoogleStatus = async () => {
  try {
    const { data } = await checkGoogleCaptchaStatus()
    googleCaptchaStatus.value = data.bound
  } catch (error) {
    console.error('检查Google验证码状态失败:', error)
  }
}

// 系统参数相关功能已被移除

// 初始化数据
const initData = async () => {
  try {
    await checkGoogleStatus()
    initModeList()
  } catch (error) {
    console.error('初始化数据失败:', error)
    // 即使失败也要初始化列表
    initModeList()
  }
}

// 立即初始化列表
initModeList()

// 监听用户信息变化
watch(
  () => userInfo.value,
  () => {
    initModeList()
  },
  { deep: true }
)

// 监听Google验证码状态变化
watch(
  () => googleCaptchaStatus.value,
  () => {
    initModeList()
  }
)

onMounted(() => {
  initData()
})
</script>

<style lang="scss" scoped></style>
