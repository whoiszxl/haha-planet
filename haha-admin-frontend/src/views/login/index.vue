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
          <div class="login-right__content">
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
          <div class="login-right__oauth">
            <div class="oauth-divider">
              <div class="divider-line"></div>
              <span class="divider-text">其他登录方式</span>
              <div class="divider-line"></div>
            </div>
            <div class="oauth-methods">
              <div 
                v-if="isEmailLogin" 
                class="oauth-method" 
                @click="toggleLoginMode"
              >
                <div class="method-icon">
                  <icon-user />
                </div>
                <div class="method-content">
                  <span class="method-title">账号登录</span>
                  <span class="method-desc">使用用户名或手机号登录</span>
                </div>
                <div class="method-arrow">
                  <icon-right />
                </div>
              </div>
              <div 
                v-else 
                class="oauth-method" 
                @click="toggleLoginMode"
              >
                <div class="method-icon">
                  <icon-email />
                </div>
                <div class="method-content">
                  <span class="method-title">邮箱登录</span>
                  <span class="method-desc">使用邮箱地址快速登录</span>
                </div>
                <div class="method-arrow">
                  <icon-right />
                </div>
              </div>
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
          <div class="login-right__content">
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
        </div>
      </a-col>
    </a-row>
    <div class="login-right__oauth">
      <div class="oauth-divider">
        <div class="divider-line"></div>
        <span class="oauth-divider-text">其他登录方式</span>
        <div class="divider-line"></div>
      </div>
      <div class="oauth-methods-mobile">
        <div 
          v-if="isEmailLogin" 
          class="oauth-method-mobile" 
          @click="toggleLoginMode"
        >
          <div class="method-icon-mobile">
            <icon-user />
          </div>
          <div class="method-content-mobile">
            <span class="method-title-mobile">账号登录</span>
            <span class="method-desc-mobile">用户名/手机号</span>
          </div>
          <div class="method-arrow-mobile">
            <icon-right />
          </div>
        </div>
        <div 
          v-else 
          class="oauth-method-mobile" 
          @click="toggleLoginMode"
        >
          <div class="method-icon-mobile">
            <icon-email />
          </div>
          <div class="method-content-mobile">
            <span class="method-title-mobile">邮箱登录</span>
            <span class="method-desc-mobile">邮箱地址</span>
          </div>
          <div class="method-arrow-mobile">
            <icon-right />
          </div>
        </div>
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
    height: calc(100vh - 104px);
    display: flex;
    flex-direction: column;
    padding: 0;
    box-sizing: border-box;
    
    &__content {
      flex: 1;
      padding: 30px 30px 100px;
      display: flex;
      flex-direction: column;
      overflow-y: auto;
    }
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
      padding: 16px 20px 20px;
      background: linear-gradient(180deg, transparent 0%, rgba(15, 23, 42, 0.98) 20%, rgba(15, 23, 42, 0.99) 100%);
      backdrop-filter: blur(25px);
      border-top: 1px solid rgba(71, 85, 105, 0.2);
      z-index: 1000;
      
      .oauth-divider {
        display: flex;
        align-items: center;
        margin: 20px 0 16px;
        gap: 12px;
        
        .divider-line {
          flex: 1;
          height: 1px;
          background: linear-gradient(90deg, transparent 0%, rgba(71, 85, 105, 0.3) 20%, rgba(71, 85, 105, 0.5) 50%, rgba(71, 85, 105, 0.3) 80%, transparent 100%);
        }
        
        &-text {
          color: #94a3b8;
          font-size: 11px;
          font-weight: 500;
          letter-spacing: 0.8px;
          text-transform: uppercase;
          white-space: nowrap;
          padding: 4px 12px;
          background: linear-gradient(180deg, rgba(15, 23, 42, 0.95) 0%, rgba(30, 41, 59, 0.9) 100%);
          border-radius: 12px;
          border: 1px solid rgba(71, 85, 105, 0.2);
          backdrop-filter: blur(10px);
          position: relative;
          
          &::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: calc(100% + 8px);
            height: 16px;
            background: radial-gradient(ellipse, rgba(15, 23, 42, 0.8) 0%, transparent 70%);
            transform: translate(-50%, -50%);
            z-index: -1;
          }
        }
      }
      
      .oauth-methods-mobile {
        display: flex;
        justify-content: center;
        align-items: center;
        
        .oauth-method-mobile {
          display: flex;
          align-items: center;
          padding: 12px 16px;
          background: linear-gradient(135deg, rgba(30, 41, 59, 0.4) 0%, rgba(15, 23, 42, 0.5) 100%);
          border: 1px solid rgba(71, 85, 105, 0.4);
          border-radius: 24px;
          cursor: pointer;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          position: relative;
          overflow: hidden;
          backdrop-filter: blur(15px);
          min-width: 140px;
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(96, 165, 250, 0.08), transparent);
            transition: left 0.5s ease;
          }
          
          .method-icon-mobile {
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, rgba(96, 165, 250, 0.15) 0%, rgba(67, 56, 202, 0.12) 100%);
            border-radius: 8px;
            margin-right: 10px;
            transition: all 0.3s ease;
            border: 1px solid rgba(96, 165, 250, 0.2);
            
            svg {
              font-size: 16px;
              color: #60a5fa;
              transition: all 0.3s ease;
            }
          }
          
          .method-content-mobile {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 1px;
            
            .method-title-mobile {
              color: #f1f5f9;
              font-size: 12px;
              font-weight: 600;
              line-height: 1.3;
              transition: all 0.3s ease;
            }
            
            .method-desc-mobile {
              color: #94a3b8;
              font-size: 10px;
              font-weight: 400;
              line-height: 1.2;
              transition: all 0.3s ease;
            }
          }
          
          .method-arrow-mobile {
            width: 20px;
            height: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s ease;
            
            svg {
              font-size: 12px;
              color: #64748b;
              transition: all 0.3s ease;
            }
          }
          
          &:hover {
            background: linear-gradient(135deg, rgba(30, 58, 138, 0.3) 0%, rgba(67, 56, 202, 0.25) 100%);
            border: 1px solid rgba(96, 165, 250, 0.5);
            transform: translateY(-1px) scale(1.02);
            box-shadow: 
              0 4px 20px rgba(96, 165, 250, 0.12),
              0 0 0 1px rgba(96, 165, 250, 0.15),
              inset 0 1px 0 rgba(255, 255, 255, 0.08);
            
            &::before {
              left: 100%;
            }
            
            .method-icon-mobile {
              background: linear-gradient(135deg, rgba(96, 165, 250, 0.25) 0%, rgba(67, 56, 202, 0.2) 100%);
              border: 1px solid rgba(96, 165, 250, 0.4);
              transform: scale(1.05);
              
              svg {
                color: #93c5fd;
                transform: scale(1.1);
              }
            }
            
            .method-content-mobile {
              .method-title-mobile {
                color: #f8fafc;
              }
              
              .method-desc-mobile {
                color: #cbd5e1;
              }
            }
            
            .method-arrow-mobile {
              transform: translateX(2px);
              
              svg {
                color: #60a5fa;
              }
            }
          }
          
          &:active {
            transform: translateY(0) scale(1.01);
            transition: all 0.1s ease;
          }
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
      width: 90%;
      max-width: 1000px;
      height: 560px;
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
    padding: 0;
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
    
    &__content {
      flex: 1;
      padding: 30px 30px 80px;
      display: flex;
      flex-direction: column;
      overflow-y: auto;
    }
    &__title {
      color: #f1f5f9;
      font-weight: 600;
      font-size: 20px;
      line-height: 32px;
      margin-bottom: 16px;
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
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 16px 30px 20px;
      background: linear-gradient(180deg, transparent 0%, rgba(15, 23, 42, 0.8) 30%, rgba(15, 23, 42, 0.95) 100%);
      backdrop-filter: blur(10px);
      border-top: 1px solid rgba(71, 85, 105, 0.1);
      z-index: 10;
      
      .oauth-divider {
        display: flex;
        align-items: center;
        margin: 0 0 12px;
        gap: 12px;
        
        .divider-line {
          flex: 1;
          height: 1px;
          background: linear-gradient(90deg, transparent 0%, rgba(71, 85, 105, 0.4) 20%, rgba(71, 85, 105, 0.6) 50%, rgba(71, 85, 105, 0.4) 80%, transparent 100%);
        }
        
        .divider-text {
          color: #94a3b8;
          font-size: 12px;
          font-weight: 500;
          letter-spacing: 0.8px;
          text-transform: uppercase;
          white-space: nowrap;
          padding: 0 4px;
          position: relative;
          
          &::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: calc(100% + 16px);
            height: 20px;
            background: radial-gradient(ellipse, rgba(15, 23, 42, 0.8) 0%, transparent 70%);
            transform: translate(-50%, -50%);
            z-index: -1;
          }
        }
      }
      
      .oauth-methods {
        display: flex;
        justify-content: center;
        
        .oauth-method {
          display: flex;
          align-items: center;
          padding: 10px 16px;
          background: linear-gradient(135deg, rgba(30, 41, 59, 0.4) 0%, rgba(15, 23, 42, 0.5) 100%);
          border: 1px solid rgba(71, 85, 105, 0.3);
          border-radius: 12px;
          cursor: pointer;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          position: relative;
          overflow: hidden;
          backdrop-filter: blur(20px);
          min-width: 140px;
          max-width: 180px;
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(96, 165, 250, 0.08), transparent);
            transition: left 0.5s ease;
          }
          
          .method-icon {
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, rgba(96, 165, 250, 0.15) 0%, rgba(67, 56, 202, 0.12) 100%);
            border-radius: 8px;
            margin-right: 12px;
            transition: all 0.3s ease;
            border: 1px solid rgba(96, 165, 250, 0.2);
            flex-shrink: 0;
            
            svg {
              font-size: 16px;
              color: #60a5fa;
              transition: all 0.3s ease;
            }
          }
          
          .method-content {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 1px;
            min-width: 0;
            
            .method-title {
              color: #f1f5f9;
              font-size: 13px;
              font-weight: 600;
              line-height: 1.3;
              transition: all 0.3s ease;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
            }
            
            .method-desc {
              color: #94a3b8;
              font-size: 11px;
              font-weight: 400;
              line-height: 1.2;
              transition: all 0.3s ease;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
            }
          }
          
          .method-arrow {
            width: 20px;
            height: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s ease;
            flex-shrink: 0;
            
            svg {
              font-size: 12px;
              color: #64748b;
              transition: all 0.3s ease;
            }
          }
          
          &:hover {
            background: linear-gradient(135deg, rgba(30, 58, 138, 0.3) 0%, rgba(67, 56, 202, 0.25) 100%);
            border: 1px solid rgba(96, 165, 250, 0.5);
            transform: translateY(-1px);
            box-shadow: 
              0 4px 16px rgba(96, 165, 250, 0.12),
              0 0 0 1px rgba(96, 165, 250, 0.2),
              inset 0 1px 0 rgba(255, 255, 255, 0.08);
            
            &::before {
              left: 100%;
            }
            
            .method-icon {
              background: linear-gradient(135deg, rgba(96, 165, 250, 0.25) 0%, rgba(67, 56, 202, 0.2) 100%);
              border: 1px solid rgba(96, 165, 250, 0.4);
              transform: scale(1.05);
              
              svg {
                color: #93c5fd;
                transform: scale(1.05);
              }
            }
            
            .method-content {
              .method-title {
                color: #f8fafc;
              }
              
              .method-desc {
                color: #cbd5e1;
              }
            }
            
            .method-arrow {
              transform: translateX(2px);
              
              svg {
                color: #60a5fa;
              }
            }
          }
          
          &:active {
            transform: translateY(0) scale(0.98);
            transition: all 0.1s ease;
          }
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
