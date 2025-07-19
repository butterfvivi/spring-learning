package org.vivi.framework.kafka.simple;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class SimpleConsumer {

    @KafkaListener(topics = "${simple.demo.toptic}")
    public void consumer(ConsumerRecord<String, String> record) {
        Optional<String> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String json = kafkaMessage.get();
            log.info("topic={}, partition={}, key={}, value={}, msg={}",
                    record.topic(), record.partition(), record.key(), record.value(), json);
        }
    }
}
