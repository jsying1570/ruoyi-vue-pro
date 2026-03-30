package cn.iocoder.yudao.module.ai.service.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.*;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import jakarta.validation.Valid;

/**
 * 智能体 Service 接口
 *
 * @author salter
 * @since 2026-03-27
 */
public interface AiAgentService {

    /**
     * 创建智能体
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAgent(@Valid AiAgentSaveReqVO createReqVO);

    /**
     * 更新智能体
     *
     * @param updateReqVO 更新信息
     */
    void updateAgent(@Valid AiAgentSaveReqVO updateReqVO);

    /**
     * 删除智能体
     *
     * @param id 编号
     */
    void deleteAgent(Long id);

    /**
     * 获得智能体
     *
     * @param id 编号
     * @return 智能体
     */
    AiAgentDO getAgent(Long id);

    /**
     * 获得智能体分页
     *
     * @param pageReqVO 分页查询
     * @return 智能体分页
     */
    PageResult<AiAgentDO> getAgentPage(AiAgentPageReqVO pageReqVO);

    /**
     * 部署智能体
     *
     * @param id 编号
     */
    void deployAgent(Long id);

    /**
     * 停止智能体
     *
     * @param id 编号
     */
    void stopAgent(Long id);

    /**
     * 重启智能体
     *
     * @param id 编号
     */
    void restartAgent(Long id);

}
