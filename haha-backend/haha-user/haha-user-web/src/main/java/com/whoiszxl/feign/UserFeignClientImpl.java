package com.whoiszxl.feign;

import com.whoiszxl.dto.UserFeignDTO;
import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import com.whoiszxl.starter.web.model.R;
import com.whoiszxl.user.model.entity.UserInfoDO;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public R<Map<Long, UserFeignDTO>> batchGetUsersByIds(List<Long> userIds) {
        // 参数校验
        if (CollectionUtils.isEmpty(userIds)) {
            return R.ok(new HashMap<>());
        }

        try {
            // 批量查询用户信息
            List<UserInfoDO> userInfoList = userInfoService.listByIds(userIds);
            
            if (CollectionUtils.isEmpty(userInfoList)) {
                return R.ok(new HashMap<>());
            }

            // 转换为 Map<Long, UserFeignDTO> 格式
            Map<Long, UserFeignDTO> userInfoMap = userInfoList.stream()
                    .collect(Collectors.toMap(
                            UserInfoDO::getId,
                            userInfo -> HahaBeanUtil.toBean(userInfo, UserFeignDTO.class),
                            (existing, replacement) -> existing // 处理重复key的情况
                    ));

            return R.ok(userInfoMap);
        } catch (Exception e) {
            // 记录异常日志（这里假设有日志框架）
            return R.fail("批量获取用户信息失败");
        }
    }
}
