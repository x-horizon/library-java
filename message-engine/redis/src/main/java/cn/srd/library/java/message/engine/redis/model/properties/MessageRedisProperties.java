// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.message.engine.redis.model.properties;

import cn.srd.library.java.message.engine.contract.model.properties.MessageEngineProperties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * properties for message engine redis
 *
 * @author wjm
 * @since 2024-05-25 16:35
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "library-java.message-engine.redis")
public class MessageRedisProperties extends MessageEngineProperties {

    @Getter private static MessageRedisProperties instance = null;

    @PostConstruct
    public void initialize() {
        instance = this;
    }

}