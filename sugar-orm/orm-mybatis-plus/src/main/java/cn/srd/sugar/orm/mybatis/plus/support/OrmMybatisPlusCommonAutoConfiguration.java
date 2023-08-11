package cn.srd.sugar.orm.mybatis.plus.support;

import cn.srd.sugar.orm.mybatis.plus.config.properties.OrmMybatisPlusProperties;
import cn.srd.sugar.orm.mybatis.plus.interceptor.MybatisPlusInnerInterceptorsConfigurer;
import cn.srd.sugar.orm.mybatis.plus.interceptor.MybatisPlusInterceptors;
import cn.srd.sugar.tool.lang.core.ReflectsUtil;
import cn.srd.sugar.tool.spring.common.core.SpringsUtil;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * {@link EnableAutoConfiguration AutoConfiguration} for Sugar ORM Mybatis Plus Common
 *
 * @author wjm
 * @since 2022-07-05
 */
@AutoConfiguration
@Import({PostgresqlMetadataInjector.class})
@EnableConfigurationProperties(OrmMybatisPlusProperties.class)
public class OrmMybatisPlusCommonAutoConfiguration {

    /**
     * 装配 {@link MybatisPlusInterceptor}
     *
     * @return 装配对象
     */
    @Bean
    @Order
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        SpringsUtil.getBeansOfType(MybatisPlusInterceptors.class).forEach((beanName, beanClass) -> ReflectsUtil.invoke(beanClass, "addInterceptor"));
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        MybatisPlusInnerInterceptorsConfigurer.get().forEach(innerInterceptorSupplier -> mybatisPlusInterceptor.addInnerInterceptor(innerInterceptorSupplier.get()));
        return mybatisPlusInterceptor;
    }

    /**
     * 装配 {@link ConfigurationCustomizer}
     *
     * @return 装配对象
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        // 开启 XML resultMap 下划线自动转换驼峰，不用写 <result> 相关的 数据库字段名 与 实体类字段名 的转换
        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }

    // /**
    //  * 装配 {@link MysqlBinaryPrimaryKeyGenerator}
    //  *
    //  * @return 装配对象
    //  */
    // @Bean
    // @DependsOn("sugarMybatisPlusProperties")
    // public MysqlBinaryPrimaryKeyGenerator mysqlBinUuidPrimaryKeyGenerator() {
    //     if (Objects.equals(DbType.MYSQL, EnumsUtil.capableToEnum(SpringsUtil.getBean(SugarMybatisPlusProperties.class).getDbType(), DbType.class))) {
    //         return new MysqlBinaryPrimaryKeyGenerator();
    //     }
    //     return null;
    // }

}
