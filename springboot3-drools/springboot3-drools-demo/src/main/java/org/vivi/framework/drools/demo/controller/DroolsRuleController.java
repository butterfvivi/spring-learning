package org.vivi.framework.drools.demo.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.drools.demo.config.DroolsManager;
import org.vivi.framework.drools.demo.model.DroolsRule;
import org.vivi.framework.drools.demo.service.DroolsRuleService;

import java.util.List;

@RestController
@RequestMapping("/drools/rule")
public class DroolsRuleController {

    @Resource
    private DroolsRuleService droolsRuleService;
    @Resource
    private DroolsManager droolsManager;

    @GetMapping("findAll")
    public List<DroolsRule> findAll() {
        return droolsRuleService.findAll();
    }

    @PostMapping("add")
    public String addRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.addDroolsRule(droolsRule);
        return "添加成功";
    }

    @PostMapping("update")
    public String updateRule(@RequestBody DroolsRule droolsRule) {
        droolsRuleService.updateDroolsRule(droolsRule);
        return "修改成功";
    }

    @PostMapping("deleteRule")
    public String deleteRule(@RequestParam Long ruleId, @RequestParam String ruleName) {
        droolsRuleService.deleteDroolsRule(ruleId, ruleName);
        return "删除成功";
    }

    @GetMapping("fireRule")
    public String fireRule(@RequestParam String kieBaseName, @RequestParam Integer params) {
        return droolsManager.fireRule(kieBaseName, params);
    }

}
