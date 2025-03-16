
package org.vivi.framework.report.simple.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.report.simple.common.response.ResponseBean;
import org.vivi.framework.report.simple.entity.datasetparam.dto.DataSetParamDto;
import org.vivi.framework.report.simple.service.DataSetParamService;
import org.vivi.framework.report.simple.web.param.DataSetParamValidationParam;

/**
* @desc 数据集动态参数 controller
**/
@RestController

@RequestMapping("/dataSetParam")
public class DataSetParamController  {

    @Autowired
    private DataSetParamService dataSetParamService;

    /**
     * 测试 查询参数是否正确
     * @param param
     * @return
     */
    @PostMapping("/verification")
    public ResponseBean verification(@Validated @RequestBody DataSetParamValidationParam param) {
        DataSetParamDto dto = new DataSetParamDto();
        dto.setSampleItem(param.getSampleItem());
        dto.setValidationRules(param.getValidationRules());
        return ResponseBean.success(dataSetParamService.verification(dto));
    }
}
