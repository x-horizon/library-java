// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.studio.low.code.model.vo;

import cn.srd.library.java.orm.contract.model.base.VO;
import cn.srd.library.java.studio.low.code.model.bo.TeacherBO;
import cn.srd.library.java.studio.low.code.model.po.TeacherPO;
import cn.srd.library.java.tool.convert.all.Converts;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 教师信息 model
 *
 * @author TODO 请填写作者名字
 * @since 2024-04-15 23:57
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AutoMappers({@AutoMapper(target = TeacherBO.class), @AutoMapper(target = TeacherPO.class)})
public class TeacherVO extends TeacherBO implements VO {

    @Serial private static final long serialVersionUID = -6211058936120808600L;

    @Override
    public TeacherPO toPO() {
        return Converts.withMapstruct().toBean(this, TeacherPO.class);
    }

}