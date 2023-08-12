package cn.srd.library.orm.mybatis.plus.config.properties;

import cn.srd.library.orm.mybatis.plus.database.postgresql.metadata.handler.PostgresqlTableColumnHandler;
import cn.srd.library.orm.mybatis.plus.database.postgresql.metadata.handler.PostgresqlTableHandler;
import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for Library Mybatis Plus
 *
 * @author wjm
 * @since 2022-07-18 17:59:54
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "library.orm.mybatis-plus")
public class OrmMybatisPlusProperties {

    /**
     * 数据库类型，可用值参考：{@link DbType} TODO wjm 后续实现直接注入枚举
     */
    private String database;

    /**
     * 是否启用元数据表操作，see {@link PostgresqlTableHandler}、{@link PostgresqlTableColumnHandler}
     */
    private Boolean enableMetadata;

}
