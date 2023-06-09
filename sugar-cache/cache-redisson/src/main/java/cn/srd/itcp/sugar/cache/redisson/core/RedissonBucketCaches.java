package cn.srd.itcp.sugar.cache.redisson.core;

import cn.srd.itcp.sugar.context.redisson.core.RedissonManager;
import cn.srd.itcp.sugar.tool.core.CollectionsUtil;
import cn.srd.itcp.sugar.tool.core.Objects;
import cn.srd.itcp.sugar.tool.core.time.DurationWrapper;
import cn.srd.itcp.sugar.tool.core.time.TimeUtil;
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
 * Redisson 缓存操作（桶）
 *
 * @author wjm
 * @since 2023-01-12 10:37:12
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedissonBucketCaches implements RedissonCacheTemplate {

    /**
     * singleton pattern
     */
    protected static final RedissonBucketCaches INSTANCE = new RedissonBucketCaches();

    @Override
    public <V> void set(String key, V value, Duration expiration) {
        RBucket<V> cache = RedissonManager.getClient().getBucket(key);
        if (Objects.equals(Duration.ZERO, expiration)) {
            cache.set(value);
        } else {
            DurationWrapper durationWrapper = TimeUtil.toDurationWrapper(expiration);
            cache.set(value, durationWrapper.getTime(), durationWrapper.getTimeUnit());
        }
    }

    @Override
    public <V> boolean setIfExists(String key, V value, Duration expiration) {
        RBucket<V> cache = RedissonManager.getClient().getBucket(key);
        if (Objects.equals(Duration.ZERO, expiration)) {
            return cache.setIfExists(value);
        }
        DurationWrapper durationWrapper = TimeUtil.toDurationWrapper(expiration);
        return cache.setIfExists(value, durationWrapper.getTime(), durationWrapper.getTimeUnit());
    }

    @Override
    public <V> boolean setIfAbsent(String key, V value, Duration expiration) {
        RBucket<V> cache = RedissonManager.getClient().getBucket(key);
        if (Objects.equals(Duration.ZERO, expiration)) {
            return cache.setIfAbsent(value);
        }
        return cache.setIfAbsent(value, expiration);
    }

    @Override
    public <V> boolean compareAndSet(String key, V expectedValue, V updateValue) {
        return RedissonManager.getClient().getBucket(key).compareAndSet(expectedValue, updateValue);
    }

    @Override
    public Object get(String key) {
        return RedissonManager.getClient().getBucket(key).get();
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
        return RedissonManager.getClient().getBuckets().get(keys);
    }

    @Override
    public <V> Map<String, V> getMap(Collection<String> keys) {
        return getMap(CollectionsUtil.toArray(keys, String.class));
    }

    @Override
    public <V> List<V> getByNamespace(String namespace) {
        return getByPattern(namespace + NAMESPACE_KEY_WORD);
    }

    @Override
    public <V> List<V> getByPattern(String pattern) {
        return get(CollectionsUtil.toArray(RedissonManager.getClient().getKeys().getKeysByPattern(pattern), String.class));
    }

    @Override
    public <V> List<V> getByNamespaceWithoutNullValue(String namespace) {
        return getByPatternWithoutNullValue(namespace + NAMESPACE_KEY_WORD);
    }

    @Override
    public <V> List<V> getByPatternWithoutNullValue(String pattern) {
        return getWithoutNullValue(CollectionsUtil.toArray(RedissonManager.getClient().getKeys().getKeysByPattern(pattern), String.class));
    }

    @Override
    public void delete(String key) {
        RedissonManager.getClient().getBucket(key).getAndDelete();
    }

    @SneakyThrows
    @Override
    public long delete(String... keys) {
        RBatch pipeline = RedissonManager.getClient().createBatch();
        RFuture<Long> future = pipeline.getKeys().deleteAsync(keys);
        pipeline.execute();
        return future.get();
    }

    @Override
    public long delete(Collection<String> keys) {
        return delete(CollectionsUtil.toArray(keys, String.class));
    }

    @Override
    public long deleteByNamespace(String namespace) {
        return deleteByPattern(namespace + NAMESPACE_KEY_WORD);
    }

    @SneakyThrows
    @Override
    public long deleteByPattern(String pattern) {
        RBatch pipeline = RedissonManager.getClient().createBatch();
        RFuture<Long> future = pipeline.getKeys().deleteByPatternAsync(pattern);
        pipeline.execute();
        return future.get();
    }

    @Override
    public DurationWrapper getExpirationTime(String key) {
        return toDurationWrapper(RedissonManager.getClient().getBucket(key).getExpireTime());
    }

    @Override
    public DurationWrapper getTimeToLive(String key) {
        return toDurationWrapper(RedissonManager.getClient().getBucket(key).remainTimeToLive());
    }

    // TODO wjm 这两个函数应放到 RedissonListCacheHandler 或 其他 Handler 中，包括 get list 的也是在那边实现
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
