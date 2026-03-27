# App-Key 管理功能设计文档

**创建日期：** 2026-03-27
**状态：** 已批准
**作者：** 芋道源码

---

## 1. 概述

### 1.1 需求背景

为外部第三方系统提供接入平台的 API 密钥管理功能，支持 HMAC 签名认证、接口权限控制和调用频率限制。

### 1.2 功能范围

| 功能 | 描述 |
|------|------|
| 应用密钥管理 | 创建、更新、删除、查询 app-key |
| HMAC 签名认证 | 基于 appId + timestamp + sign 的请求认证 |
| 接口权限控制 | 为每个 app-key 绑定可访问的接口范围 |
| 调用频率限制 | 限制每个 app-key 的 API 调用频率 |
| 多租户支持 | 每个租户独立管理自己的 app-key |

### 1.3 不在范围内

- OAuth2 完整授权流程（授权码、隐式授权等）
- 前端应用的交互式登录
- 个人用户的 Token 管理

---

## 2. 架构设计

### 2.1 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                    外部应用请求                               │
│              appId + timestamp + sign                        │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│              认证拦截器 (AppKeyAuthInterceptor)              │
│  1. 验证 appId 是否存在                                      │
│  2. 验证 app 状态是否启用                                     │
│  3. 验证 timestamp 是否过期                                   │
│  4. 使用 appSecret 验证签名                                   │
│  5. 检查限流配置                                             │
│  6. 检查接口权限                                             │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    业务接口                                   │
└─────────────────────────────────────────────────────────────┘
```

### 2.2 模块结构

```
yudao-module-system/
├── yudao-module-system-api/
│   └── src/main/java/cn/iocoder/yudao/module/system/api/
│       ├── appkey/
│       │   ├── AppKeyApi.java              # API 接口
│       │   └── dto/
│       │       └── AppKeyAuthDTO.java      # 认证信息 DTO
│       └── enums/
│           └── AppKeyErrorCodeConstants.java
│
└── yudao-module-system-biz/
    ├── controller/admin/appkey/
    │   ├── AppKeyController.java
    │   └── vo/
    │       ├── AppKeyPageReqVO.java
    │       ├── AppKeyRespVO.java
    │       ├── AppKeySaveReqVO.java
    │       └── AppKeyScopeRespVO.java
    ├── dal/
    │   ├── dataobject/appkey/
    │   │   ├── AppKeyDO.java
    │   │   └── AppKeyScopeDO.java
    │   └── mysql/appkey/
    │       ├── AppKeyMapper.java
    │       └── AppKeyScopeMapper.java
    ├── service/appkey/
    │   ├── AppKeyService.java
    │   └── AppKeyServiceImpl.java
    └── framework/appkey/
        ├── AppKeyAuthFilter.java
        ├── AppKeySignUtils.java
        └── AppKeyRateLimiter.java
```

---

## 3. 数据模型设计

### 3.1 system_app_key - 应用密钥表

```sql
CREATE TABLE `system_app_key` (
  `id` bigint(20) NOT NULL COMMENT '主键 ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户 ID',
  `app_id` varchar(64) NOT NULL COMMENT '应用 ID（公开）',
  `app_secret` varchar(128) NOT NULL COMMENT '应用密钥（加密存储）',
  `name` varchar(100) NOT NULL COMMENT '应用名称',
  `description` varchar(255) DEFAULT NULL COMMENT '应用描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0-禁用 1-启用）',
  `rate_limit` int(11) DEFAULT '100' COMMENT '每秒请求限制',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间（NULL=永不过期）',
  `last_used_time` datetime DEFAULT NULL COMMENT '最后使用时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_app_id` (`tenant_id`, `app_id`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用密钥表';
```

### 3.2 system_app_key_scope - 应用权限范围表

```sql
CREATE TABLE `system_app_key_scope` (
  `id` bigint(20) NOT NULL COMMENT '主键 ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户 ID',
  `app_id` varchar(64) NOT NULL COMMENT '应用 ID',
  `scope_type` tinyint(1) NOT NULL COMMENT '权限类型（1=菜单 2=接口）',
  `scope_value` varchar(255) NOT NULL COMMENT '权限值（菜单 ID 或 接口路径）',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tenant_app_id` (`tenant_id`, `app_id`),
  KEY `idx_scope_type` (`scope_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用权限范围表';
```

---

## 4. API 接口设计

### 4.1 管理后台接口

| 方法 | 路径 | 描述 | 权限 |
|------|------|------|------|
| POST | /system/app-key/create | 创建应用密钥 | system:app-key:create |
| PUT | /system/app-key/update | 更新应用密钥 | system:app-key:update |
| DELETE | /system/app-key/delete | 删除应用密钥 | system:app-key:delete |
| GET | /system/app-key/get | 获取应用密钥详情 | system:app-key:query |
| GET | /system/app-key/page | 分页查询 | system:app-key:query |
| POST | /system/app-key/reset-secret | 重置密钥 | system:app-key:reset-secret |
| POST | /system/app-key/scope/save | 保存权限范围 | system:app-key:scope |
| GET | /system/app-key/scope/list | 获取权限范围列表 | system:app-key:query |

### 4.2 开放平台接口（可选）

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /open-api/auth/token | 使用 app-key 交换访问 Token |

### 4.3 请求头规范

外部应用调用 API 时需携带以下请求头：

```
X-App-Id: your_app_id
X-Timestamp: 1711526400000
X-Nonce: a1b2c3d4e5f6
X-Sign: HMAC-SHA256 签名值
```

---

## 5. 认证流程

### 5.1 签名生成算法

```java
// 客户端签名生成
String sign = HMAC-SHA256(
    appSecret,
    appId + "|" + timestamp + "|" + nonce
);
```

### 5.2 服务端验证流程

```
1. 提取请求头 X-App-Id, X-Timestamp, X-Nonce, X-Sign
2. 根据 appId 查询应用密钥信息
3. 验证应用状态是否为启用
4. 验证 timestamp 是否在允许时间窗口内（默认 5 分钟）
5. 验证 nonce 是否已使用（防重放攻击，可选）
6. 使用存储的 appSecret 重新计算签名并比对
7. 检查限流配置
8. 检查接口权限
9. 验证通过，放行请求
```

---

## 6. 限流设计

### 6.1 限流策略

- 基于 Redis 的滑动窗口限流
- 每个 app-key 独立的限流计数器
- Key 格式：`app_key:rate_limit:{tenant_id}:{app_id}:{timestamp_second}`

### 6.2 限流响应

当超过限流阈值时，返回：

```json
{
  "code": "429",
  "msg": "请求频率超限",
  "data": null
}
```

---

## 7. 错误码设计

| 错误码 | 描述 | HTTP 状态码 |
|--------|------|-------------|
| APP_KEY_NOT_EXISTS | App-Key 不存在 | 401 |
| APP_KEY_DISABLED | App-Key 已禁用 | 401 |
| APP_KEY_EXPIRED | App-Key 已过期 | 401 |
| APP_KEY_SIGN_INVALID | 签名无效 | 401 |
| APP_KEY_TIMESTAMP_EXPIRED | 请求时间已过期 | 401 |
| APP_KEY_RATE_LIMIT_EXCEEDED | 请求频率超限 | 429 |
| APP_KEY_SCOPE_DENIED | 无接口访问权限 | 403 |

---

## 8. 密钥生成规则

### 8.1 App-ID 格式

```
app_{timestamp}_{random8}
例：app_1711526400_a1b2c3d4
```

### 8.2 App-Secret 格式

```
sk_{timestamp}_{random32}
例：sk_1711526400_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

---

## 9. 安全考虑

1. **密钥存储：** app_secret 加密存储（BCrypt 或 AES）
2. **传输安全：** 强制 HTTPS，密钥不在网络明文传输
3. **防重放攻击：** timestamp + nonce 机制
4. **权限最小化：** 默认无权限，需显式授权
5. **审计日志：** 记录 app-key 的使用情况

---

## 10. 测试计划

### 10.1 单元测试

- AppKeySignUtils 签名生成和验证
- AppKeyServiceImpl 业务逻辑
- AppKeyRateLimiter 限流逻辑

### 10.2 集成测试

- AppKeyController API 接口
- 认证过滤器端到端测试

### 10.3 性能测试

- 签名验证延迟 < 10ms
- 限流检查延迟 < 5ms
- 支持 1000+ QPS

---

## 11. 里程碑

- [ ] 数据库表创建
- [ ] DO/Mapper/Service 层实现
- [ ] Controller 层实现
- [ ] 认证过滤器实现
- [ ] 签名工具类实现
- [ ] 限流器实现
- [ ] 单元测试编写
- [ ] 集成测试编写
- [ ] 文档完善
