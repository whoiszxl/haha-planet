package com.whoiszxl.admin.cqrs.response.log;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.whoiszxl.admin.enums.LogStatusEnum;
import com.whoiszxl.starter.crud.converter.ExcelBaseEnumConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志导出信息
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "操作日志导出信息")
public class OperationLogExportResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 操作时间
     */
    @Schema(description = "操作时间", example = "2023-08-08 08:08:08", type = "string")
    @ExcelProperty(value = "操作时间")
    private LocalDateTime createTime;

    /**
     * 操作人
     */
    @Schema(description = "操作人", example = "张三")
    @ExcelProperty(value = "操作人")
    private String createUserString;

    /**
     * 操作内容
     */
    @Schema(description = "操作内容", example = "账号登录")
    @ExcelProperty(value = "操作内容")
    private String description;

    /**
     * 所属模块
     */
    @Schema(description = "所属模块", example = "部门管理")
    @ExcelProperty(value = "所属模块")
    private String module;

    /**
     * 状态
     */
    @Schema(description = "状态（1：成功；2：失败）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    @ExcelProperty(value = "状态", converter = ExcelBaseEnumConverter.class)
    private LogStatusEnum status;

    /**
     * 操作 IP
     */
    @Schema(description = "操作 IP", example = "")
    @ExcelProperty(value = "操作 IP")
    private String ip;

    /**
     * 操作地点
     */
    @Schema(description = "操作地点", example = "中国北京北京市")
    @ExcelProperty(value = "操作地点")
    private String address;

    /**
     * 耗时（ms）
     */
    @Schema(description = "耗时（ms）", example = "58")
    @ExcelProperty(value = "耗时（ms）")
    private Long timeTaken;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器", example = "Chrome 115.0.0.0")
    @ExcelProperty(value = "浏览器")
    private String browser;

    /**
     * 终端系统
     */
    @Schema(description = "终端系统", example = "Windows 10")
    @ExcelProperty(value = "终端系统")
    private String os;
}
