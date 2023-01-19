package cn.srd.itcp.sugar.tools.core.validation;

import cn.srd.itcp.sugar.tools.core.StringsUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 用于验证字符串类型的字段不包含中文字符
 *
 * @author
 * @see RequireNotContainChinese
 * @since
 */
public class RequireNotContainChineseSupport implements ConstraintValidator<RequireNotContainChinese, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !StringsUtil.isChineseIncluded(value);
    }

}
