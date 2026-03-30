package cn.iocoder.yudao.module.ai.dal.dataobject.agent;

import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.Map;

/**
 * 智能体操作日志 DO
 *
 * @author salter
 * @since 2026-03-27
 */
@TableName(value = "ai_agent_operate_log", autoResultMap = true)
@KeySequence("ai_agent_operate_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAgentOperateLogDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 智能体 ID
     */
    private Long agentId;

    /**
     * 操作者类型
     */
    private Integer operatorType;

    /**
     * 操作者 ID
     */
    private Long operatorId;

    /**
     * 操作类型
     */
    private String action;

    /**
     * 目标状态
     */
    private Integer targetStatus;

    /**
     * 请求参数 (JSON)
     */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> requestParams;

    /**
     * 额外数据 (JSON)
     */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraData;

    /**
     * 结果状态
     */
    private Integer resultStatus;

    /**
     * 错误信息
     */
    private String errorMessage;

}
