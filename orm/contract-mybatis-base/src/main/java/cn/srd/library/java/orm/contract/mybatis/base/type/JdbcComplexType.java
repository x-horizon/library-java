// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.contract.mybatis.base.type;

import cn.srd.library.java.orm.contract.mybatis.base.cache.ColumnMappingJavaTypeCache;
import cn.srd.library.java.tool.enums.EnumAutowired;
import lombok.Getter;

/**
 * the jdbc complex type
 *
 * @author wjm
 * @since 2023-11-08 17:07
 */
@Getter
@EnumAutowired(rootClasses = ColumnMappingJavaTypeCache.class)
public enum JdbcComplexType {

    JSON,

    ;

    /**
     * the complex database column data type and java type mapping cache
     */
    private ColumnMappingJavaTypeCache columnMappingJavaTypeCache;

}