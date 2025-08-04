package com.whoiszxl.messaging.mail.service.impl;

import com.whoiszxl.messaging.mail.properties.MailProperties;
import com.whoiszxl.messaging.mail.service.MailTemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.util.Map;

/**
 * FreeMarker 邮件模板服务实现
 *
 * @author whoiszxl
 */
@Slf4j
@RequiredArgsConstructor
public class FreemarkerMailTemplateService implements MailTemplateService {

    private final Configuration freemarkerConfiguration;
    private final MailProperties mailProperties;

    @Override
    public String renderTemplate(String templateName, Map<String, Object> variables) {
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName + mailProperties.getTemplate().getSuffix());
            StringWriter writer = new StringWriter();
            template.process(variables, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("[HaHa Mail] FreeMarker 模板渲染失败: {}", templateName, e);
            throw new RuntimeException("模板渲染失败: " + templateName, e);
        }
    }

    @Override
    public boolean templateExists(String templateName) {
        try {
            freemarkerConfiguration.getTemplate(templateName + mailProperties.getTemplate().getSuffix());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getTemplateContent(String templateName) {
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName + mailProperties.getTemplate().getSuffix());
            StringWriter writer = new StringWriter();
            template.process(null, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("[HaHa Mail] 获取 FreeMarker 模板内容失败: {}", templateName, e);
            throw new RuntimeException("获取模板内容失败: " + templateName, e);
        }
    }
}