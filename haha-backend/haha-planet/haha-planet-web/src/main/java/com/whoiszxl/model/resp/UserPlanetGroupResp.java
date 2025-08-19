package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户星球分组响应DTO
 * 将用户的所有星球按类型分组返回
 * 
 * @author whoiszxl
 */
@Data
@Schema(description = "用户星球分组信息")
public class UserPlanetGroupResp {
    
    @Schema(description = "我创建的星球列表")
    private List<UserPlanetResp> createdPlanets;
    
    @Schema(description = "我管理的星球列表（管理员角色）")
    private List<UserPlanetResp> managedPlanets;
    
    @Schema(description = "我加入的星球列表（普通成员）")
    private List<UserPlanetResp> joinedPlanets;
    
    @Schema(description = "统计信息")
    private PlanetStats stats;
    
    /**
     * 星球统计信息
     */
    @Data
    @Schema(description = "星球统计信息")
    public static class PlanetStats {
        
        @Schema(description = "创建的星球数量")
        private Integer createdCount;
        
        @Schema(description = "管理的星球数量")
        private Integer managedCount;
        
        @Schema(description = "加入的星球数量")
        private Integer joinedCount;
        
        @Schema(description = "总星球数量")
        private Integer totalCount;
        
        /**
         * 默认构造函数 - Jackson反序列化需要
         */
        public PlanetStats() {
            this.createdCount = 0;
            this.managedCount = 0;
            this.joinedCount = 0;
            this.totalCount = 0;
        }
        
        public PlanetStats(Integer createdCount, Integer managedCount, Integer joinedCount) {
            this.createdCount = createdCount != null ? createdCount : 0;
            this.managedCount = managedCount != null ? managedCount : 0;
            this.joinedCount = joinedCount != null ? joinedCount : 0;
            this.totalCount = this.createdCount + this.managedCount + this.joinedCount;
        }
    }
}
