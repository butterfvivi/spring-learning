
package org.vivi.framework.report.simple.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.report.simple.service.DataSetTransformService;

/**
* @desc 数据集数据转换 controller
* @website https://gitee.com/anji-plus/gaea
**/
@RestController

@RequestMapping("/dataSetTransform")
public class DataSetTransformController {

    @Autowired
    private DataSetTransformService dataSetTransformService;


}
