package cn.iocoder.yudao.module.ai.enums.agent;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 智能体状态枚举
 *
 * @author salter
 * @since 2026-03-27
 */
@AllArgsConstructor
@Getter
public enum AiAgentStatusEnum implements ArrayValuable<Integer> {

    /**
     * 草稿
     */
    DRAFT(0, "草稿"),

    /**
     * 部署中
     */
    DEPLOYING(1, "部署中"),

    /**
     * 运行中
     */
    RUNNING(2, "运行中"),

    /**
     * 已停止
     */
    STOPPED(3, "已停止"),

    /**
     * 失败
     */
    FAILED(4, "失败");

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(AiAgentStatusEnum::getStatus).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
