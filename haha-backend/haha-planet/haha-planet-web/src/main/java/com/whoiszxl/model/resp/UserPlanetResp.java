package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户星球响应DTO
 * 用于用户星球列表查询的轻量级响应对象
 */
@Data
@Schema(description = "用户星球信息")
public class UserPlanetResp {
    
    @Schema(description = "星球ID")
    private Long id;
    
    @Schema(description = "星球编码")
    private String planetCode;
    
    @Schema(description = "星球名称")
    private String name;
    
    @Schema(description = "星球头像")
    private String avatar;
    
    @Schema(description = "星球封面")
    private String coverImage;
    
    @Schema(description = "星球描述")
    private String description;
    
    @Schema(description = "分类名称")
    private String categoryName;
    
    @Schema(description = "价格")
    private BigDecimal price;
    
    @Schema(description = "成员数量")
    private Integer memberCount;
    
    @Schema(description = "帖子数量")
    private Integer postCount;
    
    @Schema(description = "热度评分")
    private Integer hotScore;
    
    @Schema(description = "质量评分")
    private BigDecimal qualityScore;
    
    @Schema(description = "是否精选")
    private Integer isFeatured;
    
    @Schema(description = "是否官方认证")
    private Integer isOfficial;
    
    @Schema(description = "星球状态")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "最后活跃时间")
    private LocalDateTime lastActiveTime;
    
    // 用户在该星球的角色信息（仅在查询加入的星球时有值）
    @Schema(description = "用户成员类型：1-普通成员，2-管理员，3-星球主")
    private Integer memberType;
    
    @Schema(description = "成员类型名称")
    private String memberTypeName;
    
    @Schema(description = "加入时间")
    private LocalDateTime joinTime;
    
    @Schema(description = "在星球中的昵称")
    private String planetNickname;
    
    /**
     * 获取成员类型名称
     */
    public String getMemberTypeName() {
        if (memberType == null) {
            return null;
        }
        switch (memberType) {
            case 1:
                return "普通成员";
            case 2:
                return "管理员";
            case 3:
                return "星球主";
            default:
                return "未知";
        }
    }
}
