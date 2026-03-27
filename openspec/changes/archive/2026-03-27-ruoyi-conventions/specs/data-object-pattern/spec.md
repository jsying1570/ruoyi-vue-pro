## ADDED Requirements

### Requirement: 所有数据实体继承 BaseDO 基类

BaseDO 包含自动填充的审计字段（createTime、updateTime、creator、updater）和逻辑删除字段（deleted）。

#### Scenario: 创建新数据实体
- **WHEN** 创建新的数据实体类
- **THEN** 应继承 `BaseDO`（无多租户）或 `TenantBaseDO`（需要多租户）

#### Scenario: 自动填充创建时间
- **WHEN** 插入新数据时
- **THEN** createTime 和 creator 字段应自动填充当前时间和用户 ID

#### Scenario: 自动填充更新时间
- **WHEN** 更新数据时
- **THEN** updateTime 和 updater 字段应自动填充当前时间和用户 ID

#### Scenario: 逻辑删除
- **WHEN** 执行删除操作时
- **THEN** 由于 `@TableLogic` 注解，实际是将 deleted 字段设为 true

### Requirement: 多租户实体使用 TenantBaseDO

需要多租户支持的模块，其数据实体应继承 `TenantBaseDO`，包含 tenantId 字段。

#### Scenario: 创建多租户数据实体
- **WHEN** 创建需要多租户隔离的数据实体
- **THEN** 应继承 `TenantBaseDO`，自动包含 tenantId 字段

#### Scenario: 租户数据隔离查询
- **WHEN** 执行数据库查询时
- **THEN** 应自动添加 tenantId 条件过滤（由框架层处理）

### Requirement: 使用 MyBatis-Plus 注解配置表映射

数据实体应使用 MyBatis-Plus 的 `@TableName`、`@TableId`、`@TableField` 等注解配置表映射关系。

#### Scenario: 指定表名
- **WHEN** 实体类与表名不一致时
- **THEN** 应使用 `@TableName("表名")` 注解指定

#### Scenario: 配置主键
- **WHEN** 配置主键生成策略
- **THEN** 应使用 `@TableId` 注解，可配合 `@KeySequence` 指定序列

### Requirement: Mapper 继承 BaseMapperX 获得通用方法

所有 Mapper 接口应继承 `BaseMapperX<T>`，获得 `selectOne`、`selectList`、`selectPage`、`insertBatch` 等通用方法。

#### Scenario: 按字段查询
- **WHEN** 需要根据单个字段查询
- **THEN** 可直接调用 `selectOne(AdminUserDO::getUsername, username)`

#### Scenario: 分页查询
- **WHEN** 需要分页查询数据
- **THEN** 可使用 `selectPage(pageParam, wrapper)` 方法
