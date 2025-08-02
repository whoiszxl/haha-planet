package com.whoiszxl.admin.cqrs.response.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 仪表盘-客户端统计信息
 * @author whoiszxl
 */
@Data
@Schema(description = "仪表盘-客户端统计信息")
public class DashboardClientStatsResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 浏览器分布
     */
    @Schema(description = "浏览器分布")
    private List<ClientStatItem> browsers;

    /**
     * 操作系统分布
     */
    @Schema(description = "操作系统分布")
    private List<ClientStatItem> operatingSystems;

    /**
     * 客户端统计项
     */
    @Data
    @Schema(description = "客户端统计项")
    public static class ClientStatItem implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 名称
         */
        @Schema(description = "名称", example = "Chrome")
        private String name;

        /**
         * 数量
         */
        @Schema(description = "数量", example = "1234")
        private Long value;
    }
}
