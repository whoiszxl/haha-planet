package com.whoiszxl.cache.redisson.util;

import com.whoiszxl.starter.core.constants.StringConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redisson工具类
 * 提供常用的Redis操作方法
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonUtil {

    private final RedissonClient redissonClient;

    // =============================String操作=============================

    /**
     * 设置字符串值
     */
    public void set(String key, Object value) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置字符串值并指定过期时间
     */
    public void set(String key, Object value, Duration duration) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, duration);
    }

    /**
     * 获取字符串值
     */
    public <T> T get(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 获取字符串值，如果不存在则返回默认值
     */
    public <T> T get(String key, T defaultValue) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        T value = bucket.get();
        return value != null ? value : defaultValue;
    }

    /**
     * 删除键
     */
    public boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 批量删除键
     */
    public long delete(String... keys) {
        return redissonClient.getKeys().delete(keys);
    }

    /**
     * 检查键是否存在
     */
    public boolean exists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 设置键的过期时间
     */
    public boolean expire(String key, Duration duration) {
        return redissonClient.getBucket(key).expire(duration);
    }

    /**
     * 获取键的剩余过期时间
     */
    public long getExpire(String key) {
        return redissonClient.getBucket(key).remainTimeToLive();
    }

    // =============================Hash操作=============================

    /**
     * 获取Hash对象
     */
    public <K, V> RMap<K, V> getMap(String key) {
        return redissonClient.getMap(key);
    }

    /**
     * 设置Hash字段值
     */
    public <K, V> V mapPut(String key, K field, V value) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map.put(field, value);
    }

    /**
     * 获取Hash字段值
     */
    public <K, V> V mapGet(String key, K field) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map.get(field);
    }

    /**
     * 删除Hash字段
     */
    public <K, V> V mapRemove(String key, K field) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map.remove(field);
    }

    /**
     * 获取Hash所有字段和值
     */
    public <K, V> Map<K, V> mapGetAll(String key) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map.readAllMap();
    }

    // =============================List操作=============================

    /**
     * 获取List对象
     */
    public <T> RList<T> getList(String key) {
        return redissonClient.getList(key);
    }

    /**
     * 向List右侧添加元素
     */
    public <T> boolean listAdd(String key, T value) {
        RList<T> list = redissonClient.getList(key);
        return list.add(value);
    }

    /**
     * 向List左侧添加元素
     */
    public <T> void listAddFirst(String key, T value) {
        RList<T> list = redissonClient.getList(key);
        list.add(0, value);
    }

    /**
     * 获取List指定索引的元素
     */
    public <T> T listGet(String key, int index) {
        RList<T> list = redissonClient.getList(key);
        return list.get(index);
    }

    /**
     * 获取List所有元素
     */
    public <T> List<T> listGetAll(String key) {
        RList<T> list = redissonClient.getList(key);
        return list.readAll();
    }

    /**
     * 获取List大小
     */
    public int listSize(String key) {
        RList<Object> list = redissonClient.getList(key);
        return list.size();
    }

    // =============================Set操作=============================

    /**
     * 获取Set对象
     */
    public <T> RSet<T> getSet(String key) {
        return redissonClient.getSet(key);
    }

    /**
     * 向Set添加元素
     */
    public <T> boolean setAdd(String key, T value) {
        RSet<T> set = redissonClient.getSet(key);
        return set.add(value);
    }

    /**
     * 从Set移除元素
     */
    public <T> boolean setRemove(String key, T value) {
        RSet<T> set = redissonClient.getSet(key);
        return set.remove(value);
    }

    /**
     * 检查Set是否包含元素
     */
    public <T> boolean setContains(String key, T value) {
        RSet<T> set = redissonClient.getSet(key);
        return set.contains(value);
    }

    /**
     * 获取Set所有元素
     */
    public <T> Set<T> setGetAll(String key) {
        RSet<T> set = redissonClient.getSet(key);
        return set.readAll();
    }

    // =============================ZSet操作=============================

    /**
     * 获取SortedSet对象
     */
    public <T> RScoredSortedSet<T> getSortedSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    /**
     * 向SortedSet添加元素
     */
    public <T> boolean sortedSetAdd(String key, double score, T value) {
        RScoredSortedSet<T> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.add(score, value);
    }

    /**
     * 获取SortedSet指定范围的元素
     */
    public <T> Collection<T> sortedSetRange(String key, int startIndex, int endIndex) {
        RScoredSortedSet<T> sortedSet = redissonClient.getScoredSortedSet(key);
        return sortedSet.valueRange(startIndex, endIndex);
    }

    // =============================分布式锁操作=============================

    /**
     * 获取分布式锁
     */
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    /**
     * 尝试获取锁
     */
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取锁被中断: {}", key, e);
            return false;
        }
    }

    /**
     * 释放锁
     */
    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    // =============================分布式信号量操作=============================

    /**
     * 获取信号量
     */
    public RSemaphore getSemaphore(String key) {
        return redissonClient.getSemaphore(key);
    }

    /**
     * 尝试获取信号量许可
     */
    public boolean tryAcquire(String key, int permits, long waitTime, TimeUnit unit) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        try {
            return semaphore.tryAcquire(permits, waitTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取信号量被中断: {}", key, e);
            return false;
        }
    }

    /**
     * 释放信号量许可
     */
    public void release(String key, int permits) {
        RSemaphore semaphore = redissonClient.getSemaphore(key);
        semaphore.release(permits);
    }

    // =============================发布订阅操作=============================

    /**
     * 获取主题
     */
    public <T> RTopic getTopic(String topicName) {
        return redissonClient.getTopic(topicName);
    }

    /**
     * 发布消息
     */
    public <T> long publish(String topicName, T message) {
        RTopic topic = redissonClient.getTopic(topicName);
        return topic.publish(message);
    }

    /**
     * 订阅消息
     */
    public <T> void subscribe(String topicName, MessageListener<T> listener) {
        RTopic topic = redissonClient.getTopic(topicName);
        topic.addListener(Object.class, listener);
    }

    // =============================原子操作=============================

    /**
     * 获取原子长整型
     */
    public RAtomicLong getAtomicLong(String key) {
        return redissonClient.getAtomicLong(key);
    }

    /**
     * 原子递增
     */
    public long incrementAndGet(String key) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.incrementAndGet();
    }

    /**
     * 原子递减
     */
    public long decrementAndGet(String key) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.decrementAndGet();
    }

    /**
     * 原子增加指定值
     */
    public long addAndGet(String key, long delta) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.addAndGet(delta);
    }

    /**
     * 查询缓存剩余过期时间
     *
     * @param key 键
     * @return 缓存剩余过期时间（单位：毫秒）
     */
    public long getTimeToLive(String key) {
        return redissonClient.getBucket(key).remainTimeToLive();
    }

    /**
     * 查询缓存列表
     *
     * @param pattern 键模式
     * @return 缓存列表
     */
    public Collection<String> keys(String pattern) {
        return redissonClient.getKeys().getKeysStreamByPattern(pattern).toList();
    }

    /**
     * 格式化键，将各子键用 : 拼接起来
     *
     * @param subKeys 子键列表
     * @return 键
     */
    public String formatKey(String... subKeys) {
        return String.join(StringConstants.COLON, subKeys);
    }
}