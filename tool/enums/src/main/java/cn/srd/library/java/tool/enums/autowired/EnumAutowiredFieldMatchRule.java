// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.enums.autowired;

import java.util.List;

/**
 * see {@link EnumAutowired#matchRule()}
 *
 * @author wjm
 * @since 2021-09-08 16:07
 */
public interface EnumAutowiredFieldMatchRule {

    /**
     * get the most suitable subclass simple name implemented by {@link EnumAutowired#rootClass()} to autowired the subclass instance in spring ioc
     *
     * @param enumField                  the enum marked with {@link EnumAutowired} field
     * @param enumAutowiredSubclassNames all subclass simple names implement by {@link EnumAutowired#rootClass()}
     * @param <E>                        the enum marked with {@link EnumAutowired} type
     * @return the most suitable subclass simple name
     */
    <E extends Enum<E>> String getMostSuitableAutowiredClassName(Enum<E> enumField, List<String> enumAutowiredSubclassNames);

}