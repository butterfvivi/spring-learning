package org.vivi.framework.rules.service;

import org.vivi.framework.rules.web.request.FeeRuleRequestForm;
import org.vivi.framework.rules.web.vo.FeeRuleResponseVo;

public interface FeeRuleService {

    //计算订单费用
    FeeRuleResponseVo calculateOrderFee(FeeRuleRequestForm calculateOrderFee);
}
