package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 星球响应类
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球响应")
public class PlanetResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "星球ID")
    private Long id;

    @Schema(description = "星球编码，唯一标识")
    private String planetCode;

    @Schema(description = "星球名称")
    private String name;

    @Schema(description = "星球简介")
    private String description;

    @Schema(description = "星球头像")
    private String avatar;

    @Schema(description = "星球封面图")
    private String coverImage;

    @Schema(description = "星球主ID")
    private Long ownerId;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "标签，逗号分隔")
    private String tags;

    @Schema(description = "价格类型: 1-免费 2-付费")
    private Integer priceType;

    @Schema(description = "加入价格")
    private BigDecimal price;

    @Schema(description = "原价")
    private BigDecimal originalPrice;

    @Schema(description = "优惠价")
    private BigDecimal discountPrice;

    @Schema(description = "加入方式: 1-直接加入 2-申请审核 3-邀请制")
    private Integer joinType;

    @Schema(description = "是否公开: 0-私密 1-公开")
    private Integer isPublic;

    @Schema(description = "最大成员数，0表示无限制")
    private Integer maxMembers;

    @Schema(description = "成员数量")
    private Integer memberCount;

    @Schema(description = "帖子数量")
    private Integer postCount;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "热度分数")
    private Integer hotScore;

    @Schema(description = "质量评分")
    private BigDecimal qualityScore;

    @Schema(description = "最后活跃时间")
    private LocalDateTime lastActiveTime;

    @Schema(description = "推荐权重")
    private Integer recommendWeight;

    @Schema(description = "是否精选: 0-否 1-是")
    private Integer isFeatured;

    @Schema(description = "是否官方: 0-否 1-是")
    private Integer isOfficial;

    @Schema(description = "业务状态: 0-禁用 1-启用 2-审核中 3-已关闭")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "星球管理员和星球主信息")
    private List<PlanetMemberUserResp> adminUsers;
}
