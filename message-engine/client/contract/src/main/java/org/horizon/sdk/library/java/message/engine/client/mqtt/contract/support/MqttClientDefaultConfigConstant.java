package org.horizon.sdk.library.java.message.engine.client.mqtt.contract.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author wjm
 * @since 2024-06-11 16:38
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MqttClientDefaultConfigConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Client {

        public static final String COMPLETION_TIMEOUT = "30s";

        public static final String DISCONNECT_COMPLETION_TIMEOUT = "5s";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Producer {

        public static final boolean NEED_TO_SEND_ASYNC = false;

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Consumer {

    }

}