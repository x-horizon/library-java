// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.mybatis.flex.base.listener;

import cn.srd.library.java.contract.constant.jvm.SuppressWarningConstant;
import com.mybatisflex.annotation.InsertListener;
import com.mybatisflex.annotation.UpdateListener;

/**
 * @author wjm
 * @since 2023-11-13 21:14
 */
public interface BaseListener<T> extends InsertListener, UpdateListener {

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    @Override
    default void onInsert(Object entity) {
        action((T) entity);
    }

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    @Override
    default void onUpdate(Object entity) {
        action((T) entity);
    }

    Class<T> getEntityType();

    void action(T entity);

}
