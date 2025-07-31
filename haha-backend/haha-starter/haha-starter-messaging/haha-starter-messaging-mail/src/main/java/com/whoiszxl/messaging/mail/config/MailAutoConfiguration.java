package com.whoiszxl.messaging.mail.config;

import com.whoiszxl.messaging.mail.properties.MailProperties;
import com.whoiszxl.messaging.mail.service.MailService;
import com.whoiszxl.messaging.mail.service.MailTemplateService;
import com.whoiszxl.messaging.mail.service.impl.MailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * 邮件自动配置类
 *
 * @author whoiszxl
 */
@Slf4j
@AutoConfiguration(after = MailSenderAutoConfiguration.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnProperty(prefix = "haha.messaging.mail", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableAsync
@EnableRetry
@Import({MailTaskExecutorConfiguration.class, MailTemplateConfiguration.class})
public class MailAutoConfiguration {

    private final MailProperties mailProperties;

    /**
     * 邮件服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(JavaMailSender.class)
    public MailService mailService(JavaMailSender mailSender, 
                                   MailTemplateService mailTemplateService,
                                   Executor mailTaskExecutor) {
        log.info("[HaHa Mail] 初始化邮件服务");
        return new MailServiceImpl(mailSender, mailTemplateService, mailProperties, mailTaskExecutor);
    }
}