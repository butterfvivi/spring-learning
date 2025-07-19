package org.vivi.framework.kafka.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

//@Component
public class SimpleProduct {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${simple.demo.toptic}")
    private String toptic;

    public void send(String message)
    {
        kafkaTemplate.send(toptic, message);
    }
}
