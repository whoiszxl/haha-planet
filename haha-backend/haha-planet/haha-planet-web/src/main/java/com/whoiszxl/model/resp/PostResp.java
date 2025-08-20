package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子响应对象
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "帖子响应对象")
public class PostResp {

    @Schema(description = "帖子ID")
    private Long id;

    @Schema(description = "星球ID")
    private Long planetId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "帖子标题")
    private String title;

    @Schema(description = "帖子概要")
    private String summary;

    @Schema(description = "内容类型：1-主题 2-文章")
    private Integer contentType;

    @Schema(description = "媒体文件URL，JSON数组格式")
    private String mediaUrls;

    @Schema(description = "链接地址")
    private String link;

    @Schema(description = "提取码")
    private String extractCode;

    @Schema(description = "是否匿名：0-否 1-是")
    private Integer isAnonymous;

    @Schema(description = "是否置顶：0-否 1-是")
    private Integer isTop;

    @Schema(description = "是否精华：0-否 1-是")
    private Integer isEssence;

    @Schema(description = "浏览数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "评论数")
    private Integer commentCount;

    @Schema(description = "分享数")
    private Integer shareCount;

    @Schema(description = "收藏数")
    private Integer collectCount;

    @Schema(description = "审核状态：1-待审核 2-审核通过 3-审核拒绝")
    private Integer auditStatus;

    @Schema(description = "最后评论时间")
    private LocalDateTime lastCommentTime;

    @Schema(description = "热度分数")
    private Integer hotScore;

    @Schema(description = "状态：1-正常 0-禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(description = "文章扩展信息（仅文章类型帖子有值）")
    private ArticleExtension articleExtension;

    /**
     * 文章扩展信息静态内部类
     */
    @Data
    @Schema(description = "文章扩展信息")
    public static class ArticleExtension {
        
        @Schema(description = "文章内容")
        private String content;

        @Schema(description = "封面图片URL")
        private String coverImage;

        @Schema(description = "文章标签，逗号分隔")
        private String tags;

        @Schema(description = "字数统计")
        private Integer wordCount;

        @Schema(description = "预估阅读时间（分钟）")
        private Integer readingTime;

        @Schema(description = "是否原创: 0-否 1-是")
        private Integer isOriginal;

        @Schema(description = "来源链接")
        private String sourceUrl;
    }
}
