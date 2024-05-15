package cn.srd.library.java.cache.redis;

import cn.srd.library.java.tool.lang.time.DurationWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;

/**
 * Redis 缓存操作（哈希）
 *
 * @author wjm
 * @since 2023-01-12 10:37
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisCacheHash implements RedisCacheTemplate {

    /**
     * singleton pattern
     */
    protected static final RedisCacheHash INSTANCE = new RedisCacheHash();

    @Override
    public Object get(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> Map<String, V> getMapByNamespace(String namespace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long deleteAll(String namespace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> boolean compareAndSet(String key, V expectedValue, V updateValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> Map<String, V> getMapByPattern(String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long deleteByNamespace(String namespace) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long deleteByPattern(String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> void set(String key, V value, Duration expiration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> boolean setIfExists(String key, V value, Duration expiration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> boolean setIfAbsent(String key, V value, Duration expiration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DurationWrapper getExpirationTime(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DurationWrapper getTimeToLive(String key) {
        throw new UnsupportedOperationException();
    }

}