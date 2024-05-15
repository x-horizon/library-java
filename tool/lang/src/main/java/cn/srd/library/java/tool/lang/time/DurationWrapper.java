// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.lang.time;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Duration =&gt; long time and timeunit
 *
 * @author wjm
 * @since 2023-06-07 10:18
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
public class DurationWrapper {

    /**
     * is it negative
     */
    private Boolean negativeIs;

    /**
     * duration wrap to time
     */
    private Long time;

    /**
     * duration wrap tp time unit
     */
    private TimeUnit timeUnit;

}