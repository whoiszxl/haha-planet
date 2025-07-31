package com.whoiszxl.starter.mybatis.datapermission;

/**
 * 数据权限过滤接口
 */
public interface DataPermissionFilter {

    boolean isFilter();

    DataPermissionCurrentUser getCurrentUser();
}
