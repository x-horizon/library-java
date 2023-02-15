package cn.srd.itcp.sugar.framework.spring.tool.webmvc.core.web;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用 {@link WebMVCExceptionHandler} 的功能
 *
 * @author wjm
 * @since 2022-07-16 18:16:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(WebMVCExceptionHandler.class)
public @interface EnableWebMVCExceptionHandler {

}
