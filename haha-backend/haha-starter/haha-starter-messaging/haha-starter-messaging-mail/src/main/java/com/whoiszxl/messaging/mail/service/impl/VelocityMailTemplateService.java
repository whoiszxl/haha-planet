package com.whoiszxl.messaging.mail.service.impl;

import com.whoiszxl.messaging.mail.properties.MailProperties;
import com.whoiszxl.messaging.mail.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

/**
 * Velocity 邮件模板服务实现
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VelocityMailTemplateService implements MailTemplateService {

    private final VelocityEngine velocityEngine;
    private final MailProperties mailProperties;

    @Override
    public String renderTemplate(String templateName, Map<String, Object> variables) {
        try {
            VelocityContext context = new VelocityContext(variables);
            StringWriter writer = new StringWriter();
            String templatePath = mailProperties.getTemplate().getPrefix() + templateName + mailProperties.getTemplate().getSuffix();
            velocityEngine.mergeTemplate(templatePath, "UTF-8", context, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("[HaHa Mail] Velocity 模板渲染失败: {}", templateName, e);
            throw new RuntimeException("模板渲染失败: " + templateName, e);
        }
    }

    @Override
    public boolean templateExists(String templateName) {
        try {
            String templatePath = mailProperties.getTemplate().getPrefix() + templateName + mailProperties.getTemplate().getSuffix();
            return velocityEngine.resourceExists(templatePath);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getTemplateContent(String templateName) {
        try {
            VelocityContext context = new VelocityContext();
            StringWriter writer = new StringWriter();
            String templatePath = mailProperties.getTemplate().getPrefix() + templateName + mailProperties.getTemplate().getSuffix();
            velocityEngine.mergeTemplate(templatePath, "UTF-8", context, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("[HaHa Mail] 获取 Velocity 模板内容失败: {}", templateName, e);
            throw new RuntimeException("获取模板内容失败: " + templateName, e);
        }
    }
}