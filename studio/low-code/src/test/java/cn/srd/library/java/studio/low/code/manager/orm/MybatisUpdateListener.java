// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.studio.low.code.manager.orm;

import cn.srd.library.java.orm.contract.mybatis.flex.model.bo.BaseBO;
import cn.srd.library.java.orm.mybatis.flex.base.listener.BaseUpdateListener;

import java.time.LocalDateTime;

/**
 * @author TODO 请填写作者名字
 * @since 2024-04-15 23:57
 */
public class MybatisUpdateListener implements BaseUpdateListener<BaseBO> {

    @Override
    public Class<BaseBO> getEntityType() {
        return BaseBO.class;
    }

    @Override
    public void action(BaseBO entity) {
        entity.setUpdaterId(1L).setUpdateTime(LocalDateTime.now());
    }

}