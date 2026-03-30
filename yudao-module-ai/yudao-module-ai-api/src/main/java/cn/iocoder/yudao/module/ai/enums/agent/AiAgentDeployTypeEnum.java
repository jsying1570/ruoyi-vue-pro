package cn.iocoder.yudao.module.ai.enums.agent;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 智能体部署类型枚举
 *
 * @author salter
 * @since 2026-03-27
 */
@AllArgsConstructor
@Getter
public enum AiAgentDeployTypeEnum implements ArrayValuable<Integer> {

    /**
     * K8s 部署
     */
    K8S(1, "K8s"),

    /**
     * Docker 部署
     */
    DOCKER(2, "Docker"),

    /**
     * 本地进程部署
     */
    LOCAL(3, "本地进程");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(AiAgentDeployTypeEnum::getType).toArray(Integer[]::new);

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
