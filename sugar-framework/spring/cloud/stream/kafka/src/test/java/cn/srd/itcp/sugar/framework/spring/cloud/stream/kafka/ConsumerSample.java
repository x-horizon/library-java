package cn.srd.itcp.sugar.framework.spring.cloud.stream.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ConsumerSample {

    @Bean
    public Consumer<String> consume() {
        return System.out::println;
    }

}
