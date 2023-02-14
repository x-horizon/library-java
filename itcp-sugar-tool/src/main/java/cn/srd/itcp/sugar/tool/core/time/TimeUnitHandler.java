package cn.srd.itcp.sugar.tool.core.time;

import cn.srd.itcp.sugar.tool.constant.TimeUnitPool;
import cn.srd.itcp.sugar.tool.core.StringsUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

/**
 * 时间单位处理器
 *
 * @author wjm
 * @since 2023-02-13 22:09:11
 */
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@Accessors(chain = true)
public abstract class TimeUnitHandler {

    /**
     * 定义如何将 时间字符串 转换为 时间单位，如 “2s” 如何转换为 “s”
     */
    protected static final List<Function<String, String>> CONVERT_TO_TIME_UNIT_FUNCTIONS = new ArrayList<>();

    /**
     * 时间单位与其对应处理器的映射
     */
    protected static final Map<TimeUnitPool, TimeUnitHandler> TIME_UNIT_MAPPING_HANDLER_MAP = new HashMap<>();

    /**
     * 时间，如 “2s” 中的 “2”
     */
    private Long time;

    /**
     * 时间单位，如 “2s” 中的 “s”
     */
    private String timeUnit;

    static {
        initConvertToTimeUnitFunction();
        establishStrategyMapping();
    }

    /**
     * 初始化 “如何将 时间字符串 转换为 时间单位” 的转换逻辑
     */
    private static void initConvertToTimeUnitFunction() {
        addConvertToTimeUnitFunctionWithRemoveAllDigit();
    }

    /**
     * 使用去除 时间字符串 中的 时间 作为转换逻辑
     */
    private static void addConvertToTimeUnitFunctionWithRemoveAllDigit() {
        addConvertToTimeUnitFunction(StringsUtil::removeAllDigit);
    }

    /**
     * 建立 时间单位 与其 对应处理器 的映射
     */
    private static void establishStrategyMapping() {
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.MILLISECOND, TimeUnitMillisecondHandler.INSTANCE);
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.SECOND, TimeUnitSecondHandler.INSTANCE);
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.MINUTE, TimeUnitMinuteHandler.INSTANCE);
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.HOUR, TimeUnitHourHandler.INSTANCE);
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.DAY, TimeUnitDayHandler.INSTANCE);
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.MONTH, TimeUnitMonthHandler.INSTANCE);
        TIME_UNIT_MAPPING_HANDLER_MAP.put(TimeUnitPool.YEAR, TimeUnitYearHandler.INSTANCE);
    }

    /**
     * 提供给外部添加 时间字符串 转换为 时间单位 的处理逻辑
     *
     * @param convertToTimeUnitFunctions 时间字符串 转换为 时间单位 的处理逻辑
     */
    @SafeVarargs
    public static void addConvertToTimeUnitFunction(Function<String, String>... convertToTimeUnitFunctions) {
        CONVERT_TO_TIME_UNIT_FUNCTIONS.addAll(Arrays.asList(convertToTimeUnitFunctions));
    }

    /**
     * 创建实例
     *
     * @return 实例
     */
    protected abstract TimeUnitHandler newInstance();

    /**
     * 转换为毫秒
     *
     * @return {@link Duration}
     */
    public abstract Duration toMillisecond();

    /**
     * 转换为秒
     *
     * @return {@link Duration}
     */
    public abstract Duration toSecond();

    /**
     * 转换为分钟
     *
     * @return {@link Duration}
     */
    public abstract Duration toMinute();

    /**
     * 转换为小时
     *
     * @return {@link Duration}
     */
    public abstract Duration toHour();

    /**
     * 转换为日
     *
     * @return {@link Duration}
     */
    public abstract Duration toDay();

}
