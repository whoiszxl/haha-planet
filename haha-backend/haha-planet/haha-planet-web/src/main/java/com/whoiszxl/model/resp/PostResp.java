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

    @Schema(description = "帖子内容")
    private String content;

    @Schema(description = "内容类型：1-文本 2-图片 3-视频 4-文件")
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
}
