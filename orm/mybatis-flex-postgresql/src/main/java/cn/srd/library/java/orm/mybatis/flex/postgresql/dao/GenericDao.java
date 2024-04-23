// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.mybatis.flex.postgresql.dao;

import cn.srd.library.java.contract.constant.text.SuppressWarningConstant;
import cn.srd.library.java.orm.contract.model.base.PO;
import cn.srd.library.java.orm.contract.model.base.POJO;
import cn.srd.library.java.orm.mybatis.flex.base.adapter.BaseMapperAdapter;
import cn.srd.library.java.orm.mybatis.flex.postgresql.chain.JsonbQueryChainer;
import cn.srd.library.java.orm.mybatis.flex.postgresql.chain.QueryChainer;
import com.mybatisflex.core.BaseMapper;

/**
 * the generic dao
 *
 * @param <P> the entity extends {@link PO}
 * @author wjm
 * @since 2024-04-18 21:54
 */
public interface GenericDao<P extends PO> extends cn.srd.library.java.orm.mybatis.flex.base.dao.GenericDao<P> {

    // @Override
    // default QueryChainer<P> openQuery() {
    //     return QueryChainer.of(getBaseMapper());
    // }

    default <Q extends POJO> JsonbQueryChainer<Q, P> openJsonbQuery() {
        return JsonbQueryChainer.of(QueryChainer.of(getBaseMapper()));
    }

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    private BaseMapper<P> getBaseMapper() {
        return (BaseMapper<P>) BaseMapperAdapter.getInstance().getBaseMapper(this.getClass());
    }

}