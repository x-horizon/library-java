package cn.srd.itcp.sugar.redisson.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties For Redisson
 *
 * @author wjm
 * @since 2020/12/12 18:06
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

    /**
     * 数据库槽
     */
    private Integer database;

    /**
     * 域名
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时时间，单位：毫秒
     */
    private Integer timeout;

}
