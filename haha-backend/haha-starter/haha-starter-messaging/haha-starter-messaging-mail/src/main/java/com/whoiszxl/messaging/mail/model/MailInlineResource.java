package com.whoiszxl.messaging.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * 邮件内联资源
 *
 * @author whoiszxl
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailInlineResource {

    /**
     * 资源ID
     */
    @NotBlank(message = "资源ID不能为空")
    private String contentId;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    private String filename;

    /**
     * 资源内容类型
     */
    private String contentType;

    /**
     * 资源数据
     */
    private byte[] data;

    /**
     * 资源文件路径
     */
    private String filePath;
}