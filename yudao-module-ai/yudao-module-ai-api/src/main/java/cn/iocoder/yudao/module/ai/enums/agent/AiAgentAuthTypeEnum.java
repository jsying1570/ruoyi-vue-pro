package cn.iocoder.yudao.module.ai.enums.agent;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 智能体认证类型枚举
 *
 * @author salter
 * @since 2026-03-27
 */
@AllArgsConstructor
@Getter
public enum AiAgentAuthTypeEnum implements ArrayValuable<Integer> {

    /**
     * 无认证
     */
    NONE(0, "无认证"),

    /**
     * API Key 认证
     */
    API_KEY(1, "API Key"),

    /**
     * JWT 认证
     */
    JWT(2, "JWT");

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(AiAgentAuthTypeEnum::getType).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
