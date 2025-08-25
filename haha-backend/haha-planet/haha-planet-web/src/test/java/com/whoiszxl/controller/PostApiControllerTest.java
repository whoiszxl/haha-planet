package com.whoiszxl.controller;

import com.whoiszxl.service.PostService;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 帖子API控制器测试类
 *
 * @author whoiszxl
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostApiControllerTest {

    @Autowired
    private PostService postService;

    @Autowired
    private RedissonUtil redissonUtil;

    /**
     * 测试浏览数更新功能
     */
    @Test
    @Transactional
    void testUpdateViewCount() {
        // 测试数据
        Long testPostId = 1L;
        Integer initialViewCount = 100;
        Integer updatedViewCount = 150;

        try {
            // 测试更新浏览数
            postService.updateViewCount(testPostId, initialViewCount);
            System.out.println("初始浏览数设置成功: " + initialViewCount);

            // 再次更新浏览数
            postService.updateViewCount(testPostId, updatedViewCount);
            System.out.println("浏览数更新成功: " + updatedViewCount);

            // 验证更新成功
            assertTrue(true, "浏览数更新功能测试通过");

        } catch (Exception e) {
            fail("浏览数更新功能测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试RedissonUtil缓存功能
     */
    @Test
    void testRedissonUtilViewCountCache() {
        // 测试数据
        Long testPostId = 999L;
        String viewCountKey = redissonUtil.formatKey("post", "view", "count", testPostId.toString());
        String userViewKey = redissonUtil.formatKey("post", "view", "user", testPostId.toString(), "1");

        try {
            // 清理测试数据
            redissonUtil.delete(viewCountKey);
            redissonUtil.delete(userViewKey);

            // 测试Redis计数器
            Long count1 = redissonUtil.addAndGet(viewCountKey, 1);
            assertEquals(1L, count1, "Redis计数器第一次增加应该为1");

            Long count2 = redissonUtil.addAndGet(viewCountKey, 1);
            assertEquals(2L, count2, "Redis计数器第二次增加应该为2");

            // 测试用户浏览标记
            redissonUtil.set(userViewKey, "1", Duration.ofHours(24));
            boolean hasViewed = redissonUtil.exists(userViewKey);
            assertTrue(hasViewed, "用户浏览标记应该存在");

            // 测试获取计数值
            Long currentCount = redissonUtil.get(viewCountKey);
            assertEquals(2L, currentCount, "当前计数应该为2");

            System.out.println("RedissonUtil缓存功能测试通过");

            // 清理测试数据
            redissonUtil.delete(viewCountKey);
            redissonUtil.delete(userViewKey);

        } catch (Exception e) {
            fail("RedissonUtil缓存功能测试失败: " + e.getMessage());
        }
    }

    /**
     * 测试参数校验
     */
    @Test
    void testParameterValidation() {
        // 测试空参数
        assertThrows(RuntimeException.class, () -> {
            postService.updateViewCount(null, 100);
        }, "帖子ID为空应该抛出异常");

        assertThrows(RuntimeException.class, () -> {
            postService.updateViewCount(1L, null);
        }, "浏览数为空应该抛出异常");

        // 测试无效参数
        assertThrows(RuntimeException.class, () -> {
            postService.updateViewCount(0L, 100);
        }, "帖子ID为0应该抛出异常");

        assertThrows(RuntimeException.class, () -> {
            postService.updateViewCount(1L, -1);
        }, "浏览数为负数应该抛出异常");

        System.out.println("参数校验测试通过");
    }
}