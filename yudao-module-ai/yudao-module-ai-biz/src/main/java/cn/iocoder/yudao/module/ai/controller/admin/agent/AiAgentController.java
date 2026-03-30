package cn.iocoder.yudao.module.ai.controller.admin.agent;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.*;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentDO;
import cn.iocoder.yudao.module.ai.service.agent.AiAgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 智能体")
@RestController
@RequestMapping("/ai/agent")
@Validated
@Slf4j
public class AiAgentController {

    @Resource
    private AiAgentService agentService;

    @PostMapping("/create")
    @Operation(summary = "创建智能体")
    @PreAuthorize("@ss.hasPermission('ai:agent:create')")
    public CommonResult<Long> createAgent(@Valid @RequestBody AiAgentSaveReqVO createReqVO) {
        return success(agentService.createAgent(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新智能体")
    @PreAuthorize("@ss.hasPermission('ai:agent:update')")
    public CommonResult<Boolean> updateAgent(@Valid @RequestBody AiAgentSaveReqVO updateReqVO) {
        agentService.updateAgent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除智能体")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ai:agent:delete')")
    public CommonResult<Boolean> deleteAgent(@RequestParam("id") Long id) {
        agentService.deleteAgent(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得智能体")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ai:agent:query')")
    public CommonResult<AiAgentRespVO> getAgent(@RequestParam("id") Long id) {
        AiAgentDO agent = agentService.getAgent(id);
        return success(BeanUtils.toBean(agent, AiAgentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得智能体分页")
    @PreAuthorize("@ss.hasPermission('ai:agent:query')")
    public CommonResult<PageResult<AiAgentRespVO>> getAgentPage(@Valid AiAgentPageReqVO pageReqVO) {
        PageResult<AiAgentDO> pageResult = agentService.getAgentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiAgentRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得智能体简单列表")
    @PreAuthorize("@ss.hasPermission('ai:agent:query')")
    public CommonResult<java.util.List<AiAgentSimpleRespVO>> getAgentSimpleList() {
        // TODO: 根据需要实现简单列表查询
        return success(java.util.Collections.emptyList());
    }

    @PostMapping("/deploy")
    @Operation(summary = "部署智能体")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ai:agent:deploy')")
    public CommonResult<Boolean> deployAgent(@RequestParam("id") Long id) {
        agentService.deployAgent(id);
        return success(true);
    }

    @PostMapping("/stop")
    @Operation(summary = "停止智能体")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ai:agent:stop')")
    public CommonResult<Boolean> stopAgent(@RequestParam("id") Long id) {
        agentService.stopAgent(id);
        return success(true);
    }

    @PostMapping("/restart")
    @Operation(summary = "重启智能体")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ai:agent:restart')")
    public CommonResult<Boolean> restartAgent(@RequestParam("id") Long id) {
        agentService.restartAgent(id);
        return success(true);
    }

}
