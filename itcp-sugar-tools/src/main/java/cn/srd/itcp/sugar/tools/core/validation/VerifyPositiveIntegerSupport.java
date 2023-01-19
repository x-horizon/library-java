package cn.srd.itcp.sugar.tools.core.validation;

import cn.srd.itcp.sugar.tools.core.Objects;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Integer 类型数字的正整数校验注解（必须大于 0）
 *
 * @author wjm
 * @date 2020/12/08 13:49
 */
public class VerifyPositiveIntegerSupport implements ConstraintValidator<VerifyPositiveInteger, Integer> {

    @Override
    public void initialize(VerifyPositiveInteger verifyPositiveLong) {

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return !Objects.isNotPositive(value);
    }

}
