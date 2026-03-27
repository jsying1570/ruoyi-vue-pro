## ADDED Requirements

### Requirement: 类和文件命名采用大驼峰命名法（UpperCamelCase）

所有类、接口、枚举、注解类型均使用大驼峰命名法，名称应清晰表达其职责。

#### Scenario: 创建新的 Service 接口
- **WHEN** 创建用户管理服务接口
- **THEN** 应命名为 `AdminUserService.java`，实现类命名为 `AdminUserServiceImpl.java`

#### Scenario: 创建新的 Controller
- **WHEN** 创建用户管理控制器
- **THEN** 应命名为 `UserController.java`

#### Scenario: 创建数据对象
- **WHEN** 创建用户数据实体
- **THEN** 应命名为 `AdminUserDO.java`，遵循 `{Domain}DO` 命名模式

### Requirement: VO 类按用途采用特定后缀

请求 VO 使用 `ReqVO` 后缀，响应 VO 使用 `RespVO` 后缀，API 层 DTO 使用 `RespDTO` 后缀。

#### Scenario: 创建用户保存请求
- **WHEN** 创建用户新增/修改的请求对象
- **THEN** 应命名为 `UserSaveReqVO.java`

#### Scenario: 创建用户分页查询请求
- **WHEN** 创建用户分页查询的请求对象
- **THEN** 应命名为 `UserPageReqVO.java`

#### Scenario: 创建用户详情响应
- **WHEN** 创建用户详情的响应对象
- **THEN** 应命名为 `UserRespVO.java`

#### Scenario: 创建跨模块用户数据传输对象
- **WHEN** 创建跨模块调用的用户响应 DTO
- **THEN** 应命名为 `AdminUserRespDTO.java`，位于 api 模块

### Requirement: 权限标识符采用三段式格式

权限标识符格式为 `{模块}:{资源}:{操作}`，如 `system:user:create`。

#### Scenario: 配置用户创建权限
- **WHEN** 在 Controller 方法上配置权限控制
- **THEN** 应使用 `@PreAuthorize("@ss.hasPermission('system:user:create')")`

#### Scenario: 配置用户查询权限
- **WHEN** 在 Controller 方法上配置查询权限
- **THEN** 应使用 `@PreAuthorize("@ss.hasPermission('system:user:query')")`

### Requirement: 包结构按分层组织

每个模块的包结构应为：`controller`、`service`、`dal`、`convert`、`enums`、`framework` 等。

#### Scenario: 创建新的服务实现
- **WHEN** 创建新的 Service 实现类
- **THEN** 应放在 `service/` 包下，如 `cn.iocoder.yudao.module.system.service.user`

#### Scenario: 创建数据访问层
- **WHEN** 创建 Mapper 和 DO 实体
- **THEN** Mapper 放在 `dal/mysql/` 包，DO 放在 `dal/dataobject/` 包
