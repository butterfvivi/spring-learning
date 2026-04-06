package org.vivi.framework.drools;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.drools.model.entity.Customer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest(classes = DroolsApplication.class)
public class DroolsAppTest  {

    @Test
    public void test1() {
        // 构建KieServices
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        // 获取kmodule.xml中配置中名称为ksession-rule的session，默认为有状态的。
        KieSession kSession = kieContainer.newKieSession("ksession-rule");
        Customer customer = new Customer();
        Map<String, Integer> map = new HashMap<>();
        customer.setFactorValueMap(map);

        customer.setCustomerNo("yangjingjing");
        customer.setCustomerName("杨京京");
        customer.setCustomerAge(18);
        kSession.insert(customer);

        int count = kSession.fireAllRules();

        customer.getFactorValueMap().forEach((key, value) -> {
            log.info("key:{},value:{}", key, value);
        });
        log.info("count:{}", count);
    }
}
