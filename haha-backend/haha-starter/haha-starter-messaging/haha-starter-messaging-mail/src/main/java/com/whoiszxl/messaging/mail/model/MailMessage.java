package com.whoiszxl.messaging.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 邮件消息
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailMessage {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 发件人邮箱
     */
    @Email(message = "发件人邮箱格式不正确")
    private String from;

    /**
     * 发件人姓名
     */
    private String fromName;

    /**
     * 收件人列表
     */
    @NotEmpty(message = "收件人列表不能为空")
    private List<String> to;

    /**
     * 抄送列表
     */
    private List<String> cc;

    /**
     * 密送列表
     */
    private List<String> bcc;

    /**
     * 回复邮箱
     */
    @Email(message = "回复邮箱格式不正确")
    private String replyTo;

    /**
     * 邮件主题
     */
    @NotBlank(message = "邮件主题不能为空")
    private String subject;

    /**
     * 邮件内容（纯文本）
     */
    private String text;

    /**
     * 邮件内容（HTML）
     */
    private String html;

    /**
     * 模板名称
     */
    private String template;

    /**
     * 模板变量
     */
    private Map<String, Object> templateVariables;

    /**
     * 附件列表
     */
    private List<MailAttachment> attachments;

    /**
     * 内联资源列表
     */
    private List<MailInlineResource> inlineResources;

    /**
     * 优先级
     */
    private Priority priority = Priority.NORMAL;

    /**
     * 是否异步发送
     */
    private boolean async = true;

    /**
     * 是否启用重试
     */
    private boolean retryEnabled = true;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 邮件优先级
     */
    public enum Priority {
        LOW(5), NORMAL(3), HIGH(1);

        private final int value;

        Priority(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}