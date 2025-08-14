package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.planet.mapper.PlanetPostMapper;
import com.whoiszxl.planet.model.entity.PlanetPostDO;
import com.whoiszxl.service.PostService;
import com.whoiszxl.starter.crud.model.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public PageResponse<PostResp> getPostsByPlanetId(Long planetId, Integer page, Integer pageSize, Integer sortType) {
        log.info("[帖子服务] 查询星球帖子列表，星球ID: {}, 页码: {}, 页大小: {}, 排序方式: {}", 
                planetId, page, pageSize, sortType);

        // 参数校验
        if (planetId == null || planetId <= 0) {
            log.warn("[帖子服务] 星球ID参数无效: {}", planetId);
            throw new IllegalArgumentException("星球ID参数无效");
        }

        if (page <= 0) {
            page = 1;
        }

        if (pageSize <= 0 || pageSize > 100) {
            pageSize = 20;
        }

        try {
            // 构建查询条件
            LambdaQueryWrapper<PlanetPostDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PlanetPostDO::getPlanetId, planetId)
                       .eq(PlanetPostDO::getStatus, 1) // 只查询正常状态的帖子
                       .eq(PlanetPostDO::getIsDeleted, 0) // 未删除
                       .eq(PlanetPostDO::getAuditStatus, 2); // 审核通过

            // 根据排序方式设置排序规则
            switch (sortType) {
                case 2: // 最多点赞
                    queryWrapper.orderByDesc(PlanetPostDO::getLikeCount)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
                case 3: // 最多评论
                    queryWrapper.orderByDesc(PlanetPostDO::getCommentCount)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
                case 4: // 最多浏览
                    queryWrapper.orderByDesc(PlanetPostDO::getViewCount)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
                default: // 最新发布
                    queryWrapper.orderByDesc(PlanetPostDO::getIsTop) // 置顶帖子优先
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
            }

            // 分页查询
            Page<PlanetPostDO> pageParam = new Page<>(page, pageSize);
            IPage<PlanetPostDO> pageResult = planetPostMapper.selectPage(pageParam, queryWrapper);

            // 转换为响应对象
            List<PostResp> postList = pageResult.getRecords().stream()
                    .map(this::convertToPostResp)
                    .collect(Collectors.toList());

            // 构建分页响应
            PageResponse<PostResp> pageResponse = new PageResponse<>();
            pageResponse.setList(postList);
            pageResponse.setTotal(pageResult.getTotal());

            log.info("[帖子服务] 查询到 {} 条帖子记录，总计 {} 条", postList.size(), pageResult.getTotal());

            return pageResponse;

        } catch (Exception e) {
            log.error("[帖子服务] 查询星球帖子列表失败，星球ID: {}", planetId, e);
            throw new RuntimeException("查询帖子列表失败，请稍后重试", e);
        }
    }

    /**
     * 将实体对象转换为响应对象
     */
    private PostResp convertToPostResp(PlanetPostDO postDO) {
        PostResp postResp = new PostResp();
        BeanUtils.copyProperties(postDO, postResp);
        
        // TODO: 这里可以添加用户信息的查询和填充
        // 例如：根据userId查询用户名和头像
        // UserDO user = userService.getUserById(postDO.getUserId());
        // if (user != null) {
        //     postResp.setUserName(user.getUsername());
        //     postResp.setUserAvatar(user.getAvatar());
        // }
        
        return postResp;
    }
}
