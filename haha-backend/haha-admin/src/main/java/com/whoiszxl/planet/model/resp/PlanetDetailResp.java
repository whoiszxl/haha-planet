package com.whoiszxl.planet.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 星球详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "星球详情信息")
public class PlanetDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球编码，唯一标识
     */
    @Schema(description = "星球编码，唯一标识")
    @ExcelProperty(value = "星球编码，唯一标识")
    private String planetCode;

    /**
     * 星球名称
     */
    @Schema(description = "星球名称")
    @ExcelProperty(value = "星球名称")
    private String name;

    /**
     * 星球简介
     */
    @Schema(description = "星球简介")
    @ExcelProperty(value = "星球简介")
    private String description;

    /**
     * 星球头像
     */
    @Schema(description = "星球头像")
    @ExcelProperty(value = "星球头像")
    private String avatar;

    /**
     * 星球封面图
     */
    @Schema(description = "星球封面图")
    @ExcelProperty(value = "星球封面图")
    private String coverImage;

    /**
     * 星球主ID
     */
    @Schema(description = "星球主ID")
    @ExcelProperty(value = "星球主ID")
    private Long ownerId;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    @ExcelProperty(value = "分类ID")
    private Long categoryId;

    /**
     * 标签，逗号分隔
     */
    @Schema(description = "标签，逗号分隔")
    @ExcelProperty(value = "标签，逗号分隔")
    private String tags;

    /**
     * 价格类型: 1-免费 2-付费
     */
    @Schema(description = "价格类型: 1-免费 2-付费")
    @ExcelProperty(value = "价格类型: 1-免费 2-付费")
    private Integer priceType;

    /**
     * 加入价格
     */
    @Schema(description = "加入价格")
    @ExcelProperty(value = "加入价格")
    private BigDecimal price;

    /**
     * 原价
     */
    @Schema(description = "原价")
    @ExcelProperty(value = "原价")
    private BigDecimal originalPrice;

    /**
     * 优惠价
     */
    @Schema(description = "优惠价")
    @ExcelProperty(value = "优惠价")
    private BigDecimal discountPrice;

    /**
     * 优惠开始时间
     */
    @Schema(description = "优惠开始时间")
    @ExcelProperty(value = "优惠开始时间")
    private LocalDateTime discountStartTime;

    /**
     * 优惠结束时间
     */
    @Schema(description = "优惠结束时间")
    @ExcelProperty(value = "优惠结束时间")
    private LocalDateTime discountEndTime;

    /**
     * 加入方式: 1-直接加入 2-申请审核 3-邀请制
     */
    @Schema(description = "加入方式: 1-直接加入 2-申请审核 3-邀请制")
    @ExcelProperty(value = "加入方式: 1-直接加入 2-申请审核 3-邀请制")
    private Integer joinType;

    /**
     * 是否公开: 0-私密 1-公开
     */
    @Schema(description = "是否公开: 0-私密 1-公开")
    @ExcelProperty(value = "是否公开: 0-私密 1-公开")
    private Integer isPublic;

    /**
     * 最大成员数，0表示无限制
     */
    @Schema(description = "最大成员数，0表示无限制")
    @ExcelProperty(value = "最大成员数，0表示无限制")
    private Integer maxMembers;

    /**
     * 加入问题
     */
    @Schema(description = "加入问题")
    @ExcelProperty(value = "加入问题")
    private String joinQuestion;

    /**
     * 是否自动通过申请: 0-否 1-是
     */
    @Schema(description = "是否自动通过申请: 0-否 1-是")
    @ExcelProperty(value = "是否自动通过申请: 0-否 1-是")
    private Integer autoApprove;

    /**
     * 是否允许成员发帖: 0-否 1-是
     */
    @Schema(description = "是否允许成员发帖: 0-否 1-是")
    @ExcelProperty(value = "是否允许成员发帖: 0-否 1-是")
    private Integer allowMemberPost;

    /**
     * 发帖是否需要审核: 0-否 1-是
     */
    @Schema(description = "发帖是否需要审核: 0-否 1-是")
    @ExcelProperty(value = "发帖是否需要审核: 0-否 1-是")
    private Integer postNeedApprove;

    /**
     * 是否允许匿名发帖: 0-否 1-是
     */
    @Schema(description = "是否允许匿名发帖: 0-否 1-是")
    @ExcelProperty(value = "是否允许匿名发帖: 0-否 1-是")
    private Integer allowAnonymous;

    /**
     * 是否开启水印: 0-否 1-是
     */
    @Schema(description = "是否开启水印: 0-否 1-是")
    @ExcelProperty(value = "是否开启水印: 0-否 1-是")
    private Integer watermarkEnabled;

    /**
     * 成员数量
     */
    @Schema(description = "成员数量")
    @ExcelProperty(value = "成员数量")
    private Integer memberCount;

    /**
     * 帖子数量
     */
    @Schema(description = "帖子数量")
    @ExcelProperty(value = "帖子数量")
    private Integer postCount;

    /**
     * 浏览次数
     */
    @Schema(description = "浏览次数")
    @ExcelProperty(value = "浏览次数")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    @ExcelProperty(value = "点赞数")
    private Integer likeCount;

    /**
     * 分享次数
     */
    @Schema(description = "分享次数")
    @ExcelProperty(value = "分享次数")
    private Integer shareCount;

    /**
     * 总收入
     */
    @Schema(description = "总收入")
    @ExcelProperty(value = "总收入")
    private BigDecimal totalIncome;

    /**
     * 热度分数
     */
    @Schema(description = "热度分数")
    @ExcelProperty(value = "热度分数")
    private Integer hotScore;

    /**
     * 质量评分
     */
    @Schema(description = "质量评分")
    @ExcelProperty(value = "质量评分")
    private BigDecimal qualityScore;

    /**
     * 最后活跃时间
     */
    @Schema(description = "最后活跃时间")
    @ExcelProperty(value = "最后活跃时间")
    private LocalDateTime lastActiveTime;

    /**
     * 推荐权重
     */
    @Schema(description = "推荐权重")
    @ExcelProperty(value = "推荐权重")
    private Integer recommendWeight;

    /**
     * 是否精选: 0-否 1-是
     */
    @Schema(description = "是否精选: 0-否 1-是")
    @ExcelProperty(value = "是否精选: 0-否 1-是")
    private Integer isFeatured;

    /**
     * 是否官方: 0-否 1-是
     */
    @Schema(description = "是否官方: 0-否 1-是")
    @ExcelProperty(value = "是否官方: 0-否 1-是")
    private Integer isOfficial;

    /**
     * 有效开始时间
     */
    @Schema(description = "有效开始时间")
    @ExcelProperty(value = "有效开始时间")
    private LocalDateTime validStartTime;

    /**
     * 有效结束时间
     */
    @Schema(description = "有效结束时间")
    @ExcelProperty(value = "有效结束时间")
    private LocalDateTime validEndTime;

    /**
     * 关闭原因
     */
    @Schema(description = "关闭原因")
    @ExcelProperty(value = "关闭原因")
    private String closeReason;

    /**
     * 扩展配置，JSON格式
     */
    @Schema(description = "扩展配置，JSON格式")
    @ExcelProperty(value = "扩展配置，JSON格式")
    private String extraConfig;

    /**
     * 星球公告
     */
    @Schema(description = "星球公告")
    @ExcelProperty(value = "星球公告")
    private String notice;

    /**
     * 星球规则
     */
    @Schema(description = "星球规则")
    @ExcelProperty(value = "星球规则")
    private String rules;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @ExcelProperty(value = "乐观锁")
    private Long version;

    /**
     * 业务状态: 0-禁用 1-启用 2-审核中 3-已关闭
     */
    @Schema(description = "业务状态: 0-禁用 1-启用 2-审核中 3-已关闭")
    @ExcelProperty(value = "业务状态: 0-禁用 1-启用 2-审核中 3-已关闭")
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @ExcelProperty(value = "逻辑删除 1: 已删除， 0: 未删除")
    private Integer isDeleted;
}