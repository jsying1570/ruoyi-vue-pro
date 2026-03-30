package cn.iocoder.yudao.module.ai.service.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentOperateLogPageReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentOperateLogDO;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentActionEnum;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 智能体操作日志 Service 接口
 *
 * @author salter
 * @since 2026-03-27
 */
public interface AiAgentLogService {

    /**
     * 创建操作日志
     *
     * @param agentId 智能体 ID
     * @param action 操作类型
     * @param targetStatus 目标状态
     * @param resultStatus 结果状态
     * @param errorMessage 错误信息
     */
    void createLog(Long agentId, AiAgentActionEnum action, Integer targetStatus, Integer resultStatus, String errorMessage);

    /**
     * 获得操作日志分页
     *
     * @param pageReqVO 分页查询
     * @return 操作日志分页
     */
    PageResult<AiAgentOperateLogDO> getLogPage(@Valid AiAgentOperateLogPageReqVO pageReqVO);

    /**
     * 获得操作日志
     *
     * @param id 编号
     * @return 操作日志
     */
    AiAgentOperateLogDO getLog(Long id);

    /**
     * 获得智能体的操作日志列表
     *
     * @param agentId 智能体 ID
     * @return 操作日志列表
     */
    List<AiAgentOperateLogDO> getLogListByAgentId(Long agentId);

}
