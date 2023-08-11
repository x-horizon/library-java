package cn.srd.itcp.sugar.cache.redis.core;

import cn.srd.sugar.context.redis.core.RedisManager;
import cn.srd.sugar.tool.lang.core.CollectionsUtil;
import cn.srd.sugar.tool.lang.core.object.Objects;
import cn.srd.sugar.tool.lang.core.time.DurationWrapper;
import cn.srd.sugar.tool.lang.core.time.TimeUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.redisson.api.RBatch;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Redis 缓存操作（桶）
 *
 * @author wjm
 * @since 2023-01-12 10:37:12
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisCacheBucket implements RedisCacheTemplate {

    /**
     * singleton pattern
     */
    protected static final RedisCacheBucket INSTANCE = new RedisCacheBucket();

    @Override
    public <V> void set(String key, V value, Duration expiration) {
        RBucket<V> cache = RedisManager.getClient().getBucket(key);
        if (Objects.equals(Duration.ZERO, expiration)) {
            cache.set(value);
        } else {
            DurationWrapper durationWrapper = TimeUtil.toDurationWrapper(expiration);
            cache.set(value, durationWrapper.getTime(), durationWrapper.getTimeUnit());
        }
    }

    @Override
    public <V> boolean setIfExists(String key, V value, Duration expiration) {
        RBucket<V> cache = RedisManager.getClient().getBucket(key);
        if (Objects.equals(Duration.ZERO, expiration)) {
            return cache.setIfExists(value);
        }
        DurationWrapper durationWrapper = TimeUtil.toDurationWrapper(expiration);
        return cache.setIfExists(value, durationWrapper.getTime(), durationWrapper.getTimeUnit());
    }

    @Override
    public <V> boolean setIfAbsent(String key, V value, Duration expiration) {
        RBucket<V> cache = RedisManager.getClient().getBucket(key);
        if (Objects.equals(Duration.ZERO, expiration)) {
            return cache.setIfAbsent(value);
        }
        return cache.setIfAbsent(value, expiration);
    }

    @Override
    public <V> boolean compareAndSet(String key, V expectedValue, V updateValue) {
        return RedisManager.getClient().getBucket(key).compareAndSet(expectedValue, updateValue);
    }

    @Override
    public Object get(String key) {
        return RedisManager.getClient().getBucket(key).get();
    }

    @Override
    public <V> List<V> get(String... keys) {
        return CollectionsUtil.toList(getMap(keys));
    }

    @Override
    public <V> List<V> get(Collection<String> keys) {
        return get(CollectionsUtil.toArray(keys, String.class));
    }

    @Override
    public <V> Map<String, V> getMap(String... keys) {
        return RedisManager.getClient().getBuckets().get(keys);
    }

    @Override
    public <V> Map<String, V> getMap(Collection<String> keys) {
        return getMap(CollectionsUtil.toArray(keys, String.class));
    }

    @Override
    public <V> Map<String, V> getMapByNamespace(String namespace) {
        return getMapByPattern(resolveFuzzyKey(namespace));
    }

    @Override
    public <V> Map<String, V> getMapByPattern(String pattern) {
        return getMap(CollectionsUtil.toArray(RedisManager.getClient().getKeys().getKeysByPattern(pattern), String.class));
    }

    @Override
    public void delete(String key) {
        RedisManager.getClient().getBucket(key).getAndDelete();
    }

    @SneakyThrows
    @Override
    public long delete(String... keys) {
        RBatch pipeline = RedisManager.getClient().createBatch();
        RFuture<Long> future = pipeline.getKeys().deleteAsync(keys);
        pipeline.execute();
        return future.get();
    }

    @Override
    public long delete(Collection<String> keys) {
        return delete(CollectionsUtil.toArray(keys, String.class));
    }

    @Override
    public long deleteAll(String namespace) {
        return deleteByNamespace(namespace);
    }

    @Override
    public long deleteByNamespace(String namespace) {
        return deleteByPattern(resolveFuzzyKey(namespace));
    }

    @SneakyThrows
    @Override
    public long deleteByPattern(String pattern) {
        RBatch pipeline = RedisManager.getClient().createBatch();
        RFuture<Long> future = pipeline.getKeys().deleteByPatternAsync(pattern);
        pipeline.execute();
        return future.get();
    }

    @Override
    public DurationWrapper getExpirationTime(String key) {
        return toDurationWrapper(RedisManager.getClient().getBucket(key).getExpireTime());
    }

    @Override
    public DurationWrapper getTimeToLive(String key) {
        return toDurationWrapper(RedisManager.getClient().getBucket(key).remainTimeToLive());
    }

    // TODO wjm 这两个函数应放到 RedisListCacheHandler 或 其他 Handler 中，包括 get list 的也是在那边实现
    // /**
    //  * 批量设置缓存
    //  *
    //  * @param values         操作集合
    //  * @param getKeyFunction 如何获取要缓存的 key 名，将操作集合中元素本身作为要缓存的对象
    //  * @param <T>            操作集合中元素的类型
    //  */
    // public <T> void set(List<T> values, Function<T, String> getKeyFunction) {
    //     RedissonManager.getClient().getBuckets().set(CollectionsUtil.toMap(values, getKeyFunction));
    // }
    //
    // /**
    //  * 批量设置缓存
    //  *
    //  * @param values           操作集合
    //  * @param getKeyFunction   如何获取要缓存的 key 名
    //  * @param getValueFunction 如何获取要缓存的对象
    //  * @param <T>              操作集合中元素的类型
    //  * @param <V>              要缓存对象的类型
    //  */
    // public <T, V> void set(List<T> values, Function<T, String> getKeyFunction, Function<T, V> getValueFunction) {
    //     RedissonManager.getClient().getBuckets().set(CollectionsUtil.toMap(values, getKeyFunction, getValueFunction));
    // }

    // cache.set(CACHE_NAME1, CACHE_OBJECT1);
    // boolean result21 = cache.compareAndSet(CACHE_NAME1, CACHE_OBJECT2, CACHE_OBJECT2);
    // boolean result22 = cache.compareAndSet(CACHE_NAME1, CACHE_OBJECT1, CACHE_OBJECT2);
    //
    // cache.deleteAll();

}
