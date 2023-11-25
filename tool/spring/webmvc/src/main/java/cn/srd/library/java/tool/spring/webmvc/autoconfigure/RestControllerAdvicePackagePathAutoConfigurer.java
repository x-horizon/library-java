// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.spring.webmvc.autoconfigure;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.constant.spring.SpringInitializeConstant;
import cn.srd.library.java.contract.constant.spring.SpringWebMVCConstant;
import cn.srd.library.java.tool.lang.annotation.Annotations;
import cn.srd.library.java.tool.lang.collection.Collections;
import cn.srd.library.java.tool.lang.object.Nil;
import cn.srd.library.java.tool.lang.reflect.Reflects;
import cn.srd.library.java.tool.spring.contract.Classes;
import cn.srd.library.java.tool.spring.webmvc.advice.EnableWebMVCResponseBodyAdvice;
import cn.srd.library.java.tool.spring.webmvc.advice.WebMVCResponseBodyAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

/**
 * @author wjm
 * @since 2023-10-07 15:23
 */
@Slf4j
@Order(SpringInitializeConstant.HIGH_INITIALIZE_PRIORITY)
public class RestControllerAdvicePackagePathAutoConfigurer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        log.debug("{}rest controller advice package path starting optimizing...", ModuleView.TOOL_SPRING_WEBMVC_SYSTEM);

        EnableWebMVCResponseBodyAdvice webMVCResponseBodyAdvice = Annotations.getAnnotation(EnableWebMVCResponseBodyAdvice.class);
        if (Nil.isNotNull(webMVCResponseBodyAdvice)) {
            Set<String> advicePackagePaths = Classes.parseAntStylePackagePathsToPackagePaths(webMVCResponseBodyAdvice.advicePackagePaths());
            if (Nil.isNotEmpty(advicePackagePaths)) {
                RestControllerAdvice restControllerAdvice = Annotations.getAnnotation(WebMVCResponseBodyAdvice.class, RestControllerAdvice.class);
                String[] beforeReplaceBasePackagePaths = restControllerAdvice.basePackages();
                Reflects.setAnnotationValue(restControllerAdvice, SpringWebMVCConstant.FIELD_NAME_BASE_PACKAGE_ON_ANNOTATION_REST_CONTROLLER_ADVICE, Collections.toArray(advicePackagePaths, String[]::new));
                log.debug("{}replace the annotation [@{}] field [{}] value on class [@{}], before replace value {}, after replace value {}.",
                        ModuleView.TOOL_SPRING_WEBMVC_SYSTEM,
                        RestControllerAdvice.class.getSimpleName(),
                        SpringWebMVCConstant.FIELD_NAME_BASE_PACKAGE_ON_ANNOTATION_REST_CONTROLLER_ADVICE,
                        WebMVCResponseBodyAdvice.class.getSimpleName(),
                        beforeReplaceBasePackagePaths,
                        advicePackagePaths
                );
            }
        }

        log.debug("{}rest controller advice package path has been optimized.", ModuleView.TOOL_SPRING_WEBMVC_SYSTEM);
    }

}
