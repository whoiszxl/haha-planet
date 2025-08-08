package com.whoiszxl.service;

import com.whoiszxl.user.model.entity.UserInfoDO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;

/**
 * 用户基础信息业务接口
 *
 * @author whoiszxl
 */
public interface UserInfoService {

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    Optional<UserInfoDO> getByUsername(String username);

    /**
     * 通过邮箱获取用户信息
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<UserInfoDO> getByEmail(String email);

    /**
     * 通过手机号获取用户信息
     * @param phone 手机号
     * @return 用户信息
     */
    Optional<UserInfoDO> getByPhone(String phone);

    /**
     * 通过ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoDO getById(Long userId);
}