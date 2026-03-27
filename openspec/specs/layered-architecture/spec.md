## ADDED Requirements

### Requirement: 代码分为 Controller、Service、DAL 三层

Controller 层负责请求处理和响应返回，Service 层负责业务逻辑，DAL 层负责数据访问。

#### Scenario: 创建用户接口流程
- **WHEN** 调用用户创建接口
- **THEN** 请求依次经过 UserController → AdminUserService → AdminUserMapper → Database

#### Scenario: Controller 层职责
- **WHEN** 在 Controller 层编写代码
- **THEN** 只负责参数接收、校验、调用 Service、返回响应，不包含业务逻辑

#### Scenario: Service 层职责
- **WHEN** 在 Service 层编写代码
- **THEN** 负责业务逻辑、事务管理、跨模块调用，不直接操作数据库

### Requirement: Service 层使用接口 + 实现类模式

每个 Service 必须有接口定义和实现类，接口命名为 `{Domain}Service`，实现类命名为 `{Domain}ServiceImpl`。

#### Scenario: 定义用户服务
- **WHEN** 定义用户服务接口
- **THEN** 应创建 `AdminUserService.java` 接口和 `AdminUserServiceImpl.java` 实现类

#### Scenario: 事务管理
- **WHEN** 需要在 Service 方法中使用事务
- **THEN** 应在实现类方法上使用 `@Transactional` 注解

### Requirement: DAL 层使用 MyBatis-Plus + BaseMapperX

DAL 层使用 MyBatis-Plus 进行数据访问，Mapper 接口继承 `BaseMapperX<T>`，DO 实体继承 `BaseDO` 或 `TenantBaseDO`。

#### Scenario: 创建数据实体
- **WHEN** 创建用户数据实体
- **THEN** 应继承 `TenantBaseDO`（需要多租户）或 `BaseDO`，使用 `@TableName` 注解指定表名

#### Scenario: 创建 Mapper 接口
- **WHEN** 创建用户数据访问接口
- **THEN** 应继承 `BaseMapperX<AdminUserDO>`，可使用默认方法如 `selectOne()`、`selectList()`

#### Scenario: 逻辑删除
- **WHEN** 执行删除操作
- **THEN** 由于 DO 有 `@TableLogic` 注解，实际执行的是逻辑删除而非物理删除

### Requirement: 使用 MapStruct 进行层间对象转换

Controller 层的 VO 与 DAL 层的 DO 之间转换必须使用 MapStruct，转换逻辑放在 convert 包下。

#### Scenario: DO 转 VO
- **WHEN** 将查询到的 DO 转换为 VO 返回给前端
- **THEN** 应使用 `XxxConvert.INSTANCE.convert(do, ...)` 方法

#### Scenario: ReqVO 转 DO
- **WHEN** 将前端提交的 ReqVO 转换为 DO 进行保存
- **THEN** 应使用 BeanUtils 或 MapStruct 进行转换
