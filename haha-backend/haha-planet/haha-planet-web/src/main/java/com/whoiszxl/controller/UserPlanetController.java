package com.whoiszxl.controller;

import com.whoiszxl.common.context.UserContextHolder;
import com.whoiszxl.model.resp.UserPlanetGroupResp;
import com.whoiszxl.service.UserPlanetService;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户星球控制器
 * 提供统一的用户星球查询接口，按类型分组返回
 * @author whoiszxl
 */
@Slf4j
@RestController
@RequestMapping("/api/user/planet")
@RequiredArgsConstructor
@Tag(name = "用户星球管理", description = "用户星球查询相关接口")
public class UserPlanetController {
    
    private final UserPlanetService userPlanetService;

    @GetMapping("/all")
    @Operation(summary = "获取用户所有星球", description = "按类型分组返回用户创建、管理、加入的所有星球")
    public R<UserPlanetGroupResp> getUserAllPlanets(
            @Parameter(description = "每个分组的最大数量，默认50") @RequestParam(defaultValue = "50") Integer limit) {

        Long userId = UserContextHolder.getMemberId();

        if (limit < 1 || limit > 200) {
            limit = 50;
        }
        
        try {
            long startTime = System.currentTimeMillis();
            UserPlanetGroupResp result = userPlanetService.getUserAllPlanets(userId, limit);
            long duration = System.currentTimeMillis() - startTime;
            
            log.info("查询用户星球完成，userId: {}, limit: {}, 耗时: {}ms", userId, limit, duration);
            return R.ok(result);
        } catch (Exception e) {
            log.error("查询用户所有星球失败，userId: {}", userId, e);
            return R.fail("查询失败，请稍后重试");
        }
    }

}
