package com.whoiszxl.feign;

import com.whoiszxl.common.feign.FeignTokenConfig;
import com.whoiszxl.dto.UserFeignDTO;
import com.whoiszxl.starter.web.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户 feign 客户端接口
 * @author whoiszxl
 */
@FeignClient(name = "haha-user-web", contextId = "userFeign", configuration = FeignTokenConfig.class)
public interface UserFeignClient {

    /**
     * 通过 id 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/getUserById")
    R<UserFeignDTO> getUserById(@RequestParam Long userId);
}
