// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.web.grpc.client.autoconfigure;

import cn.srd.library.java.web.grpc.client.interceptor.GrpcClientWebMvcExceptionInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * {@link EnableAutoConfiguration AutoConfiguration} for Library Java Web Grpc Client
 *
 * @author wjm
 * @since 2024-06-14 16:28
 */
@AutoConfiguration
public class GrpcClientAutoConfigurer {

    @Bean
    @ConditionalOnBean(GrpcClientWebMvcExceptionInterceptorRegistrar.class)
    public GrpcClientWebMvcExceptionInterceptor grpcClientWebMvcExceptionInterceptor() {
        return new GrpcClientWebMvcExceptionInterceptor();
    }

}