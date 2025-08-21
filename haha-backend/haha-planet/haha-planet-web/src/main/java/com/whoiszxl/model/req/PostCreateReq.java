package com.whoiszxl.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 新增帖子请求类
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "新增帖子请求")
public class PostCreateReq {

    @Schema(description = "星球ID", example = "1")
    @NotNull(message = "星球ID不能为空")
    private Long planetId;

    @Schema(description = "帖子标题", example = "这是一个帖子标题")
    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    private String title;

    @Schema(description = "帖子摘要/主题内容", example = "这是帖子的摘要或主题的主要内容")
    @NotBlank(message = "帖子摘要不能为空")
    @Size(max = 500, message = "摘要长度不能超过500个字符")
    private String summary;

    @Schema(description = "内容类型: 1-主题 2-文章", example = "1")
    @NotNull(message = "内容类型不能为空")
    private Integer contentType;

    @Schema(description = "文章内容(仅当contentType=2时需要)", example = "这是文章的详细内容")
    private String content;

    @Schema(description = "封面图片(仅当contentType=2时可选)", example = "https://example.com/cover.jpg")
    private String coverImage;

    @Schema(description = "媒体文件URLs", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
    private List<String> mediaUrls;

    @Schema(description = "标签列表(仅当contentType=2时可选)", example = "[\"技术\", \"Java\"]")
    private List<String> tags;

    @Schema(description = "是否匿名发布", example = "false")
    private Boolean isAnonymous = false;

    @Schema(description = "是否置顶(仅星球主可设置)", example = "false")
    private Boolean isTop = false;

    @Schema(description = "是否精华(仅星球主可设置)", example = "false")
    private Boolean isEssence = false;

    @Schema(description = "是否原创(仅当contentType=2时可选)", example = "true")
    private Boolean isOriginal = true;

    @Schema(description = "来源链接(仅当contentType=2且isOriginal=false时可选)", example = "https://example.com/source")
    private String sourceUrl;
}