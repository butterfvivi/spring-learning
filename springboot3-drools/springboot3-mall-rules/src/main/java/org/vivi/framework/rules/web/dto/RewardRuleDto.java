package org.vivi.framework.rules.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RewardRuleDto {

    @Schema(description = "奖励金额")
    private BigDecimal rewardAmount;
}
