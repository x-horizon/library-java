// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.mybatis.flex.base.autoconfigure;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.model.throwable.LibraryJavaInternalException;
import cn.srd.library.java.orm.mybatis.flex.base.audit.AuditLogConfig;
import cn.srd.library.java.orm.mybatis.flex.base.audit.UnsupportedAuditLogTelemeter;
import cn.srd.library.java.orm.mybatis.flex.base.id.IdConfig;
import cn.srd.library.java.orm.mybatis.flex.base.listener.*;
import cn.srd.library.java.orm.mybatis.flex.base.lock.OptimisticLockConfig;
import cn.srd.library.java.orm.mybatis.flex.base.logic.DeleteLogicConfig;
import cn.srd.library.java.orm.mybatis.flex.base.property.PropertyConfig;
import cn.srd.library.java.tool.lang.compare.Comparators;
import cn.srd.library.java.tool.lang.convert.Converts;
import cn.srd.library.java.tool.lang.functional.Assert;
import cn.srd.library.java.tool.lang.object.Nil;
import cn.srd.library.java.tool.lang.object.Objects;
import cn.srd.library.java.tool.lang.reflect.Reflects;
import cn.srd.library.java.tool.lang.text.Strings;
import cn.srd.library.java.tool.spring.contract.Annotations;
import cn.srd.library.java.tool.spring.contract.Classes;
import cn.srd.library.java.tool.spring.contract.Springs;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.mybatis.FlexConfiguration;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;

/**
 * the global mybatis flex customizer
 *
 * @author wjm
 * @since 2023-11-12 21:06
 */
@Slf4j
public class MybatisFlexCustomizer implements ConfigurationCustomizer, MyBatisFlexCustomizer {

    /**
     * the xml mapper class paths field name in {@link MybatisFlexProperties#getMapperLocations()}
     */
    private static final String MYBATIS_FLEX_PROPERTIES_XML_MAPPER_CLASS_PATHS_FIELD_NAME = "mapperLocations";

    /**
     * the xml mapper class paths default field value in {@link MybatisFlexProperties#getMapperLocations()}
     */
    private static final String[] MYBATIS_FLEX_PROPERTIES_XML_MAPPER_CLASS_PATHS_DEFAULT_FIELD_VALUE = new String[]{"classpath*:/mapper/**/*.xml"};

    /**
     * the xml mapper entity package alias class paths field name in {@link MybatisFlexProperties#getTypeAliasesPackage()}
     */
    private static final String MYBATIS_FLEX_PROPERTIES_XML_MAPPER_ENTITY_PACKAGE_ALIAS_CLASS_PATHS_FIELD_NAME = "typeAliasesPackage";

    /**
     * the configuration field name in {@link MybatisFlexProperties#getConfiguration()}
     */
    private static final String MYBATIS_FLEX_PROPERTIES_CONFIGURATION_FIELD_NAME = "configuration";

    /**
     * the native mybatis log field name in {@link MybatisFlexProperties.CoreConfiguration#getLogImpl()}
     */
    private static final String MYBATIS_FLEX_PROPERTIES_CONFIGURATION_LOG_FIELD_NAME = "logImpl";

    @Override
    public void customize(FlexConfiguration configuration) {
    }

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        log.debug("{}mybatis flex customizer is enabled, starting initializing...", ModuleView.ORM_MYBATIS_SYSTEM);

        EnableMybatisFlexCustomizer mybatisFlexCustomizer = Annotations.getAnnotation(EnableMybatisFlexCustomizer.class);
        handleIdGenerateConfig(globalConfig, mybatisFlexCustomizer.globalIdGenerateConfig());
        handleDeleteLogicConfig(globalConfig, mybatisFlexCustomizer.globalDeleteLogicConfig());
        handleListenerConfig(globalConfig, mybatisFlexCustomizer.globalListenerConfig());
        handleOptimisticLockConfig(globalConfig, mybatisFlexCustomizer.globalOptimisticLockConfig());
        handleAuditConfig(mybatisFlexCustomizer.globalAuditConfig());
        handleMybatisFlexProperties(mybatisFlexCustomizer.globalPropertyConfig());

        log.debug(""" 
                        {}mybatis flex customizer has loaded the following configurations:
                        --------------------------------------------------------------------------------------------------------------------------------
                        Global ID Generate Config:
                           generateType                            = [{}]
                           generator                               = [{}]
                           generateSQL                             = [{}]
                        Global Delete Logic Config:
                           normalValue                             = [{}]
                           deletedValue                            = [{}]
                        Global Listener Config:
                           whenInsert                              = [{}]
                           whenUpdate                              = [{}]
                        Global Optimistic Lock Config:
                           columnName                              = [{}]
                        Global Audit Config:
                           constructor                             = [{}]
                           printer                                 = [{}]
                           telemeter                               = [{}]
                        Global Property Config:
                           nativeMybatisLog                        = [{}]
                           xmlMapperClassPaths                     = {}
                           xmlMapperEntityPackageAliasPackagePaths = {}
                        --------------------------------------------------------------------------------------------------------------------------------""",
                ModuleView.ORM_MYBATIS_SYSTEM,
                mybatisFlexCustomizer.globalIdGenerateConfig().generateType().name(), mybatisFlexCustomizer.globalIdGenerateConfig().generator().getName(), mybatisFlexCustomizer.globalIdGenerateConfig().generateSQL(),
                mybatisFlexCustomizer.globalDeleteLogicConfig().normalValue(), mybatisFlexCustomizer.globalDeleteLogicConfig().deletedValue(),
                mybatisFlexCustomizer.globalListenerConfig().whenInsert().getName(), mybatisFlexCustomizer.globalListenerConfig().whenUpdate().getName(),
                mybatisFlexCustomizer.globalOptimisticLockConfig().columnName(),
                mybatisFlexCustomizer.globalAuditConfig().constructor().getName(), mybatisFlexCustomizer.globalAuditConfig().printer().getName(), mybatisFlexCustomizer.globalAuditConfig().telemeter().getName(),
                mybatisFlexCustomizer.globalPropertyConfig().nativeMybatisLog().getName(), mybatisFlexCustomizer.globalPropertyConfig().xmlMapperClassPaths(), mybatisFlexCustomizer.globalPropertyConfig().xmlMapperEntityPackageAliasPackagePaths()
        );

        log.debug("{}mybatis flex customizer initialized.", ModuleView.ORM_MYBATIS_SYSTEM);
    }

    /**
     * handle the global id config
     *
     * @param globalConfig the mybatis flex global config
     * @param idConfig     the global id config
     */
    private void handleIdGenerateConfig(FlexGlobalConfig globalConfig, IdConfig idConfig) {
        globalConfig.setKeyConfig(idConfig.generateType().getStrategy().build(idConfig));
    }

    /**
     * handle the global delete logic config
     *
     * @param globalConfig      the mybatis flex global config
     * @param deleteLogicConfig the global delete logic config
     */
    private void handleDeleteLogicConfig(FlexGlobalConfig globalConfig, DeleteLogicConfig deleteLogicConfig) {
        String normalValue = deleteLogicConfig.normalValue();
        String deletedValue = deleteLogicConfig.deletedValue();

        Assert.of().setMessage("")
                .setThrowable(LibraryJavaInternalException.class)
                .throwsIfAnyBlank(normalValue, deletedValue);

        globalConfig.setNormalValueOfLogicDelete(getDeleteLogicValue(normalValue));
        globalConfig.setDeletedValueOfLogicDelete(getDeleteLogicValue(deletedValue));
    }

    /**
     * handle the global listener config
     *
     * @param globalConfig   the mybatis flex global config
     * @param listenerConfig the global listener config
     */
    private void handleListenerConfig(FlexGlobalConfig globalConfig, ListenerConfig listenerConfig) {
        BaseInsertListener<?> insertListener = Reflects.newInstance(listenerConfig.whenInsert());
        BaseUpdateListener<?> updateListener = Reflects.newInstance(listenerConfig.whenUpdate());
        if (Comparators.notEquals(UnsupportedInsertListener.class, listenerConfig.whenInsert())) {
            globalConfig.registerInsertListener(insertListener, insertListener.getEntityType());
        }
        if (Comparators.notEquals(UnsupportedUpdateListener.class, listenerConfig.whenUpdate())) {
            globalConfig.registerUpdateListener(updateListener, updateListener.getEntityType());
        }
    }

    /**
     * handle the global optimistic lock config
     *
     * @param globalConfig         the mybatis flex global config
     * @param optimisticLockConfig the global id config
     */
    private void handleOptimisticLockConfig(FlexGlobalConfig globalConfig, OptimisticLockConfig optimisticLockConfig) {
        Objects.setIfNotBlank(optimisticLockConfig.columnName(), globalConfig::setVersionColumn);
    }

    /**
     * handle the audit log config
     *
     * @param auditLogConfig the audit log config
     */
    private void handleAuditConfig(AuditLogConfig auditLogConfig) {
        if (auditLogConfig.enable()) {
            AuditManager.setAuditEnable(true);
            AuditManager.setMessageFactory(Reflects.newInstance(auditLogConfig.constructor()));
            AuditManager.setMessageCollector(Reflects.newInstance(auditLogConfig.printer()));
            if (Comparators.notEquals(UnsupportedAuditLogTelemeter.class, auditLogConfig.telemeter())) {
                AuditManager.setMessageReporter(Reflects.newInstance(auditLogConfig.telemeter()));
            }
        }
    }

    /**
     * handle all properties on {@link MybatisFlexProperties}
     *
     * @param propertyConfig the global property config
     */
    private void handleMybatisFlexProperties(PropertyConfig propertyConfig) {
        handleMybatisFlexPropertiesXMLMapperClassPathsIfNeed(propertyConfig.xmlMapperClassPaths());
        handleMybatisFlexPropertiesEntityPackageAliasPackagePathsIfNeed(propertyConfig.xmlMapperEntityPackageAliasPackagePaths());
        handleMybatisFlexPropertiesCoreConfigurationIfNeed(propertyConfig);
    }

    /**
     * handle xml mapper class paths on {@link MybatisFlexProperties#getMapperLocations()}
     *
     * @param xmlMapperClassPaths xml mapper class paths
     */
    private void handleMybatisFlexPropertiesXMLMapperClassPathsIfNeed(String[] xmlMapperClassPaths) {
        MybatisFlexProperties mybatisFlexProperties = Springs.getBean(MybatisFlexProperties.class);
        if (Comparators.equals(MYBATIS_FLEX_PROPERTIES_XML_MAPPER_CLASS_PATHS_DEFAULT_FIELD_VALUE, mybatisFlexProperties.getMapperLocations())) {
            Reflects.setFieldValue(mybatisFlexProperties,
                    MYBATIS_FLEX_PROPERTIES_XML_MAPPER_CLASS_PATHS_FIELD_NAME,
                    xmlMapperClassPaths
            );
        }
    }

    /**
     * handle xml mapper class paths on {@link MybatisFlexProperties#getTypeAliasesPackage()}
     *
     * @param xmlMapperEntityPackageAliasPackagePaths xml mapper entity package alias package paths
     */
    private void handleMybatisFlexPropertiesEntityPackageAliasPackagePathsIfNeed(String[] xmlMapperEntityPackageAliasPackagePaths) {
        MybatisFlexProperties mybatisFlexProperties = Springs.getBean(MybatisFlexProperties.class);
        if (Nil.isNull(mybatisFlexProperties.getTypeAliasesPackage())) {
            Reflects.setFieldValue(mybatisFlexProperties,
                    MYBATIS_FLEX_PROPERTIES_XML_MAPPER_ENTITY_PACKAGE_ALIAS_CLASS_PATHS_FIELD_NAME,
                    Strings.joinWithComma(Classes.parseAntStylePackagePathsToPackagePaths(xmlMapperEntityPackageAliasPackagePaths))
            );
        }
    }

    /**
     * handle configuration on {@link MybatisFlexProperties.CoreConfiguration}
     */
    private void handleMybatisFlexPropertiesCoreConfigurationIfNeed(PropertyConfig propertyConfig) {
        initMybatisFlexPropertiesCoreConfigurationIfNeed();
        handleMybatisFlexPropertiesCoreConfigurationNativeMybatisLogIfNeed(propertyConfig.nativeMybatisLog());
    }

    /**
     * handle native mybatis log on {@link MybatisFlexProperties.CoreConfiguration#getLogImpl()
     *
     * @param nativeMybatisLog core configuration native mybatis log
     */
    private void handleMybatisFlexPropertiesCoreConfigurationNativeMybatisLogIfNeed(Class<? extends Log> nativeMybatisLog) {
        MybatisFlexProperties mybatisFlexProperties = Springs.getBean(MybatisFlexProperties.class);
        if (Nil.isNull(mybatisFlexProperties.getConfiguration().getLogImpl())) {
            Reflects.setFieldValue(mybatisFlexProperties.getConfiguration(),
                    MYBATIS_FLEX_PROPERTIES_CONFIGURATION_LOG_FIELD_NAME,
                    nativeMybatisLog
            );
        }
    }

    /**
     * init {@link MybatisFlexProperties.CoreConfiguration} if no value in spring configuration file
     */
    private void initMybatisFlexPropertiesCoreConfigurationIfNeed() {
        MybatisFlexProperties mybatisFlexProperties = Springs.getBean(MybatisFlexProperties.class);
        if (Nil.isNull(mybatisFlexProperties.getConfiguration())) {
            Reflects.setFieldValue(mybatisFlexProperties,
                    MYBATIS_FLEX_PROPERTIES_CONFIGURATION_FIELD_NAME,
                    new MybatisFlexProperties.CoreConfiguration()
            );
        }
    }

    private Object getDeleteLogicValue(String value) {
        // note:
        // must convert value to number first and then convert value to boolean,
        // because the value "0" can convert to boolean false, but the actual value should be integer 0.
        Object actualValue = Converts.toInteger(value);
        if (Nil.isNull(actualValue)) {
            actualValue = Converts.toBoolean(value);
            if (Nil.isNull(actualValue)) {
                actualValue = Converts.toString(value);
            }
        }
        return actualValue;
    }

}