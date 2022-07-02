package cn.sugar.convert.mapstruct.core.util;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapstruct 属性映射注解标记，标记了该注解的类可以作为 Mapstruct 中的转换方法
 *
 * @author wjm
 * @date 2021/3/11 10:25
 */
@Qualifier
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MapstructMappingQualify {
}