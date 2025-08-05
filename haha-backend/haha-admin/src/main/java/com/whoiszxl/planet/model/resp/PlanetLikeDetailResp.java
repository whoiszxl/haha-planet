package com.whoiszxl.planet.model.resp;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * 点赞记录详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "点赞记录详情信息")
public class PlanetLikeDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @ExcelProperty(value = "星球ID")
    private Long planetId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 点赞目标类型: 1-帖子 2-评论
     */
    @Schema(description = "点赞目标类型: 1-帖子 2-评论")
    @ExcelProperty(value = "点赞目标类型: 1-帖子 2-评论")
    private Integer targetType;

    /**
     * 目标ID
     */
    @Schema(description = "目标ID")
    @ExcelProperty(value = "目标ID")
    private Long targetId;

    /**
     * 被点赞的用户ID
     */
    @Schema(description = "被点赞的用户ID")
    @ExcelProperty(value = "被点赞的用户ID")
    private Long targetUserId;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 业务状态
     */
    @Schema(description = "业务状态")
    @ExcelProperty(value = "业务状态")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @ExcelProperty(value = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;
}