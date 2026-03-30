package cn.iocoder.yudao.module.ai.service.agent;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentSaveReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import cn.iocoder.yudao.module.ai.dal.mysql.agent.AiAgentMapper;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentAuthTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentDeployTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.module.ai.enums.ErrorCodeConstants.AGENT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AiAgentServiceImpl} 的单元测试类
 *
 * @author salter
 */
@Import({AiAgentServiceImpl.class})
public class AiAgentServiceTest extends BaseDbUnitTest {

    @Resource
    private AiAgentServiceImpl agentService;

    @Resource
    private AiAgentMapper agentMapper;

    @MockBean
    private Validator validator;

    @Test
    public void testCreateAgent_success() {
        // 准备参数
        Map<String, Object> modelParams = new HashMap<>();
        modelParams.put("temperature", 0.7);
        Map<String, Object> envVars = new HashMap<>();
        envVars.put("API_KEY", "test-key");

        AiAgentSaveReqVO reqVO = randomPojo(AiAgentSaveReqVO.class, o -> {
            o.setName("测试智能体");
            o.setDescription("测试描述");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
            o.setModelId(1L);
            o.setStatus(AiAgentStatusEnum.DRAFT.getStatus());
            o.setAuthType(AiAgentAuthTypeEnum.API_KEY.getType());
            o.setResourceCpu("1000m");
            o.setResourceMemory("512Mi");
            o.setReplicas(1);
        });

        // 调用
        Long agentId = agentService.createAgent(reqVO);

        // 校验记录的属性是否正确
        AiAgentDO agent = agentMapper.selectById(agentId);
        assertNotNull(agent);
        assertEquals(reqVO.getName(), agent.getName());
        assertEquals(reqVO.getDescription(), agent.getDescription());
        assertEquals(reqVO.getDeployType(), agent.getDeployType());
        assertEquals(reqVO.getModelId(), agent.getModelId());
        assertEquals(reqVO.getStatus(), agent.getStatus());
        assertEquals(reqVO.getAuthType(), agent.getAuthType());
        assertEquals(reqVO.getResourceCpu(), agent.getResourceCpu());
        assertEquals(reqVO.getResourceMemory(), agent.getResourceMemory());
        assertEquals(reqVO.getReplicas(), agent.getReplicas());
    }

    @Test
    public void testUpdateAgent_success() {
        // mock 数据
        AiAgentDO dbAgent = randomPojo(AiAgentDO.class, o -> {
            o.setName("原名称");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
            o.setStatus(AiAgentStatusEnum.DRAFT.getStatus());
        });
        agentMapper.insert(dbAgent);

        // 准备参数
        AiAgentSaveReqVO reqVO = randomPojo(AiAgentSaveReqVO.class, o -> {
            o.setId(dbAgent.getId());
            o.setName("新名称");
            o.setDescription("新描述");
            o.setStatus(AiAgentStatusEnum.RUNNING.getStatus());
        });

        // 调用
        agentService.updateAgent(reqVO);

        // 校验是否更新正确
        AiAgentDO agent = agentMapper.selectById(reqVO.getId());
        assertEquals(reqVO.getName(), agent.getName());
        assertEquals(reqVO.getDescription(), agent.getDescription());
        assertEquals(reqVO.getStatus(), agent.getStatus());
    }

    @Test
    public void testUpdateAgent_notExists() {
        // 准备参数
        AiAgentSaveReqVO reqVO = randomPojo(AiAgentSaveReqVO.class, o -> {
            o.setId(randomLongId());
            o.setName("测试");
        });

        // 调用，并断言异常
        assertServiceException(() -> agentService.updateAgent(reqVO), AGENT_NOT_EXISTS);
    }

    @Test
    public void testDeleteAgent_success() {
        // mock 数据
        AiAgentDO dbAgent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
        });
        agentMapper.insert(dbAgent);

        // 准备参数
        Long id = dbAgent.getId();

        // 调用
        agentService.deleteAgent(id);

        // 校验数据不存在了
        assertNull(agentMapper.selectById(id));
    }

    @Test
    public void testDeleteAgent_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用，并断言异常
        assertServiceException(() -> agentService.deleteAgent(id), AGENT_NOT_EXISTS);
    }

    @Test
    public void testGetAgent() {
        // mock 数据
        AiAgentDO dbAgent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
        });
        agentMapper.insert(dbAgent);

        // 准备参数
        Long id = dbAgent.getId();

        // 调用
        AiAgentDO agent = agentService.getAgent(id);

        // 校验是否正确
        assertPojoEquals(dbAgent, agent);
    }

    @Test
    public void testGetAgentPage() {
        // mock 数据
        AiAgentDO dbAgent01 = randomPojo(AiAgentDO.class, o -> {
            o.setName("智能体 1");
            o.setStatus(AiAgentStatusEnum.RUNNING.getStatus());
        });
        agentMapper.insert(dbAgent01);

        AiAgentDO dbAgent02 = randomPojo(AiAgentDO.class, o -> {
            o.setName("智能体 2");
            o.setStatus(AiAgentStatusEnum.DRAFT.getStatus());
        });
        agentMapper.insert(dbAgent02);

        // 调用：查询所有
        var pageResult = agentService.getAgentPage(null);
        assertEquals(2, pageResult.getTotal());

        // 调用：按状态筛选
        // TODO: 需要实现 AiAgentPageReqVO 的测试
    }

    @Test
    public void testDeployAgent_success() {
        // mock 数据
        AiAgentDO dbAgent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
            o.setStatus(AiAgentStatusEnum.DRAFT.getStatus());
        });
        agentMapper.insert(dbAgent);

        // 准备参数
        Long id = dbAgent.getId();

        // 调用
        agentService.deployAgent(id);

        // 校验状态变为部署中
        AiAgentDO agent = agentMapper.selectById(id);
        assertEquals(AiAgentStatusEnum.DEPLOYING.getStatus(), agent.getStatus());
    }

    @Test
    public void testDeployAgent_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用，并断言异常
        assertServiceException(() -> agentService.deployAgent(id), AGENT_NOT_EXISTS);
    }

    @Test
    public void testStopAgent_success() {
        // mock 数据
        AiAgentDO dbAgent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
            o.setStatus(AiAgentStatusEnum.RUNNING.getStatus());
        });
        agentMapper.insert(dbAgent);

        // 准备参数
        Long id = dbAgent.getId();

        // 调用
        agentService.stopAgent(id);

        // 校验状态变为已停止
        AiAgentDO agent = agentMapper.selectById(id);
        assertEquals(AiAgentStatusEnum.STOPPED.getStatus(), agent.getStatus());
    }

    @Test
    public void testStopAgent_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用，并断言异常
        assertServiceException(() -> agentService.stopAgent(id), AGENT_NOT_EXISTS);
    }

    @Test
    public void testRestartAgent_success() {
        // mock 数据
        AiAgentDO dbAgent = randomPojo(AiAgentDO.class, o -> {
            o.setName("测试");
            o.setStatus(AiAgentStatusEnum.STOPPED.getStatus());
        });
        agentMapper.insert(dbAgent);

        // 准备参数
        Long id = dbAgent.getId();

        // 调用
        agentService.restartAgent(id);

        // 校验状态变为部署中
        AiAgentDO agent = agentMapper.selectById(id);
        assertEquals(AiAgentStatusEnum.DEPLOYING.getStatus(), agent.getStatus());
    }

    @Test
    public void testRestartAgent_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用，并断言异常
        assertServiceException(() -> agentService.restartAgent(id), AGENT_NOT_EXISTS);
    }

}
