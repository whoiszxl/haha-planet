package com.whoiszxl.planet.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import lombok.Data;

import java.io.Serial;

/**
 * 星球帖子文章扩展实体
 *
 * @author whoiszxl
 */
@Data
@TableName("pla_planet_post_article")
public class PlanetPostArticleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    private Long postId;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 文章标签，逗号分隔
     */
    private String tags;

    /**
     * 字数统计
     */
    private Integer wordCount;

    /**
     * 预估阅读时间（分钟）
     */
    private Integer readingTime;

    /**
     * 是否原创: 0-否 1-是
     */
    private Integer isOriginal;

    /**
     * 来源链接
     */
    private String sourceUrl;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 业务状态: 1-正常 2-已删除
     */
    private Integer status;

    /**
     * 逻辑删除 1: 已删除， 0: 未删除
     */
    private Integer isDeleted;
}