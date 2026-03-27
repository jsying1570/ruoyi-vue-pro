# 新人上手指南

欢迎加入！本文档帮助新成员快速上手 ruoyi-vue-pro 项目。

---

## 1. 环境准备

### 1.1 必需软件

| 软件 | 版本要求 | 用途 |
|------|----------|------|
| JDK | 17/21（master-jdk17 分支）或 8（master 分支） | Java 运行时 |
| Maven | 3.8+ | 构建工具 |
| MySQL | 5.7+ / 8.0+ | 数据库 |
| Redis | 5.0+ / 6.0+ / 7.0+ | 缓存 |
| Git | 最新稳定版 | 版本控制 |
| IntelliJ IDEA | 2023.x+ | 推荐 IDE |

### 1.2 推荐插件

- **Lombok** - 简化 Java 代码
- **MapStruct Support** - MapStruct 代码提示
- **MyBatisX** - MyBatis 增强工具
- **RestfulToolkit** - API 接口导航

---

## 2. 项目启动

### 2.1 克隆项目

```bash
git clone https://github.com/YunaiV/ruoyi-vue-pro.git
cd ruoyi-vue-pro
```

### 2.2 导入数据库

1. 执行 `sql/mysql/` 目录下的 SQL 脚本
2. 按顺序执行：`001.sql` → `002.sql` → ...

### 2.3 配置本地环境

编辑 `yudao-server/src/main/resources/application-local.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ruoyi_vue_pro?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: ""
```

### 2.4 启动服务

```bash
# 方式一：使用 IDEA
直接在 yudao-server 模块中找到 ServerApplication.java 运行

# 方式二：使用 Maven
mvn clean install -DskipTests
cd yudao-server
mvn spring-boot:run
```

### 2.5 访问系统

- 后端 API：http://localhost:48080
- Swagger 文档：http://localhost:48080/swagger-ui/index.html
- 管理后台：http://localhost/admin（前端项目）

默认账号：`admin` / `admin123`

---

## 3. 项目结构

### 3.1 整体架构

```
ruoyi-vue-pro/
├── yudao-framework/          # 框架层（通用组件）
├── yudao-server/             # 主应用入口
├── yudao-module-system/      # 系统模块
├── yudao-module-infra/       # 基础设施模块
└── ...                       # 其他业务模块
```

### 3.2 模块结构

每个业务模块采用 API + BIZ 双层结构：

```
yudao-module-system/
├── yudao-module-system-api/   # API 层（跨模块调用）
└── yudao-module-system-biz/   # 业务实现层
    ├── controller/            # 控制器
    ├── service/               # 服务层
    ├── dal/                   # 数据访问层
    └── convert/               # 对象转换器
```

### 3.3 代码分层

```
请求 → Controller → Service → Mapper → Database
       ↓           ↓
      VO 转 DO     DO 转 VO
```

---

## 4. 开发规范速查

### 4.1 命名规范

| 类型 | 命名模式 | 示例 |
|------|----------|------|
| Controller | `{Domain}Controller` | `UserController` |
| Service 接口 | `{Domain}Service` | `AdminUserService` |
| Service 实现 | `{Domain}ServiceImpl` | `AdminUserServiceImpl` |
| 数据对象 | `{Domain}DO` | `AdminUserDO` |
| 请求 VO | `{Action}ReqVO` | `UserSaveReqVO` |
| 响应 VO | `{Domain}RespVO` | `UserRespVO` |

### 4.2 API 响应格式

```java
// 成功响应
return success(data);
// { "code": 0, "data": {...}, "msg": "" }

// 错误响应
return error(errorCode, params);
// { "code": 500, "data": null, "msg": "错误信息" }
```

### 4.3 权限注解

```java
@PostMapping("/create")
@PreAuthorize("@ss.hasPermission('system:user:create')")
public CommonResult<Long> createUser(@Valid @RequestBody UserSaveReqVO reqVO) {
    // ...
}
```

### 4.4 对象转换

```java
// DO → VO
UserRespVO vo = UserConvert.INSTANCE.convert(do, dept);

// ReqVO → DO（使用 BeanUtils）
AdminUserDO user = BeanUtils.toBean(reqVO, AdminUserDO.class);
```

---

## 5. 常见开发任务

### 5.1 新增一个实体

1. 创建 DO 实体（继承 `TenantBaseDO` 或 `BaseDO`）
2. 创建 Mapper 接口（继承 `BaseMapperX<T>`）
3. 创建 Service 接口和实现类
4. 创建 Controller
5. 创建 Convert 转换器
6. 创建 VO 类

### 5.2 新增一个 API 接口

1. 在 Controller 中新增方法
2. 添加 `@Operation` 注解描述接口
3. 添加 `@PreAuthorize` 注解配置权限
4. 在 Service 中实现业务逻辑
5. 编写单元测试

### 5.3 使用代码生成器

1. 访问：http://localhost:48080/swagger-ui/index.html
2. 找到代码生成接口
3. 输入表名，生成代码
4. 下载并复制到对应模块

---

## 6. 调试技巧

### 6.1 日志查看

- 日志文件：`logs/ruoyi-server.log`
- 日志级别：在 `application-local.yaml` 中配置

```yaml
logging:
  level:
    cn.iocoder.yudao: DEBUG
```

### 6.2 接口调试

- 使用 Swagger UI：http://localhost:48080/swagger-ui/index.html
- 使用 Postman / Apifox
- 使用 IDEA 的 HTTP Client

### 6.3 数据库调试

- 开启 SQL 日志：

```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

---

## 7. 单元测试

### 7.1 编写测试

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateUser() throws Exception {
        // 测试代码
    }
}
```

### 7.2 运行测试

```bash
# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=UserControllerTest

# 运行并生成覆盖率报告
mvn clean test jacoco:report
```

---

## 8. 提交代码

### 8.1 Git 提交规范

采用 Conventional Commits：

```
<type>: <description>

[optional body]
```

**type 类型：**
- `feat`: 新功能
- `fix`: 修复 bug
- `refactor`: 重构（既不是新功能也不是 bug 修复）
- `docs`: 文档变更
- `test`: 添加或修改测试
- `chore`: 构建过程或辅助工具变动

**示例：**
```
feat: 新增用户导入功能

- 支持 Excel 文件导入
- 支持数据校验
- 支持重复数据跳过
```

### 8.2 提交流程

```bash
# 1. 拉取最新代码
git pull origin master

# 2. 创建功能分支
git checkout -b feature/user-import

# 3. 提交代码
git add .
git commit -m "feat: 新增用户导入功能"

# 4. 推送到远程
git push origin feature/user-import

# 5. 创建 Pull Request
```

---

## 9. 学习资源

### 9.1 官方文档

- [项目启动文档](https://doc.iocoder.cn/quick-start/)
- [视频教程](https://doc.iocoder.cn/video/)
- [开发文档](https://doc.iocoder.cn/)

### 9.2 技术文档

- [Spring Boot 文档](https://spring.io/projects/spring-boot)
- [MyBatis Plus 文档](https://mp.baomidou.com/)
- [MapStruct 文档](https://mapstruct.org/)

### 9.3 项目文档

- [项目开发规范](openspec/specs/ruoyi-conventions/spec.md)
- [README.md](README.md)

---

## 10. 遇到问题？

### 10.1 常见问题

1. **启动失败**：检查数据库、Redis 是否启动，配置是否正确
2. **权限不足**：检查用户角色权限，重新登录刷新 Token
3. **代码生成失败**：检查数据库表是否存在，是否有注释

### 10.2 获取帮助

- **Issues**: [GitHub Issues](https://github.com/YunaiV/ruoyi-vue-pro/issues)
- **微信群**: 查看 README.md 加入方式
- **邮件**: aix9975@iocoder.cn

---

## 11. 快速检查清单

- [ ] JDK、Maven、MySQL、Redis 已安装
- [ ] 项目代码已克隆
- [ ] 数据库脚本已执行
- [ ] `application-local.yaml` 已配置
- [ ] 服务可以正常启动
- [ ] Swagger 可以访问
- [ ] 可以正常登录
- [ ] 单元测试可以通过

完成以上检查，说明你已经成功上手项目！🎉
