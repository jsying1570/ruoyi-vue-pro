package cn.iocoder.yudao.module.ai.service.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentOperateLogPageReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentOperateLogDO;
import cn.iocoder.yudao.module.ai.dal.mysql.agent.AiAgentOperateLogMapper;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentActionEnum;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能体操作日志 Service 实现类
 *
 * @author salter
 * @since 2026-03-27
 */
@Service
@Slf4j
public class AiAgentLogServiceImpl implements AiAgentLogService {

    @Resource
    private AiAgentOperateLogMapper logMapper;

    @Override
    public void createLog(Long agentId, AiAgentActionEnum action, Integer targetStatus, Integer resultStatus, String errorMessage) {
        AiAgentOperateLogDO logDO = AiAgentOperateLogDO.builder()
                .agentId(agentId)
                .action(action.getAction())
                .targetStatus(targetStatus)
                .resultStatus(resultStatus)
                .errorMessage(errorMessage)
                .operatorType(1) // TODO: 从上下文获取操作者类型
                .operatorId(null) // TODO: 从上下文获取操作者 ID
                .requestParams(new HashMap<>()) // TODO: 从上下文获取请求参数
                .extraData(new HashMap<>())
                .build();
        logMapper.insert(logDO);
    }

    @Override
    public PageResult<AiAgentOperateLogDO> getLogPage(AiAgentOperateLogPageReqVO pageReqVO) {
        return logMapper.selectPage(pageReqVO);
    }

    @Override
    public AiAgentOperateLogDO getLog(Long id) {
        return logMapper.selectById(id);
    }

    @Override
    public List<AiAgentOperateLogDO> getLogListByAgentId(Long agentId) {
        return logMapper.selectListByAgentId(agentId);
    }

}
