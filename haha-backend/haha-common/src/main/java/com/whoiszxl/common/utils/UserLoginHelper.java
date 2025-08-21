package com.whoiszxl.common.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.common.context.UserContext;
import com.whoiszxl.common.context.UserContextHolder;
import com.whoiszxl.common.constants.RedisPrefixConstants;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.crud.service.CommonMemberService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * C端用户登录助手
 *
 * @author whoiszxl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginHelper {

    private static final RedissonUtil REDISSON_UTIL = SpringUtil.getBean(RedissonUtil.class);

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户会自动踢下线前一个token
     *
     * @param userContext 用户信息
     * @param deviceType  设备类型
     */
    public static void login(UserContext userContext, String deviceType) {
        SaSession tokenSession = StpUtil.getTokenSession();
        if (null == tokenSession) {
            throw new RuntimeException("当前登录状态异常");
        }
        tokenSession.set(SaSession.USER, userContext);
        UserContextHolder.setContext(userContext);
        recordLoginInfo(userContext.getId());
    }

    /**
     * 获取用户(多级缓存)
     */
    public static UserContext getLoginUser() {
        UserContext userContext = UserContextHolder.getContext();
        if (userContext != null) {
            return userContext;
        }
        SaSession tokenSession = StpUtil.getTokenSession();
        if (tokenSession != null) {
            userContext = tokenSession.getModel(SaSession.USER, UserContext.class);
            UserContextHolder.setContext(userContext);
        }
        return userContext;
    }

    /**
     * 获取用户基于用户ID
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public static UserContext getLoginUser(Long userId) {
        return UserContextHolder.getContext(userId);
    }

    /**
     * 获取登录用户 ID
     *
     * @return 登录用户 ID
     */
    public static Long getUserId() {
        return ExceptionUtils.exToNull(() -> getLoginUser().getId());
    }

    /**
     * 获取登录用户名
     *
     * @return 登录用户名
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取登录用户昵称
     *
     * @return 登录用户昵称
     */
    public static String getNickname() {
        return getNickname(getUserId());
    }

    /**
     * 获取用户昵称
     *
     * @param memberId 用户ID
     * @return 用户昵称
     */
    public static String getNickname(Long memberId) {
        return ExceptionUtils.exToNull(() -> SpringUtil.getBean(CommonMemberService.class).getNicknameById(memberId));
    }

    /**
     * 获取登录用户手机号
     *
     * @return 登录用户手机号
     */
    public static String getPhone() {
        return getLoginUser().getPhone();
    }

    /**
     * 获取登录用户邮箱
     *
     * @return 登录用户邮箱
     */
    public static String getEmail() {
        return getLoginUser().getEmail();
    }

    /**
     * 获取登录用户头像
     *
     * @return 登录用户头像
     */
    public static String getAvatar() {
        return getLoginUser().getAvatar();
    }

    /**
     * 获取登录用户客户端类型
     *
     * @return 客户端类型
     */
    public static String getClientType() {
        return getLoginUser().getClientType();
    }

    /**
     * 获取登录用户客户端Key
     *
     * @return 客户端Key
     */
    public static String getClientKey() {
        return getLoginUser().getClientKey();
    }

    /**
     * 是否为超级管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isSuperAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 是否为超级管理员
     *
     * @return 结果
     */
    public static boolean isSuperAdmin() {
        return isSuperAdmin(getUserId());
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public static void recordLoginInfo(Long userId) {
        String loginKey = RedisPrefixConstants.User.USER_LOGIN_RECORD_CACHE + userId;
        REDISSON_UTIL.set(loginKey, System.currentTimeMillis());
        REDISSON_UTIL.expire(loginKey, Duration.ofSeconds(RedisPrefixConstants.User.USER_LOGIN_RECORD_CACHE_TIMEOUT));
    }

    /**
     * 检查用户数据权限
     *
     * @param userId 用户ID
     */
    public static void checkUserDataScope(Long userId) {
        if (!isSuperAdmin() && !userId.equals(getUserId())) {
            throw new RuntimeException("没有权限访问用户数据");
        }
    }

}