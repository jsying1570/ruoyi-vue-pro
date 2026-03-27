## ADDED Requirements

### Requirement: 项目采用多层模块目录结构

项目根目录包含框架层、服务器入口、业务模块和数据库脚本。业务模块采用 API+BIZ 双层结构。

#### Scenario: 创建新业务模块
- **WHEN** 开发者需要添加新的业务模块（如 yudao-module-xxx）
- **THEN** 应在根目录创建 `yudao-module-xxx/` 目录，并包含 `yudao-module-xxx-api/` 和 `yudao-module-xxx-biz/` 两个子模块

#### Scenario: 模块依赖配置
- **WHEN** 配置模块依赖时
- **THEN** 应在根 pom.xml 的 `<modules>` 中注册新模块，-biz 模块依赖对应的-api 模块

### Requirement: Framework 层按功能拆分为独立 Starter

yudao-framework 目录包含多个独立的 Spring Boot Starter，每个 Starter 负责特定功能领域（如 web、security、mybatis、redis 等）。

#### Scenario: 使用 Redis 功能
- **WHEN** 模块需要使用 Redis 缓存功能
- **THEN** 应添加 `yudao-spring-boot-starter-redis` 依赖

#### Scenario: 使用数据权限功能
- **WHEN** 模块需要数据权限控制
- **THEN** 应添加 `yudao-spring-boot-starter-biz-data-permission` 依赖
