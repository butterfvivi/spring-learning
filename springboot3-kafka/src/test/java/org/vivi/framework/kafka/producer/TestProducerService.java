package org.vivi.framework.kafka.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestProducerService {

    @Autowired
    private ProducerService producerService;

    @Test
    public void test() throws Exception{
        producerService.send("sampleMessage");
    }
}