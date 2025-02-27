package org.horizon.sdk.library.java.tool.convert.mapstruct.plus.support;

import org.mapstruct.Qualifier;

import java.lang.annotation.*;

/**
 * Mapstruct 属性映射转换器注解，LocalDateTime =&gt; Long
 *
 * @author wjm
 * @since 2022-07-18 09:59
 */
@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface MapstructLocalDateTimeToLong {

}