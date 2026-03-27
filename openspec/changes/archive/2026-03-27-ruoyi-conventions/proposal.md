## Why

本项目（ruoyi-vue-pro）是一个复杂的多模块 Spring Boot 项目，但目前缺乏统一、显式的开发规范文档。新成员上手成本高，代码风格不一致，容易导致重复造轮子和架构偏离。此变更旨在建立完整的项目规范体系，提升开发效率和代码质量。

## What Changes

- **新增**：项目规范文档体系，包括目录结构、模块组织、命名规范、API 接口规范、权限控制模式、代码分层架构
- **新增**：OpenSpec 规格说明，将隐式的架构知识显式化
- **新增**：开发指南和设计原则文档
- **修改**：无破坏性变更，仅补充文档

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

- **影响范围**：所有模块开发、新成员培训、代码审查
- **依赖**：无外部依赖，纯文档变更
- **受益系统**：yudao-module-system、yudao-module-infra、yudao-module-ai 及所有业务模块
