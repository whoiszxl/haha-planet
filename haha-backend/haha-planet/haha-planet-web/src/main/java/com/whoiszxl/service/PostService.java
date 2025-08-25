package com.whoiszxl.service;

import com.whoiszxl.model.req.PostCreateReq;

/**
 * 帖子服务接口
 *
 * @author whoiszxl
 */
public interface PostService {

    /**
     * 创建新帖子
     *
     * @param req 创建帖子请求
     * @param userId 用户ID
     * @return 帖子ID
     */
    Long createPost(PostCreateReq req, Long userId);

    /**
     * 更新帖子浏览数
     *
     * @param postId 帖子ID
     * @param viewCount 新的浏览数
     */
    void updateViewCount(Long postId, Integer viewCount);
}
