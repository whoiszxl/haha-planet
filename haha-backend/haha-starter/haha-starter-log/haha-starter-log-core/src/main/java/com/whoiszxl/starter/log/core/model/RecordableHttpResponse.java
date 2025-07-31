package com.whoiszxl.starter.log.core.model;

import java.util.Map;

/**
 * 可记录的 HTTP 响应信息
 */
public interface RecordableHttpResponse {

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    int getStatus();

    /**
     * 获取响应头
     *
     * @return 响应头
     */
    Map<String, String> getHeaders();

    /**
     * 获取响应体
     *
     * @return 响应体
     */
    String getBody();

    /**
     * 获取响应参数
     *
     * @return 响应参数
     */
    Map<String, Object> getParam();
}
