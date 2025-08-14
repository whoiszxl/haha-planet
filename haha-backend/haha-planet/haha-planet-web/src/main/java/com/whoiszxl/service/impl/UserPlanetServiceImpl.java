package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.model.resp.UserPlanetResp;
import com.whoiszxl.planet.mapper.PlanetMapper;
import com.whoiszxl.planet.mapper.PlanetMemberMapper;
import com.whoiszxl.planet.model.entity.PlanetDO;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import com.whoiszxl.service.UserPlanetService;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import com.whoiszxl.starter.crud.model.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户星球服务实现
 * 针对500万+星球规模的高效查询优化
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPlanetServiceImpl implements UserPlanetService {
    
    private final PlanetMapper planetMapper;
    private final PlanetMemberMapper planetMemberMapper;
    
    @Override
    @Cacheable(value = "user:created:planets", key = "#userId + ':' + #page + ':' + #pageSize", 
               unless = "#result == null || #result.list.isEmpty()")
    public PageResponse<UserPlanetResp> getUserCreatedPlanets(Long userId, Integer page, Integer pageSize) {
        log.info("查询用户创建的星球列表，userId: {}, page: {}, pageSize: {}", userId, page, pageSize);
        
        // 构建分页查询条件
        Page<PlanetDO> pageParam = new Page<>(page, pageSize);
        
        // 使用优化索引：idx_planet_owner_status_time
        LambdaQueryWrapper<PlanetDO> queryWrapper = Wrappers.<PlanetDO>lambdaQuery()
                .eq(PlanetDO::getOwnerId, userId)
                .eq(PlanetDO::getStatus, 1)
                .orderByDesc(PlanetDO::getCreatedAt);
        
        // 执行分页查询
        Page<PlanetDO> planetPage = planetMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为响应DTO
        List<UserPlanetResp> planetList = planetPage.getRecords().stream()
                .map(planet -> {
                    UserPlanetResp resp = HahaBeanUtil.toBean(planet, UserPlanetResp.class);
                    // 创建者默认是星球主
                    resp.setMemberType(3);
                    resp.setMemberTypeName("星球主");
                    resp.setJoinTime(planet.getCreatedAt());
                    return resp;
                })
                .collect(Collectors.toList());
        
        PageResponse<UserPlanetResp> response = new PageResponse<>();
        response.setList(planetList);
        response.setTotal(planetPage.getTotal());
        return response;
    }
    
    @Override
    @Cacheable(value = "user:joined:planets", key = "#userId + ':' + #page + ':' + #pageSize + ':' + #memberType", 
               unless = "#result == null || #result.list.isEmpty()")
    public PageResponse<UserPlanetResp> getUserJoinedPlanets(Long userId, Integer page, Integer pageSize, Integer memberType) {
        log.info("查询用户加入的星球列表，userId: {}, page: {}, pageSize: {}, memberType: {}", 
                userId, page, pageSize, memberType);
        
        // 构建分页查询条件
        Page<PlanetMemberDO> pageParam = new Page<>(page, pageSize);
        
        // 使用优化索引：idx_member_user_status_type_time
        LambdaQueryWrapper<PlanetMemberDO> memberQuery = Wrappers.<PlanetMemberDO>lambdaQuery()
                .eq(PlanetMemberDO::getUserId, userId)
                .eq(PlanetMemberDO::getStatus, 1)
                .orderByDesc(PlanetMemberDO::getJoinTime);
        
        // 如果指定了成员类型，添加过滤条件
        if (memberType != null) {
            memberQuery.eq(PlanetMemberDO::getMemberType, memberType);
        }
        
        // 执行分页查询
        Page<PlanetMemberDO> memberPage = planetMemberMapper.selectPage(pageParam, memberQuery);
        
        if (memberPage.getRecords().isEmpty()) {
            PageResponse<UserPlanetResp> response = new PageResponse<>();
            response.setList(List.of());
            response.setTotal(0);
            return response;
        }
        
        // 提取星球ID列表
        List<Long> planetIds = memberPage.getRecords().stream()
                .map(PlanetMemberDO::getPlanetId)
                .collect(Collectors.toList());
        
        // 批量查询星球信息
        LambdaQueryWrapper<PlanetDO> planetQuery = Wrappers.<PlanetDO>lambdaQuery()
                .in(PlanetDO::getId, planetIds)
                .eq(PlanetDO::getStatus, 1);
        
        List<PlanetDO> planets = planetMapper.selectList(planetQuery);
        
        // 构建星球ID到星球对象的映射
        var planetMap = planets.stream()
                .collect(Collectors.toMap(PlanetDO::getId, planet -> planet));
        
        // 合并成员信息和星球信息
        List<UserPlanetResp> planetList = memberPage.getRecords().stream()
                .map(member -> {
                    PlanetDO planet = planetMap.get(member.getPlanetId());
                    if (planet == null) {
                        return null; // 星球不存在或已删除
                    }
                    
                    UserPlanetResp resp = HahaBeanUtil.toBean(planet, UserPlanetResp.class);
                    // 设置用户在星球中的角色信息
                    resp.setMemberType(member.getMemberType());
                    resp.setJoinTime(member.getJoinTime());
                    resp.setPlanetNickname(member.getNickname());
                    return resp;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        PageResponse<UserPlanetResp> response = new PageResponse<>();
        response.setList(planetList);
        response.setTotal(memberPage.getTotal());
        return response;
    }
    
    @Override
    @Cacheable(value = "user:planet:stats", key = "#userId")
    public UserPlanetStatsResp getUserPlanetStats(Long userId) {
        log.info("查询用户星球统计信息，userId: {}", userId);
        
        UserPlanetStatsResp stats = new UserPlanetStatsResp();
        
        // 查询创建的星球数量
        LambdaQueryWrapper<PlanetDO> createdQuery = Wrappers.<PlanetDO>lambdaQuery()
                .eq(PlanetDO::getOwnerId, userId)
                .eq(PlanetDO::getStatus, 1);
        Long createdCount = planetMapper.selectCount(createdQuery);
        stats.setCreatedCount(createdCount);
        
        // 查询加入的星球数量（包括创建的）
        LambdaQueryWrapper<PlanetMemberDO> joinedQuery = Wrappers.<PlanetMemberDO>lambdaQuery()
                .eq(PlanetMemberDO::getUserId, userId)
                .eq(PlanetMemberDO::getStatus, 1);
        Long joinedCount = planetMemberMapper.selectCount(joinedQuery);
        stats.setJoinedCount(joinedCount);
        
        // 查询管理的星球数量（管理员+星球主）
        LambdaQueryWrapper<PlanetMemberDO> managedQuery = Wrappers.<PlanetMemberDO>lambdaQuery()
                .eq(PlanetMemberDO::getUserId, userId)
                .in(PlanetMemberDO::getMemberType, 2, 3)
                .eq(PlanetMemberDO::getStatus, 1);
        Long managedCount = planetMemberMapper.selectCount(managedQuery);
        stats.setManagedCount(managedCount);
        
        return stats;
    }
}
