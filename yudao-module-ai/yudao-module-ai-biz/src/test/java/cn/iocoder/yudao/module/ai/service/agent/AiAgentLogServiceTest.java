package cn.iocoder.yudao.module.ai.service.agent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentOperateLogPageReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentOperateLogDO;
import cn.iocoder.yudao.module.ai.dal.mysql.agent.AiAgentMapper;
import cn.iocoder.yudao.module.ai.dal.mysql.agent.AiAgentOperateLogMapper;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentActionEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentDeployTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AiAgentLogServiceImpl} 的单元测试类
 *
 * @author salter
 */
@Import({AiAgentLogServiceImpl.class})
public class AiAgentLogServiceTest extends BaseDbUnitTest {

    @Resource
    private AiAgentLogServiceImpl logService;

    @Resource
    private AiAgentOperateLogMapper logMapper;

    @Resource
    private AiAgentMapper agentMapper;

    @Test
    public void testCreateLog() {
        // 准备参数
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试智能体");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
        });
        agentMapper.insert(agent);

        // 调用
        logService.createLog(agent.getId(), AiAgentActionEnum.DEPLOY,
                AiAgentStatusEnum.RUNNING.getStatus(), 1, null);

        // 校验记录
        List<AiAgentOperateLogDO> logs = logMapper.selectListByAgentId(agent.getId());
        assertEquals(1, logs.size());
        AiAgentOperateLogDO log = logs.get(0);
        assertEquals(agent.getId(), log.getAgentId());
        assertEquals(AiAgentActionEnum.DEPLOY.getAction(), log.getAction());
        assertEquals(AiAgentStatusEnum.RUNNING.getStatus(), log.getTargetStatus());
    }

    @Test
    public void testGetLog() {
        // mock 数据
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
        });
        agentMapper.insert(agent);

        AiAgentOperateLogDO dbLog = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.DEPLOY.getAction());
        });
        logMapper.insert(dbLog);

        // 调用
        AiAgentOperateLogDO log = logService.getLog(dbLog.getId());

        // 校验
        assertPojoEquals(dbLog, log);
    }

    @Test
    public void testGetLogPage() {
        // mock 数据
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
        });
        agentMapper.insert(agent);

        AiAgentOperateLogDO log01 = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.DEPLOY.getAction());
        });
        logMapper.insert(log01);

        AiAgentOperateLogDO log02 = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.STOP.getAction());
        });
        logMapper.insert(log02);

        // 调用：查询所有
        AiAgentOperateLogPageReqVO pageReqVO = new AiAgentOperateLogPageReqVO();
        pageReqVO.setAgentId(agent.getId());
        PageResult<AiAgentOperateLogDO> pageResult = logService.getLogPage(pageReqVO);

        // 校验
        assertEquals(2, pageResult.getTotal());
    }

    @Test
    public void testGetLogPage_byAction() {
        // mock 数据
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
        });
        agentMapper.insert(agent);

        AiAgentOperateLogDO log01 = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.DEPLOY.getAction());
        });
        logMapper.insert(log01);

        AiAgentOperateLogDO log02 = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.STOP.getAction());
        });
        logMapper.insert(log02);

        // 调用：按 action 筛选
        AiAgentOperateLogPageReqVO pageReqVO = new AiAgentOperateLogPageReqVO();
        pageReqVO.setAgentId(agent.getId());
        pageReqVO.setAction(AiAgentActionEnum.DEPLOY.getAction());
        PageResult<AiAgentOperateLogDO> pageResult = logService.getLogPage(pageReqVO);

        // 校验
        assertEquals(1, pageResult.getTotal());
        assertEquals(AiAgentActionEnum.DEPLOY.getAction(), pageResult.getList().get(0).getAction());
    }

    @Test
    public void testGetLogListByAgentId() {
        // mock 数据
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
        });
        agentMapper.insert(agent);

        AiAgentOperateLogDO log01 = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.DEPLOY.getAction());
        });
        logMapper.insert(log01);

        AiAgentOperateLogDO log02 = randomPojo(AiAgentOperateLogDO.class, o -> {
            o.setAgentId(agent.getId());
            o.setAction(AiAgentActionEnum.STOP.getAction());
        });
        logMapper.insert(log02);

        // 调用
        List<AiAgentOperateLogDO> logs = logService.getLogListByAgentId(agent.getId());

        // 校验
        assertEquals(2, logs.size());
    }

}
