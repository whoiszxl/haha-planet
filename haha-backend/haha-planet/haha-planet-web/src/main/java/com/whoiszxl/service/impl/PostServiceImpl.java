package com.whoiszxl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.whoiszxl.model.req.PostCreateReq;
import com.whoiszxl.planet.enums.AuditStatusEnum;
import com.whoiszxl.planet.enums.PostContentTypeEnum;
import com.whoiszxl.planet.enums.PostStatusEnum;
import com.whoiszxl.planet.enums.YesNoEnum;
import com.whoiszxl.planet.mapper.PlanetMemberMapper;
import com.whoiszxl.planet.mapper.PlanetPostArticleMapper;
import com.whoiszxl.planet.mapper.PlanetPostMapper;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import com.whoiszxl.planet.model.entity.PlanetPostArticleDO;
import com.whoiszxl.planet.model.entity.PlanetPostDO;
import com.whoiszxl.service.PostService;
import com.whoiszxl.service.PostCacheUpdateService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 帖子服务实现类
 *
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PlanetPostMapper planetPostMapper;
    private final PlanetPostArticleMapper planetPostArticleMapper;
    private final PlanetMemberMapper planetMemberMapper;
    private final PostCacheUpdateService postCacheUpdateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPost(PostCreateReq req, Long userId) {
        // 0. 基础参数校验
        CheckUtils.throwIfNull(req, "请求参数不能为空");
        CheckUtils.throwIfNull(userId, "用户ID不能为空");
        CheckUtils.throwIfNull(req.getPlanetId(), "星球ID不能为空");
        CheckUtils.throwIfBlank(req.getTitle(), "帖子标题不能为空");
        CheckUtils.throwIfNull(req.getContentType(), "内容类型不能为空");
        CheckUtils.throwIf(!PostContentTypeEnum.TOPIC.getValue().equals(req.getContentType()) && 
                           !PostContentTypeEnum.ARTICLE.getValue().equals(req.getContentType()), 
                           "内容类型只能是主题或文章");
        
        // 1. 校验用户是否为星球成员
        PlanetMemberDO member = validateMemberPermission(req.getPlanetId(), userId);
        
        // 2. 校验置顶和精华权限（只有星球主可以设置）
        validateTopAndEssencePermission(req, member);
        
        // 3. 校验文章内容
        validateArticleContent(req);
        
        // 4. 创建帖子基础信息
        PlanetPostDO post = buildPostDO(req, userId);
        planetPostMapper.insert(post);
        
        // 5. 如果是文章类型，创建文章扩展信息
        if (PostContentTypeEnum.ARTICLE.getValue().equals(req.getContentType())) {
            PlanetPostArticleDO article = buildArticleDO(req, post.getId());
            planetPostArticleMapper.insert(article);
        }
        
        // 6. 清除帖子列表缓存
        postCacheUpdateService.clearPostListCache(req.getPlanetId());
        
        log.info("用户[{}]在星球[{}]创建了帖子[{}]", userId, req.getPlanetId(), post.getId());
        return post.getId();
    }

    @Override
    public void updateViewCount(Long postId, Integer viewCount) {
        // 参数校验
        CheckUtils.throwIfNull(postId, "帖子ID不能为空");
        CheckUtils.throwIfNull(viewCount, "浏览数不能为空");
        CheckUtils.throwIf(postId <= 0, "帖子ID必须大于0");
        CheckUtils.throwIf(viewCount < 0, "浏览数不能为负数");
        
        try {
            // 构建更新条件
            PlanetPostDO updatePost = new PlanetPostDO();
            updatePost.setId(postId);
            updatePost.setViewCount(viewCount);
            
            // 执行更新
            int updateCount = planetPostMapper.updateById(updatePost);
            
            if (updateCount > 0) {
                log.info("[帖子服务] 更新帖子浏览数成功，帖子ID: {}, 浏览数: {}", postId, viewCount);
            } else {
                log.warn("[帖子服务] 更新帖子浏览数失败，帖子不存在，帖子ID: {}", postId);
            }
        } catch (Exception e) {
            log.error("[帖子服务] 更新帖子浏览数异常，帖子ID: {}, 浏览数: {}", postId, viewCount, e);
            throw new RuntimeException("更新帖子浏览数失败", e);
        }
    }
    
    /**
     * 校验用户成员权限
     */
    private PlanetMemberDO validateMemberPermission(Long planetId, Long userId) {
        PlanetMemberDO member = planetMemberMapper.lambdaQuery()
                .eq(PlanetMemberDO::getPlanetId, planetId)
                .eq(PlanetMemberDO::getUserId, userId)
                .eq(PlanetMemberDO::getStatus, YesNoEnum.YES.getValue()).one();
        
        CheckUtils.throwIfNull(member, "您不是该星球的成员，无法发布帖子");
        
        return member;
    }
    
    /**
     * 校验置顶和精华权限
     */
    private void validateTopAndEssencePermission(PostCreateReq req, PlanetMemberDO member) {
        // 只有星球主（memberType=3）可以设置置顶和精华
        CheckUtils.throwIf((req.getIsTop() || req.getIsEssence()) && member.getMemberType() != 3, 
                           "只有星球主可以发布置顶或精华帖子");
    }
    
    /**
     * 校验文章内容
     */
    private void validateArticleContent(PostCreateReq req) {
        if (PostContentTypeEnum.ARTICLE.getValue().equals(req.getContentType())) {
            CheckUtils.throwIfBlank(req.getContent(), "文章内容不能为空");
        }
    }
    
    /**
     * 构建帖子DO对象
     */
    private PlanetPostDO buildPostDO(PostCreateReq req, Long userId) {
        // 校验必要参数
        CheckUtils.throwIfNull(req, "请求参数不能为空");
        CheckUtils.throwIfNull(userId, "用户ID不能为空");
        
        PlanetPostDO post = new PlanetPostDO();
        post.setPlanetId(req.getPlanetId());
        post.setUserId(userId);
        post.setTitle(req.getTitle());
        post.setSummary(req.getSummary());
        post.setContentType(req.getContentType());
        
        // 处理媒体文件URLs
        if (req.getMediaUrls() != null && 
            ((req.getMediaUrls().getFile() != null && !req.getMediaUrls().getFile().isEmpty()) ||
             (req.getMediaUrls().getImage() != null && !req.getMediaUrls().getImage().isEmpty()))) {
            try {
                // 将MediaUrls对象转换为JSON格式存储到数据库
                post.setMediaUrls(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(req.getMediaUrls()));
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                // 如果JSON序列化失败，使用手动拼接的方式构建JSON
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{");
                
                // 添加file数组
                jsonBuilder.append("\"file\":[")
                    .append(req.getMediaUrls().getFile() != null ? 
                        req.getMediaUrls().getFile().stream()
                            .map(f -> "\"" + f + "\"")
                            .reduce((a, b) -> a + "," + b).orElse("") : "")
                    .append("],");
                
                // 添加image数组
                jsonBuilder.append("\"image\":[")
                    .append(req.getMediaUrls().getImage() != null ? 
                        req.getMediaUrls().getImage().stream()
                            .map(i -> "\"" + i + "\"")
                            .reduce((a, b) -> a + "," + b).orElse("") : "")
                    .append("]}");
                
                post.setMediaUrls(jsonBuilder.toString());
            }
        }
        
        post.setIsAnonymous(YesNoEnum.getByBoolean(req.getIsAnonymous()).getValue());
        post.setIsTop(YesNoEnum.getByBoolean(req.getIsTop()).getValue());
        post.setIsEssence(YesNoEnum.getByBoolean(req.getIsEssence()).getValue());
        
        // 初始化统计字段
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setShareCount(0);
        post.setCollectCount(0);
        
        // 设置审核状态（默认通过，可根据星球设置调整）
        post.setAuditStatus(AuditStatusEnum.APPROVED.getValue());
        post.setStatus(PostStatusEnum.NORMAL.getValue());
        
        return post;
    }
    
    /**
     * 构建文章DO对象
     */
    private PlanetPostArticleDO buildArticleDO(PostCreateReq req, Long postId) {
        // 校验必要参数
        CheckUtils.throwIfNull(req, "请求参数不能为空");
        CheckUtils.throwIfNull(postId, "帖子ID不能为空");
        CheckUtils.throwIfBlank(req.getContent(), "文章内容不能为空");
        
        PlanetPostArticleDO article = new PlanetPostArticleDO();
        article.setPostId(postId);
        article.setContent(req.getContent());
        article.setCoverImage(req.getCoverImage());
        
        // 处理标签
        if (req.getTags() != null && !req.getTags().isEmpty()) {
            article.setTags(String.join(",", req.getTags()));
        }
        
        // 计算字数和阅读时间
        int wordCount = calculateWordCount(req.getContent());
        int readingTime = calculateReadingTime(wordCount);
        
        article.setWordCount(wordCount);
        article.setReadingTime(readingTime);
        article.setIsOriginal(YesNoEnum.getByBoolean(req.getIsOriginal()).getValue());
        article.setSourceUrl(req.getSourceUrl());
        article.setStatus(PostStatusEnum.NORMAL.getValue());
        
        return article;
    }
    
    /**
     * 计算字数（支持Markdown格式）
     */
    private int calculateWordCount(String content) {
        // 使用CheckUtils进行参数校验，空内容返回0
        if (StrUtil.isBlank(content)) {
            return 0;
        }
        
        // 移除Markdown语法标记
        String cleanContent = content
                // 移除代码块
                .replaceAll("```[\\s\\S]*?```", "")
                .replaceAll("`[^`]*`", "")
                // 移除链接
                .replaceAll("\\[([^]]+)]\\([^)]+\\)", "$1")
                // 移除图片
                .replaceAll("!\\[([^]]*)]\\([^)]+\\)", "$1")
                // 移除粗体和斜体
                .replaceAll("\\*\\*([^*]+)\\*\\*", "$1")
                .replaceAll("\\*([^*]+)\\*", "$1")
                .replaceAll("__([^_]+)__", "$1")
                .replaceAll("_([^_]+)_", "$1")
                // 移除删除线
                .replaceAll("~~([^~]+)~~", "$1")
                // 移除标题标记（#号）
                .replaceAll("#{1,6}\\s*", "")
                // 移除引用标记（>号）
                .replaceAll(">\\s*", "")
                // 移除列表标记
                .replaceAll("[-*+]\\s+", "")
                .replaceAll("\\d+\\.\\s+", "")
                // 移除HTML标签
                .replaceAll("<[^>]*>", "")
                // 移除多余空白字符
                .replaceAll("\\s+", " ")
                .trim();
        
        // 中文字符按1个字计算，英文单词按空格分割计算
        int chineseCount = 0;
        int englishWordCount = 0;
        
        for (char c : cleanContent.toCharArray()) {
            if (c >= 0x4e00 && c <= 0x9fa5) {
                chineseCount++;
            }
        }
        
        // 计算英文单词数
        String englishContent = cleanContent.replaceAll("[\u4e00-\u9fa5]", " ");
        String[] words = englishContent.split("\\s+");
        for (String word : words) {
            if (word.matches("[a-zA-Z]+")) {
                englishWordCount++;
            }
        }
        
        return chineseCount + englishWordCount;
    }
    
    /**
     * 计算阅读时间（分钟）
     * 按照中文每分钟 300 字，英文每分钟 200 词的标准计算
     */
    private int calculateReadingTime(int wordCount) {
        // 校验字数参数
        CheckUtils.throwIf(wordCount < 0, "字数不能为负数");
        
        if (wordCount <= 0) {
            return 1;
        }
        
        // 按每分钟 250 字的平均阅读速度计算
        int minutes = (int) Math.ceil((double) wordCount / 250);
        return Math.max(1, minutes);
    }
}
