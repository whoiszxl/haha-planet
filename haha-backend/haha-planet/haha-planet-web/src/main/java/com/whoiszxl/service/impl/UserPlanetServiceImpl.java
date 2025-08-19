package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.model.resp.UserPlanetGroupResp;
import com.whoiszxl.model.resp.UserPlanetResp;
import com.whoiszxl.planet.enums.PlanetMemberTypeEnum;
import com.whoiszxl.planet.mapper.PlanetMapper;
import com.whoiszxl.planet.mapper.PlanetMemberMapper;
import com.whoiszxl.planet.model.entity.PlanetDO;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import com.whoiszxl.service.UserPlanetService;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 用户星球服务实现
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPlanetServiceImpl implements UserPlanetService {
    
    private final PlanetMapper planetMapper;
    private final PlanetMemberMapper planetMemberMapper;
    private final RedissonUtil redissonUtil;
    
    @Override
    public UserPlanetGroupResp getUserAllPlanets(Long userId, Integer limit) {
        log.info("查询用户所有星球，userId: {}, limit: {}", userId, limit);
        
        // 构建缓存键
        String cacheKey = buildCacheKey(userId, limit);
        
        // 先尝试从Redis缓存获取
        UserPlanetGroupResp cachedResult = redissonUtil.get(cacheKey);
        if (cachedResult != null) {
            log.info("从Redis缓存获取用户星球数据，userId: {}, limit: {}", userId, limit);
            return cachedResult;
        }
        
        try {
            // 缓存未命中，异步并行查询三种角色的星球
            CompletableFuture<List<UserPlanetResp>> createdFuture = 
                getPlanetsByMemberTypeAsync(userId, PlanetMemberTypeEnum.OWNER, limit);
            CompletableFuture<List<UserPlanetResp>> managedFuture = 
                getPlanetsByMemberTypeAsync(userId, PlanetMemberTypeEnum.ADMIN, limit);
            CompletableFuture<List<UserPlanetResp>> joinedFuture = 
                getPlanetsByMemberTypeAsync(userId, PlanetMemberTypeEnum.NORMAL_MEMBER, limit);
            
            // 等待所有异步查询完成
            CompletableFuture.allOf(createdFuture, managedFuture, joinedFuture).get();
            
            List<UserPlanetResp> createdPlanets = createdFuture.get();
            List<UserPlanetResp> managedPlanets = managedFuture.get();
            List<UserPlanetResp> joinedPlanets = joinedFuture.get();
            
            UserPlanetGroupResp result = buildResponse(createdPlanets, managedPlanets, joinedPlanets);
            
            // 将结果缓存到Redis，TTL 30分钟
            redissonUtil.set(cacheKey, result, Duration.ofMinutes(30));
            log.info("用户星球数据已缓存到Redis，userId: {}, limit: {}, TTL: 30分钟", userId, limit);
            
            return result;
            
        } catch (Exception e) {
            log.error("异步查询用户星球失败，降级到同步查询，userId: {}", userId, e);
            return getUserAllPlanetsSync(userId, limit);
        }
    }
    
    /**
     * 同步查询降级方案
     */
    private UserPlanetGroupResp getUserAllPlanetsSync(Long userId, Integer limit) {
        log.warn("使用同步查询降级方案，userId: {}", userId);
        
        List<UserPlanetResp> createdPlanets = getPlanetsByMemberType(userId, PlanetMemberTypeEnum.OWNER, limit);
        List<UserPlanetResp> managedPlanets = getPlanetsByMemberType(userId, PlanetMemberTypeEnum.ADMIN, limit);
        List<UserPlanetResp> joinedPlanets = getPlanetsByMemberType(userId, PlanetMemberTypeEnum.NORMAL_MEMBER, limit);
        
        UserPlanetGroupResp result = buildResponse(createdPlanets, managedPlanets, joinedPlanets);
        
        // 降级查询也缓存结果，但TTL较短（10分钟）
        try {
            String cacheKey = buildCacheKey(userId, limit);
            redissonUtil.set(cacheKey, result, Duration.ofMinutes(10));
            log.info("降级查询结果已缓存，userId: {}, TTL: 10分钟", userId);
        } catch (Exception e) {
            log.warn("降级查询缓存失败，userId: {}", userId, e);
        }
        
        return result;
    }
    
    /**
     * 构建响应对象
     */
    private UserPlanetGroupResp buildResponse(List<UserPlanetResp> createdPlanets, 
                                            List<UserPlanetResp> managedPlanets, 
                                            List<UserPlanetResp> joinedPlanets) {
        UserPlanetGroupResp response = new UserPlanetGroupResp();
        response.setCreatedPlanets(createdPlanets);
        response.setManagedPlanets(managedPlanets);
        response.setJoinedPlanets(joinedPlanets);
        response.setStats(new UserPlanetGroupResp.PlanetStats(
            createdPlanets.size(),
            managedPlanets.size(),
            joinedPlanets.size()
        ));
        return response;
    }
    
    /**
     * 异步查询用户星球列表
     */
    @Async("taskExecutor")
    public CompletableFuture<List<UserPlanetResp>> getPlanetsByMemberTypeAsync(Long userId, PlanetMemberTypeEnum memberType, Integer limit) {
        return CompletableFuture.completedFuture(getPlanetsByMemberType(userId, memberType, limit));
    }
    
    /**
     * 根据成员类型获取用户星球列表
     */
    private List<UserPlanetResp> getPlanetsByMemberType(Long userId, PlanetMemberTypeEnum memberType, Integer limit) {
        // 查询成员记录
        LambdaQueryWrapper<PlanetMemberDO> memberQuery = Wrappers.<PlanetMemberDO>lambdaQuery()
                .eq(PlanetMemberDO::getUserId, userId)
                .eq(PlanetMemberDO::getMemberType, memberType.getCode())
                .eq(PlanetMemberDO::getStatus, 1)
                .orderByDesc(PlanetMemberDO::getJoinTime)
                .last("LIMIT " + limit);
        
        List<PlanetMemberDO> members = planetMemberMapper.selectList(memberQuery);
        if (members.isEmpty()) {
            return List.of();
        }
        
        // 批量查询星球信息
        List<Long> planetIds = members.stream()
                .map(PlanetMemberDO::getPlanetId)
                .collect(Collectors.toList());
        
        Map<Long, PlanetDO> planetMap = planetMapper.selectList(
                Wrappers.<PlanetDO>lambdaQuery()
                        .in(PlanetDO::getId, planetIds)
                        .eq(PlanetDO::getStatus, 1)
        ).stream().collect(Collectors.toMap(PlanetDO::getId, planet -> planet));
        
        // 构建响应对象
        return members.stream()
                .map(member -> buildUserPlanetResp(member, planetMap.get(member.getPlanetId()), memberType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * 构建用户星球响应对象
     */
    private UserPlanetResp buildUserPlanetResp(PlanetMemberDO member, PlanetDO planet, PlanetMemberTypeEnum memberType) {
        UserPlanetResp resp = HahaBeanUtil.toBean(planet, UserPlanetResp.class);
        if (resp == null) {
            return null;
        }
        resp.setMemberType(memberType.getCode());
        resp.setMemberTypeName(memberType.getName());
        resp.setJoinTime(member.getJoinTime());
        resp.setPlanetNickname(member.getNickname());
        
        return resp;
    }
    
    /**
     * 构建缓存键
     */
    private String buildCacheKey(Long userId, Integer limit) {
        return String.format("haha:planet:user:planets:%d:%d", userId, limit);
    }
    
    /**
     * 清除用户星球缓存
     */
    public void evictUserPlanetsCache(Long userId) {
        if (userId == null) {
            return;
        }
        
        try {
            // 删除所有相关的缓存键（不同limit值）
            String[] patterns = {
                buildCacheKey(userId, 10),
                buildCacheKey(userId, 20),
                buildCacheKey(userId, 50),
                buildCacheKey(userId, 100)
            };
            
            for (String key : patterns) {
                redissonUtil.delete(key);
            }
            
            log.info("清除用户星球缓存成功，userId: {}", userId);
        } catch (Exception e) {
            log.error("清除用户星球缓存失败，userId: {}", userId, e);
        }
    }
    
    /**
     * 用户加入星球时清除缓存
     */
    public void onUserJoinPlanet(Long userId, Long planetId) {
        log.info("用户加入星球，清除缓存，userId: {}, planetId: {}", userId, planetId);
        evictUserPlanetsCache(userId);
    }
    
    /**
     * 用户退出星球时清除缓存
     */
    public void onUserLeavePlanet(Long userId, Long planetId) {
        log.info("用户退出星球，清除缓存，userId: {}, planetId: {}", userId, planetId);
        evictUserPlanetsCache(userId);
    }
    
    /**
     * 预热用户星球缓存
     */
    public void warmUpUserPlanetsCache(Long userId) {
        if (userId == null) {
            return;
        }
        
        try {
            log.info("预热用户星球缓存，userId: {}", userId);
            
            // 预热常用的limit值
            getUserAllPlanets(userId, 10);
            getUserAllPlanets(userId, 20);
            
            log.info("预热用户星球缓存完成，userId: {}", userId);
        } catch (Exception e) {
            log.error("预热用户星球缓存失败，userId: {}", userId, e);
        }
    }
}
