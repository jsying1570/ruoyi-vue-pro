## ADDED Requirements

### Requirement: 使用 Spring Security @PreAuthorize 进行权限控制

所有需要权限控制的 Controller 方法必须使用 `@PreAuthorize` 注解，配合 `@ss.hasPermission()` 表达式。

#### Scenario: 创建资源权限控制
- **WHEN** 实现用户创建接口
- **THEN** 应使用 `@PreAuthorize("@ss.hasPermission('system:user:create')")`

#### Scenario: 查询资源权限控制
- **WHEN** 实现用户查询接口
- **THEN** 应使用 `@PreAuthorize("@ss.hasPermission('system:user:query')")`

#### Scenario: 删除资源权限控制
- **WHEN** 实现用户删除接口
- **THEN** 应使用 `@PreAuthorize("@ss.hasPermission('system:user:delete')")`

### Requirement: 权限标识符采用标准化三段式格式

权限标识符格式为 `{模块}:{资源}:{操作}`，操作包括 create、update、delete、query、export、import 等。

#### Scenario: 定义用户管理权限
- **WHEN** 定义用户管理相关权限
- **THEN** 应使用 `system:user:create`、`system:user:update`、`system:user:delete`、`system:user:query`

#### Scenario: 定义部门管理权限
- **WHEN** 定义部门管理相关权限
- **THEN** 应使用 `system:dept:create`、`system:dept:update`、`system:dept:delete`、`system:dept:query`

### Requirement: 支持数据权限控制

系统应支持基于数据范围的权限控制（如仅本人、本部门、全部数据）。

#### Scenario: 查询用户数据时应用数据权限
- **WHEN** 用户查询列表数据
- **THEN** 应自动过滤用户只能看到其权限范围内的数据
