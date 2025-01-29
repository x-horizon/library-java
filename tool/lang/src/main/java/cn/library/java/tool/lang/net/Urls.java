package cn.library.java.tool.lang.net;

import org.dromara.hutool.core.net.url.UrlQuery;
import cn.library.java.contract.constant.module.ModuleView;
import cn.library.java.contract.constant.regex.RegexConstant;
import cn.library.java.contract.model.throwable.LibraryJavaInternalException;
import cn.library.java.tool.lang.object.Nil;
import cn.library.java.tool.lang.text.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * @author wjm
 * @since 2024-07-17 20:12
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Urls {

    public static boolean isValid(String url) {
        return Nil.isNotNull(url) && RegexConstant.URL_PATTERN.matcher(url).matches();
    }

    public static boolean isNotValid(String url) {
        return !isValid(url);
    }

    public static String getProtocol(String url) {
        return Optional.ofNullable(Strings.getByRegex(url, RegexConstant.URL_PROTOCOL_PATTERN))
                .orElseThrow(() -> new LibraryJavaInternalException(Strings.format("{}the url [{}] is not valid, please check!", ModuleView.TOOL_NET_SYSTEM, url)));
    }

    public static String getUri(String url) {
        return URI.create(url).getPath();
    }

    public static String getAuthority(String url) {
        return URI.create(url).getAuthority();
    }

    public static CharSequence getQueryParam(String url, String queryParamName) {
        return UrlQuery.of(url, null).get(queryParamName);
    }

    public static Map<CharSequence, CharSequence> getQueryParamMap(String url) {
        return UrlQuery.of(url, null).getQueryMap();
    }

}