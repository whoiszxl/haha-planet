package com.whoiszxl.starter.crud.service;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;
import com.whoiszxl.starter.crud.constants.FillConstants;

/**
 * 公共管理员相关接口
 */
public interface CommonAdminService {

    @ContainerMethod(namespace = FillConstants.ADMIN_NICKNAME, type = MappingType.ORDER_OF_KEYS)
    String getNicknameById(Long id);

}
