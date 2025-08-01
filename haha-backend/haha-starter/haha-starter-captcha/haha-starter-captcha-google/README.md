# HaHa Google Captcha Spring Boot Starter

一个功能完整的商业级Google验证码（Google Authenticator）Spring Boot Starter，支持多种存储方式、完善的安全机制和开箱即用的Web API。

## 功能特性

### 🔐 核心功能
- **密钥生成与管理**：自动生成安全的密钥，支持用户绑定/解绑
- **二维码生成**：自动生成Google Authenticator扫码用的二维码
- **验证码验证**：支持时间窗口容差的验证码校验
- **备用恢复码**：生成和验证备用恢复码，防止设备丢失

### 🛡️ 安全机制
- **防重放攻击**：记录已使用的验证码，防止重复使用
- **失败次数限制**：支持验证失败次数限制和自动锁定
- **时间窗口容差**：支持前后时间窗口的验证码校验
- **安全随机数**：使用SecureRandom生成高强度随机码

### 💾 存储支持
- **内存存储**：适用于开发和测试环境
- **Redis存储**：适用于分布式环境，支持过期自动清理
- **MySQL存储**：适用于持久化需求，支持定时清理过期数据

### 🌐 Web API
- **RESTful接口**：提供完整的REST API
- **Swagger文档**：自动生成API文档
- **统一响应格式**：标准化的JSON响应格式
- **参数校验**：完善的请求参数校验

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.whoiszxl</groupId>
    <artifactId>haha-starter-captcha-google</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 配置文件

```yaml
haha:
  captcha:
    google:
      enabled: true
      app-name: "我的应用"
      issuer: "MyCompany"
      storage:
        type: MEMORY  # 可选：MEMORY、REDIS、MYSQL
```

### 3. 使用服务

```java
@Autowired
private GoogleCaptchaService googleCaptchaService;

// 生成密钥和二维码
GoogleCaptchaResult result = googleCaptchaService.generateSecret("user123", "张三");

// 验证验证码
boolean isValid = googleCaptchaService.validate("user123", "123456");

// 检查绑定状态
boolean isBound = googleCaptchaService.isBound("user123");
```

## 配置说明

### 基础配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `haha.captcha.google.enabled` | boolean | true | 是否启用功能 |
| `haha.captcha.google.app-name` | String | "HaHa Platform" | 应用名称 |
| `haha.captcha.google.issuer` | String | "HaHa" | 发行者名称 |
| `haha.captcha.google.secret-length` | int | 20 | 密钥长度（字节） |
| `haha.captcha.google.period` | int | 30 | 验证码有效期（秒） |
| `haha.captcha.google.digits` | int | 6 | 验证码位数 |
| `haha.captcha.google.window-size` | int | 1 | 时间窗口容差 |

### 二维码配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `haha.captcha.google.qr-code.width` | int | 300 | 二维码宽度 |
| `haha.captcha.google.qr-code.height` | int | 300 | 二维码高度 |
| `haha.captcha.google.qr-code.format` | String | "PNG" | 图片格式 |
| `haha.captcha.google.qr-code.margin` | int | 1 | 二维码边距 |

### 存储配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `haha.captcha.google.storage.type` | enum | MEMORY | 存储类型 |
| `haha.captcha.google.storage.key-prefix` | String | "captcha:google:" | Redis键前缀 |
| `haha.captcha.google.storage.key-expire-time` | Duration | PT24H | 密钥过期时间 |
| `haha.captcha.google.storage.table-prefix` | String | "google_captcha_" | 数据库表前缀 |

### 安全配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `haha.captcha.google.security.enable-anti-replay` | boolean | true | 启用防重放攻击 |
| `haha.captcha.google.security.used-code-expire-time` | Duration | PT5M | 验证码使用记录过期时间 |
| `haha.captcha.google.security.max-failure-count` | int | 5 | 最大验证失败次数 |
| `haha.captcha.google.security.lockout-duration` | Duration | PT15M | 验证失败锁定时间 |

### Web接口配置

| 配置项 | 类型 | 默认值 | 说明 |
|-------|------|--------|------|
| `haha.captcha.google.web.enabled` | boolean | true | 是否启用Web接口 |

## API 接口

### 生成密钥和二维码

```http
POST /api/captcha/google/generate
Content-Type: application/x-www-form-urlencoded

userId=user123&username=张三
```

### 验证验证码

```http
POST /api/captcha/google/validate
Content-Type: application/json

{
  "userId": "user123",
  "code": "123456"
}
```

### 检查绑定状态

```http
GET /api/captcha/google/status?userId=user123
```

### 生成备用恢复码

```http
POST /api/captcha/google/backup-codes/generate
Content-Type: application/x-www-form-urlencoded

userId=user123&count=10
```

### 验证备用恢复码

```http
POST /api/captcha/google/backup-codes/validate
Content-Type: application/x-www-form-urlencoded

userId=user123&backupCode=ABCD1234
```

### 解绑验证器

```http
DELETE /api/captcha/google/unbind?userId=user123
```

### 获取二维码图片

```http
GET /api/captcha/google/qrcode?userId=user123&username=张三&width=300&height=300&format=PNG
```

## 存储方案

### 内存存储

适用于单机环境和开发测试：

```yaml
haha:
  captcha:
    google:
      storage:
        type: MEMORY
```

### Redis存储

适用于分布式环境：

```yaml
haha:
  captcha:
    google:
      storage:
        type: REDIS
        key-prefix: "captcha:google:"
        key-expire-time: PT24H

spring:
  data:
    redis:
      host: localhost
      port: 6379
```

### MySQL存储

适用于需要持久化的环境：

```yaml
haha:
  captcha:
    google:
      storage:
        type: MYSQL
        table-prefix: "google_captcha_"

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: 123456
  jpa:
    hibernate:
      ddl-auto: update
```

## 数据库表结构

使用MySQL存储时，会自动创建以下表：

- `google_captcha_secret`：存储用户密钥
- `google_captcha_used_code`：存储已使用的验证码
- `google_captcha_backup_code`：存储备用恢复码
- `google_captcha_failure`：存储验证失败记录

## 最佳实践

### 1. 生产环境配置

```yaml
haha:
  captcha:
    google:
      storage:
        type: REDIS  # 推荐使用Redis
      security:
        enable-anti-replay: true
        max-failure-count: 3
        lockout-duration: PT30M
```

### 2. 集成到认证流程

```java
@Service
public class AuthService {
    
    @Autowired
    private GoogleCaptchaService googleCaptchaService;
    
    public boolean login(String username, String password, String totpCode) {
        // 1. 验证用户名密码
        if (!validateCredentials(username, password)) {
            return false;
        }
        
        // 2. 检查是否绑定了Google验证器
        if (googleCaptchaService.isBound(username)) {
            // 3. 验证TOTP验证码
            return googleCaptchaService.validate(username, totpCode);
        }
        
        return true;
    }
}
```

### 3. 前端集成示例

```javascript
// 生成二维码
async function generateQrCode(userId, username) {
    const response = await fetch('/api/captcha/google/generate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `userId=${userId}&username=${username}`
    });
    
    const result = await response.json();
    if (result.success) {
        document.getElementById('qrcode').src = result.data.qrCodeImage;
        displayBackupCodes(result.data.backupCodes);
    }
}

// 验证验证码
async function validateCode(userId, code) {
    const response = await fetch('/api/captcha/google/validate/simple', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `userId=${userId}&code=${code}`
    });
    
    const result = await response.json();
    return result.success && result.data.valid;
}
```

## 异常处理

所有业务异常都会抛出 `GoogleCaptchaException`，包含详细的错误信息：

```java
try {
    googleCaptchaService.validate(userId, code);
} catch (GoogleCaptchaException e) {
    log.error("验证失败: {}", e.getMessage());
    // 处理具体的业务异常
}
```

## 监控和日志

启用DEBUG日志可以查看详细的操作记录：

```yaml
logging:
  level:
    com.whoiszxl.captcha.google: DEBUG
```

## 性能优化

1. **Redis连接池配置**：合理配置Redis连接池参数
2. **定时清理**：启用定时任务清理过期数据
3. **缓存策略**：合理设置过期时间，避免内存泄漏

## 安全建议

1. **HTTPS传输**：生产环境必须使用HTTPS
2. **访问控制**：限制API接口的访问权限
3. **审计日志**：记录所有关键操作的审计日志
4. **备用恢复码**：妥善保管备用恢复码，建议加密存储

## 版本兼容性

- Spring Boot 3.0+
- Java 17+
- MySQL 8.0+
- Redis 6.0+
