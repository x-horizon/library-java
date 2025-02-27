package org.horizon.sdk.library.java.message.engine.server.mqtt.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author wjm
 * @since 2025-01-06 22:24
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
public class ClientConnectAuthRequestDTO {

    private String clientId;

    private String username;

    private String password;

}