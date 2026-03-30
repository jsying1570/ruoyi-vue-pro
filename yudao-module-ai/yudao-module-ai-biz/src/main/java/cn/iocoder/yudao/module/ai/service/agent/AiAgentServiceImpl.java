package cn.iocoder.yudao.module.ai.service.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentPageReqVO;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentSaveReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import cn.iocoder.yudao.module.ai.dal.mysql.agent.AiAgentMapper;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentActionEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ai.enums.ErrorCodeConstants.AGENT_NOT_EXISTS;

/**
 * 智能体 Service 实现类
 *
 * @author salter
 * @since 2026-03-27
 */
@Service
@Slf4j
public class AiAgentServiceImpl implements AiAgentService {

    @Resource
    private AiAgentMapper agentMapper;

    @Resource
    private AiAgentLogService agentLogService;

    @Override
    public Long createAgent(AiAgentSaveReqVO createReqVO) {
        // 1. 插入数据库
        AiAgentDO agent = BeanUtils.toBean(createReqVO, AiAgentDO.class);
        agentMapper.insert(agent);

        // 2. 记录操作日志
        agentLogService.createLog(agent.getId(), AiAgentActionEnum.CREATE, null, agent.getStatus(), null);

        return agent.getId();
    }

    @Override
    public void updateAgent(AiAgentSaveReqVO updateReqVO) {
        // 1. 校验存在
        validateAgentExists(updateReqVO.getId());

        // 2. 更新智能体配置
        AiAgentDO updateObj = BeanUtils.toBean(updateReqVO, AiAgentDO.class);
        agentMapper.updateById(updateObj);

        // 3. 记录操作日志
        agentLogService.createLog(updateReqVO.getId(), AiAgentActionEnum.UPDATE, null, updateReqVO.getStatus(), null);
    }

    @Override
    public void deleteAgent(Long id) {
        // 1. 校验存在
        validateAgentExists(id);

        // 2. 删除智能体配置
        agentMapper.deleteById(id);

        // 3. 记录操作日志
        agentLogService.createLog(id, AiAgentActionEnum.DELETE, null, null, null);
    }

    @Override
    public AiAgentDO getAgent(Long id) {
        return agentMapper.selectById(id);
    }

    @Override
    public PageResult<AiAgentDO> getAgentPage(AiAgentPageReqVO pageReqVO) {
        return agentMapper.selectPage(pageReqVO);
    }

    @Override
    public void deployAgent(Long id) {
        // 1. 校验存在
        AiAgentDO agent = validateAgentExists(id);

        // 2. 更新状态为部署中
        AiAgentDO updateObj = new AiAgentDO();
        updateObj.setId(id);
        updateObj.setStatus(AiAgentStatusEnum.DEPLOYING.getStatus());
        agentMapper.updateById(updateObj);

        // 3. 记录操作日志
        agentLogService.createLog(id, AiAgentActionEnum.DEPLOY, AiAgentStatusEnum.DEPLOYING.getStatus(), null, null);

        // TODO: 4. 实现实际的部署逻辑
        log.info("部署智能体：id={}, name={}, deployType={}", id, agent.getName(), agent.getDeployType());
    }

    @Override
    public void stopAgent(Long id) {
        // 1. 校验存在
        AiAgentDO agent = validateAgentExists(id);

        // 2. 更新状态为已停止
        AiAgentDO updateObj = new AiAgentDO();
        updateObj.setId(id);
        updateObj.setStatus(AiAgentStatusEnum.STOPPED.getStatus());
        agentMapper.updateById(updateObj);

        // 3. 记录操作日志
        agentLogService.createLog(id, AiAgentActionEnum.STOP, AiAgentStatusEnum.STOPPED.getStatus(), null, null);

        // TODO: 4. 实现实际的停止逻辑
        log.info("停止智能体：id={}, name={}", id, agent.getName());
    }

    @Override
    public void restartAgent(Long id) {
        // 1. 校验存在
        AiAgentDO agent = validateAgentExists(id);

        // 2. 更新状态为部署中
        AiAgentDO updateObj = new AiAgentDO();
        updateObj.setId(id);
        updateObj.setStatus(AiAgentStatusEnum.DEPLOYING.getStatus());
        agentMapper.updateById(updateObj);

        // 3. 记录操作日志
        agentLogService.createLog(id, AiAgentActionEnum.RESTART, AiAgentStatusEnum.RUNNING.getStatus(), null, null);

        // TODO: 4. 实现实际的重启逻辑
        log.info("重启智能体：id={}, name={}", id, agent.getName());
    }

    private AiAgentDO validateAgentExists(Long id) {
        AiAgentDO agent = agentMapper.selectById(id);
        if (agent == null) {
            throw exception(AGENT_NOT_EXISTS);
        }
        return agent;
    }

}
