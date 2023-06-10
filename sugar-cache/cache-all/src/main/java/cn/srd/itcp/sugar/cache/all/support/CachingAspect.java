package cn.srd.itcp.sugar.cache.all.support;

import cn.srd.itcp.sugar.cache.all.core.CacheEvict;
import cn.srd.itcp.sugar.cache.all.core.CacheWrite;
import cn.srd.itcp.sugar.cache.all.core.Caching;
import cn.srd.itcp.sugar.framework.spring.tool.common.core.NullValueUtil;
import cn.srd.itcp.sugar.tool.core.CollectionsUtil;
import cn.srd.itcp.sugar.tool.core.Objects;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * @author wjm
 * @since 2023-06-09 15:06:14
 */
@Aspect
public class CachingAspect implements CacheAspect {

    @Pointcut("@annotation(cn.srd.itcp.sugar.cache.all.core.Caching)")
    public void pointcut() {
    }

    @SneakyThrows
    @Around("pointcut()")
    public Object aroundPointcut(ProceedingJoinPoint joinPoint) {
        Caching cachingAnnotation = getAnnotationMarkedOnMethod(joinPoint, Caching.class);
        // handle read
        Object value = null;
        List<CacheAspectContext> readContexts = CollectionsUtil.toList(cachingAnnotation.read(), annotation -> buildContext(joinPoint, annotation.namespaces(), annotation.cacheTypes(), annotation.key(), annotation.enablePreventCachePenetrate()));
        for (CacheAspectContext context : readContexts) {
            value = getCacheValue(context);
        }
        if (Objects.isNull(value)) {
            value = doProceed(joinPoint);
        }
        for (CacheAspectContext context : readContexts) {
            setCacheValue(context.setValue(value));
        }
        // handle write
        Object finalValue = value;
        for (CacheWrite cacheWriteAnnotation : cachingAnnotation.write()) {
            CacheAspectContext context = buildContext(joinPoint, cacheWriteAnnotation.namespaces(), cacheWriteAnnotation.cacheTypes(), cacheWriteAnnotation.key(), cacheWriteAnnotation.enablePreventCachePenetrate());
            doWrite(joinPoint, context, useless -> finalValue);
        }
        // handle evict
        for (CacheEvict cacheEvictAnnotation : cachingAnnotation.evict()) {
            CacheAspectContext context = buildContext(joinPoint, cacheEvictAnnotation.namespaces(), cacheEvictAnnotation.cacheTypes(), cacheEvictAnnotation.key(), cacheEvictAnnotation.enablePreventCachePenetrate());
            doEvict(joinPoint, context, useless -> finalValue);
        }
        return NullValueUtil.convertToNullIfNullValue(value);
    }

}
