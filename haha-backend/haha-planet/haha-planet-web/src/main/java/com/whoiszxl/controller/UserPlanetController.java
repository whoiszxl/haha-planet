package com.whoiszxl.controller;

import com.whoiszxl.common.context.UserContextHolder;
import com.whoiszxl.model.resp.UserPlanetResp;
import com.whoiszxl.service.UserPlanetService;
import com.whoiszxl.service.UserPlanetService.UserPlanetStatsResp;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户星球控制器
 * 提供用户星球查询相关接口，针对500万+数据规模优化
 * @author whoiszxl
 */
@Slf4j
@RestController
@RequestMapping("/api/user/planet")
@RequiredArgsConstructor
@Tag(name = "用户星球管理", description = "用户星球查询相关接口")
public class UserPlanetController {
    
    private final UserPlanetService userPlanetService;
    
    @GetMapping("/created")
    @Operation(summary = "获取用户创建的星球列表", description = "分页查询用户创建的星球")
    public R<PageResponse<UserPlanetResp>> getUserCreatedPlanets(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量，建议20-50") @RequestParam(defaultValue = "20") Integer pageSize) {

        Long userId = UserContextHolder.getMemberId();

        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 20;
        }
        
        try {
            PageResponse<UserPlanetResp> result = userPlanetService.getUserCreatedPlanets(userId, page, pageSize);
            return R.ok(result);
        } catch (Exception e) {
            log.error("查询用户创建的星球失败，userId: {}", userId, e);
            return R.fail("查询失败，请稍后重试");
        }
    }
    
    @GetMapping("/joined")
    @Operation(summary = "获取用户加入的星球列表", description = "分页查询用户加入的星球，支持按成员类型过滤")
    public R<PageResponse<UserPlanetResp>> getUserJoinedPlanets(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量，建议20-50") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "成员类型：1-普通成员，2-管理员，3-星球主，不传则查询全部") @RequestParam(required = false) Integer memberType) {

        Long userId = UserContextHolder.getMemberId();

        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 20;
        }
        if (memberType != null && (memberType < 1 || memberType > 3)) {
            return R.fail("成员类型参数无效");
        }
        
        try {
            PageResponse<UserPlanetResp> result = userPlanetService.getUserJoinedPlanets(userId, page, pageSize, memberType);
            return R.ok(result);
        } catch (Exception e) {
            log.error("查询用户加入的星球失败，userId: {}, memberType: {}", userId, memberType, e);
            return R.fail("查询失败，请稍后重试");
        }
    }
    
    @GetMapping("/stats")
    @Operation(summary = "获取用户星球统计信息", description = "获取用户创建、加入、管理的星球数量统计")
    public R<UserPlanetStatsResp> getUserPlanetStats() {

        Long userId = UserContextHolder.getMemberId();
        
        try {
            UserPlanetStatsResp stats = userPlanetService.getUserPlanetStats(userId);
            return R.ok(stats);
        } catch (Exception e) {
            log.error("查询用户星球统计信息失败，userId: {}", userId, e);
            return R.fail("查询失败，请稍后重试");
        }
    }
    
    @GetMapping("/managed")
    @Operation(summary = "获取用户管理的星球列表", description = "查询用户作为管理员或星球主的星球")
    public R<PageResponse<UserPlanetResp>> getUserManagedPlanets(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量，建议20-50") @RequestParam(defaultValue = "20") Integer pageSize) {

        Long userId = UserContextHolder.getMemberId();

        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 20;
        }
        
        try {
            // 查询管理员和星球主角色的星球（memberType: 2, 3）
            PageResponse<UserPlanetResp> result = userPlanetService.getUserJoinedPlanets(userId, page, pageSize, null);
            
            // 过滤出管理角色的星球
            result.getList().removeIf(planet -> planet.getMemberType() == null || planet.getMemberType() == 1);
            
            return R.ok(result);
        } catch (Exception e) {
            log.error("查询用户管理的星球失败，userId: {}", userId, e);
            return R.fail("查询失败，请稍后重试");
        }
    }
}
