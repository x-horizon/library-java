package cn.srd.itcp.sugar.tool.core;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.srd.itcp.sugar.tool.core.object.Objects;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 枚举工具
 *
 * @author wjm
 * @since 2021/5/10 17:46
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumsUtil extends EnumUtil {

    /**
     * see {@link EnumUtil#isEnum(Class)} ，去除非空校验
     *
     * @param clazz 指定类
     * @return 是否为 Enum 类
     */
    public static boolean isEnum(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return false;
        }
        return clazz.isEnum();
    }

    /**
     * 指定类是否不为 Enum 类
     *
     * @param clazz 指定类
     * @return 是否不为 Enum 类
     */
    public static boolean isNotEnum(Class<?> clazz) {
        return !isEnum(clazz);
    }

    /**
     * see {@link EnumUtil#isEnum(Object)}，去除非空校验
     *
     * @param obj 指定对象
     * @return 是否为 Enum 类
     */
    public static boolean isEnum(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        return obj.getClass().isEnum();
    }

    /**
     * 指定对象是否不为 Enum 类
     *
     * @param obj 指定对象
     * @return 是否不为 Enum 类
     */
    public static boolean isNotEnum(Object obj) {
        return !isEnum(obj);
    }

    /**
     * 获取 Enum 中 T 类型的属性值，仅支持一种 T 类型的字段；其余情况返回 null
     *
     * @param eEnum 输入枚举
     * @param clazz 待获取的属性类型
     * @param <T>   枚举类类型
     * @return 获取的属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getEnumValue(Enum<?> eEnum, Class<T> clazz) {
        if (Objects.isNull(eEnum, clazz)) {
            return null;
        }

        Class<?> eEnumClass = eEnum.getClass();

        final Field[] fields = ReflectUtil.getFields(eEnumClass);
        final Enum<?>[] enums = (Enum<?>[]) eEnumClass.getEnumConstants();
        String fieldName;
        Class<?> fieldType;
        for (Field field : fields) {
            fieldName = field.getName();
            fieldType = field.getType();

            // 跳过特殊字段、name、enum 类型的字段；只寻找 Enum 类中为对应类型且除特殊字段外的字段
            if (isEnum(fieldType) || Objects.equals("name", fieldName) || isEnumSpecialField(field) || !ClassesUtil.isAssignable(fieldType, clazz)) {
                continue;
            }
            for (Enum<?> enumObj : enums) {
                if (Objects.equals(eEnum, enumObj)) {
                    return (T) ReflectUtil.getFieldValue(enumObj, field);
                }
            }
        }

        return null;
    }

    /**
     * 根据枚举类获取枚举值
     *
     * @param enumClass 输入枚举类
     * @param <E>       输入枚举类类型
     * @return 获取到的枚举集合
     */
    @NonNull
    public static <E extends Enum<E>> List<E> getEnumValues(@NonNull Class<E> enumClass) {
        Objects.requireNotNull(enumClass);
        E[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return new ArrayList<>();
        }
        return CollectionsUtil.newArrayList(enumConstants);
    }

    /**
     * 转换 Enum，不匹配时返回 null<br>
     * 如枚举字段为 SUNDAY，传入 "SUNDAY" 可获取对应的枚举
     *
     * @param enumValue 枚举值
     * @param enumClass 枚举类
     * @param <E>       枚举类类型
     * @return 转换后的枚举
     */
    public static <E extends Enum<E>> E toEnum(String enumValue, Class<E> enumClass) {
        if (Objects.isNull(enumClass, enumValue)) {
            return null;
        }
        return Try.of(() -> Enum.valueOf(enumClass, enumValue)).getOrNull();
    }

    /**
     * 转换 Enum，不匹配时返回 null<br>
     * 如枚举字段为 SUNDAY(1，"星期一")，传入 "星期一"/1 可获取对应的枚举；如果多个枚举都有一样的值，则返回匹配到的第一个枚举
     *
     * @param enumFiledValue 枚举字段值
     * @param enumClass      枚举类
     * @param <E>            枚举类类型
     * @return 转换后的枚举
     */
    public static <E extends Enum<E>> E capableToEnum(Object enumFiledValue, Class<E> enumClass) {
        if (Objects.isNull(enumClass, enumFiledValue)) {
            return null;
        }

        Object currentEnumFiledValue = enumFiledValue;
        if (enumFiledValue instanceof CharSequence) {
            currentEnumFiledValue = enumFiledValue.toString().trim();
        }

        final Field[] fields = ReflectUtil.getFields(enumClass);
        final E[] enums = enumClass.getEnumConstants();
        for (Field field : fields) {
            // 跳过特殊字段、enum 类型的字段；
            if (field.getType().isEnum() || isEnumSpecialField(field)) {
                continue;
            }
            for (E enumObj : enums) {
                Object fieldValue = ReflectsUtil.getFieldValueIgnoreThrowable(enumObj, field);
                if (Objects.equals(fieldValue, currentEnumFiledValue)) {
                    return enumObj;
                }
                // 用于适应枚举里的值为可变参数
                if (ArraysUtil.isArray(fieldValue) && ArraysUtil.contains((Object[]) fieldValue, currentEnumFiledValue)) {
                    return enumObj;
                }
            }
        }
        return null;
    }

    /**
     * 是否为 Enum 的特殊字段： ENUM$VALUES、ordinal
     *
     * @param field 字段对象
     * @return 是否为 Enum 的特殊字段
     */
    public static boolean isEnumSpecialField(Field field) {
        return "ENUM$VALUES".equals(field.getName()) || "ordinal".equals(field.getName());
    }

}