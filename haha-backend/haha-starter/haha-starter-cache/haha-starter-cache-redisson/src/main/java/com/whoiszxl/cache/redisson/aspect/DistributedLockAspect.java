package com.whoiszxl.cache.redisson.aspect;

import com.whoiszxl.cache.redisson.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁切面
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = buildLockKey(joinPoint, distributedLock);
        RLock lock = redissonClient.getLock(lockKey);
        
        boolean acquired = false;
        try {
            acquired = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            
            if (acquired) {
                log.debug("成功获取分布式锁: {}", lockKey);
                return joinPoint.proceed();
            } else {
                log.warn("获取分布式锁失败: {}", lockKey);
                return handleLockFailure(distributedLock, joinPoint);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取分布式锁被中断: {}", lockKey, e);
            throw new RuntimeException("获取分布式锁被中断", e);
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("释放分布式锁: {}", lockKey);
            }
        }
    }

    /**
     * 构建锁的key
     */
    private String buildLockKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        String key = distributedLock.key();
        
        if (key.contains("#")) {
            // 解析SpEL表达式
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String[] paramNames = discoverer.getParameterNames(method);
            Object[] args = joinPoint.getArgs();
            
            EvaluationContext context = new StandardEvaluationContext();
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    context.setVariable(paramNames[i], args[i]);
                }
            }
            
            Expression expression = parser.parseExpression(key);
            key = expression.getValue(context, String.class);
        }
        
        return distributedLock.prefix() + key;
    }

    /**
     * 处理锁获取失败的情况
     */
    private Object handleLockFailure(DistributedLock distributedLock, ProceedingJoinPoint joinPoint) {
        switch (distributedLock.failStrategy()) {
            case EXCEPTION:
                throw new RuntimeException(distributedLock.failMessage());
            case RETURN_NULL:
                return null;
            case RETURN_DEFAULT:
                // 返回方法返回类型的默认值
                Class<?> returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                if (returnType == boolean.class || returnType == Boolean.class) {
                    return false;
                } else if (returnType.isPrimitive()) {
                    return 0;
                }
                return null;
            default:
                throw new RuntimeException(distributedLock.failMessage());
        }
    }
}