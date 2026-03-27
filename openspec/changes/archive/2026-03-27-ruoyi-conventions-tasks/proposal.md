## Why

ruoyi-conventions 变更已创建综合规范文档，但第 1-6 章的 22 个任务仍标记为未完成。本变更旨在完成这些剩余任务，将规范文档拆分到独立的能力目录中，提高可读性和可维护性。

## What Changes

- **新增**：6 个独立的能力规范文档（project-structure, naming-conventions, api-spec, permission-model, layered-architecture, data-object-pattern）
- **新增**：每个能力规范的 specs 文件到 openspec/specs/ 目录
- **完成**：标记 tasks.md 中 22 个任务为完成

## Capabilities

### New Capabilities
- `project-structure`: 项目目录结构和模块组织规范
- `naming-conventions`: 文件、类、方法、接口的命名规范
- `api-spec`: REST API 接口规范（请求/响应结构、错误处理）
- `permission-model`: 基于 Spring Security 的权限控制模式
- `layered-architecture`: 代码分层架构（Controller/Service/DAL）
- `data-object-pattern`: 数据对象模式和 MyBatis-Plus 使用规范

### Modified Capabilities
- 无

## Impact

- **影响范围**：openspec/specs/ 目录结构
- **依赖**：无外部依赖，纯文档变更
- **受益系统**：所有模块开发、新成员培训、代码审查
