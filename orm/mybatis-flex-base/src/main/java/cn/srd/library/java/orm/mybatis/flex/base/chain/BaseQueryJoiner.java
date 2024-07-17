// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.mybatis.flex.base.chain;

import cn.srd.library.java.contract.model.base.POJO;
import com.mybatisflex.core.query.Joiner;

/**
 * @author wjm
 * @since 2023-12-05 16:38
 */
public abstract class BaseQueryJoiner<PJ extends POJO> {

    protected abstract Joiner<QueryChain<PJ>> getNativeQueryJoiner();

}