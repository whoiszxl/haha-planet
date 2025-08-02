package com.whoiszxl.admin.cqrs.response.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 仪表盘-系统性能指标
 * @author whoiszxl
 */
@Data
@Schema(description = "仪表盘-系统性能指标")
public class DashboardPerformanceResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 平均响应时间（毫秒）
     */
    @Schema(description = "平均响应时间（毫秒）", example = "125.5")
    private BigDecimal avgResponseTime;

    /**
     * 慢请求数量（>1秒）
     */
    @Schema(description = "慢请求数量", example = "12")
    private Long slowRequestCount;

    /**
     * 错误率（百分比）
     */
    @Schema(description = "错误率（百分比）", example = "2.5")
    private BigDecimal errorRate;

    /**
     * 总请求数
     */
    @Schema(description = "总请求数", example = "10000")
    private Long totalRequests;
}
