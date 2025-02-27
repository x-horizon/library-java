package org.horizon.sdk.library.java.tool.spring.webmvc.autoconfigure;

import org.horizon.sdk.library.java.tool.spring.webmvc.advice.WebMvcResponseBodyAdvice;
import org.horizon.sdk.library.java.tool.spring.webmvc.interceptor.WebMvcExceptionInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * {@link EnableAutoConfiguration AutoConfiguration} for Library Java Tool Spring WebMVC
 *
 * @author wjm
 * @since 2023-11-09 21:01
 */
@AutoConfiguration
public class WebMvcAutoConfigurer {

    @Bean
    @ConditionalOnBean(WebMvcResponseBodyAdviceRegistrar.class)
    public WebMvcResponseBodyAdvice webMVCResponseBodyAdvice() {
        return new WebMvcResponseBodyAdvice();
    }

    @Bean
    @ConditionalOnBean(WebMvcExceptionInterceptorRegistrar.class)
    public WebMvcExceptionInterceptor webMVCExceptionInterceptor() {
        return new WebMvcExceptionInterceptor();
    }

}