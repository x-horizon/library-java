package cn.srd.itcp.sugar.redisson.core.lock;

import cn.srd.itcp.sugar.redisson.support.RedissonManager;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

/**
 * Redisson 分布式单点公平锁操作，参考 {@link RedissonFairLock}
 *
 * @author wjm
 * @since 2020/12/12 18:06
 */
@Component
public class RedissonFairLockHandler implements RedissonLockTemplate {

    private static RedissonFairLockHandler instance = null;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static RedissonFairLockHandler getInstance() {
        return instance;
    }

    @Override
    public RLock getRLock(String lockName) {
        return RedissonManager.getClient().getFairLock(lockName);
    }

}
