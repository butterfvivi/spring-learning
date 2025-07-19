package org.vivi.framework.kafka.producer;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.kafka.partition.PartitionProduct;
import org.vivi.framework.kafka.simple.SimpleProduct;

@SpringBootTest
class TestProducerService {

    @Autowired
    private SimpleProduct product1;

    @Autowired
    private PartitionProduct product2;

    @Test
    public void test01() {
        for (int i = 0; i < 10; i++) {
            product1.send("tom" + i);
        }
        ThreadUtil.sleep(3);
    }

    @Test
    public void test02() {
        product2.send("tom1", "北京");
        product2.send("tom2", "天津");
        product2.send("tom3", "上海");
        ThreadUtil.sleep(3);
    }

}