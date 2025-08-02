<template>
  <div class="login pc">
    <h3 class="login-logo">
      <img v-if="logo" :src="logo" alt="logo" />
      <img v-else src="/logo.svg" alt="logo" />
      <span>{{ title }}</span>
    </h3>

    <a-row align="stretch" class="login-box">
      <a-col :xs="0" :sm="12" :md="13">
        <div class="login-left">
          <img class="login-left__img" :src="randomBanner" alt="banner" />
        </div>
      </a-col>
      <a-col :xs="24" :sm="12" :md="11">
        <div class="login-right">
          <h3 v-if="isEmailLogin" class="login-right__title">邮箱登录</h3>
          <EmailLogin v-show="isEmailLogin" />
          <a-tabs v-show="!isEmailLogin" class="login-right__form">
            <a-tab-pane key="1" title="账号登录">
              <AccountLogin />
            </a-tab-pane>
            <a-tab-pane key="2" title="手机号登录">
              <PhoneLogin />
            </a-tab-pane>
          </a-tabs>
          <div class="login-right__oauth">
            <a-divider orientation="center">其他登录方式</a-divider>
            <div class="list">
              <div v-if="isEmailLogin" class="mode item" @click="toggleLoginMode"><icon-user /> 账号/手机号登录</div>
              <div v-else class="mode item" @click="toggleLoginMode"><icon-email /> 邮箱登录</div>

            </div>
          </div>
        </div>
      </a-col>
    </a-row>

    <div v-if="isDesktop" class="footer">
      <div class="beian">
        <div class="below text" v-html="appStore.getCopyright()"></div>
      </div>
    </div>

    <GiThemeBtn class="theme-btn" />
    <Background />
  </div>
  <div class="login h5">
    <div class="login-logo">
      <img v-if="logo" :src="logo" alt="logo" />
      <img v-else src="/logo.svg" alt="logo" />
      <span>{{ title }}</span>
    </div>
    <a-row align="stretch" class="login-box">
      <a-col :xs="24" :sm="12" :md="11">
        <div class="login-right">
          <h3 v-if="isEmailLogin" class="login-right__title">邮箱登录</h3>
          <EmailLogin v-show="isEmailLogin" />
          <a-tabs v-show="!isEmailLogin" class="login-right__form">
            <a-tab-pane key="1" title="账号登录">
              <AccountLogin />
            </a-tab-pane>
            <a-tab-pane key="2" title="手机号登录">
              <PhoneLogin />
            </a-tab-pane>
          </a-tabs>
        </div>
      </a-col>
    </a-row>
    <div class="login-right__oauth">
      <a-divider orientation="center">其他登录方式</a-divider>
      <div class="list">
        <div v-if="isEmailLogin" class="mode item" @click="toggleLoginMode"><icon-user /> 账号/手机号登录</div>
        <div v-else class="mode item" @click="toggleLoginMode"><icon-email /> 邮箱登录</div>

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Background from './components/background/index.vue'
import AccountLogin from './components/account/index.vue'
import PhoneLogin from './components/phone/index.vue'
import EmailLogin from './components/email/index.vue'

import { useAppStore } from '@/stores'
import { useDevice } from '@/hooks'
import { ref, onBeforeMount } from 'vue'

// 导入所有横幅图片
import banner1 from '@/assets/images/banner1.png'
import banner2 from '@/assets/images/banner2.png'
import banner3 from '@/assets/images/banner3.png'
import banner4 from '@/assets/images/banner4.png'
import banner5 from '@/assets/images/banner5.png'
import banner6 from '@/assets/images/banner6.png'
import banner7 from '@/assets/images/banner7.png'
import banner8 from '@/assets/images/banner8.png'

defineOptions({ name: 'Login' })

const { isDesktop } = useDevice()
const appStore = useAppStore()
const title = computed(() => appStore.getTitle())
const logo = computed(() => appStore.getLogo())

const isEmailLogin = ref(false)
// 切换登录模式
const toggleLoginMode = () => {
  isEmailLogin.value = !isEmailLogin.value
}



// 横幅图片数组
const banners = [
  banner1, banner2, banner3, banner4, 
  banner5, banner6, banner7, banner8
]

// 随机选择一张图片
const randomBanner = ref('')

onBeforeMount(() => {
  const randomIndex = Math.floor(Math.random() * banners.length)
  randomBanner.value = banners[randomIndex]
})
</script>

<style lang="scss" scoped>
@media screen and (max-width: 570px) {
  .pc {
    display: none !important;
    background-color: white !important;
  }
  .login {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
    background-color: var(--color-bg-5);
    color: #121314;
    &-logo {
      width: 100%;
      height: 104px;
      font-weight: 700;
      font-size: 20px;
      line-height: 32px;
      display: flex;
      padding: 0 20px;
      align-items: center;
      justify-content: start;
      background-image: url('/src/assets/images/login_h5.jpg');
      background-size: 100% 100%;
      box-sizing: border-box;
      img {
        width: 34px;
        height: 34px;
        margin-right: 8px;
      }
    }
    &-box {
      width: 100%;
      display: flex;
      z-index: 999;
    }
  }
  .login-right {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    padding: 30px 30px 0;
    box-sizing: border-box;
    &__title {
      color: #f1f5f9;
      font-weight: 600;
      font-size: 20px;
      line-height: 32px;
      margin-bottom: 20px;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
    }
    &__form {
      :deep(.arco-tabs-nav-tab) {
        display: flex;
        justify-content: start;
        align-items: center;
      }
      :deep(.arco-tabs-tab) {
        color: var(--color-text-2);
        margin: 0 20px 0 0;
      }
      :deep(.arco-tabs-tab-title) {
        font-size: 16px;
        font-weight: 500;
        line-height: 22px;
      }
      :deep(.arco-tabs-content) {
        margin-top: 10px;
      }
      :deep(.arco-tabs-tab-active),
      :deep(.arco-tabs-tab-title:hover) {
        color: rgb(var(--arcoblue-6));
      }
      :deep(.arco-tabs-nav::before) {
        display: none;
      }
      :deep(.arco-tabs-tab-title:before) {
        display: none;
      }
    }
    &__oauth {
      width: 100%;
      position: fixed;
      bottom: 0;
      left: 0;
      padding-bottom: 20px;
      // margin-top: auto;
      // margin-bottom: 20px;
      :deep(.arco-divider-text) {
        color: #64748b;
        font-size: 12px;
        font-weight: 400;
        line-height: 20px;
      }
      .list {
        align-items: center;
        display: flex;
        justify-content: center;
        width: 100%;
        .item {
          margin-right: 15px;
        }
        .mode {
          color: #94a3b8;
          font-size: 12px;
          font-weight: 400;
          line-height: 20px;
          padding: 6px 10px;
          align-items: center;
          border: 1px solid rgba(71, 85, 105, 0.3);
          border-radius: 32px;
          box-sizing: border-box;
          display: flex;
          height: 32px;
          justify-content: center;
          cursor: pointer;
          background: rgba(30, 41, 59, 0.2);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
          .icon {
            width: 21px;
            height: 20px;
          }
        }
        .mode svg {
          font-size: 16px;
          margin-right: 10px;
        }
        .mode:hover,
        .mode svg:hover {
          background: rgba(30, 58, 138, 0.15);
          border: 1px solid rgba(96, 165, 250, 0.4);
          color: #60a5fa;
          box-shadow: 0 0 12px rgba(96, 165, 250, 0.2);
          transform: translateY(-1px);
        }
      }
    }
  }

  .theme-btn {
    position: fixed;
    top: 20px;
    right: 30px;
    z-index: 9999;
  }

  .footer {
    align-items: center;
    box-sizing: border-box;
    position: absolute;
    bottom: 10px;
    z-index: 999;
    .beian {
      .text {
        font-size: 12px;
        font-weight: 400;
        letter-spacing: 0.2px;
        line-height: 20px;
        text-align: center;
      }
      .below {
        align-items: center;
        display: flex;
      }
    }
  }
}
@media screen and (min-width: 571px) {
  .h5 {
    display: none !important;
  }
  .login {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: var(--color-bg-5);
    &-logo {
      position: fixed;
      top: 20px;
      left: 30px;
      z-index: 9999;
      color: #f1f5f9;
      font-weight: 600;
      font-size: 20px;
      line-height: 32px;
      margin-bottom: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
      backdrop-filter: blur(10px);
      background: rgba(15, 23, 42, 0.3);
      padding: 8px 16px;
      border-radius: 12px;
      border: 1px solid rgba(71, 85, 105, 0.2);
      img {
        width: 34px;
        height: 34px;
        margin-right: 8px;
      }
    }
    &-box {
      width: 86%;
      max-width: 850px;
      height: 490px;
      display: flex;
      z-index: 999;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3), 0 0 0 1px rgba(71, 85, 105, 0.1);
      border-radius: 16px;
      overflow: hidden;
      backdrop-filter: blur(20px);
    }
  }

  .login-left {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    overflow: hidden;
    background: linear-gradient(135deg, #0f172a 0%, #1e293b 25%, #334155 50%, #1e293b 75%, #0f172a 100%);
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: radial-gradient(ellipse at 30% 70%, rgba(30, 58, 138, 0.1) 0%, transparent 60%),
                  radial-gradient(ellipse at 70% 30%, rgba(67, 56, 202, 0.08) 0%, transparent 50%);
      z-index: 1;
    }
    
    &__img {
      width: 100%;
      position: absolute;
      bottom: 0;
      right: 0;
      top: 50%;
      left: 50%;
      transform: translateX(-50%) translateY(-50%);
      transition: all 0.3s;
      object-fit: cover;
      z-index: 2;
      opacity: 0.9;
      filter: brightness(1.1) contrast(1.05);
    }
  }

  .login-right {
    width: 100%;
    height: 100%;
    background: rgba(15, 23, 42, 0.95);
    backdrop-filter: blur(20px);
    border-left: 1px solid rgba(71, 85, 105, 0.2);
    display: flex;
    flex-direction: column;
    padding: 30px 30px 0;
    box-sizing: border-box;
    position: relative;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: linear-gradient(180deg, rgba(30, 41, 59, 0.1) 0%, rgba(15, 23, 42, 0.05) 100%);
      pointer-events: none;
    }
    &__title {
      color: #f1f5f9;
      font-weight: 600;
      font-size: 20px;
      line-height: 32px;
      margin-bottom: 20px;
      text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
      position: relative;
      z-index: 1;
    }
    &__form {
      :deep(.arco-tabs-nav-tab) {
        display: flex;
        justify-content: center;
        align-items: center;
      }
      :deep(.arco-tabs-tab) {
        color: #94a3b8;
        transition: all 0.3s ease;
      }
      :deep(.arco-tabs-tab-title) {
        font-size: 16px;
        font-weight: 500;
        line-height: 22px;
      }
      :deep(.arco-tabs-content) {
        margin-top: 10px;
      }
      :deep(.arco-tabs-tab-active),
      :deep(.arco-tabs-tab-title:hover) {
        color: #60a5fa;
        text-shadow: 0 0 8px rgba(96, 165, 250, 0.3);
      }
      :deep(.arco-tabs-nav::before) {
        display: none;
      }
      :deep(.arco-tabs-tab-title:before) {
        display: none;
      }
    }
    &__oauth {
      margin-top: auto;
      margin-bottom: 20px;
      :deep(.arco-divider-text) {
        color: #64748b;
        font-size: 12px;
        font-weight: 400;
        line-height: 20px;
      }
      .list {
        align-items: center;
        display: flex;
        justify-content: center;
        width: 100%;
        .item {
          margin-right: 15px;
        }
        .mode {
          color: #94a3b8;
          font-size: 12px;
          font-weight: 400;
          line-height: 20px;
          padding: 6px 10px;
          align-items: center;
          border: 1px solid rgba(71, 85, 105, 0.3);
          border-radius: 32px;
          box-sizing: border-box;
          display: flex;
          height: 32px;
          justify-content: center;
          cursor: pointer;
          background: rgba(30, 41, 59, 0.2);
          backdrop-filter: blur(10px);
          transition: all 0.3s ease;
          .icon {
            width: 21px;
            height: 20px;
          }
        }
        .mode svg {
          font-size: 16px;
          margin-right: 10px;
        }
        .mode:hover,
        .mode svg:hover {
          background: rgba(30, 58, 138, 0.15);
          border: 1px solid rgba(96, 165, 250, 0.4);
          color: #60a5fa;
          box-shadow: 0 0 12px rgba(96, 165, 250, 0.2);
          transform: translateY(-1px);
        }
      }
    }
  }

  .theme-btn {
    position: fixed;
    top: 20px;
    right: 30px;
    z-index: 9999;
  }

  .footer {
    align-items: center;
    box-sizing: border-box;
    position: absolute;
    bottom: 10px;
    z-index: 999;
    .beian {
      .text {
        font-size: 12px;
        font-weight: 400;
        letter-spacing: 0.2px;
        line-height: 20px;
        text-align: center;
      }
      .below {
        align-items: center;
        display: flex;
      }
    }
  }
}
</style>
