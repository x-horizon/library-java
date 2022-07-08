package cn.srd.itcp.sugar.security.sa.token.support;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.srd.itcp.sugar.security.sa.token.utils.SaTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * Sa-Token 配置
 *
 * @author wjm
 * @date 2022-07-07
 */
public class SaConfig implements WebMvcConfigurer {

    /**
     * 代表所有 URI 的路径匹配符
     */
    private static final String MATCH_ALL_ROUTE_PATTER = "/**";

    /**
     * 注册拦截器
     */
    @DependsOn("saAnonymousSupporter")
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 的路由拦截器，自定义验证规则
        registry.addInterceptor(new SaRouteInterceptor((request, response, handler) -> SaRouter
                // 获取所有 Endpoints
                .match(MATCH_ALL_ROUTE_PATTER)
                // 从所有 Endpoints 中排除掉不需要拦截的 Endpoints
                .notMatch(SaAnonymousSupporter.getSaAnonymousUrlsToExcludes())
                // 对未被排除的 Endpoints 使用自定义的方法进行检查
                .check(SaTokenUtil::checkLogin))
        ).addPathPatterns(MATCH_ALL_ROUTE_PATTER);
        // 注册 Sa-Token 的注解式拦截器，对所有 Endpoints 通过注解进行鉴权
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns(MATCH_ALL_ROUTE_PATTER);
    }

    @Bean
    public StpLogic stpLogic() {
        // 注入整合了 jwt 的 StpLogic
        return new StpLogicJwtForSimple();
    }

    @PostConstruct
    public void enhanceSaToken() {
        // 增加注解合并功能
        SaStrategy.me.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
    }

}
