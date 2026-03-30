package cn.iocoder.yudao.module.ai.controller.admin.agent.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentDeployTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 智能体分页 Request VO")
@Data
public class AiAgentPageReqVO extends PageParam {

    @Schema(description = "智能体名称", example = "助手")
    private String name;

    @Schema(description = "部署类型", example = "1")
    @InEnum(AiAgentDeployTypeEnum.class)
    private Integer deployType;

    @Schema(description = "状态", example = "2")
    @InEnum(AiAgentStatusEnum.class)
    private Integer status;

    @Schema(description = "模型 ID", example = "1")
    private Long modelId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
