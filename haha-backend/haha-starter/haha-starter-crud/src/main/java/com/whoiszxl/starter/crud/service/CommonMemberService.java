package com.whoiszxl.starter.crud.service;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;
import com.whoiszxl.starter.crud.constants.FillConstants;

/**
 * 公共用户相关接口
 */
public interface CommonMemberService {

    @ContainerMethod(namespace = FillConstants.MEMBER_NICKNAME, type = MappingType.ORDER_OF_KEYS)
    String getNicknameById(Long id);

}