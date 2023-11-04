// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.spring.webflux;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * toolkit for spring web flux
 *
 * @author wjm
 * @since 2023-02-04 17:49
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringsWebFluxs {

    /**
     * get {@link ServerHttpRequest}
     *
     * @return {@link ServerHttpRequest}
     */
    public static Mono<ServerHttpRequest> getServerHttpRequest() {
        return Mono.deferContextual(Mono::just).map(context -> context.get(ReactiveRequestContextFilter.CONTEXT_KEY));
    }

}
