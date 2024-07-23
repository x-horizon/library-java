// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.convert.jackson.deserializer;

import cn.srd.library.java.tool.lang.time.Times;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalTime;

/**
 * the jackson deserializer to convert {@link String} to {@link LocalTime}
 *
 * @author wjm
 * @since 2023-06-21 12:01
 */
public class JacksonStringToLocalTimeDeserializer extends StdConverter<String, LocalTime> {

    @Override
    public LocalTime convert(String from) {
        return Times.toLocalTime(from);
    }

}