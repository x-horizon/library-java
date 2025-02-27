package org.horizon.sdk.library.java.tool.convert.spring;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * convert toolkit for spring
 *
 * @author wjm
 * @since 2021-05-01 14:13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringConverts {

    /**
     * singleton pattern
     */
    private static final SpringConverts INSTANCE = new SpringConverts();

    /**
     * get singleton instance
     *
     * @return instance
     */
    public static SpringConverts getInstance() {
        return INSTANCE;
    }

}