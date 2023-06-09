package cn.srd.itcp.sugar.framework.hsweb.curd.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * All in one 转换器
 *
 * @author wjm
 * @since 2022/6/18 19:17
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Converts extends cn.srd.itcp.sugar.component.convert.all.core.Converts {

    /**
     * 应用 支持 HsWeb 实体的 Mapstruct 转换器
     *
     * @return 支持 HsWeb 实体的 Mapstruct 转换器
     */
    @Deprecated
    public static MapstructHsWebConverts withHsWebMapstruct() {
        return MapstructHsWebConverts.getInstance();
    }

}

