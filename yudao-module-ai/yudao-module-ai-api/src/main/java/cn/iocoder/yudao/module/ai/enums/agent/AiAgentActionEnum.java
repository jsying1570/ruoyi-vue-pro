package cn.iocoder.yudao.module.ai.enums.agent;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 智能体操作类型枚举
 *
 * @author salter
 * @since 2026-03-27
 */
@AllArgsConstructor
@Getter
public enum AiAgentActionEnum {

    /**
     * 创建
     */
    CREATE("create", "创建"),

    /**
     * 更新
     */
    UPDATE("update", "更新"),

    /**
     * 删除
     */
    DELETE("delete", "删除"),

    /**
     * 部署
     */
    DEPLOY("deploy", "部署"),

    /**
     * 停止
     */
    STOP("stop", "停止"),

    /**
     * 重启
     */
    RESTART("restart", "重启"),

    /**
     * 启动
     */
    START("start", "启动");

    /**
     * 操作类型
     */
    private final String action;

    /**
     * 描述
     */
    private final String desc;

}
