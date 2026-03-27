## ADDED Requirements

### Requirement: 所有 API 响应使用统一包装结构

所有 REST API 响应必须使用 `CommonResult<T>` 包装类，包含 code、data、msg 三个字段。

#### Scenario: 成功响应
- **WHEN** API 调用成功
- **THEN** 返回 `{ "code": 0, "data": {...}, "msg": "" }`

#### Scenario: 错误响应
- **WHEN** API 调用失败
- **THEN** 返回 `{ "code": 错误码，"data": null, "msg": "错误信息" }`

#### Scenario: 分页查询响应
- **WHEN** 调用分页查询接口
- **THEN** 返回 `{ "code": 0, "data": { "list": [...], "total": 100 }, "msg": "" }`

### Requirement: 使用 Jakarta Validation 进行请求参数校验

所有请求 VO 必须使用 Jakarta Validation 注解（@NotBlank、@Size、@Email 等）进行参数校验。

#### Scenario: 用户账号校验
- **WHEN** 创建用户时传递用户账号
- **THEN** 应使用 `@NotBlank`、`@Pattern`、`@Size` 注解校验格式和长度

#### Scenario: 邮箱校验
- **WHEN** 创建用户时传递邮箱
- **THEN** 应使用 `@Email` 和 `@Size` 注解校验

#### Scenario: 手机号校验
- **WHEN** 创建用户时传递手机号
- **THEN** 应使用自定义 `@Mobile` 注解校验格式

### Requirement: 使用 OpenAPI 3 注解进行接口文档化

所有 Controller 和 VO 必须使用 OpenAPI 3（io.swagger.v3）注解进行文档化。

#### Scenario: Controller 文档化
- **WHEN** 定义 Controller 类
- **THEN** 应使用 `@Tag(name = "...")` 描述接口分组

#### Scenario: 接口方法文档化
- **WHEN** 定义接口方法
- **THEN** 应使用 `@Operation(summary = "...")` 描述接口功能

#### Scenario: VO 字段文档化
- **WHEN** 定义 VO 字段
- **THEN** 应使用 `@Schema(description = "...", example = "...")` 描述字段含义

### Requirement: 使用 MapStruct 进行 DO 与 VO 之间的转换

数据对象（DO）与视图对象（VO）之间的转换必须使用 MapStruct 定义的 Convert 接口。

#### Scenario: 用户列表转换
- **WHEN** 将 AdminUserDO 列表转换为 UserRespVO 列表
- **THEN** 应使用 `UserConvert.INSTANCE.convertList(list, deptMap)`

#### Scenario: 单个对象转换
- **WHEN** 将 AdminUserDO 转换为 UserRespVO
- **THEN** 应使用 `UserConvert.INSTANCE.convert(user, dept)`
