package cn.srd.itcp.sugar.cache.all.support.strategy;

import cn.srd.itcp.sugar.cache.all.support.manager.CacheDataManager;

import java.util.Map;

/**
 * the cache mode strategy
 *
 * @author wjm
 * @since 2023-06-12 20:49:21
 */
public interface CacheModeStrategy {

    /**
     * get cache value by key
     *
     * @param dataManager            see {@link CacheDataManager}
     * @param namespace              the cache namespace
     * @param key                    the cache key
     * @param findCacheTypeNameIndex current find cache type name index
     * @return cache value
     */
    Object get(CacheDataManager dataManager, String namespace, String key, int findCacheTypeNameIndex);

    /**
     * get cache value by namespace
     *
     * @param dataManager            see {@link CacheDataManager}
     * @param namespace              the cache namespace
     * @param findCacheTypeNameIndex current find cache type name index
     * @param <V>                    the cache value type
     * @return cache value
     */
    <V> Map<String, V> getMapByNamespace(CacheDataManager dataManager, String namespace, int findCacheTypeNameIndex);

}


