package cn.srd.library.java.message.engine.client.contract.autoconfigure;

import cn.srd.library.java.contract.constant.spring.SpringInitializeConstant;
import cn.srd.library.java.tool.lang.object.BasePackagePath;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

/**
 * register message engine base package path
 *
 * @author wjm
 * @since 2024-05-24 16:56
 */
@Order(SpringInitializeConstant.HIGHER_INITIALIZE_PRIORITY)
public class MessageClientBasePackagePathAutoConfigurer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static final String BASE_PACKAGE_PATH = "cn.srd.library.java.message.engine";

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        BasePackagePath.register(BASE_PACKAGE_PATH);
    }

}