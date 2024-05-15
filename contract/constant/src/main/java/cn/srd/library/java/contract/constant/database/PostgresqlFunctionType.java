// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.contract.constant.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * the function in database postgresql
 *
 * @author wjm
 * @since 2024-04-26 15:03
 */
@Getter
@AllArgsConstructor
public enum PostgresqlFunctionType {

    EXIST("EXIST"),

    JSONB_EXTRACT_PATH("JSONB_EXTRACT_PATH"),
    JSONB_ARRAY_ELEMENTS("JSONB_ARRAY_ELEMENTS"),

    ;

    private final String value;

}