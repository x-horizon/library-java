// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.id.snowflake;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wjm
 * @since 2023-11-13 10:42
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnowflakeIdOnStandAloneSingleInstanceStrategy implements SnowflakeIdEnvironmentStrategy {

    private static final short DEFAULT_WORKER_ID = 1;

    @Override
    public short getWorkerId(EnableSnowflakeId snowflakeIdConfig) {
        return DEFAULT_WORKER_ID;
    }

}
