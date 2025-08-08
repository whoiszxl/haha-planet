package com.whoiszxl.service;

import com.whoiszxl.user.model.entity.UserTokenDO;
import me.zhyd.oauth.model.AuthUser;

/**
 * 用户令牌业务接口
 *
 * @author whoiszxl
 */
public interface UserTokenService {

    /**
     * 添加用户token
     * @param memberToken 用户token信息
     */
    void add(UserTokenDO memberToken);

    /**
     * 通过 source 和 uuid 获取用户 token 信息
     * @param source 来源
     * @param uuid id
     * @return 用户 token 信息
     */
    UserTokenDO getBySourceAndToken(String source, String uuid);

    /**
     * 更新用户 token
     * @param userTokenDO 用户 token 信息
     */
    void updateById(UserTokenDO userTokenDO);

    /**
     * 绑定第三方账号
     * @param authUser 第三方账号信息
     * @param userId 用户ID
     */
    void bind(AuthUser authUser, Long userId);
}