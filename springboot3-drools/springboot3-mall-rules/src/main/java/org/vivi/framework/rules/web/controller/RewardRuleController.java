package org.vivi.framework.rules.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.rules.service.RewardRuleService;
import org.vivi.framework.rules.web.request.RewardRuleRequestForm;
import org.vivi.framework.rules.web.vo.RewardRuleResponseVo;

@Slf4j
@RestController
@RequestMapping("/rules/reward")
@SuppressWarnings({"unchecked", "rawtypes"})
public class RewardRuleController {

    @Autowired
    private RewardRuleService rewardRuleService;

    @Operation(summary = "计算订单奖励费用")
    @PostMapping("/calculateOrderRewardFee")
    public ResponseEntity<RewardRuleResponseVo>
            calculateOrderRewardFee(@RequestBody RewardRuleRequestForm rewardRuleRequestForm) {
        return ResponseEntity.ok(rewardRuleService.calculateOrderRewardFee(rewardRuleRequestForm));
    }
}

