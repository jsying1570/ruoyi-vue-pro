package cn.iocoder.yudao.module.ai.controller.admin.agent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 智能体简单信息 Response VO")
@Data
public class AiAgentSimpleRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "智能体名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "助手")
    private String name;

    @Schema(description = "图标 URL", example = "https://example.com/icon.png")
    private String icon;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

}
