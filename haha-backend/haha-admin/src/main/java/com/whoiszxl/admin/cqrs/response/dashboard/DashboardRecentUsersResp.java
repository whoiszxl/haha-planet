package com.whoiszxl.admin.cqrs.response.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 仪表盘-最近活跃用户
 * @author whoiszxl
 */
@Data
@Schema(description = "仪表盘-最近活跃用户")
public class DashboardRecentUsersResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    /**
     * 最后访问时间
     */
    @Schema(description = "最后访问时间", example = "2024-01-01T10:30:00")
    private LocalDateTime lastAccessTime;

    /**
     * 访问次数
     */
    @Schema(description = "访问次数", example = "25")
    private Long accessCount;

    /**
     * IP地址信息
     */
    @Schema(description = "IP地址信息", example = "中国|北京|北京市|移动")
    private String ipAddress;
}
