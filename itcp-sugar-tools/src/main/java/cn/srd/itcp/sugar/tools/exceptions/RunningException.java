package cn.srd.itcp.sugar.tools.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义运行时异常
 *
 * @author wjm
 * @since 2020/6/7 20:52
 */
@Getter
@Setter
public class RunningException extends RuntimeException {

    /**
     * 自定义异常模板
     */
    ExceptionTemplate exceptionTemplate;

    /**
     * public constructor
     *
     * @param message 异常信息
     */
    public RunningException(String message) {
        super(message);
    }

    /**
     * public constructor
     *
     * @param message 异常信息
     * @param cause   wrapper exception
     */
    public RunningException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * public constructor
     *
     * @param exceptionTemplate 自定义异常模板
     */
    public RunningException(ExceptionTemplate exceptionTemplate) {
        super(exceptionTemplate.getDescription());
        this.exceptionTemplate = exceptionTemplate;
    }

    /**
     * public constructor
     *
     * @param exceptionTemplate 自定义异常模板
     * @param cause             wrapper exception
     */
    public RunningException(ExceptionTemplate exceptionTemplate, Throwable cause) {
        super(exceptionTemplate.getDescription(), cause);
        this.exceptionTemplate = exceptionTemplate;
    }

}
