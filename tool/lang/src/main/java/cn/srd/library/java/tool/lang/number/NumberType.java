// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.lang.number;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * number type
 *
 * @author wjm
 * @since 2023-09-21 21:36
 */
@Getter
@AllArgsConstructor
public enum NumberType {

    BYTE(1, ByteHandler.INSTANCE),
    SHORT(2, ShortHandler.INSTANCE),
    INTEGER(3, IntegerHandler.INSTANCE),
    LONG(4, LongHandler.INSTANCE),
    FLOAT(5, FloatHandler.INSTANCE),
    DOUBLE(6, DoubleHandler.INSTANCE),

    ;

    private final int code;

    private final NumberHandler handler;

}