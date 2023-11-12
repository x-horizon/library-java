// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.mybatis.flex.base.id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * the uncontrolled type id generate strategy
 *
 * @author wjm
 * @see IdConfig
 * @since 2023-11-12 21:06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerateByUncontrolledStrategy implements IdGenerateStrategy {

    protected static final IdGenerateByUncontrolledStrategy INSTANCE = new IdGenerateByUncontrolledStrategy();

}
