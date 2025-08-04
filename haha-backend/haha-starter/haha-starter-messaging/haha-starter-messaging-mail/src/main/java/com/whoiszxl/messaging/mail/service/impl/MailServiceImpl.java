package com.whoiszxl.messaging.mail.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.whoiszxl.messaging.mail.model.MailAttachment;
import com.whoiszxl.messaging.mail.model.MailInlineResource;
import com.whoiszxl.messaging.mail.model.MailMessage;
import com.whoiszxl.messaging.mail.model.MailSendResult;
import com.whoiszxl.messaging.mail.properties.MailProperties;
import com.whoiszxl.messaging.mail.service.MailService;
import com.whoiszxl.messaging.mail.service.MailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

/**
 * 邮件服务实现类
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final MailTemplateService mailTemplateService;
    private final MailProperties mailProperties;
    private final Executor mailTaskExecutor;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @Override
    public MailSendResult sendText(String to, String subject, String text) {
        return sendText(Collections.singletonList(to), subject, text);
    }

    @Override
    public MailSendResult sendText(List<String> to, String subject, String text) {
        MailMessage message = MailMessage.builder()
                .to(to)
                .subject(subject)
                .text(text)
                .build();
        return send(message);
    }

    @Override
    public MailSendResult sendHtml(String to, String subject, String html) {
        return sendHtml(Collections.singletonList(to), subject, html);
    }

    @Override
    public MailSendResult sendHtml(List<String> to, String subject, String html) {
        MailMessage message = MailMessage.builder()
                .to(to)
                .subject(subject)
                .html(html)
                .build();
        return send(message);
    }

    @Override
    public MailSendResult sendTemplate(String to, String subject, String template, Map<String, Object> variables) {
        return sendTemplate(Collections.singletonList(to), subject, template, variables);
    }

    @Override
    public MailSendResult sendTemplate(List<String> to, String subject, String template, Map<String, Object> variables) {
        MailMessage message = MailMessage.builder()
                .to(to)
                .subject(subject)
                .template(template)
                .templateVariables(variables)
                .build();
        return send(message);
    }

    @Override
    @Retryable(
            value = {MailException.class, MessagingException.class},
            maxAttemptsExpression = "#{@mailProperties.retry.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "#{@mailProperties.retry.delay.toMillis()}",
                    multiplierExpression = "#{@mailProperties.retry.multiplier}",
                    maxDelayExpression = "#{@mailProperties.retry.maxDelay.toMillis()}"
            )
    )
    public MailSendResult send(MailMessage message) {
        long startTime = System.currentTimeMillis();
        String messageId = generateMessageId(message);
        
        try {
            // 验证邮件消息
            validateMailMessage(message);
            
            // 填充默认发件人信息
            fillDefaultSender(message);
            
            // 发送邮件
            if (hasHtmlContent(message)) {
                sendMimeMessage(message);
            } else {
                sendSimpleMessage(message);
            }
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("[HaHa Mail] 邮件发送成功 - MessageId: {}, 耗时: {}ms", messageId, duration);
            
            return MailSendResult.success(messageId, duration);
            
        } catch (Exception e) {
            log.error("[HaHa Mail] 邮件发送失败 - MessageId: {}", messageId, e);
            return MailSendResult.failure(messageId, e.getMessage(), e);
        }
    }

    @Override
    @Async("mailTaskExecutor")
    public CompletableFuture<MailSendResult> sendAsync(MailMessage message) {
        return CompletableFuture.completedFuture(send(message));
    }

    @Override
    public List<MailSendResult> sendBatch(List<MailMessage> messages) {
        List<MailSendResult> results = new ArrayList<>();
        for (MailMessage message : messages) {
            results.add(send(message));
        }
        return results;
    }

    @Override
    @Async("mailTaskExecutor")
    public CompletableFuture<List<MailSendResult>> sendBatchAsync(List<MailMessage> messages) {
        return CompletableFuture.completedFuture(sendBatch(messages));
    }

    @Override
    public boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean testConnection() {
        try {
            mailSender.createMimeMessage();
            log.info("[HaHa Mail] 邮件服务连接测试成功");
            return true;
        } catch (Exception e) {
            log.error("[HaHa Mail] 邮件服务连接测试失败", e);
            return false;
        }
    }

    /**
     * 发送简单邮件
     */
    private void sendSimpleMessage(MailMessage message) {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom(message.getFrom());
        simpleMessage.setTo(message.getTo().toArray(new String[0]));
        
        if (CollUtil.isNotEmpty(message.getCc())) {
            simpleMessage.setCc(message.getCc().toArray(new String[0]));
        }
        if (CollUtil.isNotEmpty(message.getBcc())) {
            simpleMessage.setBcc(message.getBcc().toArray(new String[0]));
        }
        if (StringUtils.hasText(message.getReplyTo())) {
            simpleMessage.setReplyTo(message.getReplyTo());
        }
        
        simpleMessage.setSubject(message.getSubject());
        simpleMessage.setText(message.getText());
        simpleMessage.setSentDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        
        mailSender.send(simpleMessage);
    }

    /**
     * 发送MIME邮件
     */
    private void sendMimeMessage(MailMessage message) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
        // 设置发件人
        if (StringUtils.hasText(message.getFromName())) {
            helper.setFrom(message.getFrom(), message.getFromName());
        } else {
            helper.setFrom(message.getFrom());
        }
        
        // 设置收件人
        helper.setTo(message.getTo().toArray(new String[0]));
        
        // 设置抄送
        if (CollUtil.isNotEmpty(message.getCc())) {
            helper.setCc(message.getCc().toArray(new String[0]));
        }
        
        // 设置密送
        if (CollUtil.isNotEmpty(message.getBcc())) {
            helper.setBcc(message.getBcc().toArray(new String[0]));
        }
        
        // 设置回复地址
        if (StringUtils.hasText(message.getReplyTo())) {
            helper.setReplyTo(message.getReplyTo());
        }
        
        // 设置主题
        helper.setSubject(message.getSubject());
        
        // 设置内容
        String content = getMailContent(message);
        helper.setText(content, true);
        
        // 添加附件
        addAttachments(helper, message.getAttachments());
        
        // 添加内联资源
        addInlineResources(helper, message.getInlineResources());
        
        // 设置发送时间
        helper.setSentDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        
        mailSender.send(mimeMessage);
    }

    /**
     * 获取邮件内容
     */
    private String getMailContent(MailMessage message) {
        if (StringUtils.hasText(message.getTemplate())) {
            // 使用模板渲染
            return mailTemplateService.renderTemplate(message.getTemplate(), message.getTemplateVariables());
        } else if (StringUtils.hasText(message.getHtml())) {
            // 使用HTML内容
            return message.getHtml();
        } else {
            // 使用纯文本内容
            return message.getText();
        }
    }

    /**
     * 添加附件
     */
    private void addAttachments(MimeMessageHelper helper, List<MailAttachment> attachments) throws MessagingException {
        if (CollUtil.isEmpty(attachments)) {
            return;
        }
        
        for (MailAttachment attachment : attachments) {
            if (attachment.getData() != null) {
                helper.addAttachment(attachment.getFilename(), 
                        () -> new ByteArrayInputStream(attachment.getData()));
            } else if (attachment.getInputStream() != null) {
                helper.addAttachment(attachment.getFilename(), 
                        () -> attachment.getInputStream());
            } else if (StringUtils.hasText(attachment.getFilePath())) {
                helper.addAttachment(attachment.getFilename(), 
                        new org.springframework.core.io.FileSystemResource(attachment.getFilePath()));
            }
        }
    }

    /**
     * 添加内联资源
     */
    private void addInlineResources(MimeMessageHelper helper, List<MailInlineResource> inlineResources) throws MessagingException {
        if (CollUtil.isEmpty(inlineResources)) {
            return;
        }
        
        for (MailInlineResource resource : inlineResources) {
            if (resource.getData() != null) {
                helper.addInline(resource.getContentId(), 
                        new ByteArrayDataSource(resource.getData(), "application/octet-stream"));
            } else if (StringUtils.hasText(resource.getFilePath())) {
                helper.addInline(resource.getContentId(), 
                        new org.springframework.core.io.FileSystemResource(resource.getFilePath()));
            }
        }
    }

    /**
     * 验证邮件消息
     */
    private void validateMailMessage(MailMessage message) {
        if (CollUtil.isEmpty(message.getTo())) {
            throw new IllegalArgumentException("收件人列表不能为空");
        }
        
        if (!StringUtils.hasText(message.getSubject())) {
            throw new IllegalArgumentException("邮件主题不能为空");
        }
        
        if (!hasContent(message)) {
            throw new IllegalArgumentException("邮件内容不能为空");
        }
        
        // 验证邮件地址格式
        for (String email : message.getTo()) {
            if (!isValidEmail(email)) {
                throw new IllegalArgumentException("收件人邮箱格式不正确: " + email);
            }
        }
    }

    /**
     * 填充默认发件人信息
     */
    private void fillDefaultSender(MailMessage message) {
        MailProperties.Sender defaultSender = mailProperties.getDefaultSender();
        
        if (!StringUtils.hasText(message.getFrom()) && StringUtils.hasText(defaultSender.getEmail())) {
            message.setFrom(defaultSender.getEmail());
        }
        
        if (!StringUtils.hasText(message.getFromName()) && StringUtils.hasText(defaultSender.getName())) {
            message.setFromName(defaultSender.getName());
        }
        
        if (!StringUtils.hasText(message.getReplyTo()) && StringUtils.hasText(defaultSender.getReplyTo())) {
            message.setReplyTo(defaultSender.getReplyTo());
        }
    }

    /**
     * 生成消息ID
     */
    private String generateMessageId(MailMessage message) {
        if (StringUtils.hasText(message.getMessageId())) {
            return message.getMessageId();
        }
        
        String messageId = UUID.randomUUID().toString();
        message.setMessageId(messageId);
        return messageId;
    }

    /**
     * 检查是否有内容
     */
    private boolean hasContent(MailMessage message) {
        return StringUtils.hasText(message.getText()) || 
               StringUtils.hasText(message.getHtml()) || 
               StringUtils.hasText(message.getTemplate());
    }

    /**
     * 检查是否有HTML内容
     */
    private boolean hasHtmlContent(MailMessage message) {
        return StringUtils.hasText(message.getHtml()) || 
               StringUtils.hasText(message.getTemplate()) ||
               CollUtil.isNotEmpty(message.getAttachments()) ||
               CollUtil.isNotEmpty(message.getInlineResources());
    }
}