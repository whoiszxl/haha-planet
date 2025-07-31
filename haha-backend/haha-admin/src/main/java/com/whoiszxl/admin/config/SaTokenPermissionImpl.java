package com.whoiszxl.admin.config;

import cn.dev33.satoken.stp.StpInterface;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.common.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token 权限认证实现
 * @author whoiszxl
 */
public class SaTokenPermissionImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginAdmin loginAdmin = LoginHelper.getLoginAdmin();
        return new ArrayList<>(loginAdmin.getPermissions());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginAdmin loginAdmin = LoginHelper.getLoginAdmin();
        return new ArrayList<>(loginAdmin.getRoleCodes());
    }
}
