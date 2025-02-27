package org.horizon.sdk.library.java.tool.enums;

import org.horizon.sdk.library.java.tool.enums.autoconfigure.EnableEnumAutowired;
import org.horizon.sdk.library.java.tool.enums.strategy.EnumAutowiredCollector;
import org.horizon.sdk.library.java.tool.enums.strategy.EnumAutowiredFieldMatchByMostSimilarRule;
import org.horizon.sdk.library.java.tool.enums.strategy.EnumAutowiredFieldMatchRule;
import org.horizon.sdk.library.java.tool.lang.text.Strings;
import org.horizon.sdk.library.java.tool.spring.contract.support.Classes;
import org.springframework.core.type.filter.TypeFilter;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 * autowired spring bean to enum field.
 *
 * example code:
 * {@code
 *      // define an root interface
 *      public interface GenderStrategy {}
 *
 *      // a spring ioc subclass bean implement by root interface
 *      @Component
 *      public class GenderManStrategy implements GenderStrategy {}
 *
 *      // a spring ioc subclass bean implement by root interface
 *      @Component
 *      public class GenderWomanStrategy implements GenderStrategy {}
 *
 *      // a spring ioc subclass bean implement by root interface
 *      @Component
 *      public class GenderUnknownStrategy implements GenderStrategy {}
 *
 *      // define an enum and set the root interface
 *      @Getter
 *      @EnumAutowired(rootClass = GenderStrategy.class)
 *      public enum GenderType {
 *
 *          WOMAN(1, "woman"),
 *          MAN(2, "man"),
 *          UNKNOWN(3, "unknown"),
 *
 *          ;
 *
 *          GenderType(int code, String description) {
 *              this.code = code;
 *              this.description = description;
 *          }
 *
 *          private final int code;
 *          private final String description;
 *          private GenderStrategy strategy;
 *
 *      }
 *
 *      // define the spring boot main function
 *      @EnableEnumAutowired
 *      @SpringBootApplication
 *      public class Test {
 *          public static void main(String[] args) {
 *              SpringApplication.run(Test.class, args);
 *          }
 *      }
 * }
 * </pre>
 *
 * <pre>
 * when run the main function, it will happen:
 * 1. merge the main function root package path and the package paths define in {@link EnableEnumAutowired#scanPackagePaths()}.
 * 2. scan all package paths to find all enum classes marked with {@link EnumAutowired}, such as [GenderType] marked with @{@link EnumAutowired}(rootClass = GenderStrategy.class).
 * 3. set {@link EnumAutowired#autowiredFiledName()} to [strategy] because it was not explicitly specified and there is only one data type [GenderStrategy] field the same as {@link EnumAutowired#rootClasses()},
 *    if the enum marked with {@link EnumAutowired} has multiple data type fields are same as {@link EnumAutowired#rootClasses()}, you must specified {@link EnumAutowired#autowiredFiledName()},
 *    otherwise will not be able to find the suitable field to autowired.
 * 4. scan the subclass simple names of {@link EnumAutowired#rootClasses()}, such as ["GenderManStrategy", "GenderWomanStrategy", "GenderUnknownStrategy"].
 * 5. compare the enum field name and the subclass simple name to get the most similar to the enum field name, such as enum field name ["WOMAN"] and the subclass simple name ["GenderWomanStrategy"].
 * 6. autowired [GenderWomanStrategy] instance in spring ioc to the enum field [WOMAN].
 * </pre>
 *
 * @author wjm
 * @see Classes#scanByTypeFilter(TypeFilter, String...)
 * @see Strings#getMostSimilar(String, Collection)
 * @see EnumAutowiredCollector
 * @see EnumAutowiredFieldMatchByMostSimilarRule#getMostSuitableAutowiredClassSimpleName(Enum, List)
 * @since 2021-09-08 16:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumAutowired {

    /**
     * the root interface to autowired into enum field
     *
     * @return the root interface to autowired into enum field
     */
    Class<?>[] rootClasses();

    /**
     * <pre>
     * the specified field name to autowired,
     * it will find field name with the same data type as {@link #rootClasses()} in the enum marked with {@link EnumAutowired} if it is blank.
     * </pre>
     *
     * @return the specified field name to autowired
     */
    String autowiredFiledName() default "";

    /**
     * if set it to true, will never autowired the instance to spring ioc when the {@link #matchRule()} cannot find the suitable class.
     *
     * @return the specified field name to autowired
     */
    boolean allowNull() default false;

    /**
     * the rule to match the subclass name of {@link #rootClasses()} and the field name in the enum marked with {@link EnumAutowired}
     *
     * @return the specified rule
     */
    Class<? extends EnumAutowiredFieldMatchRule> matchRule() default EnumAutowiredFieldMatchByMostSimilarRule.class;

}