package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.constants.PostCacheConstants;
import com.whoiszxl.model.cache.PostDetailCache;
import com.whoiszxl.model.cache.PostListCache;
import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.planet.mapper.PlanetPostMapper;
import com.whoiszxl.planet.mapper.PlanetPostArticleMapper;
import com.whoiszxl.planet.model.entity.PlanetPostDO;
import com.whoiszxl.planet.model.entity.PlanetPostArticleDO;
import com.whoiszxl.dto.UserFeignDTO;
import com.whoiszxl.feign.UserFeignClient;
import com.whoiszxl.service.PostCachedService;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import com.whoiszxl.starter.web.model.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 帖子Redis缓存服务实现类（不使用本地缓存）
 *
 * @author whoiszxl
 */
@Slf4j
@Service("postRedisCachedServiceImpl")
@RequiredArgsConstructor
public class PostRedisCachedServiceImpl implements PostCachedService {

    private final RedissonUtil redissonUtil;
    private final RedissonClient redissonClient;
    private final PlanetPostMapper planetPostMapper;
    private final PlanetPostArticleMapper planetPostArticleMapper;
    private final UserFeignClient userFeignClient;

    @Override
    public PostListCache getCachedPostList(Long planetId, Integer page, Integer pageSize, Integer sortType, Long version) {
        // 构建缓存键
        String cacheKey = buildPostListCacheKey(planetId, page, pageSize, sortType);
        String postListKey = PostCacheConstants.CACHE_POST_LIST_PREFIX + cacheKey;

        // 1. 从分布式缓存Redis中获取帖子列表数据
        PostListCache postListCache = redissonUtil.get(postListKey);

        // 2. 如果Redis缓存中存在数据且版本号匹配
        if (postListCache != null && postListCache.isExist()) {
            // 2.1 版本号为空则直接返回Redis缓存
            if (version == null) {
                log.info("[帖子Redis缓存] 获取帖子列表命中Redis缓存，星球ID: {}", planetId);
                return postListCache;
            }

            // 2.2 版本号存在则判断传入版本号是否小于等于缓存内的版本号，如果小于等于则说明Redis缓存为最新
            if (version <= postListCache.getVersion()) {
                log.info("[帖子Redis缓存] 获取帖子列表命中Redis缓存，星球ID: {}，版本号: {}", planetId, version);
                return postListCache;
            }
        }

        // 3. Redis缓存中不存在列表，需要从数据库中读取，并更新缓存
        if (postListCache == null) {
            // 3.1 使用布隆过滤器检查星球ID是否可能存在（防止缓存穿透）
            // 布隆过滤器（星球ID）
            RBloomFilter<Long> planetIdBloomFilter = redissonClient.getBloomFilter(PostCacheConstants.BLOOM_PLANET_ID);
            if (planetIdBloomFilter.isExists() && !planetIdBloomFilter.contains(planetId)) {
                // 布隆过滤器判断不存在，直接返回不存在（防止缓存穿透）
                log.info("[帖子Redis缓存] 布隆过滤器判断星球ID不存在: {}", planetId);
                postListCache = new PostListCache().notExist().setPlanetId(planetId);
                
                // 将不存在的结果缓存一段时间，防止频繁查询
                redissonUtil.set(postListKey, postListCache, Duration.ofMinutes(1));
            } else {
                // 布隆过滤器判断可能存在，从数据库加载数据
                log.info("[帖子Redis缓存] 星球ID可能存在，从数据库加载: planetId={}", planetId);
                postListCache = loadPostListFromDb(planetId, page, pageSize, sortType);
            }
        }

        return postListCache;
    }

    /**
     * 从数据库加载帖子列表
     */
    private PostListCache loadPostListFromDb(Long planetId, Integer page, Integer pageSize, Integer sortType) {
        log.info("[帖子Redis缓存] 从数据库获取帖子列表，星球ID: {}", planetId);

        String cacheKey = buildPostListCacheKey(planetId, page, pageSize, sortType);
        String lockKey = PostCacheConstants.LOCK_GET_POST_LIST_FROM_DB_PREFIX + cacheKey;

        // 1. 从数据库中获取列表数据需要加分布式锁，防止多个微服务同时去数据库中获取数据更新缓存造成数据错误的问题
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean lockFlag = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!lockFlag) {
                return new PostListCache().tryLater();
            }

            // 2. 参数校验
            if (planetId == null || planetId <= 0) {
                log.warn("[帖子Redis缓存] 星球ID参数无效: {}", planetId);
                return new PostListCache().notExist().setPlanetId(planetId);
            }

            if (page <= 0) {
                page = 1;
            }

            if (pageSize <= 0 || pageSize > 100) {
                pageSize = 20;
            }

            // 3. 构建查询条件
            LambdaQueryWrapper<PlanetPostDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PlanetPostDO::getPlanetId, planetId)
                       .eq(PlanetPostDO::getStatus, 1)
                       .eq(PlanetPostDO::getAuditStatus, 2);

            // 4. 根据排序方式设置排序规则
            switch (sortType) {
                case 2:
                    queryWrapper.orderByDesc(PlanetPostDO::getLikeCount)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
                case 3:
                    queryWrapper.orderByDesc(PlanetPostDO::getCommentCount)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
                case 4:
                    queryWrapper.orderByDesc(PlanetPostDO::getViewCount)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
                default:
                    queryWrapper.orderByDesc(PlanetPostDO::getIsTop)
                               .orderByDesc(PlanetPostDO::getCreatedAt);
                    break;
            }

            // 5. 分页查询
            Page<PlanetPostDO> pageParam = new Page<>(page, pageSize);
            IPage<PlanetPostDO> pageResult = planetPostMapper.selectPage(pageParam, queryWrapper);

            PostListCache postListCache;
            if (pageResult.getRecords().isEmpty()) {
                // 6. 如果数据库中不存在数据列表，则返回无记录对象
                postListCache = new PostListCache().notExist()
                        .setPlanetId(planetId)
                        .setPage(page)
                        .setPageSize(pageSize)
                        .setSortType(sortType);
            } else {
                // 7. 如果数据库中存在记录，则返回有记录对象，并设置版本号，版本号为当前时间的时间戳
                List<PostResp> postRespList = HahaBeanUtil.copyToList(pageResult.getRecords(), PostResp.class);
                
                // 8. 通过Feign调用用户服务，获取用户昵称和头像信息
                enrichPostListWithUserInfo(postRespList);
                
                postListCache = new PostListCache()
                        .setTotal(pageResult.getTotal())
                        .setPostList(postRespList)
                        .setPlanetId(planetId)
                        .setPage(page)
                        .setPageSize(pageSize)
                        .setSortType(sortType)
                        .setVersion(System.currentTimeMillis())
                        .exist();
            }

            // 9. 将数据库中获取的数据列表回写到Redis，并设置失效时间为3分钟，具体失效时间根据业务而定
            String postListKey = PostCacheConstants.CACHE_POST_LIST_PREFIX + cacheKey;
            Duration expireTime = postListCache.isExist() ? Duration.ofMinutes(3) : Duration.ofMinutes(1);
            redissonUtil.set(postListKey, postListCache, expireTime);
            
            log.info("[帖子Redis缓存] 从数据库获取帖子列表，更新Redis缓存成功，星球ID: {}, 记录数: {}", 
                    planetId, postListCache.isExist() ? postListCache.getPostList().size() : 0);
            return postListCache;
        } catch (Exception e) {
            log.error("[帖子Redis缓存] 从数据库获取帖子列表，更新Redis缓存失败，星球ID: {}", planetId, e);
            // 10. 如果抛出异常，则返回一个稍后再试的缓存对象
            return new PostListCache().tryLater();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 通过Feign调用用户服务，为帖子列表补充用户信息
     *
     * @param postRespList 帖子响应列表
     */
    private void enrichPostListWithUserInfo(List<PostResp> postRespList) {
        if (CollectionUtils.isEmpty(postRespList)) {
            return;
        }

        try {
            // 1. 收集所有唯一的用户ID
            Set<Long> userIds = postRespList.stream()
                    .map(PostResp::getUserId)
                    .filter(userId -> userId != null && userId > 0)
                    .collect(Collectors.toSet());

            if (userIds.isEmpty()) {
                log.warn("[帖子Redis缓存] 帖子列表中没有有效的用户ID");
                return;
            }

            // 2. 批量调用用户服务获取用户信息
            Map<Long, UserFeignDTO> userInfoMap = batchGetUserInfo(userIds);

            // 3. 为每个帖子设置用户昵称和头像
            postRespList.forEach(postResp -> {
                Long userId = postResp.getUserId();
                if (userId != null && userInfoMap.containsKey(userId)) {
                    UserFeignDTO userInfo = userInfoMap.get(userId);
                    postResp.setUserName(userInfo.getNickname());
                    postResp.setUserAvatar(userInfo.getAvatar());
                }
            });

            log.info("[帖子Redis缓存] 成功为 {} 个帖子补充用户信息，涉及 {} 个用户", 
                    postRespList.size(), userInfoMap.size());

        } catch (Exception e) {
            log.error("[帖子Redis缓存] 补充用户信息失败", e);
            // 即使用户信息获取失败，也不影响帖子列表的返回
        }
    }

    /**
     * 批量获取用户信息
     *
     * @param userIds 用户ID集合
     * @return 用户ID到用户信息的映射
     */
    private Map<Long, UserFeignDTO> batchGetUserInfo(Set<Long> userIds) {
        try {
            // 使用批量接口获取用户信息
            List<Long> userIdList = new ArrayList<>(userIds);
            R<Map<Long, UserFeignDTO>> result = userFeignClient.batchGetUsersByIds(userIdList);
            
            if (result != null && result.isSuccess() && result.getData() != null) {
                log.info("[帖子Redis缓存] 批量获取用户信息成功，请求 {} 个用户，返回 {} 个用户信息", 
                        userIds.size(), result.getData().size());
                return result.getData();
            } else {
                log.warn("[帖子Redis缓存] 批量获取用户信息失败，响应: {}", result);
                return new HashMap<>();
            }
        } catch (Exception e) {
            log.error("[帖子Redis缓存] 批量获取用户信息异常，用户ID数量: {}", userIds.size(), e);
            // 如果批量接口调用失败，降级为单个接口调用
            return batchGetUserInfoFallback(userIds);
        }
    }

    /**
     * 批量获取用户信息的降级方法（单个调用）
     *
     * @param userIds 用户ID集合
     * @return 用户ID到用户信息的映射
     */
    private Map<Long, UserFeignDTO> batchGetUserInfoFallback(Set<Long> userIds) {
        log.warn("[帖子Redis缓存] 使用降级方案，单个获取用户信息");
        Map<Long, UserFeignDTO> userInfoMap = userIds.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        userId -> userId,
                        this::getUserInfoById,
                        (existing, replacement) -> existing
                ));

        // 过滤掉获取失败的用户信息
        return userInfoMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    /**
     * 通过用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息，获取失败返回null
     */
    private UserFeignDTO getUserInfoById(Long userId) {
        try {
            R<UserFeignDTO> result = userFeignClient.getUserById(userId);
            if (result != null && result.isSuccess() && result.getData() != null) {
                return result.getData();
            } else {
                log.warn("[帖子Redis缓存] 获取用户信息失败，用户ID: {}, 响应: {}", userId, result);
                return null;
            }
        } catch (Exception e) {
            log.error("[帖子Redis缓存] 调用用户服务失败，用户ID: {}", userId, e);
            return null;
        }
    }

    @Override
    public PostDetailCache getCachedPostDetail(Long postId, Long version) {
        // 这里保留原有的帖子详情缓存逻辑，因为需求只要求修改帖子列表接口
        // 如果需要也修改帖子详情接口，可以按照类似的方式实现
        String cacheKey = buildPostDetailCacheKey(postId);
        String postDetailKey = PostCacheConstants.CACHE_POST_DETAIL_PREFIX + cacheKey;

        // 1. 从分布式缓存Redis中获取帖子详情数据
        PostDetailCache postDetailCache = redissonUtil.get(postDetailKey);

        // 2. 分布式缓存中不存在详情，需要从数据库中读取，并更新缓存
        if (postDetailCache == null) {
            // 2.1 使用布隆过滤器检查帖子ID是否可能存在（防止缓存穿透）
            // 布隆过滤器（帖子ID）
            RBloomFilter<Long> postIdBloomFilter = redissonClient.getBloomFilter(PostCacheConstants.BLOOM_POST_ID);
            if (postIdBloomFilter.isExists() && !postIdBloomFilter.contains(postId)) {
                // 布隆过滤器判断不存在，直接返回不存在（防止缓存穿透）
                log.info("[帖子Redis缓存] 布隆过滤器判断帖子ID不存在: {}", postId);
                postDetailCache = new PostDetailCache().notExist().setPostId(postId);
                
                // 将不存在的结果缓存一段时间，防止频繁查询
                redissonUtil.set(postDetailKey, postDetailCache, Duration.ofMinutes(1));
            } else {
                // 布隆过滤器判断可能存在，从数据库加载数据
                log.info("[帖子Redis缓存] 帖子ID可能存在，从数据库加载: postId={}", postId);
                postDetailCache = loadPostDetailFromDb(postId);
            }
        }

        return postDetailCache;
    }

    /**
     * 从数据库加载帖子详情
     */
    private PostDetailCache loadPostDetailFromDb(Long postId) {
        log.info("[帖子Redis缓存] 从数据库获取帖子详情，帖子ID: {}", postId);

        String cacheKey = buildPostDetailCacheKey(postId);
        String lockKey = PostCacheConstants.LOCK_GET_POST_DETAIL_FROM_DB_PREFIX + cacheKey;

        // 1. 从数据库中获取详情数据需要加分布式锁，防止多个微服务同时去数据库中获取数据更新缓存造成数据错误的问题
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean lockFlag = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!lockFlag) {
                return new PostDetailCache().tryLater();
            }

            // 2. 参数校验
            if (postId == null || postId <= 0) {
                log.warn("[帖子Redis缓存] 帖子ID参数无效: {}", postId);
                return new PostDetailCache().notExist().setPostId(postId);
            }

            // 3. 构建查询条件
            LambdaQueryWrapper<PlanetPostDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PlanetPostDO::getId, postId)
                       .eq(PlanetPostDO::getStatus, 1)
                       .eq(PlanetPostDO::getAuditStatus, 2);

            // 4. 查询帖子详情
            PlanetPostDO planetPostDO = planetPostMapper.selectOne(queryWrapper);

            PostDetailCache postDetailCache;
            if (planetPostDO == null) {
                // 5. 如果数据库中不存在帖子，则返回无记录对象
                postDetailCache = new PostDetailCache().notExist().setPostId(postId);
            } else {
                // 6. 如果数据库中存在记录，则返回有记录对象，并设置版本号，版本号为当前时间的时间戳
                PostResp postResp = HahaBeanUtil.copyProperties(planetPostDO, PostResp.class);
                
                // 7. 如果是文章类型（contentType=2），查询文章扩展信息
                if (postResp.getContentType() != null && postResp.getContentType() == 2) {
                    enrichPostWithArticleInfo(postResp);
                }
                
                // 8. 通过Feign调用用户服务，获取用户昵称和头像信息
                enrichPostDetailWithUserInfo(postResp);
                
                postDetailCache = new PostDetailCache()
                        .setPostDetail(postResp)
                        .setPostId(postId)
                        .setVersion(System.currentTimeMillis())
                        .exist();
            }

            // 8. 将数据库中获取的帖子详情回写到Redis，并设置失效时间为5分钟，具体失效时间根据业务而定
            String postDetailKey = PostCacheConstants.CACHE_POST_DETAIL_PREFIX + cacheKey;
            Duration expireTime = postDetailCache.isExist() ? Duration.ofMinutes(5) : Duration.ofMinutes(1);
            redissonUtil.set(postDetailKey, postDetailCache, expireTime);
            
            log.info("[帖子Redis缓存] 从数据库获取帖子详情，更新Redis缓存成功，帖子ID: {}, 存在: {}", 
                    postId, postDetailCache.isExist());
            return postDetailCache;
        } catch (Exception e) {
            log.error("[帖子Redis缓存] 从数据库获取帖子详情，更新Redis缓存失败，帖子ID: {}", postId, e);
            // 9. 如果抛出异常，则返回一个稍后再试的缓存对象
            return new PostDetailCache().tryLater();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 为帖子补充文章扩展信息
     *
     * @param postResp 帖子响应对象
     */
    private void enrichPostWithArticleInfo(PostResp postResp) {
        if (postResp == null || postResp.getId() == null) {
            return;
        }

        try {
            // 查询文章扩展信息
            LambdaQueryWrapper<PlanetPostArticleDO> articleQueryWrapper = new LambdaQueryWrapper<>();
            articleQueryWrapper.eq(PlanetPostArticleDO::getPostId, postResp.getId())
                              .eq(PlanetPostArticleDO::getStatus, 1);
            
            PlanetPostArticleDO articleDO = planetPostArticleMapper.selectOne(articleQueryWrapper);
            
            if (articleDO != null) {
                // 创建文章扩展信息对象
                PostResp.ArticleExtension articleExtension = new PostResp.ArticleExtension();
                articleExtension.setContent(articleDO.getContent());
                articleExtension.setCoverImage(articleDO.getCoverImage());
                articleExtension.setTags(articleDO.getTags());
                articleExtension.setWordCount(articleDO.getWordCount());
                articleExtension.setReadingTime(articleDO.getReadingTime());
                articleExtension.setIsOriginal(articleDO.getIsOriginal());
                articleExtension.setSourceUrl(articleDO.getSourceUrl());
                
                // 设置文章扩展信息到帖子响应对象
                postResp.setArticleExtension(articleExtension);
                
                log.info("[帖子Redis缓存] 成功为帖子补充文章扩展信息，帖子ID: {}", postResp.getId());
            } else {
                log.warn("[帖子Redis缓存] 文章类型帖子未找到扩展信息，帖子ID: {}", postResp.getId());
            }
        } catch (Exception e) {
            log.error("[帖子Redis缓存] 查询文章扩展信息失败，帖子ID: {}", postResp.getId(), e);
            // 即使文章扩展信息获取失败，也不影响帖子详情的返回
        }
    }

    /**
     * 通过Feign调用用户服务，为帖子详情补充用户信息
     *
     * @param postResp 帖子响应对象
     */
    private void enrichPostDetailWithUserInfo(PostResp postResp) {
        if (postResp == null || postResp.getUserId() == null || postResp.getUserId() <= 0) {
            return;
        }

        try {
            // 获取用户信息
            UserFeignDTO userInfo = getUserInfoById(postResp.getUserId());
            if (userInfo != null) {
                postResp.setUserName(userInfo.getNickname());
                postResp.setUserAvatar(userInfo.getAvatar());
                log.info("[帖子Redis缓存] 成功为帖子详情补充用户信息，帖子ID: {}, 用户ID: {}", 
                        postResp.getId(), postResp.getUserId());
            }
        } catch (Exception e) {
            log.error("[帖子Redis缓存] 为帖子详情补充用户信息失败，帖子ID: {}", postResp.getId(), e);
            // 即使用户信息获取失败，也不影响帖子详情的返回
        }
    }

    /**
     * 构建帖子列表缓存键
     */
    private String buildPostListCacheKey(Long planetId, Integer page, Integer pageSize, Integer sortType) {
        return String.format("%d_%d_%d_%d", planetId, page, pageSize, sortType);
    }

    /**
     * 构建帖子详情缓存键
     */
    private String buildPostDetailCacheKey(Long postId) {
        return String.valueOf(postId);
    }
}
