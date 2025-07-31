package com.whoiszxl.messaging.mail.config;

import com.whoiszxl.messaging.mail.properties.MailProperties;
import com.whoiszxl.messaging.mail.service.MailTemplateService;
import com.whoiszxl.messaging.mail.service.impl.FreemarkerMailTemplateService;
import com.whoiszxl.messaging.mail.service.impl.ThymeleafMailTemplateService;
import com.whoiszxl.messaging.mail.service.impl.VelocityMailTemplateService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.TemplateEngine;

/**
 * 邮件模板配置
 *
 * @author whoiszxl
 */
@Slf4j
@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "haha.messaging.mail.template", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MailTemplateConfiguration {

    private final MailProperties mailProperties;

    /**
     * Thymeleaf 邮件模板服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(TemplateEngine.class)
    @ConditionalOnProperty(prefix = "haha.messaging.mail.template", name = "engine", havingValue = "thymeleaf", matchIfMissing = true)
    public MailTemplateService thymeleafMailTemplateService(TemplateEngine templateEngine) {
        log.info("[HaHa Mail] 初始化 Thymeleaf 邮件模板服务");
        return new ThymeleafMailTemplateService(templateEngine, mailProperties);
    }

    /**
     * FreeMarker 邮件模板服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Configuration.class)
    @ConditionalOnProperty(prefix = "haha.messaging.mail.template", name = "engine", havingValue = "freemarker")
    public MailTemplateService freemarkerMailTemplateService(Configuration freemarkerConfiguration) {
        log.info("[HaHa Mail] 初始化 FreeMarker 邮件模板服务");
        return new FreemarkerMailTemplateService(freemarkerConfiguration, mailProperties);
    }

    /**
     * Velocity 邮件模板服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(VelocityEngine.class)
    @ConditionalOnProperty(prefix = "haha.messaging.mail.template", name = "engine", havingValue = "velocity")
    public MailTemplateService velocityMailTemplateService(VelocityEngine velocityEngine) {
        log.info("[HaHa Mail] 初始化 Velocity 邮件模板服务");
        return new VelocityMailTemplateService(velocityEngine, mailProperties);
    }
}