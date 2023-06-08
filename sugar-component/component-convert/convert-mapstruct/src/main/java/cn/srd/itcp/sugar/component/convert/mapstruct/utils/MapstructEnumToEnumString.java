package cn.srd.itcp.sugar.component.convert.mapstruct.utils;

import org.mapstruct.Qualifier;

import java.lang.annotation.*;

/**
 * Mapstruct 属性映射转换器注解，Enum =&gt; Enum 的字符串字段值
 *
 * @author wjm
 * @since 2021/3/11 10:25
 */
@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface MapstructEnumToEnumString {
}