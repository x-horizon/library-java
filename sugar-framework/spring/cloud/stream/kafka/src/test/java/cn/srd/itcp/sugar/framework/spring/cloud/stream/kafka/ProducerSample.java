package cn.srd.itcp.sugar.framework.spring.cloud.stream.kafka;

import cn.srd.itcp.sugar.framework.spring.cloud.stream.kafka.core.StreamBridgeManager;
import org.springframework.stereotype.Component;

@Component
public class ProducerSample {

    public void produce() {
        StreamBridgeManager.get().send("produce-out-0", "333");
    }

}
