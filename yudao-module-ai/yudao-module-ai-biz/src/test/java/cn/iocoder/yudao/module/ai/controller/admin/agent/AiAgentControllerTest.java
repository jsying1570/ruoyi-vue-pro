package cn.iocoder.yudao.module.ai.controller.admin.agent;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentPageReqVO;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentRespVO;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentSaveReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentAuthTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentDeployTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum;
import cn.iocoder.yudao.module.ai.service.agent.AiAgentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * {@link AiAgentController} 的单元测试类
 *
 * @author salter
 */
public class AiAgentControllerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private AiAgentController agentController;

    @Mock
    private AiAgentService agentService;

    @Test
    public void testCreateAgent() {
        // 准备参数
        AiAgentSaveReqVO reqVO = randomPojo(AiAgentSaveReqVO.class, o -> {
            o.setName("测试智能体");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
            o.setModelId(1L);
            o.setStatus(AiAgentStatusEnum.DRAFT.getStatus());
        });
        Long agentId = randomLongId();

        // mock 服务返回
        when(agentService.createAgent(eq(reqVO))).thenReturn(agentId);

        // 调用
        CommonResult<Long> result = agentController.createAgent(reqVO);

        // 断言
        assertEquals(0, result.getCode());
        assertEquals(agentId, result.getData());
        verify(agentService).createAgent(eq(reqVO));
    }

    @Test
    public void testUpdateAgent() {
        // 准备参数
        AiAgentSaveReqVO reqVO = randomPojo(AiAgentSaveReqVO.class, o -> {
            o.setId(randomLongId());
            o.setName("新名称");
        });

        // 调用
        CommonResult<Boolean> result = agentController.updateAgent(reqVO);

        // 断言
        assertEquals(0, result.getCode());
        assertTrue(result.getData());
        verify(agentService).updateAgent(eq(reqVO));
    }

    @Test
    public void testDeleteAgent() {
        // 准备参数
        Long id = randomLongId();

        // 调用
        CommonResult<Boolean> result = agentController.deleteAgent(id);

        // 断言
        assertEquals(0, result.getCode());
        assertTrue(result.getData());
        verify(agentService).deleteAgent(eq(id));
    }

    @Test
    public void testGetAgent() {
        // 准备参数
        Long id = randomLongId();
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> {
            o.setId(id);
            o.setName("测试智能体");
            o.setDeployType(AiAgentDeployTypeEnum.LOCAL.getType());
        });

        // mock 服务返回
        when(agentService.getAgent(eq(id))).thenReturn(agent);

        // 调用
        CommonResult<AiAgentRespVO> result = agentController.getAgent(id);

        // 断言
        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertEquals(agent.getName(), result.getData().getName());
        verify(agentService).getAgent(eq(id));
    }

    @Test
    public void testGetAgentPage() {
        // 准备参数
        AiAgentPageReqVO pageReqVO = randomPojo(AiAgentPageReqVO.class);
        AiAgentDO agent = randomPojo(AiAgentDO.class, o -> o.setName("测试"));
        PageResult<AiAgentDO> pageResult = new PageResult<>(List.of(agent), 1L);

        // mock 服务返回
        when(agentService.getAgentPage(eq(pageReqVO))).thenReturn(pageResult);

        // 调用
        CommonResult<PageResult<AiAgentRespVO>> result = agentController.getAgentPage(pageReqVO);

        // 断言
        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData().getTotal());
        verify(agentService).getAgentPage(eq(pageReqVO));
    }

    @Test
    public void testDeployAgent() {
        // 准备参数
        Long id = randomLongId();

        // 调用
        CommonResult<Boolean> result = agentController.deployAgent(id);

        // 断言
        assertEquals(0, result.getCode());
        assertTrue(result.getData());
        verify(agentService).deployAgent(eq(id));
    }

    @Test
    public void testStopAgent() {
        // 准备参数
        Long id = randomLongId();

        // 调用
        CommonResult<Boolean> result = agentController.stopAgent(id);

        // 断言
        assertEquals(0, result.getCode());
        assertTrue(result.getData());
        verify(agentService).stopAgent(eq(id));
    }

    @Test
    public void testRestartAgent() {
        // 准备参数
        Long id = randomLongId();

        // 调用
        CommonResult<Boolean> result = agentController.restartAgent(id);

        // 断言
        assertEquals(0, result.getCode());
        assertTrue(result.getData());
        verify(agentService).restartAgent(eq(id));
    }

}
