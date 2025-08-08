package com.whoiszxl.service.impl;

import com.whoiszxl.model.response.UserClientResponse;
import com.whoiszxl.service.UserClientService;
import com.whoiszxl.starter.core.utils.BeanUtil;
import com.whoiszxl.user.mapper.UserClientMapper;
import com.whoiszxl.user.model.entity.UserClientDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户客户端业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserClientMapper userClientMapper;

    @Override
    public UserClientResponse getByClientId(String clientKey) {
        return userClientMapper.lambdaQuery()
                .eq(UserClientDO::getClientKey, clientKey)
                .oneOpt()
                .map(memberClient -> BeanUtil.copyProperties(memberClient, UserClientResponse.class))
                .orElse(null);
    }
}