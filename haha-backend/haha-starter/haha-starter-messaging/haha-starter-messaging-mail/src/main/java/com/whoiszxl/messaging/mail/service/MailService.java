package com.whoiszxl.messaging.mail.service;

import com.whoiszxl.messaging.mail.model.MailMessage;
import com.whoiszxl.messaging.mail.model.MailSendResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 邮件服务接口
 *
 * @author whoiszxl
 */
public interface MailService {

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param text    内容
     * @return 发送结果
     */
    MailSendResult sendText(String to, String subject, String text);

    /**
     * 发送简单文本邮件（多个收件人）
     *
     * @param to      收件人列表
     * @param subject 主题
     * @param text    内容
     * @return 发送结果
     */
    MailSendResult sendText(List<String> to, String subject, String text);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param html    HTML内容
     * @return 发送结果
     */
    MailSendResult sendHtml(String to, String subject, String html);

    /**
     * 发送HTML邮件（多个收件人）
     *
     * @param to      收件人列表
     * @param subject 主题
     * @param html    HTML内容
     * @return 发送结果
     */
    MailSendResult sendHtml(List<String> to, String subject, String html);

    /**
     * 发送模板邮件
     *
     * @param to        收件人
     * @param subject   主题
     * @param template  模板名称
     * @param variables 模板变量
     * @return 发送结果
     */
    MailSendResult sendTemplate(String to, String subject, String template, Map<String, Object> variables);

    /**
     * 发送模板邮件（多个收件人）
     *
     * @param to        收件人列表
     * @param subject   主题
     * @param template  模板名称
     * @param variables 模板变量
     * @return 发送结果
     */
    MailSendResult sendTemplate(List<String> to, String subject, String template, Map<String, Object> variables);

    /**
     * 发送邮件
     *
     * @param message 邮件消息
     * @return 发送结果
     */
    MailSendResult send(MailMessage message);

    /**
     * 异步发送邮件
     *
     * @param message 邮件消息
     * @return 异步发送结果
     */
    CompletableFuture<MailSendResult> sendAsync(MailMessage message);

    /**
     * 批量发送邮件
     *
     * @param messages 邮件消息列表
     * @return 发送结果列表
     */
    List<MailSendResult> sendBatch(List<MailMessage> messages);

    /**
     * 异步批量发送邮件
     *
     * @param messages 邮件消息列表
     * @return 异步发送结果列表
     */
    CompletableFuture<List<MailSendResult>> sendBatchAsync(List<MailMessage> messages);

    /**
     * 验证邮件地址格式
     *
     * @param email 邮件地址
     * @return 是否有效
     */
    boolean isValidEmail(String email);

    /**
     * 测试邮件服务连接
     *
     * @return 是否连接成功
     */
    boolean testConnection();
}