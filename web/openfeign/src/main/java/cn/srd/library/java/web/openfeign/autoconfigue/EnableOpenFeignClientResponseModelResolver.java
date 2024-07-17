// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.web.openfeign.autoconfigue;

import cn.srd.library.java.contract.constant.suppress.SuppressWarningConstant;
import cn.srd.library.java.contract.model.protocol.TransportModel;
import cn.srd.library.java.web.openfeign.interceptor.OpenFeignClientResponseInterceptor;

import java.lang.annotation.*;

/**
 * enable {@link OpenFeignClientResponseInterceptor}
 *
 * @author wjm
 * @since 2023-03-04 16:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EnableOpenFeignClientResponseModelResolver {

    /**
     * the response models to resolve
     *
     * @return the response models to resolve
     */
    @SuppressWarnings(SuppressWarningConstant.RAW_TYPE)
    Class<? extends TransportModel>[] value();

}