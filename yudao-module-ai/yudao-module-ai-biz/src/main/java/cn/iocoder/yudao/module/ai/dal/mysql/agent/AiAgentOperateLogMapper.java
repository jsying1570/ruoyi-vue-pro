package cn.iocoder.yudao.module.ai.dal.mysql.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentOperateLogPageReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentOperateLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 智能体操作日志 Mapper
 *
 * @author salter
 * @since 2026-03-27
 */
@Mapper
public interface AiAgentOperateLogMapper extends BaseMapperX<AiAgentOperateLogDO> {

    default PageResult<AiAgentOperateLogDO> selectPage(AiAgentOperateLogPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<AiAgentOperateLogDO>()
                .eqIfPresent(AiAgentOperateLogDO::getAgentId, pageReqVO.getAgentId())
                .eqIfPresent(AiAgentOperateLogDO::getAction, pageReqVO.getAction())
                .eqIfPresent(AiAgentOperateLogDO::getResultStatus, pageReqVO.getResultStatus())
                .betweenIfPresent(AiAgentOperateLogDO::getCreateTime, pageReqVO.getCreateTime()));
    }

    default List<AiAgentOperateLogDO> selectListByAgentId(Long agentId) {
        return selectList(AiAgentOperateLogDO::getAgentId, agentId);
    }

}
