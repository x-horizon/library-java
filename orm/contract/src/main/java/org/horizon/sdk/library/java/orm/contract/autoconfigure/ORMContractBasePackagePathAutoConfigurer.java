package org.horizon.sdk.library.java.orm.contract.autoconfigure;

import org.horizon.sdk.library.java.contract.constant.spring.SpringInitializeConstant;
import org.horizon.sdk.library.java.tool.lang.object.BasePackagePath;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

/**
 * register orm mybatis base package path
 *
 * @author wjm
 * @since 2023-11-08 17:07
 */
@Order(SpringInitializeConstant.HIGHER_INITIALIZE_PRIORITY)
public class ORMContractBasePackagePathAutoConfigurer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static final String BASE_PACKAGE_PATH = "org.horizon.sdk.library.java.orm";

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        BasePackagePath.register(BASE_PACKAGE_PATH);
    }

}