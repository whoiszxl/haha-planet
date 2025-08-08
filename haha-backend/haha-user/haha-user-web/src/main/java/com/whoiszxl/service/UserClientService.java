package com.whoiszxl.service;

import com.whoiszxl.model.response.UserClientResponse;

/**
 * 用户客户端业务接口
 *
 * @author whoiszxl
 */
public interface UserClientService {

    /**
     * 根据客户端 ID 查詢
     *
     * @param clientId 客戶端 ID
     * @return 客户端信息
     */
    UserClientResponse getByClientId(String clientId);
}