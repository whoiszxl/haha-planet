package com.whoiszxl.messaging.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.InputStream;

/**
 * 邮件附件
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailAttachment {

    /**
     * 附件名称
     */
    @NotBlank(message = "附件名称不能为空")
    private String filename;

    /**
     * 附件内容类型
     */
    private String contentType;

    /**
     * 附件数据
     */
    private byte[] data;

    /**
     * 附件输入流
     */
    private InputStream inputStream;

    /**
     * 附件文件路径
     */
    private String filePath;

    /**
     * 附件大小（字节）
     */
    private Long size;

    /**
     * 是否内联
     */
    private boolean inline = false;

    /**
     * 内联ID（用于HTML中引用）
     */
    private String contentId;
}