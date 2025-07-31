package com.whoiszxl.admin.service.impl;

import cn.crane4j.annotation.AutoOperate;
import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.whoiszxl.admin.cqrs.query.OnlineUserQuery;
import com.whoiszxl.admin.cqrs.response.OnlineUserResp;
import com.whoiszxl.admin.service.OnlineAdminService;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.starter.core.constants.StringConstants;
import com.whoiszxl.starter.crud.constants.FillConstants;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 在线用户业务实现
 * @author whoiszxl
 */
@Service
public class OnlineAdminServiceImpl implements OnlineAdminService {

    @Override
    @AutoOperate(type = OnlineUserResp.class, on = "list")
    public PageResponse<OnlineUserResp> page(OnlineUserQuery query, PageQuery pageQuery) {
        List<LoginAdmin> loginAdminList = this.list(query);
        List<OnlineUserResp> list = BeanUtil.copyToList(loginAdminList, OnlineUserResp.class);
        return PageResponse.build(pageQuery.getPage(), pageQuery.getSize(), list);
    }

    @Override
    public List<LoginAdmin> list(OnlineUserQuery query) {
        List<LoginAdmin> LoginAdminList = new ArrayList<>();
        // 查询所有登录用户
        List<String> tokenKeyList = StpUtil.searchTokenValue(StringConstants.EMPTY, 0, -1, false);
        for (String tokenKey : tokenKeyList) {
            String token = StrUtil.subAfter(tokenKey, StringConstants.COLON, true);
            // 忽略已过期或失效 Token
            if (StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < SaTokenDao.NEVER_EXPIRE) {
                continue;
            }
            // 检查是否符合查询条件
            LoginAdmin LoginAdmin = LoginHelper.getLoginAdmin(token);
            if (this.isMatchQuery(query, LoginAdmin)) {
                LoginAdminList.add(LoginAdmin);
            }
        }
        // 设置排序
        CollUtil.sort(LoginAdminList, Comparator.comparing(LoginAdmin::getLoginTime).reversed());
        return LoginAdminList;
    }

    @Override
    @ContainerMethod(namespace = FillConstants.ONLINE_USER_LAST_ACTIVE_TIME, type = MappingType.ORDER_OF_KEYS)
    public LocalDateTime getLastActiveTime(String token) {
        long lastActiveTime = StpUtil.getStpLogic().getTokenLastActiveTime(token);
        return lastActiveTime == SaTokenDao.NOT_VALUE_EXPIRE ? null : DateUtil.date(lastActiveTime).toLocalDateTime();
    }

    @Override
    public void cleanByRoleId(Long roleId) {
        List<LoginAdmin> LoginAdminList = this.list(new OnlineUserQuery());
        LoginAdminList.parallelStream().forEach(u -> {
            if (u.getRoles().stream().anyMatch(r -> r.getId().equals(roleId))) {
                try {
                    StpUtil.logoutByTokenValue(u.getToken());
                } catch (NotLoginException ignored) {
                }
            }
        });
    }

    @Override
    public void cleanByUserId(Long userId) {
        if (!StpUtil.isLogin(userId)) {
            return;
        }
        StpUtil.logout(userId);
    }

    /**
     * 是否符合查询条件
     *
     * @param query     查询条件
     * @param LoginAdmin 登录用户信息
     * @return 是否符合查询条件
     */
    private boolean isMatchQuery(OnlineUserQuery query, LoginAdmin LoginAdmin) {
        boolean flag1 = true;
        String nickname = query.getNickname();
        if (StrUtil.isNotBlank(nickname)) {
            flag1 = StrUtil.contains(LoginAdmin.getUsername(), nickname) || StrUtil.contains(LoginHelper
                    .getNickname(LoginAdmin.getId()), nickname);
        }

        boolean flag2 = true;
        List<Date> loginTime = query.getLoginTime();
        if (CollUtil.isNotEmpty(loginTime)) {
            flag2 = DateUtil.isIn(DateUtil.date(LoginAdmin.getLoginTime()).toJdkDate(), loginTime.get(0), loginTime
                .get(1));
        }
        return flag1 && flag2;
    }
}
