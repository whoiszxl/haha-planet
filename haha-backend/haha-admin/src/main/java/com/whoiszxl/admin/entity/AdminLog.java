package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.whoiszxl.admin.enums.LogStatusEnum;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_admin_log")
public class AdminLog extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "链路ID")
    private String traceId;

    @Schema(name = "日志描述")
    private String description;

    @Schema(name = "模块")
    private String module;

    @Schema(name = "请求URL")
    private String requestUrl;

    @Schema(name = "请求方式")
    private String requestHttpMethod;

    @Schema(name = "请求头")
    private String requestHeaders;

    @Schema(name = "请求体")
    private String requestBody;

    @Schema(name = "状态码")
    private Integer statusCode;

    @Schema(name = "响应头")
    private String responseHeaders;

    @Schema(name = "响应体")
    private String responseBody;

    @Schema(name = "消耗时间(ms)")
    private Long consumingTime;

    @Schema(name = "IP地址")
    private String ip;

    @Schema(name = "IP所属地区")
    private String ipAddress;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "操作系统")
    private String os;

    @Schema(name = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "日志状态")
    private LogStatusEnum status;

    @Schema(name = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;

}
