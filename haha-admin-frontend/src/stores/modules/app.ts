import { defineStore } from 'pinia'
import { computed, reactive, toRefs } from 'vue'
import { generate, getRgbStr } from '@arco-design/color'
import { type BasicConfig, listOptionDict } from '@/apis'
import defaultSettings from '@/config/setting.json'

const storeSetup = () => {
  // App配置
  const settingConfig = reactive({ ...defaultSettings }) as App.SettingConfig
  // 页面切换动画类名
  const transitionName = computed(() => (settingConfig.animate ? settingConfig.animateMode : ''))

  // 深色菜单主题色变量
  const themeCSSVar = computed<Record<string, string>>(() => {
    const obj: Record<string, string> = {}
    const list = generate(settingConfig.themeColor, { list: true, dark: true })
    list.forEach((color: string, index: number) => {
      obj[`--primary-${index + 1}`] = getRgbStr(color)
    })
    return obj
  })

  // 设置主题色
  const setThemeColor = (color: string) => {
    if (!color) return
    settingConfig.themeColor = color
    const list = generate(settingConfig.themeColor, { list: true, dark: settingConfig.theme === 'dark' })
    list.forEach((color: string, index: number) => {
      const rgbStr = getRgbStr(color)
      document.body.style.setProperty(`--primary-${index + 1}`, rgbStr)
    })
  }

  // 切换主题 暗黑模式|简白模式
  // 主题类型定义
  type ThemeType = 'light' | 'dark' | 'auto' | 'blue' | 'green'
  
  // 主题配置映射
  const themeConfigs = {
    light: {
      arcoTheme: 'light',
      themeColor: '#165DFF',
      bodyClass: 'theme-light'
    },
    dark: {
      arcoTheme: 'dark', 
      themeColor: '#165DFF',
      bodyClass: 'theme-dark'
    },
    auto: {
      arcoTheme: 'auto',
      themeColor: '#165DFF',
      bodyClass: 'theme-auto'
    },
    blue: {
      arcoTheme: 'light',
      themeColor: '#1890ff',
      bodyClass: 'theme-blue'
    },
    green: {
      arcoTheme: 'light',
      themeColor: '#52c41a', 
      bodyClass: 'theme-green'
    }
  }
  
  // 设置主题
  const setTheme = (theme: ThemeType) => {
    settingConfig.theme = theme
    const config = themeConfigs[theme]
    
    // 移除所有主题类
    Object.values(themeConfigs).forEach(cfg => {
      document.body.classList.remove(cfg.bodyClass)
    })
    
    // 添加当前主题类
    document.body.classList.add(config.bodyClass)
    
    // 处理自动主题
    if (theme === 'auto') {
      const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      if (isDark) {
        document.body.setAttribute('arco-theme', 'dark')
      } else {
        document.body.removeAttribute('arco-theme')
      }
    } else if (config.arcoTheme === 'dark') {
      document.body.setAttribute('arco-theme', 'dark')
    } else {
      document.body.removeAttribute('arco-theme')
    }
    
    // 设置主题色
    setThemeColor(config.themeColor)
  }

  // 兼容原有的toggleTheme方法
  const toggleTheme = (dark: boolean) => {
    setTheme(dark ? 'dark' : 'light')
  }

  // 初始化主题
  const initTheme = () => {
    if (!settingConfig.themeColor) return
    setThemeColor(settingConfig.themeColor)
  }

  // 设置左侧菜单折叠状态
  const setMenuCollapse = (collapsed: boolean) => {
    settingConfig.menuCollapse = collapsed
  }

  // 系统配置配置
  const siteConfig = reactive({}) as BasicConfig
  // 初始化系统配置
  const initSiteConfig = () => {
    // listOptionDict({
    //   code: ['SITE_FAVICON', 'SITE_LOGO', 'SITE_TITLE', 'SITE_COPYRIGHT']
    // }).then((res) => {
    //   const resMap = new Map()
    //   res.data.forEach((item) => {
    //     resMap.set(item.label, item.value)
    //   })
    //   siteConfig.SITE_FAVICON = resMap.get('SITE_FAVICON')
    //   siteConfig.SITE_LOGO = resMap.get('SITE_LOGO')
    //   siteConfig.SITE_TITLE = resMap.get('SITE_TITLE')
    //   siteConfig.SITE_COPYRIGHT = resMap.get('SITE_COPYRIGHT')
    //   document.title = resMap.get('SITE_TITLE')
    //   document
    //     .querySelector('link[rel="shortcut icon"]')
    //     ?.setAttribute('href', resMap.get('SITE_FAVICON') || '/favicon.ico')
    // })
  }

  // 设置系统配置
  const setSiteConfig = (config: BasicConfig) => {
    Object.assign(siteConfig, config)
    document.title = config.SITE_TITLE || ''
    document.querySelector('link[rel="shortcut icon"]')?.setAttribute('href', config.SITE_FAVICON || '/favicon.ico')
  }

  const getFavicon = () => {
    return siteConfig.SITE_FAVICON
  }

  const getLogo = () => {
    return siteConfig.SITE_LOGO
  }

  const getTitle = () => {
    return siteConfig.SITE_TITLE
  }

  const getCopyright = () => {
    return siteConfig.SITE_COPYRIGHT
  }

  return {
    ...toRefs(settingConfig),
    ...toRefs(siteConfig),
    transitionName,
    themeCSSVar,
    toggleTheme,
    setTheme,
    setThemeColor,
    initTheme,
    setMenuCollapse,
    initSiteConfig,
    setSiteConfig,
    getFavicon,
    getLogo,
    getTitle,
    getCopyright
  }
}

export const useAppStore = defineStore('app', storeSetup, { persist: true })
