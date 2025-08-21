package com.whoiszxl.feign;

import com.whoiszxl.common.feign.FeignTokenConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

/**
 * 星球 feign 客户端接口
 * @author whoiszxl
 */
@FeignClient(name = "haha-planet-web", contextId = "planetFeign", configuration = FeignTokenConfig.class)
public interface PlanetFeignClient {

    /**
     * 获取我的星球对应的ID列表
     * @return 星球ID
     */
    @GetMapping("/getMyPlanetIds")
    Set<Long> getMyPlanetIds();

}
