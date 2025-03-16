
package org.vivi.framework.report.simple.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.report.simple.common.response.ResponseBean;
import org.vivi.framework.report.simple.entity.datasource.param.ConnectionParam;
import org.vivi.framework.report.simple.service.DataSourceService;

/**
* @desc 数据源 controller
* @website https://gitee.com/anji-plus/gaea
**/
@RestController
@RequestMapping("/dataSource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 获取所有数据源
     * @return
     */
    @GetMapping("/queryAllDataSource")
    public ResponseBean queryAllDataSource() {
        return ResponseBean.success(dataSourceService.queryAllDataSource());
    }

    /**
     * 测试 连接
     * @param connectionParam
     * @return
     */
    @PostMapping("/testConnection")
    public ResponseBean testConnection(@Validated @RequestBody ConnectionParam connectionParam) {
        return ResponseBean.success(dataSourceService.testConnection(connectionParam));
    }

}
