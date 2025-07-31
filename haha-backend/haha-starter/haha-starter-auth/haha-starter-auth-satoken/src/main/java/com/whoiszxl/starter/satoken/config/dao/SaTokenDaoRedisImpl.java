package com.whoiszxl.starter.satoken.config.dao;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import com.whoiszxl.cache.redisson.util.RedissonUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sa-Token 持久层 Redis 实现（参考：Sa-Token/sa-token-plugin/sa-token-dao-redisx/SaTokenDaoOfRedis.java）
 * @author whoiszxl
 */
public class SaTokenDaoRedisImpl implements SaTokenDao {

    RedissonUtil redissonUtil;
    
    public SaTokenDaoRedisImpl(RedissonUtil redissonUtil) {
        this.redissonUtil = redissonUtil;
    }
    
    @Override
    public String get(String key) {
        return redissonUtil.get(key);
    }

    @Override
    public void set(String key, String value, long timeout) {
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            redissonUtil.set(key, value);
        } else {
            redissonUtil.set(key, value, Duration.ofSeconds(timeout));
        }
    }

    @Override
    public void update(String key, String value) {
        long expire = getTimeout(key);
        // -2：无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.set(key, value, expire);
    }

    @Override
    public void delete(String key) {
        redissonUtil.delete(key);
    }

    @Override
    public long getTimeout(String key) {
        long timeout = redissonUtil.getTimeToLive(key);
        return timeout < 0 ? timeout : timeout / 1000;
    }

    @Override
    public void updateTimeout(String key, long timeout) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getTimeout(key);
            // 如果其已经被设置为永久，则不作任何处理。如果尚未被设置为永久，那么再次 set 一次
            if (expire != SaTokenDao.NEVER_EXPIRE) {
                this.set(key, this.get(key), timeout);
            }
            return;
        }
        redissonUtil.expire(key, Duration.ofSeconds(timeout));
    }

    @Override
    public Object getObject(String key) {
        return redissonUtil.get(key);
    }

    @Override
    public void setObject(String key, Object object, long timeout) {
        if (0 == timeout || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            redissonUtil.set(key, object);
        } else {
            redissonUtil.set(key, object, Duration.ofSeconds(timeout));
        }
    }

    @Override
    public void updateObject(String key, Object object) {
        long expire = getObjectTimeout(key);
        // -2：无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.setObject(key, object, expire);
    }

    @Override
    public void deleteObject(String key) {
        redissonUtil.delete(key);
    }

    @Override
    public long getObjectTimeout(String key) {
        return this.getTimeout(key);
    }

    @Override
    public void updateObjectTimeout(String key, long timeout) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getObjectTimeout(key);
            // 如果其已经被设置为永久，则不作任何处理。如果尚未被设置为永久，那么再次 set 一次
            if (expire != SaTokenDao.NEVER_EXPIRE) {
                this.setObject(key, this.getObject(key), timeout);
            }
            return;
        }
        redissonUtil.expire(key, Duration.ofSeconds(timeout));
    }

    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        Collection<String> keys = redissonUtil.keys("%s*%s*".formatted(prefix, keyword));
        List<String> list = new ArrayList<>(keys);
        return SaFoxUtil.searchList(list, start, size, sortType);
    }
}
