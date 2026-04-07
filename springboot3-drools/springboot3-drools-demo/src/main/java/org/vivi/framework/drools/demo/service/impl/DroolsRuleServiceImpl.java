package org.vivi.framework.drools.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vivi.framework.drools.demo.config.DroolsManager;
import org.vivi.framework.drools.demo.mapper.RulesMapper;
import org.vivi.framework.drools.demo.model.DroolsRule;
import org.vivi.framework.drools.demo.service.DroolsRuleService;

import java.util.*;

@Component
@Slf4j
public class DroolsRuleServiceImpl implements DroolsRuleService {

    @Resource
    private DroolsManager droolsManager;
    /**
     * 1、模拟数据库, 存储规则
     */
    private Map<Long, DroolsRule> droolsRuleMap = new HashMap<>(16);
    /**
     * 2、数据库存储规则版本
     */
    @Autowired
    private RulesMapper rulesMapper;

    /**
     * 重新从数据库加载所有规则, 适用于规则数量不多的情况, 规则数量多时, 不建议使用
     * @return
     */
    @Override
    public String reloadAllDroolRule() {
        List<DroolsRule> droolRuleList = rulesMapper.selectList(Wrappers.emptyWrapper());
        for (DroolsRule droolRule : droolRuleList) {
            droolsManager.addOrUpdateRule(droolRule);
        }
        return "ok";
    }

    /**
     * 从MAP内存中获取规则列表
     * @return
     */
    @Override
    public List<DroolsRule> findAll() {
        return new ArrayList<>(droolsRuleMap.values());
    }

    /**
     * 新增规则到MAP，并且同步到内存中
     * @param droolsRule
     */
    @Override
    public void addDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setCreatedTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    /**
     * 更新规则到MAP，并且同步到内存中
     * @param droolsRule
     */
    @Override
    public void updateDroolsRule(DroolsRule droolsRule) {
        droolsRule.validate();
        droolsRule.setUpdateTime(new Date());
        droolsRuleMap.put(droolsRule.getRuleId(), droolsRule);
        droolsManager.addOrUpdateRule(droolsRule);
    }

    /**
     * 从MAP内存中删除规则，并且同步到内存中
     * @param ruleId
     * @param ruleName
     */
    @Override
    public void deleteDroolsRule(Long ruleId, String ruleName) {
        DroolsRule droolsRule = droolsRuleMap.get(ruleId);
        if (null != droolsRule) {
            droolsRuleMap.remove(ruleId);
            droolsManager.deleteDroolsRule(droolsRule.getKieBaseName(), droolsRule.getKiePackageName(), ruleName);
        }
    }


}
