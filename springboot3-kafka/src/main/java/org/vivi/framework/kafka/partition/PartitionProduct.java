package org.vivi.framework.kafka.partition;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;

//@Component
public class PartitionProduct {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${partition.demo.toptic}")
    private String toptic;

    public void send(String number, String province) {
        OrderInfo orderInfo = OrderInfo.builder()
                .orderId(1L).number(number).userId(1L).orderTime(new Date()).address(province)
                .build();
        String userJson = JSONUtil.toJsonStr(orderInfo);

        int partition = City.getPartition(province);

        this.kafkaTemplate.send(toptic, partition, province, userJson);
    }
}
