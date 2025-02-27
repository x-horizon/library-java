package org.horizon.sdk.library.java.orm.mybatis.flex.base.lock;

import com.mybatisflex.annotation.Column;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the global optimistic lock config
 *
 * @author wjm
 * @since 2023-11-12 21:06
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface OptimisticLockConfig {

    /**
     * <pre>
     * the optimistic lock column name.
     * it can replace {@link Column#version()} marked on the field, like:
     *
     * {@code
     *     @Column(value = "version", version = true)
     *     private Long version;
     * }
     *
     * above code is equivalent to set this field value to "version".
     * </pre>
     *
     * @return the optimistic lock column name.
     */
    String columnName() default "";

}