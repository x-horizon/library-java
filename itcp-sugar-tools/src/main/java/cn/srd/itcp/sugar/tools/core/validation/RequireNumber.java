package cn.srd.itcp.sugar.tools.core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 标记在字段上用于验证数据类型必须为数字类型
 *
 * @author wjm
 * @see RequireNumberSupport
 * @since 2022-07-20 11:18:25
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequireNumberSupport.class)
@Documented
public @interface RequireNumber {

    /**
     * 校验失败时的信息
     *
     * @return
     */
    String message() default "number is required";

    /**
     * 要校验的分组
     *
     * @return
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
