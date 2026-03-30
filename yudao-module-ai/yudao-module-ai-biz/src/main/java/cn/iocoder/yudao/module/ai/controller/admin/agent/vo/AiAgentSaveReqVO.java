package cn.iocoder.yudao.module.ai.controller.admin.agent.vo;

import cn.iocoder.yudao.framework.common.validation.InEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentAuthTypeEnum;
import cn.iocoder.yudao.module.ai.enums.agent.AiAgentDeployTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Schema(description = "管理后台 - 智能体新增/修改 Request VO")
@Data
public class AiAgentSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "智能体名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "助手")
    @NotEmpty(message = "智能体名称不能为空")
    private String name;

    @Schema(description = "描述", example = "智能助手")
    private String description;

    @Schema(description = "图标 URL", example = "https://example.com/icon.png")
    private String icon;

    @Schema(description = "所属应用 ID", example = "1")
    private Long appId;

    @Schema(description = "绑定的模型 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "绑定的模型 ID 不能为空")
    private Long modelId;

    @Schema(description = "模型参数配置", example = "{\"temperature\": 0.7}")
    private Map<String, Object> modelParams;

    @Schema(description = "部署类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "部署类型不能为空")
    @InEnum(AiAgentDeployTypeEnum.class)
    private Integer deployType;

    @Schema(description = "CPU 配额", example = "1000m")
    private String resourceCpu;

    @Schema(description = "内存配额", example = "512Mi")
    private String resourceMemory;

    @Schema(description = "GPU 数量", example = "0")
    private Integer resourceGpu;

    @Schema(description = "镜像地址", example = "example.com/agent:latest")
    private String imageUrl;

    @Schema(description = "环境变量", example = "{\"KEY\": \"value\"}")
    private Map<String, Object> envVars;

    @Schema(description = "访问端点", example = "http://localhost:8080")
    private String endpoint;

    @Schema(description = "认证类型", example = "1")
    @InEnum(AiAgentAuthTypeEnum.class)
    private Integer authType;

    @Schema(description = "API Key", example = "sk-xxx")
    private String apiKey;

    @Schema(description = "限流 (请求/分钟)", example = "60")
    private Integer rateLimit;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "副本数", example = "1")
    private Integer replicas;

}
