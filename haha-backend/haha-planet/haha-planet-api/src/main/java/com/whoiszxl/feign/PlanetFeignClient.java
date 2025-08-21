package com.whoiszxl.feign;

import com.whoiszxl.common.feign.FeignTokenConfig;
import com.whoiszxl.starter.web.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * 星球 feign 客户端接口
 * @author whoiszxl
 */
@FeignClient(name = "haha-planet-web", contextId = "planetFeign", configuration = FeignTokenConfig.class)
public interface PlanetFeignClient {

    /**
     * 获取我的星球对应的ID列表
     * @param userId 用户ID
     * @return 星球ID
     */
    @GetMapping("/getMyPlanetIds")
    R<Set<Long>> getMyPlanetIds(@RequestParam Long userId);

}
