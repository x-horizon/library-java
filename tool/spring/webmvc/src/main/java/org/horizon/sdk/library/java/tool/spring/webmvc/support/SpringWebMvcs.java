package org.horizon.sdk.library.java.tool.spring.webmvc.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * toolkit for spring webmvc
 *
 * @author wjm
 * @since 2022-08-05 22:53
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringWebMvcs {

    /**
     * get {@link ServletRequestAttributes}
     *
     * @return {@link ServletRequestAttributes}
     */
    @NonNull
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }

    /**
     * get {@link HttpServletRequest}
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getHttpServletRequest() {
        return getServletRequestAttributes().getRequest();
    }

    /**
     * get {@link HttpServletResponse}
     *
     * @return {@link HttpServletResponse}
     */
    public static HttpServletResponse getHttpServletResponse() {
        return getServletRequestAttributes().getResponse();
    }

}