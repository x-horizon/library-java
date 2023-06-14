package cn.srd.itcp.sugar.orm.mybatis.plus.common.utils;

import cn.srd.itcp.sugar.component.convert.mapstruct.core.MapstructConverts;
import cn.srd.itcp.sugar.component.convert.mapstruct.support.MapstructConvertsSupporterManager;
import cn.srd.itcp.sugar.doc.knife4j.model.PageResult;
import cn.srd.itcp.sugar.tool.core.object.Objects;
import cn.srd.itcp.sugar.tool.core.validation.Nullable;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 在 All in one 转换器的基础上增加对 {@link IPage} =&gt; {@link PageResult} 的转换支持
 *
 * @author wjm
 * @since 2021/5/1 14:13
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Deprecated
public class MapstructMybatisPlusConverts {

    /**
     * singleton pattern
     */
    private static final class SingleTonHolder {
        private static final MapstructMybatisPlusConverts INSTANCE = new MapstructMybatisPlusConverts();
    }

    /**
     * get singleton instance
     *
     * @return instance
     */
    public static MapstructMybatisPlusConverts getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    static {
        // 添加 Mapstruct MybatisPlus 转换器支持
        MapstructConvertsSupporterManager.addConvertSupporter(object -> {
            if (IPage.class.isAssignableFrom(object.getClass())) {
                return MapstructMybatisPlusConvertsSupporter.INSTANCE;
            }
            return null;
        });
    }

    /**
     * 是否静默转换，即报错不抛出异常，只打印日志，默认不静默转换
     */
    private static final boolean DEFAULT_CONVERT_QUIETLY = Converts.DEFAULT_CONVERT_QUIETLY;

    /**
     * IPage Bean 一对一转换
     *
     * @param source 待转换的 IPage
     * @param target 目标 PageResult
     * @param <T1>   待转换的 IPage Bean 类型
     * @param <T2>   目标 PageResult 类型
     * @return 目标 PageResult
     */
    @NonNull
    public <T1, T2> PageResult<T2> toPageBean(@Nullable IPage<T1> source, @Nullable Class<T2> target) {
        return toPageBean(source, target, null, DEFAULT_CONVERT_QUIETLY);
    }

    /**
     * IPage Bean 一对一转换
     *
     * @param source       待转换的 IPage
     * @param target       目标 PageResult
     * @param defaultValue 所有异常情况时的默认目标值
     * @param <T1>         待转换的 IPage Bean 类型
     * @param <T2>         目标 PageResult 类型
     * @return 目标 PageResult
     */
    @NonNull
    public <T1, T2> PageResult<T2> toPageBean(@Nullable IPage<T1> source, @Nullable Class<T2> target, @Nullable T2 defaultValue) {
        return toPageBean(source, target, defaultValue, DEFAULT_CONVERT_QUIETLY);
    }

    /**
     * IPage Bean 一对一转换
     *
     * @param source  待转换的 IPage
     * @param target  目标 PageResult
     * @param quietly 是否静默转换，即报错不抛出异常，只打印日志
     * @param <T1>    待转换的 IPage Bean 类型
     * @param <T2>    目标 PageResult 类型
     * @return 目标 PageResult
     */
    @NonNull
    public <T1, T2> PageResult<T2> toPageBean(@Nullable IPage<T1> source, @Nullable Class<T2> target, @Nullable Boolean quietly) {
        return toPageBean(source, target, null, quietly);
    }

    /**
     * IPage Bean 一对一转换
     *
     * @param source       待转换的 IPage
     * @param target       目标 PageResult
     * @param defaultValue 所有异常情况时的默认目标值
     * @param quietly      是否静默转换，即报错不抛出异常，只打印日志
     * @param <T1>         待转换的 IPage Bean 类型
     * @param <T2>         目标 PageResult 类型
     * @return 目标 PageResult
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public <T1, T2> PageResult<T2> toPageBean(@Nullable IPage<T1> source, @Nullable Class<T2> target, @Nullable T2 defaultValue, @Nullable Boolean quietly) {
        if (Objects.isNull(source, target) || Objects.isEmpty(source.getRecords())) {
            return (PageResult<T2>) MapstructMybatisPlusConvertsSupporter.INSTANCE.getDefaultValue(defaultValue);
        }
        return (PageResult<T2>) MapstructConverts.getInstance().convert(target, defaultValue, quietly, source);
    }

}
