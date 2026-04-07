package org.vivi.framework.drools.demo.service;

import org.vivi.framework.drools.demo.model.DroolsRule;

import java.util.List;

public interface DroolsRuleService {

    List<DroolsRule> findAll();

    void addDroolsRule(DroolsRule droolsRule);

    void updateDroolsRule(DroolsRule droolsRule);

    void deleteDroolsRule(Long ruleId, String ruleName);
}
