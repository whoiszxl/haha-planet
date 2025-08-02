package com.whoiszxl.admin.cqrs.response.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 仪表盘-24小时活跃分布
 * @author whoiszxl
 */
@Data
@Schema(description = "仪表盘-24小时活跃分布")
public class DashboardHourlyActivityResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 小时（0-23）
     */
    @Schema(description = "小时（0-23）", example = "14")
    private Integer hour;

    /**
     * 访问次数
     */
    @Schema(description = "访问次数", example = "156")
    private Long count;
}
