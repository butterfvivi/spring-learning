package org.vivi.framework.kafka.partition;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class PartitionProductTest {

    @InjectMocks
    private PartitionProduct partitionProduct;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testSend() {
        String number = "ORDER123456";
        String province = "北京";

        partitionProduct.send(number, province);
    }
}