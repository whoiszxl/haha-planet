package com.whoiszxl.common.mybatis;

import cn.hutool.core.convert.Convert;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.starter.mybatis.datapermission.DataPermissionCurrentUser;
import com.whoiszxl.starter.mybatis.datapermission.DataPermissionFilter;
import com.whoiszxl.starter.mybatis.datapermission.DataScope;


import java.util.stream.Collectors;

/**
 * 数据权限过滤器实现类
 */
public class DataPermissionFilterImpl implements DataPermissionFilter {

    @Override
    public boolean isFilter() {
        LoginAdmin loginAdmin = LoginHelper.getLoginAdmin();
        return !loginAdmin.isAdmin();
    }

    @Override
    public DataPermissionCurrentUser getCurrentUser() {
        LoginAdmin loginAdmin = LoginHelper.getLoginAdmin();
        DataPermissionCurrentUser currentUser = new DataPermissionCurrentUser();
        currentUser.setUserId(Convert.toStr(loginAdmin.getId()));
        currentUser.setDeptId(Convert.toStr(loginAdmin.getDeptId()));
        currentUser.setRoles(loginAdmin.getRoles()
            .stream()
            .map(r -> new DataPermissionCurrentUser
                    .CurrentUserRole(Convert.toStr(r.getId()), DataScope.valueOf(r.getDataScope().name())))
            .collect(Collectors.toSet()));
        return currentUser;
    }
}
