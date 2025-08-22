package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 画廊响应对象
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "画廊响应对象")
public class GalleryResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "画廊ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "图片宽度")
    private Integer width;

    @Schema(description = "图片高度")
    private Integer height;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "排序顺序")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}