package com.whoiszxl.service;

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
     * 获取用户创建的星球列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    PageResponse<UserPlanetResp> getUserCreatedPlanets(Long userId, Integer page, Integer pageSize);
    
    /**
     * 获取用户加入的星球列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param memberType 成员类型：1-普通成员，2-管理员，3-星球主，null-全部
     * @return 分页结果
     */
    PageResponse<UserPlanetResp> getUserJoinedPlanets(Long userId, Integer page, Integer pageSize, Integer memberType);
    
    /**
     * 获取用户星球统计信息
     * @param userId 用户ID
     * @return 统计信息
     */
    UserPlanetStatsResp getUserPlanetStats(Long userId);
    
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
