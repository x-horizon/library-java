package cn.srd.itcp.sugar.redisson.exception;

import java.io.Serial;

/**
 * Redisson 执行异常
 *
 * @author wjm
 * @since 2020/12/12 18:06
 */
public class RedissonExecuteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1216257915167180153L;

    public RedissonExecuteException() {
    }

    public RedissonExecuteException(String message) {
        super(message);
    }

    public RedissonExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedissonExecuteException(Throwable cause) {
        super(cause);
    }

}
