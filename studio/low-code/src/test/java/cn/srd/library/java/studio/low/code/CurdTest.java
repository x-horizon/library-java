// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.studio.low.code;

import cn.srd.library.java.orm.mybatis.flex.base.audit.AuditLogConfig;
import cn.srd.library.java.orm.mybatis.flex.base.autoconfigure.EnableMybatisFlexCustomizer;
import cn.srd.library.java.orm.mybatis.flex.base.id.IdConfig;
import cn.srd.library.java.orm.mybatis.flex.base.id.IdGenerateType;
import cn.srd.library.java.orm.mybatis.flex.base.listener.ListenerConfig;
import cn.srd.library.java.orm.mybatis.flex.base.lock.OptimisticLockConfig;
import cn.srd.library.java.orm.mybatis.flex.base.logic.DeleteLogicConfig;
import cn.srd.library.java.orm.mybatis.flex.base.property.PropertyConfig;
import cn.srd.library.java.studio.low.code.manager.orm.MybatisInsertListener;
import cn.srd.library.java.studio.low.code.manager.orm.MybatisUpdateListener;
import cn.srd.library.java.tool.enums.autowired.EnableEnumAutowired;
import cn.srd.library.java.tool.id.snowflake.EnableSnowflakeId;
import cn.srd.library.java.tool.id.snowflake.SnowflakeIdEnvironment;
import com.mybatisflex.core.logicdelete.impl.DateTimeLogicDeleteProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@MapperScan("cn.srd.library.java.studio.low.code")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableEnumAutowired
@EnableSnowflakeId(environment = SnowflakeIdEnvironment.MULTIPLE_NODE)
@EnableMybatisFlexCustomizer(
        globalIdGenerateConfig = @IdConfig(generateType = IdGenerateType.SNOWFLAKE),
        globalDeleteLogicConfig = @DeleteLogicConfig(processor = DateTimeLogicDeleteProcessor.class),
        globalListenerConfig = @ListenerConfig(whenInsert = MybatisInsertListener.class, whenUpdate = MybatisUpdateListener.class),
        globalOptimisticLockConfig = @OptimisticLockConfig(columnName = "version"),
        globalAuditConfig = @AuditLogConfig(enable = true),
        globalPropertyConfig = @PropertyConfig(
                xmlMapperClassPaths = {"classpath*:cn/srd/library/java/studio/low/code/**/repository/**/impl/*.xml"},
                xmlMapperEntityPackageAliasPackagePaths = {"cn.srd.library.java.studio.low.code.**.model.**"}
        )
)
@ExtendWith(SpringExtension.class)
class CurdTest {

    @Test
    void testSave() {
    }

}