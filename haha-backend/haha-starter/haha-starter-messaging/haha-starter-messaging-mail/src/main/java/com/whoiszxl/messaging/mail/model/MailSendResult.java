package com.whoiszxl.messaging.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 邮件发送结果
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailSendResult {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 是否发送成功
     */
    private boolean success;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 异常信息
     */
    private Throwable exception;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 耗时（毫秒）
     */
    private long duration;

    /**
     * 重试次数
     */
    private int retryCount;

    /**
     * 邮件大小（字节）
     */
    private long size;

    /**
     * 创建成功结果
     */
    public static MailSendResult success(String messageId, long duration) {
        return MailSendResult.builder()
                .messageId(messageId)
                .success(true)
                .sendTime(LocalDateTime.now())
                .duration(duration)
                .build();
    }

    /**
     * 创建失败结果
     */
    public static MailSendResult failure(String messageId, String errorMessage, Throwable exception) {
        return MailSendResult.builder()
                .messageId(messageId)
                .success(false)
                .errorMessage(errorMessage)
                .exception(exception)
                .sendTime(LocalDateTime.now())
                .build();
    }
}