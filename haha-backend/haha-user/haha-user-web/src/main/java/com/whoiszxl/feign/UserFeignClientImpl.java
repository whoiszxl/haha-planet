package com.whoiszxl.feign;

import com.whoiszxl.dto.UserFeignDTO;
import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import com.whoiszxl.starter.web.model.R;
import com.whoiszxl.user.model.entity.UserInfoDO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 feign 客户端接口实现
 * @author whoiszxl
 */
@RestController
@RequiredArgsConstructor
public class UserFeignClientImpl implements UserFeignClient {

    private final UserInfoService userInfoService;

    @Override
    public R<UserFeignDTO> getUserById(Long userId) {
        UserInfoDO userInfoDO = userInfoService.getById(userId);
        if(userInfoDO == null){
            return R.fail();
        }
        return R.ok(HahaBeanUtil.toBean(userInfoDO, UserFeignDTO.class));
    }
}
