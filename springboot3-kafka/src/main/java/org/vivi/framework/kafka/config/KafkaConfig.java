package org.vivi.framework.kafka.config;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void someMethod() {
        this.kafkaTemplate.send("test-group", "Hello");
    }

}