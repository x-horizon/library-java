package cn.srd.library.java.tool.spring.webflux.interceptor;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.constant.spring.SpringInitializeConstant;
import cn.srd.library.java.contract.constant.web.HttpStatus;
import cn.srd.library.java.contract.model.protocol.WebResponse;
import cn.srd.library.java.contract.model.throwable.*;
import cn.srd.library.java.tool.convert.api.Converts;
import cn.srd.library.java.tool.spring.contract.interceptor.WebExceptionInterceptor;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static cn.srd.library.java.contract.model.protocol.WebResponse.error;

/**
 * spring webflux exception interceptor
 *
 * @author wjm
 * @since 2024-07-04 11:51
 */
@Slf4j
@Order(SpringInitializeConstant.HIGH_INITIALIZE_PRIORITY)
public class WebFluxExceptionInterceptor extends WebExceptionInterceptor implements ErrorWebExceptionHandler {

    @Override
    protected String getModuleView() {
        return ModuleView.TOOL_SPRING_WEBFLUX_SYSTEM;
    }

    @Override
    @NonNull
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        String requestUri = exchange.getRequest().getPath().toString();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(Converts.onJackson().toString(switch (throwable) {
                    case NoResourceFoundException exception -> whenNoResourceFoundException(requestUri, exception);
                    case UnrecognizedPropertyException exception -> whenUnrecognizedPropertyException(requestUri, exception);
                    case InvalidIdException ignore -> whenInvalidIdException(requestUri);
                    case InvalidArgumentException exception -> whenInvalidArgumentException(requestUri, exception);
                    case UnsupportedException ignore -> whenUnsupportedException(requestUri);
                    case DataNotFoundException exception -> whenDataNotFoundException(requestUri, exception);
                    case ClientException exception -> whenClientException(requestUri, exception);
                    case RunningException exception -> whenRunningException(requestUri, exception);
                    default -> whenThrowable(requestUri, throwable);
                }).getBytes(StandardCharsets.UTF_8)))
        );
    }

    /**
     * <pre>
     * handle the exception sample as following:
     *
     *  1. define a controller.
     *  {@code
     *     @RestController
     *     @RequestMapping("/foo")
     *     public class FooController {
     *
     *         @PostMapping("/sayHello")
     *         public void sayHello(@RequestBody FooVO fooVO) {
     *             System.out.println(fooVO);
     *         }
     *
     *     }
     *  }
     *
     *  2. send a post request to /foo/sayHello2, will throw {@link NoResourceFoundException} and handled by this method.
     * </pre>
     *
     * @param uri       the http request uri
     * @param exception the exception
     * @return the web response
     */
    public WebResponse<Void> whenNoResourceFoundException(String uri, NoResourceFoundException exception) {
        log.warn(formatMessage(uri, exception.getReason()));
        return error(HttpStatus.NOT_FOUND, exception.getReason());
    }

}