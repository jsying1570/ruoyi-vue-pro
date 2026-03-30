package cn.iocoder.yudao.module.ai.controller.admin.agent;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentOperateLogPageReqVO;
import cn.iocoder.yudao.module.ai.controller.admin.agent.vo.AiAgentOperateLogRespVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.agent.AiAgentOperateLogDO;
import cn.iocoder.yudao.module.ai.service.agent.AiAgentLogService;
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

@Tag(name = "管理后台 - 智能体操作日志")
@RestController
@RequestMapping("/ai/agent/log")
@Validated
@Slf4j
public class AiAgentOperateLogController {

    @Resource
    private AiAgentLogService logService;

    @GetMapping("/get")
    @Operation(summary = "获得操作日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ai:agent:log:query')")
    public CommonResult<AiAgentOperateLogRespVO> getLog(@RequestParam("id") Long id) {
        AiAgentOperateLogDO logDO = logService.getLog(id);
        return success(BeanUtils.toBean(logDO, AiAgentOperateLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得操作日志分页")
    @PreAuthorize("@ss.hasPermission('ai:agent:log:query')")
    public CommonResult<PageResult<AiAgentOperateLogRespVO>> getLogPage(@Valid AiAgentOperateLogPageReqVO pageReqVO) {
        PageResult<AiAgentOperateLogDO> pageResult = logService.getLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiAgentOperateLogRespVO.class));
    }

}
