package cn.iocoder.yudao.module.ai.dal.mysql.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentPageReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 智能体 Mapper
 *
 * @author salter
 * @since 2026-03-27
 */
@Mapper
public interface AiAgentMapper extends BaseMapperX<AiAgentDO> {

    default AiAgentDO selectByName(String name) {
        return selectOne(AiAgentDO::getName, name);
    }

    default PageResult<AiAgentDO> selectPage(AiAgentPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<AiAgentDO>()
                .eqIfPresent(AiAgentDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(AiAgentDO::getDeployType, pageReqVO.getDeployType())
                .likeIfPresent(AiAgentDO::getName, pageReqVO.getName())
                .eqIfPresent(AiAgentDO::getModelId, pageReqVO.getModelId())
                .betweenIfPresent(AiAgentDO::getCreateTime, pageReqVO.getCreateTime()));
    }

    default List<AiAgentDO> selectListByStatus(Integer status) {
        return selectList(AiAgentDO::getStatus, status);
    }

}
