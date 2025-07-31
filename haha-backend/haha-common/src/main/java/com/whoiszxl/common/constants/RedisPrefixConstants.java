package com.whoiszxl.common.constants;

/**
 * @author whoiszxl
 */
public class RedisPrefixConstants {

    /**
     * 管理员端 Redis Key 前缀
     */
    public interface Admin {
        String ADMIN_CAPTCHA_IMAGE_KEY = "admin:captcha:image:";

        String ADMIN_LOGIN_KEY = "admin:login";

    }

    /**
     * C端 Redis Key 前缀
     */
    public interface Member {
        String MEMBER_CAPTCHA_IMAGE_KEY = "member:captcha:image:";

        String MEMBER_CAPTCHA_EMAIL_KEY = "member:captcha:email:";

        String MEMBER_CAPTCHA_PHONE_KEY = "member:captcha:phone:";

        String MEMBER_LOGIN_KEY = "member:login";

    }

    public interface Game {
        /** 评测点赞、点踩、点欢乐的布隆过滤器，后面拼接类型与用户ID */
        String GAME_REVIEW_BLOOM_KEY = "game:review:bloom:%s:%s";

        String GAME_REVIEWS_KEY = "game:reviews:%s";

        String GAME_REVIEW_COUNT_KEY = "game:reviews:count:%s";

        String GAME_REVIEW_ZSET_KEY = "game:reviews:zset:%s";

        String LUA_EXISTS_CHECK_SCRIPT = """
                -- 获取 redis key 与用户 id
                local key, reviewId = KEYS[1], ARGV[1]
                -- 如果布隆过滤器不存在返回-100，已点赞返回1，新增点赞返回0
                return redis.call('EXISTS', key) == 0 and -100
                    or redis.call('BF.EXISTS', key, reviewId) == 1 and 1
                    or (redis.call('BF.ADD', key, reviewId) and 0) 
           """;

           String LUA_BATCH_ADD_SCRIPT = """
                local key = KEYS[1]
                local argCount = #ARGV
                local expireTime = tonumber(ARGV[argCount])
            
                -- 直接使用BF.MADD批量添加元素，排除最后一个参数（过期时间）
                local results = redis.call('BF.MADD', key, unpack(ARGV, 1, argCount - 1))
            
                -- 计算成功添加的元素数量
                local addCount = 0
                for i = 1, #results do
                    if results[i] == 1 then
                        addCount = addCount + 1
                    end
                end
            
                -- 设置过期时间
                redis.call('EXPIRE', key, expireTime)
                return addCount
            """;


        String ADD_REVIEW_SCRIPT = """
            local key = KEYS[1]
            local reviewId = ARGV[1]
            local expireSeconds = ARGV[2]

            if not reviewId or not expireSeconds then
                return -2
            end

            local addResult = redis.call('BF.ADD', key, reviewId)
            redis.call('EXPIRE', key, expireSeconds)
            return addResult
        """;

        String LUA_RATE_DOWN_CHECK_SCRIPT = """
                -- 检查布隆过滤器是否存在并验证评论是否已存在
                return redis.call('EXISTS', KEYS[1]) == 0 and -100 or redis.call('BF.EXISTS', KEYS[1], ARGV[1])
                """;


        String LUA_CHECK_REVIEW_ID_EXISTS_SCRIPT = """
                local likeBloomKey = KEYS[1]
                local dislikeBloomKey = KEYS[2]
                local funnyBloomKey = KEYS[3]
                local reviewIds = ARGV
                local result = {}

                redis.log(redis.LOG_WARNING, "Checking keys: " .. likeBloomKey .. ", " .. dislikeBloomKey .. ", " .. funnyBloomKey)

                for i, reviewId in ipairs(reviewIds) do
                    redis.log(redis.LOG_WARNING, "Checking reviewId: " .. reviewId)

                    local isLiked = redis.call("BF.EXISTS", likeBloomKey, reviewId)
                    local isDisliked = redis.call("BF.EXISTS", dislikeBloomKey, reviewId)
                    local isFunny = redis.call("BF.EXISTS", funnyBloomKey, reviewId)

                    redis.log(redis.LOG_WARNING, "Results for " .. reviewId .. ": " .. isLiked .. ", " .. isDisliked .. ", " .. isFunny)

                    
                    table.insert(result, isLiked)
                    table.insert(result, isDisliked)
                    table.insert(result, isFunny)
                end
                return result
            """;
    }


    public interface Order {
        String MEMBER_CART_PREFIX = "cart:";
    }


    public interface Marketing {
        String SECKILL_LOCK_ORDER_SUBMIT = "seckill:member:order:submit:";
        String SECKILL_TASK_PLACE_ORDER_MQ = "seckill:task:place-order-mq:";
        String SECKILL_TOKEN_PLACE_ORDER = "seckill:token:place_order:";

        String SECKILL_LOCK_REFRESH_PLACE_ORDER_TOKEN = "seckill:lock:refreshPlaceOrderToken:";

    }
}
