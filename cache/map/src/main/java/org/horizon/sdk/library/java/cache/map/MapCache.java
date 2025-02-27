package org.horizon.sdk.library.java.cache.map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map Cache Operation
 *
 * @param <K> cache key type
 * @author wjm
 * @since 2023-06-06 16:14
 */
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MapCache<K> implements MapCacheTemplate<K> {

    /**
     * default initial capacity
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 256;

    /**
     * combine {@link ConcurrentHashMap}
     */
    private ConcurrentHashMap<K, Object> cache;

    /**
     * get instance
     *
     * @param <K> cache key type
     * @return instance
     */
    public static <K> MapCache<K> newInstance() {
        return newInstance(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * get instance
     *
     * @param initialCapacity initial capacity
     * @param <K>             cache key type
     * @return instance
     */
    public static <K> MapCache<K> newInstance(int initialCapacity) {
        return MapCache.<K>builder().cache(new ConcurrentHashMap<>(initialCapacity)).build();
    }

    @Override
    public <V> void set(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public Object get(K key) {
        return cache.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> Map<K, V> getMapByNamespace(String namespace) {
        return (Map<K, V>) cache;
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
    }

    @Override
    public long deleteAll(String namespace) {
        return deleteAll();
    }

    @Override
    public long deleteAll() {
        cache.clear();
        // not implement affected number, ignore the return value
        return -1;
    }

}