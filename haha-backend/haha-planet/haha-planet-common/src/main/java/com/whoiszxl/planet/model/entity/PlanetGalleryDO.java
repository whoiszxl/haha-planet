package com.whoiszxl.planet.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 星球画廊实体类(简化版，主要用于文章封面图)
 *
 * @author whoiszxl
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pla_planet_gallery")
public class PlanetGalleryDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件URL
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 图片宽度
     */
    @TableField("width")
    private Integer width;

    /**
     * 图片高度
     */
    @TableField("height")
    private Integer height;

    /**
     * 缩略图URL
     */
    @TableField("thumbnail_url")
    private String thumbnailUrl;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 分类名称
     */
    @TableField("category")
    private String category;

    /**
     * 图片年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 作者
     */
    @TableField("author")
    private String author;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态(0:禁用 1:启用)
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否删除(0:未删除 1:已删除)
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}