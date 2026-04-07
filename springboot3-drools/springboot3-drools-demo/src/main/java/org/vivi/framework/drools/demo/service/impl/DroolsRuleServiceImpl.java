package org.vivi.framework.drools.demo.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vivi.framework.drools.demo.config.DroolsManager;
import org.vivi.framework.drools.demo.model.DroolsRule;
import org.vivi.framework.drools.demo.service.DroolsRuleService;

import java.util.*;

@Component
@Slf4j
public class DroolsRuleServiceImpl implements DroolsRuleService {

    @Resource
    private DroolsManager droolsManager;
    /**
     * 模拟数据库
     */
    private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);

    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleMap.values());
    }

    @Override
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleMap.get(ruleId);
        if (null != droolsRule) {
            droolsRuleMap.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule.getKieBaseName(), droolsRule.getKiePackageName(), ruleName);
        }
    }


}
