package org.vivi.framework.canal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.canal.utils.CanalUtil;

@SpringBootTest
class CanalApplicationTests {

    @Autowired
    private CanalUtil canalUtil;

    /**
     * 测试mysql同步到redis
     */
    @Test
    public void test(){
        this.canalUtil.startMonitorSQL();
    }

}
