package org.vivi.framework.rules.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.rules.service.FeeRuleService;
import org.vivi.framework.rules.web.request.FeeRuleRequestForm;
import org.vivi.framework.rules.web.vo.FeeRuleResponseVo;

@Slf4j
@RestController
@RequestMapping("/rules/fee")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FeeRuleController {

    @Autowired
    private FeeRuleService feeRuleService;

    @Operation(summary = "计算订单费用")
    @PostMapping("/calculateOrderFee")
    public ResponseEntity<FeeRuleResponseVo> calculateOrderFee(@RequestBody FeeRuleRequestForm feeRuleRequestForm) {
        FeeRuleResponseVo feeRuleResponseVo = feeRuleService.calculateOrderFee(feeRuleRequestForm);
        return ResponseEntity.ok(feeRuleResponseVo);
    }

}
