package com.whoiszxl.service;

import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.starter.crud.model.PageResponse;

/**
 * 帖子服务接口
 *
 * @author whoiszxl
 */
public interface PostService {

    /**
     * 根据星球ID分页查询帖子列表
     *
     * @param planetId 星球ID
     * @param page 页码
     * @param pageSize 每页大小
     * @param sortType 排序方式：1-最新发布 2-最多点赞 3-最多评论 4-最多浏览
     * @return 帖子列表分页结果
     */
    PageResponse<PostResp> getPostsByPlanetId(Long planetId, Integer page, Integer pageSize, Integer sortType);
}
