package cn.srd.itcp.sugar.convert.jackson.exception;

import java.io.Serial;

/**
 * jackson 序列化异常
 *
 * @author wjm
 * @since 2021/5/1 14:13
 */
public class JacksonSerializerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5404014469580075860L;

    public JacksonSerializerException() {
    }

    public JacksonSerializerException(String message) {
        super(message);
    }

    public JacksonSerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksonSerializerException(Throwable cause) {
        super(cause);
    }

}
