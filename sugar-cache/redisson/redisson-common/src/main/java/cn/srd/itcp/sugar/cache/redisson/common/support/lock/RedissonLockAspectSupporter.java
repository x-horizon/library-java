package cn.srd.itcp.sugar.cache.redisson.common.support.lock;

import cn.hutool.core.util.IdUtil;
import cn.srd.itcp.sugar.cache.redisson.common.core.lock.RedissonFairLock;
import cn.srd.itcp.sugar.cache.redisson.common.core.lock.RedissonLockTemplate;
import cn.srd.itcp.sugar.cache.redisson.common.exception.RedissonGenerateLockNameFailedException;
import cn.srd.itcp.sugar.cache.redisson.common.exception.RedissonIllegalArgumentException;
import cn.srd.itcp.sugar.framework.spring.tool.common.core.SpringsUtil;
import cn.srd.itcp.sugar.tool.core.Objects;
import cn.srd.itcp.sugar.tool.core.ReflectsUtil;
import cn.srd.itcp.sugar.tool.core.asserts.Assert;
import cn.srd.itcp.sugar.tool.core.convert.Converts;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁注解切面支持
 *
 * @author wjm
 * @since 2020/12/12 18:06
 */
public class RedissonLockAspectSupporter {

    /**
     * instance
     */
    private static RedissonLockAspectSupporter instance = null;

    /**
     * instance init
     */
    @PostConstruct
    public void init() {
        instance = this;
    }

    /**
     * get singleton instance
     *
     * @return instance
     */
    public static RedissonLockAspectSupporter getInstance() {
        return instance;
    }

    /**
     * {@link RedissonLockTemplate} 实现类的缓存，避免每次都要从 Spring 容器中获取
     */
    private static final Map<Class<? extends RedissonLockTemplate>, RedissonLockTemplate> REDISSON_LOCK_TEMPLATE_CACHE = new ConcurrentHashMap<>();

    /**
     * 对临界区完成添加和释放分布式锁的操作
     *
     * @param lockName                  参考 {@link RedissonFairLock#lockName()}
     * @param fieldName                 参考 {@link RedissonFairLock#fieldName()}
     * @param fieldOrder                参考 {@link RedissonFairLock#fieldOrder()}
     * @param waitTime                  参考 {@link RedissonFairLock#waitTime()}
     * @param leaseTime                 参考 {@link RedissonFairLock#leaseTime()}
     * @param timeUnit                  参考 {@link RedissonFairLock#timeUnit()}
     * @param redissonLockTemplateClass 参考 {@link RedissonFairLock#redissonLockTemplate()}
     * @param joinPoint                 切点
     * @param <T>                       implement by {@link RedissonLockTemplate}
     * @return 临界区响应值
     */
    protected <T extends RedissonLockTemplate> Object lock(String lockName, String fieldName, int fieldOrder, long waitTime, long leaseTime, TimeUnit timeUnit, Class<T> redissonLockTemplateClass, ProceedingJoinPoint joinPoint) {
        return doLock(generateLockName(lockName, fieldName, fieldOrder, joinPoint), waitTime, leaseTime, timeUnit, redissonLockTemplateClass, joinPoint);
    }

    /**
     * 生成锁名
     *
     * @param lockName   参考 {@link RedissonFairLock#lockName()}
     * @param fieldName  参考 {@link RedissonFairLock#fieldName()}
     * @param fieldOrder 参考 {@link RedissonFairLock#fieldOrder()}
     * @param joinPoint  切点
     * @return 锁名
     */
    private String generateLockName(String lockName, String fieldName, int fieldOrder, ProceedingJoinPoint joinPoint) {
        if (Objects.isBlank(lockName)) {
            if (Objects.isBlank(fieldName)) {
                return IdUtil.getSnowflake().nextIdStr();
            }
            // 被注解方法的形参列表
            Object[] lockAnnotationMethodParameters = joinPoint.getArgs();
            if (lockAnnotationMethodParameters.length > 0) {
                // 根据 fieldOrder 获取方法形参列表上的第 fieldOrder 个参数，fieldOrder 为 0 时获取第 1 个，为 n 时获取第 n 个；
                Object lockAnnotationMethodParameter = fieldOrder > 0 ? lockAnnotationMethodParameters[fieldOrder - 1] : lockAnnotationMethodParameters[0];
                return Objects.requireNotBlank(
                        new RedissonGenerateLockNameFailedException("无法根据给定参数生成锁名：获取到的方法参数上对应的字段值为空，请检查！"),
                        Converts.toStr(ReflectsUtil.getFieldValue(lockAnnotationMethodParameter, fieldName))
                );
            }
        }
        return lockName;
    }

    /**
     * 对临界区完成添加和释放分布式锁的操作
     *
     * @param lockName                  参考 {@link RedissonFairLock#lockName()}
     * @param waitTime                  参考 {@link RedissonFairLock#waitTime()}
     * @param leaseTime                 参考 {@link RedissonFairLock#leaseTime()}
     * @param timeUnit                  参考 {@link RedissonFairLock#timeUnit()}
     * @param redissonLockTemplateClass 参考 {@link RedissonFairLock#redissonLockTemplate()}
     * @param joinPoint                 切点
     * @param <T>                       {@link RedissonLockTemplate} 实现类类型
     * @return 临界区响应值
     */
    private <T extends RedissonLockTemplate> Object doLock(String lockName, long waitTime, long leaseTime, TimeUnit timeUnit, Class<T> redissonLockTemplateClass, ProceedingJoinPoint joinPoint) {
        Assert.INSTANCE.set(new RedissonIllegalArgumentException("非法的 waitTime 值，请检查！")).throwsIfTrue(waitTime < 0);
        RedissonLockTemplate redissonLockTemplate = getRedissonLockTemplate(redissonLockTemplateClass);
        return redissonLockTemplate.tryLock(() -> this.proceed(joinPoint), lockName, waitTime, leaseTime, timeUnit);
    }

    /**
     * 获取 {@link RedissonLockTemplate} 实现类
     *
     * @param redissonLockTemplateClass {@link RedissonLockTemplate}类
     * @param <T>                       {@link RedissonLockTemplate} 实现类类型
     * @return implement by {@link RedissonLockTemplate}
     */
    private <T extends RedissonLockTemplate> RedissonLockTemplate getRedissonLockTemplate(Class<T> redissonLockTemplateClass) {
        RedissonLockTemplate redissonLockTemplate = REDISSON_LOCK_TEMPLATE_CACHE.get(redissonLockTemplateClass);
        if (Objects.isNull(redissonLockTemplate)) {
            redissonLockTemplate = SpringsUtil.getBean(redissonLockTemplateClass);
            REDISSON_LOCK_TEMPLATE_CACHE.put(redissonLockTemplateClass, redissonLockTemplate);
        }
        return redissonLockTemplate;
    }

    /**
     * 执行临界区
     *
     * @param joinPoint 切点
     * @return 切面响应值
     */
    @SneakyThrows
    private Object proceed(ProceedingJoinPoint joinPoint) {
        return joinPoint.proceed();
    }

}
