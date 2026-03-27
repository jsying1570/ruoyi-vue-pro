# 项目开发规范

本文档定义了 ruoyi-vue-pro 项目的开发规范，包括目录结构、模块组织、命名规范、API 接口规范、权限控制模式和代码分层架构。

---

## 目录

1. [目录结构](#1-目录结构)
2. [模块组织](#2-模块组织)
3. [命名规范](#3-命名规范)
4. [API 接口规范](#4-api-接口规范)
5. [权限控制](#5-权限控制)
6. [代码分层](#6-代码分层)
7. [数据对象模式](#7-数据对象模式)

---

## 1. 目录结构

```
ruoyi-vue-pro/
├── yudao-dependencies/              # BOM 版本管理中心
├── yudao-framework/                 # 框架层 - 通用组件和 Starter
│   ├── yudao-common/                # 基础工具类、枚举、异常、POJO
│   ├── yudao-spring-boot-starter-web/
│   ├── yudao-spring-boot-starter-security/
│   ├── yudao-spring-boot-starter-mybatis/
│   ├── yudao-spring-boot-starter-redis/
│   ├── yudao-spring-boot-starter-biz-tenant/    # 多租户支持
│   └── yudao-spring-boot-starter-biz-data-permission/  # 数据权限
├── yudao-server/                    # 主应用入口
├── yudao-module-system/             # 系统模块（用户/角色/权限）
├── yudao-module-infra/              # 基础设施模块（任务/文件/配置）
├── yudao-module-ai/                 # AI 模块
├── yudao-module-bpm/                # 工作流模块
├── sql/                             # 数据库脚本
└── script/                          # 部署脚本
```

### Framework 层 Starter 说明

| Starter | 用途 |
|---------|------|
| `yudao-spring-boot-starter-web` | Web 功能、全局异常处理 |
| `yudao-spring-boot-starter-security` | Spring Security 权限控制 |
| `yudao-spring-boot-starter-mybatis` | MyBatis-Plus 数据访问 |
| `yudao-spring-boot-starter-redis` | Redis 缓存 |
| `yudao-spring-boot-starter-biz-tenant` | 多租户支持 |
| `yudao-spring-boot-starter-biz-data-permission` | 数据权限控制 |

---

## 2. 模块组织

每个业务模块采用 **API + BIZ** 双层结构：

```
yudao-module-{name}/
├── pom.xml
├── yudao-module-{name}-api/         # API 层 - 跨模块调用接口和 DTO
│   └── src/main/java/.../{name}/
│       ├── api/                     # 跨模块调用接口
│       └── enums/                   # 模块枚举
│
└── yudao-module-{name}-biz/         # 业务实现层
    └── src/main/java/.../{name}/
        ├── controller/              # REST 控制器 (admin/app)
        ├── service/                 # 服务层
        ├── dal/                     # 数据访问层
        ├── convert/                 # MapStruct 转换器
        └── framework/               # 模块配置
```

### 模块依赖关系

```
yudao-server
    ├── system-biz → system-api
    ├── ai-biz → ai-api
    └── infra-biz → infra-api
```

---

## 3. 命名规范

### 3.1 类和文件命名

| 类型 | 命名模式 | 示例 |
|------|----------|------|
| 控制器 | `{Domain}Controller` | `UserController` |
| 服务接口 | `{Domain}Service` | `AdminUserService` |
| 服务实现 | `{Domain}ServiceImpl` | `AdminUserServiceImpl` |
| 数据对象 | `{Domain}DO` | `AdminUserDO` |
| Mapper | `{Domain}Mapper` | `AdminUserMapper` |
| 转换器 | `{Domain}Convert` | `UserConvert` |

### 3.2 VO 命名

| 类型 | 命名模式 | 示例 |
|------|----------|------|
| 请求 VO | `{Action}ReqVO` | `UserSaveReqVO`, `UserPageReqVO` |
| 响应 VO | `{Domain}RespVO` | `UserRespVO`, `UserSimpleRespVO` |
| API DTO | `{Domain}RespDTO` | `AdminUserRespDTO` |

### 3.3 权限标识符

格式：`{模块}:{资源}:{操作}`

```java
@PreAuthorize("@ss.hasPermission('system:user:create')")
@PreAuthorize("@ss.hasPermission('system:user:query')")
```

---

## 4. API 接口规范

### 4.1 统一响应结构

```java
@Data
public class CommonResult<T> {
    private Integer code;    // 错误码
    private T data;          // 响应数据
    private String msg;      // 错误消息
}
```

**成功响应示例：**
```json
{ "code": 0, "data": {...}, "msg": "" }
```

**错误响应示例：**
```json
{ "code": 500, "data": null, "msg": "系统错误" }
```

### 4.2 分页响应

```java
@Data
public class PageResult<T> {
    private List<T> list;    // 数据列表
    private Long total;      // 总数
}
```

### 4.3 参数校验

使用 Jakarta Validation 注解：

```java
@Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED)
@NotBlank(message = "用户账号不能为空")
@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "用户账号由数字、字母组成")
@Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
private String username;
```

### 4.4 对象转换

使用 MapStruct 进行 DO ↔ VO 转换：

```java
@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserRespVO convert(AdminUserDO user, DeptDO dept);
    List<UserRespVO> convertList(List<AdminUserDO> list, Map<Long, DeptDO> deptMap);
}
```

---

## 5. 权限控制

### 5.1 @PreAuthorize 注解

```java
@RestController
@RequestMapping("/system/user")
public class UserController {

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createUser(@Valid @RequestBody UserSaveReqVO reqVO) {
        // ...
    }
}
```

### 5.2 权限标识符格式

```
{模块}:{资源}:{操作}

示例：
- system:user:create    - 创建用户
- system:user:update    - 更新用户
- system:user:delete    - 删除用户
- system:user:query     - 查询用户
- system:user:export    - 导出用户
```

### 5.3 数据权限

系统支持基于数据范围的权限控制（仅本人、本部门、全部数据等）。

---

## 6. 代码分层架构

```
┌─────────────────────────────────────────┐
│           Controller 层                  │
│  请求处理、参数校验、权限控制、响应返回   │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│            Service 层                    │
│  业务逻辑、事务管理、跨模块调用           │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│              DAL 层                      │
│  数据访问（MyBatis-Plus Mapper）          │
└─────────────────────────────────────────┘
```

### 6.1 Service 层模式

```java
// 接口
public interface AdminUserService {
    Long createUser(UserSaveReqVO reqVO);
    AdminUserDO getUser(Long id);
}

// 实现
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper userMapper;

    @Override
    @Transactional
    public Long createUser(UserSaveReqVO reqVO) {
        // 业务逻辑
    }
}
```

---

## 7. 数据对象模式

### 7.1 BaseDO 基类

所有数据实体继承 `BaseDO`，包含自动填充的审计字段：

```java
@Data
public abstract class BaseDO implements Serializable {
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    @TableLogic
    private Boolean deleted;
}
```

### 7.2 多租户支持

需要多租户隔离的实体继承 `TenantBaseDO`：

```java
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseDO extends BaseDO {
    private Long tenantId;
}
```

### 7.3 Mapper 接口

继承 `BaseMapperX<T>` 获得通用方法：

```java
@Mapper
public interface AdminUserMapper extends BaseMapperX<AdminUserDO> {

    default AdminUserDO selectByUsername(String username) {
        return selectOne(AdminUserDO::getUsername, username);
    }
}
```

---

## 8. 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17 | 运行时 |
| Spring Boot | 3.4.5 | 基础框架 |
| MyBatis-Plus | 3.5.10.1 | ORM |
| Redisson | 3.41.0 | Redis 客户端 |
| Knife4j | 4.6.0 | API 文档 |
| MapStruct | 1.6.3 | 对象映射 |
