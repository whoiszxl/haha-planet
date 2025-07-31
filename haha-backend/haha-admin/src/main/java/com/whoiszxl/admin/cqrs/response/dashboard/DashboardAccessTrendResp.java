package com.whoiszxl.admin.cqrs.response.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 仪表盘-访问趋势信息
 * @author whoiszxl
 */
@Data
@Schema(description = "仪表盘-访问趋势信息")
public class DashboardAccessTrendResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    @Schema(description = "日期", example = "2023-08-08")
    private String date;

    /**
     * 浏览量（PV）
     */
    @Schema(description = "浏览量（PV）", example = "1000")
    private Long pvCount;

    /**
     * IP 数
     */
    @Schema(description = "IP 数", example = "500")
    private Long ipCount;
}
