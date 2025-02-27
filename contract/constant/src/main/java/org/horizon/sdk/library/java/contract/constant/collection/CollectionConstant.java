package org.horizon.sdk.library.java.contract.constant.collection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * collection constant
 *
 * @author wjm
 * @since 2023-09-19 19:44
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionConstant {

    /**
     * the length zero: {@code 0}
     */
    public static final int LENGTH_ZERO = 0;

    /**
     * the length one: {@code 1}
     */
    public static final int LENGTH_ONE = 1;

    /**
     * the first index: {@code 0}
     */
    public static final int INDEX_FIRST = 0;

    /**
     * the second index: {@code 1}
     */
    public static final int INDEX_SECOND = INDEX_FIRST + 1;

    /**
     * the third index: {@code 2}
     */
    public static final int INDEX_THIRD = INDEX_SECOND + 1;

    /**
     * the forth index: {@code 3}
     */
    public static final int INDEX_FORTH = INDEX_THIRD + 1;

    /**
     * not found index: {@code -1}
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * the empty capacity: {@code 0}
     */
    public static final int CAPACITY_EMPTY = 0;

    /**
     * default map initial capacity: {@code 16}
     */
    public static final int CAPACITY_DEFAULT_INITIALIZE_MAP = 16;

    /**
     * define an empty array string: {@code "[]"}
     */
    public static final String EMPTY_ARRAY_STRING = "[]";

    /**
     * define an empty map string: {@code "{}"}
     */
    public static final String EMPTY_MAP_STRING = "{}";

    /**
     * default enable parallel stream
     */
    public static final boolean DEFAULT_PARALLEL_STREAM_ENABLE = false;

}