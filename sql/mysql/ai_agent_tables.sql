-- ----------------------------
-- 智能体管理模块 - MySQL 建表脚本
-- ----------------------------

-- ----------------------------
-- 智能体实例表
-- ----------------------------
DROP TABLE IF EXISTS `ai_agent`;
CREATE TABLE `ai_agent` (
    `id` BIGINT NOT NULL COMMENT '主键 ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
    `name` VARCHAR(100) NOT NULL COMMENT '智能体名称',
    `description` VARCHAR(500) COMMENT '描述',
    `icon` VARCHAR(255) COMMENT '图标 URL',
    `app_id` BIGINT COMMENT '所属应用 ID',
    `model_id` BIGINT COMMENT '绑定的模型 ID',
    `model_params` JSON COMMENT '模型参数配置',
    `deploy_type` TINYINT NOT NULL COMMENT '部署类型 (1=K8s, 2=Docker, 3=本地进程)',
    `resource_cpu` VARCHAR(20) COMMENT 'CPU 配额',
    `resource_memory` VARCHAR(20) COMMENT '内存配额',
    `resource_gpu` TINYINT COMMENT 'GPU 数量',
    `image_url` VARCHAR(255) COMMENT '镜像地址',
    `env_vars` JSON COMMENT '环境变量',
    `endpoint` VARCHAR(255) COMMENT '访问端点',
    `auth_type` TINYINT COMMENT '认证类型 (0=无，1=API Key, 2=JWT)',
    `api_key` VARCHAR(100) COMMENT 'API Key',
    `rate_limit` INT COMMENT '限流 (请求/分钟)',
    `status` TINYINT NOT NULL COMMENT '状态 (0=草稿，1=部署中，2=运行中，3=已停止，4=失败)',
    `replicas` INT DEFAULT 1 COMMENT '副本数',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_status` (`status`),
    KEY `idx_model_id` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体实例表';

-- ----------------------------
-- 智能体操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `ai_agent_operate_log`;
CREATE TABLE `ai_agent_operate_log` (
    `id` BIGINT NOT NULL COMMENT '主键 ID',
    `tenant_id` BIGINT NOT NULL COMMENT '租户 ID',
    `agent_id` BIGINT NOT NULL COMMENT '智能体 ID',
    `operator_type` TINYINT NOT NULL COMMENT '操作者类型',
    `operator_id` BIGINT COMMENT '操作者 ID',
    `action` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `target_status` TINYINT COMMENT '目标状态',
    `request_params` JSON COMMENT '请求参数',
    `extra_data` JSON COMMENT '额外数据',
    `result_status` TINYINT COMMENT '结果状态',
    `error_message` VARCHAR(1000) COMMENT '错误信息',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_agent_id` (`agent_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能体操作日志表';
