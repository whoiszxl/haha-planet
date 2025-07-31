package com.whoiszxl.messaging.mail.service;

import java.util.Map;

/**
 * 邮件模板服务接口
 *
 * @author whoiszxl
 */
public interface MailTemplateService {

    /**
     * 渲染模板
     *
     * @param templateName 模板名称
     * @param variables    模板变量
     * @return 渲染后的内容
     */
    String renderTemplate(String templateName, Map<String, Object> variables);

    /**
     * 检查模板是否存在
     *
     * @param templateName 模板名称
     * @return 是否存在
     */
    boolean templateExists(String templateName);

    /**
     * 获取模板内容
     *
     * @param templateName 模板名称
     * @return 模板内容
     */
    String getTemplateContent(String templateName);
}