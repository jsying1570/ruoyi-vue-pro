package cn.iocoder.yudao.module.ai.controller.admin.agent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "管理后台 - 智能体操作日志 Response VO")
@Data
public class AiAgentOperateLogRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "智能体 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long agentId;

    @Schema(description = "操作者类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer operatorType;

    @Schema(description = "操作者 ID", example = "100")
    private Long operatorId;

    @Schema(description = "操作类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "deploy")
    private String action;

    @Schema(description = "目标状态", example = "2")
    private Integer targetStatus;

    @Schema(description = "请求参数", example = "{\"key\": \"value\"}")
    private Map<String, Object> requestParams;

    @Schema(description = "额外数据", example = "{\"key\": \"value\"}")
    private Map<String, Object> extraData;

    @Schema(description = "结果状态", example = "1")
    private Integer resultStatus;

    @Schema(description = "错误信息", example = "错误信息")
    private String errorMessage;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;

}
