package com.whoiszxl.service;

import com.whoiszxl.model.resp.UserPlanetGroupResp;
import com.whoiszxl.model.resp.UserPlanetResp;
import com.whoiszxl.starter.crud.model.PageResponse;
import lombok.Data;

/**
 * 用户星球服务接口
 * 针对500万+星球规模优化的高效查询服务
 * @author whoiszxl
 */
public interface UserPlanetService {
    
    /**
     * 获取用户所有星球，按类型分组
     * @param userId 用户ID
     * @param limit 每个分组的最大数量
     * @return 分组结果
     */
    UserPlanetGroupResp getUserAllPlanets(Long userId, Integer limit);
    
    
    /**
     * 用户星球统计信息
     */
    @Data
    class UserPlanetStatsResp {
        private Long createdCount;    // 创建的星球数量
        private Long joinedCount;     // 加入的星球数量
        private Long managedCount;    // 管理的星球数量（管理员+星球主）

    }
}
