package cn.srd.itcp.sugar.convert.mapstruct.utils;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mapstruct 属性映射转换器注解，null List => Empty ArrayList
 *
 * @author wjm
 * @date 2022-07-20 11:37:25
 */
@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MapstructNullListToEmptyArrayList {
}