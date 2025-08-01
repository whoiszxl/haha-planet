# Google验证码功能使用说明

## 功能概述

本项目集成了完整的Google验证码（Google Authenticator）功能，包括：

- ✅ Google验证码绑定/解绑
- ✅ 二维码生成和扫描
- ✅ 验证码验证
- ✅ 备用恢复码生成和验证
- ✅ 绑定状态检查
- ✅ 完整的前端UI界面

## 文件结构

```
src/
├── apis/common/
│   ├── captcha.ts              # Google验证码API接口
│   └── type.ts                 # 类型定义
├── views/
│   ├── setting/profile/
│   │   ├── Security.vue        # 安全设置页面（集成Google验证码）
│   │   └── components/
│   │       └── GoogleCaptchaModal.vue  # Google验证码绑定模态框
│   └── test/
│       └── GoogleCaptchaTest.vue       # 测试页面
└── assets/icons/
    └── google-color.svg        # Google图标
```

## API接口

### 1. 生成Google验证码
```typescript
generateGoogleCaptcha(): Promise<GoogleCaptchaResp>
```

### 2. 验证Google验证码
```typescript
validateGoogleCaptcha(data: { code: string }): Promise<ValidateResp>
```

### 3. 检查绑定状态
```typescript
checkGoogleCaptchaStatus(): Promise<GoogleStatusResp>
```

### 4. 生成备用恢复码
```typescript
generateBackupCodes(data: { count?: number }): Promise<BackupCodesResp>
```

### 5. 验证备用恢复码
```typescript
validateBackupCode(data: { backupCode: string }): Promise<ValidateResp>
```

### 6. 解绑Google验证器
```typescript
unbindGoogleCaptcha(): Promise<UnbindResp>
```

## 使用方式

### 1. 在安全设置中使用

访问 `/setting/profile` 页面，在安全设置卡片中可以看到Google验证器选项。

### 2. 测试页面

访问 `/setting/google-test` 页面可以进行完整的功能测试。

### 3. 组件使用

```vue
<template>
  <GoogleCaptchaModal v-model="visible" @success="onBindSuccess" />
</template>

<script setup>
import GoogleCaptchaModal from '@/views/setting/profile/components/GoogleCaptchaModal.vue'

const visible = ref(false)

const onBindSuccess = () => {
  console.log('Google验证器绑定成功')
}
</script>
```

## 绑定流程

1. **扫描二维码**
   - 用户点击"绑定Google验证器"
   - 系统生成密钥和二维码
   - 用户使用Google Authenticator扫描二维码

2. **输入验证码**
   - 用户输入Google Authenticator中显示的6位验证码
   - 系统验证验证码的正确性

3. **保存备用码**
   - 系统生成10个备用恢复码
   - 用户需要安全保存这些备用码
   - 完成绑定流程

## 安全特性

- ✅ 防重放攻击：已使用的验证码不能重复使用
- ✅ 失败次数限制：连续验证失败会触发锁定机制
- ✅ 备用恢复码：设备丢失时的恢复方案
- ✅ 时间窗口容错：支持时间偏差容忍
- ✅ 自动过期清理：定期清理过期数据

## 配置说明

后端配置在 `application-dev.yml` 中：

```yaml
haha:
  captcha:
    google:
      # 存储配置
      storage:
        type: MYSQL  # 存储类型：MEMORY、REDIS、MYSQL
      
      # 安全配置
      security:
        failure-count-limit: 5      # 失败次数限制
        lockout-duration-minutes: 30  # 锁定时长（分钟）
        anti-replay-enabled: true   # 启用防重放
      
      # 二维码配置
      qr-code:
        size: 200                   # 二维码尺寸
        format: PNG                 # 图片格式
```

## 注意事项

1. **时间同步**：确保服务器时间准确，Google验证码基于时间生成
2. **备用码保存**：提醒用户妥善保存备用恢复码
3. **设备安全**：建议用户在安全的设备上进行绑定操作
4. **网络安全**：建议在HTTPS环境下使用

## 故障排除

### 1. 验证码验证失败
- 检查服务器时间是否准确
- 确认用户输入的验证码是最新的
- 检查是否启用了防重放机制

### 2. 二维码无法扫描
- 检查二维码图片是否正常生成
- 确认Google Authenticator应用版本
- 尝试手动输入密钥

### 3. 备用码验证失败
- 确认备用码未被使用过
- 检查备用码格式是否正确
- 验证用户输入是否有误

## 开发调试

1. 启动后端服务：`mvn spring-boot:run`
2. 启动前端服务：`npm run dev`
3. 访问测试页面：`http://localhost:3000/#/setting/google-test`
4. 查看控制台日志进行调试

## 相关链接

- [Google Authenticator官方文档](https://support.google.com/accounts/answer/1066447)
- [TOTP算法规范 RFC 6238](https://tools.ietf.org/html/rfc6238)
- [Spring Boot Starter项目](../haha-backend/haha-starter/haha-starter-captcha/haha-starter-captcha-google/)
