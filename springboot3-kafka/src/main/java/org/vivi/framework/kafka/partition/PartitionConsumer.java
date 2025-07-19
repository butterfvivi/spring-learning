package org.vivi.framework.kafka.partition;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PartitionConsumer {

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "${partition.demo.toptic}", partitions = {"1"})})
    public void consumer1(ConsumerRecord<String, String> record){
        log.info("消息partitions=1, partition={}, topic={}, key={}, value={}",
                record.partition(), record.topic(), record.key(), record.value());
    }

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "${partition.demo.toptic}", partitions = {"2"})})
    public void consumer2(ConsumerRecord<String, String> record) {
        log.info("消息partitions=2, partition={}, topic={}, key={}, value={}",
                record.partition(), record.topic(), record.key(), record.value());
    }

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "${partition.demo.toptic}", partitions = {"3"})})
    public void consumer3(ConsumerRecord<String, String> record) {
        log.info("消息partitions=3, partition={}, topic={}, key={}, value={}",
                record.partition(), record.topic(), record.key(), record.value());
    }
}
