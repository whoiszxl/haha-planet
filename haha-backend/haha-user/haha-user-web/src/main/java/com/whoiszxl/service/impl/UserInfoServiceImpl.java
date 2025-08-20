package com.whoiszxl.service.impl;

import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.user.mapper.UserInfoMapper;
import com.whoiszxl.user.model.entity.UserInfoDO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户基础信息业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    @Override
    public Optional<UserInfoDO> getByUsername(String username) {
        return userInfoMapper.lambdaQuery()
                .eq(UserInfoDO::getUsername, username)
                .oneOpt();
    }

    @Override
    public Optional<UserInfoDO> getByEmail(String email) {
        return userInfoMapper.lambdaQuery()
                .eq(UserInfoDO::getEmail, email)
                .oneOpt();
    }

    @Override
    public Optional<UserInfoDO> getByPhone(String phone) {
        return userInfoMapper.lambdaQuery()
                .eq(UserInfoDO::getPhone, phone)
                .oneOpt();
    }

    @Override
    public UserInfoDO getById(Long userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public List<UserInfoDO> listByIds(List<Long> userIds) {
        return userInfoMapper.selectBatchIds(userIds);
    }
}