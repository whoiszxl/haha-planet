package com.whoiszxl.messaging.mail.service.impl;

import com.whoiszxl.messaging.mail.properties.MailProperties;
import com.whoiszxl.messaging.mail.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Map;
import java.util.Set;

/**
 * Thymeleaf 邮件模板服务实现
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThymeleafMailTemplateService implements MailTemplateService {

    private final TemplateEngine templateEngine;
    private final MailProperties mailProperties;

    @Override
    public String renderTemplate(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            
            String templatePath = getTemplatePath(templateName);
            String result = templateEngine.process(templatePath, context);
            
            log.debug("[HaHa Mail] Thymeleaf 模板渲染成功: {}", templateName);
            return result;
            
        } catch (Exception e) {
            log.error("[HaHa Mail] Thymeleaf 模板渲染失败: {}", templateName, e);
            throw new RuntimeException("模板渲染失败: " + templateName, e);
        }
    }

    @Override
    public boolean templateExists(String templateName) {
        try {
            String templatePath = getTemplatePath(templateName);
            
            // 获取所有模板解析器
            Set<ITemplateResolver> templateResolvers = templateEngine.getTemplateResolvers();
            
            // 检查是否有任何解析器可以解析该模板
            for (ITemplateResolver resolver : templateResolvers) {
                try {
                    if (resolver.resolveTemplate(
                            templateEngine.getConfiguration(),
                            null,
                            templatePath,
                            null) != null) {
                        return true;
                    }
                } catch (Exception e) {
                    // 继续尝试下一个解析器
                    log.debug("[HaHa Mail] 模板解析器无法解析模板: {}", templateName);
                }
            }
            
            return false;
            
        } catch (Exception e) {
            log.debug("[HaHa Mail] Thymeleaf 模板不存在: {}", templateName);
            return false;
        }
    }

    @Override
    public String getTemplateContent(String templateName) {
        try {
            String templatePath = getTemplatePath(templateName);
            // 使用空的上下文获取模板原始内容
            Context context = new Context();
            return templateEngine.process(templatePath, context);
        } catch (Exception e) {
            log.error("[HaHa Mail] 获取 Thymeleaf 模板内容失败: {}", templateName, e);
            throw new RuntimeException("获取模板内容失败: " + templateName, e);
        }
    }

    /**
     * 获取模板路径
     */
    private String getTemplatePath(String templateName) {
        // 移除前缀和后缀，因为 Thymeleaf 会自动处理
        String prefix = mailProperties.getTemplate().getPrefix();
        String suffix = mailProperties.getTemplate().getSuffix();
        
        // 如果模板名已经包含路径信息，直接使用
        if (templateName.startsWith("classpath:") || templateName.startsWith("/")) {
            return templateName.replace(prefix, "").replace(suffix, "");
        }
        
        // 否则只返回模板名（不包含前缀和后缀）
        return templateName;
    }
}