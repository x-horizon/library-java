// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.convert.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * convert toolkit for jackson
 *
 * @author wjm
 * @since 2023-06-17 09:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Jacksons {

    /**
     * new {@link TypeReference}
     *
     * @param <T> the {@link TypeReference} type
     * @return {@link TypeReference}
     */
    public static <T> TypeReference<T> newTypeReference() {
        return new TypeReference<>() {
        };
    }

}