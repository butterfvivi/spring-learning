package org.vivi.framework.ireport.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.ireport.demo.common.response.R;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.modules.IReportDataStrategy;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSettingService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/dataSet")
public class DataSetController {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private IReportDataStrategy IReportDataStrategy;

    @Autowired
    private ReportSheetSettingService reportSheetSettingService;

    @PostMapping("/data")
    public R getDatas(@RequestBody GenerateReportDto reportDto){
        String querySql = "SELECT * FROM user \n" +
                "<where>\n" +
                "<if test=\"gender !=null and gender !=''\">\n" +
                "  gender = #{gender}\n" +
                "  </if>\n" +
                "</where>";
        Map<String, Object> params = new HashMap<>();
        params.put("gender","1");

        return R.success(dataSetService.getDatas(reportDto));
    }

    @PostMapping("/allData")
    public R getAllData(@RequestBody GenerateReportDto reportDto){
        return R.success(dataSetService.getAllData(reportDto));
    }

    @PostMapping("/column")
    public R getColumnInfos(@RequestBody GenerateReportDto reportDto){
        return R.success(dataSetService.getColumnInfos(reportDto));
    }

    @GetMapping("/headers")
    public R getHeaders(@RequestParam ("id") Integer id){
        return R.success(reportSheetSettingService.getHeaders(id));
    }
}
