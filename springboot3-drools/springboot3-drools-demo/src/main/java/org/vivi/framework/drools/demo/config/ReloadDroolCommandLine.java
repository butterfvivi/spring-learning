package org.vivi.framework.drools.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.vivi.framework.drools.demo.service.DroolsRuleService;

@Component
public class ReloadDroolCommandLine implements CommandLineRunner {
    @Autowired
    private DroolsRuleService droolsRuleService;
    @Override
    public void run(String... args) throws Exception {
        droolsRuleService.reloadAllDroolRule();
    }
}