package org.vivi.framework.rules.service;


import org.vivi.framework.rules.web.request.ProfitsharingRuleRequestForm;
import org.vivi.framework.rules.web.vo.ProfitsharingRuleResponseVo;

public interface ProfitsharingRuleService {

    ProfitsharingRuleResponseVo calculateOrderProfitsharingFee(ProfitsharingRuleRequestForm profitsharingRuleRequest);
}
