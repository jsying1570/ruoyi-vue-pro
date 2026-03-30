package cn.iocoder.yudao.module.ai.dal.dataobject.agent;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Map;

/**
 * 智能体实例 DO
 *
 * @author salter
 * @since 2026-03-27
 */
@TableName(value = "ai_agent", autoResultMap = true)
@KeySequence("ai_agent_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 智能体名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 图标 URL
     */
    private String icon;

    /**
     * 所属应用 ID
     */
    private Long appId;

    /**
     * 绑定的模型 ID
     */
    private Long modelId;

    /**
     * 模型参数配置 (JSON)
     */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> modelParams;

    /**
     * 部署类型 (1=K8s, 2=Docker, 3=本地进程)
     *
     * 枚举 {@link cn.iocoder.yudao.module.ai.enums.agent.AiAgentDeployTypeEnum}
     */
    private Integer deployType;

    /**
     * CPU 配额
     */
    private String resourceCpu;

    /**
     * 内存配额
     */
    private String resourceMemory;

    /**
     * GPU 数量
     */
    private Integer resourceGpu;

    /**
     * 镜像地址
     */
    private String imageUrl;

    /**
     * 环境变量 (JSON)
     */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> envVars;

    /**
     * 访问端点
     */
    private String endpoint;

    /**
     * 认证类型 (0=无，1=API Key, 2=JWT)
     *
     * 枚举 {@link cn.iocoder.yudao.module.ai.enums.agent.AiAgentAuthTypeEnum}
     */
    private Integer authType;

    /**
     * API Key
     */
    private String apiKey;

    /**
     * 限流 (请求/分钟)
     */
    private Integer rateLimit;

    /**
     * 状态 (0=草稿，1=部署中，2=运行中，3=已停止，4=失败)
     *
     * 枚举 {@link cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum}
     */
    private Integer status;

    /**
     * 副本数
     */
    private Integer replicas;

}
