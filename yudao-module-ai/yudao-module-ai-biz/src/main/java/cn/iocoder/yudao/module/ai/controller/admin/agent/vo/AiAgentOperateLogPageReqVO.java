package cn.iocoder.yudao.module.ai.controller.admin.agent.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentActionEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 智能体操作日志分页 Request VO")
@Data
public class AiAgentOperateLogPageReqVO extends PageParam {

    @Schema(description = "智能体 ID", example = "1")
    private Long agentId;

    @Schema(description = "操作类型", example = "deploy")
    private String action;

    @Schema(description = "结果状态", example = "1")
    @InEnum(AiAgentStatusEnum.class)
    private Integer resultStatus;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
