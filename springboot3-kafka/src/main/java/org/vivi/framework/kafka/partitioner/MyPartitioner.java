package org.vivi.framework.kafka.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * kafka自定义分区器实现自定义分区
 * key和分区是在发送消息时new ProducerRecord对象时指定。
 * kafka默认的分区规则需要查看org.apache.kafka.clients.producer.internals.DefaultPartitioner的partition方法
 */
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        if ("partition_0".equals(key.toString())) {
            return 0;
        } else if ("partition_1".equals(key.toString())) {
            return 1;
        } else {
            //如果没有自定义的分区规则,则调用kafka内部的分区规则
            return new DefaultPartitioner().partition(topic, key, keyBytes, value, valueBytes, cluster);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
