package org.vivi.framework.ireport.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.ireport.demo.common.response.R;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSetService;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/dataSet")
public class DataSetController {

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    private ReportSheetSetService reportSheetSetService;

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
    public R getHeaders(@RequestParam ("id") Long id){
        return R.success(reportSheetSetService.getHeaders(id));
    }
}
