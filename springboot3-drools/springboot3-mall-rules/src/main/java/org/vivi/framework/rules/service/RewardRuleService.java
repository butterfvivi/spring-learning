package org.vivi.framework.rules.service;


import org.vivi.framework.rules.web.request.RewardRuleRequestForm;
import org.vivi.framework.rules.web.vo.RewardRuleResponseVo;

public interface RewardRuleService {

    RewardRuleResponseVo calculateOrderRewardFee(RewardRuleRequestForm rewardRuleRequest);
}
