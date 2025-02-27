package org.horizon.sdk.library.java.tool.lang.number;

import org.horizon.sdk.library.java.tool.lang.object.Classes;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * double number handler
 *
 * @author wjm
 * @since 2023-09-21 21:37
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DoubleHandler implements NumberHandler {

    /**
     * the singleton instance
     */
    static final DoubleHandler INSTANCE = new DoubleHandler();

    @Override
    public boolean isAssignable(Class<?> input) {
        return Classes.isAssignable(Double.class, input);
    }

    @Override
    public <T extends Number> Number getValue(T input) {
        return input.doubleValue();
    }

}