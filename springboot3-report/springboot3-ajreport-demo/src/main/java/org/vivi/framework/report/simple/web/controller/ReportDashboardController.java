
package org.vivi.framework.report.simple.web.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.simple.common.response.ResponseBean;
import org.vivi.framework.report.simple.service.ReportDashboardService;
import org.vivi.framework.report.simple.service.ReportService;
import org.vivi.framework.report.simple.utils.RequestUtil;
import org.vivi.framework.report.simple.web.dto.ChartDto;
import org.vivi.framework.report.simple.web.dto.ReportDashboardObjectDto;

import java.util.concurrent.CompletableFuture;

/**
* @desc 大屏设计 controller
* @website https://gitee.com/anji-plus/gaea
**/
@RestController
@RequestMapping("/reportDashboard")
@Slf4j
public class ReportDashboardController {

    @Autowired
    private ReportDashboardService reportDashboardService;

    /**
     * 预览、查询大屏详情
     * @param reportCode
     * @return
     */
    @GetMapping({"/{reportCode}"})
    public ResponseBean detail(@PathVariable("reportCode") String reportCode) {
        return ResponseBean.builder().data(reportDashboardService.getDetail(reportCode)).build();
    }

    /**
     * 保存大屏设计
     * @param dto
     * @return
     */
    @PostMapping
    public ResponseBean insert(@RequestBody ReportDashboardObjectDto dto) {
        reportDashboardService.insertDashboard(dto);
        return ResponseBean.builder().build();
    }


    /**
     * 获取去单个图层数据
     * @param dto
     * @return
     */
    @PostMapping("/getData")
    public ResponseBean getData(@RequestBody ChartDto dto) {
        return ResponseBean.builder().data(reportDashboardService.getChartData(dto)).build();
    }

    @Autowired
    private ReportService reportService;

    /**
     * 导出大屏
     * @param reportCode
     * @return
     */
    @GetMapping("/export")
    public ResponseBean exportDashboard(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam("reportCode") String reportCode, @RequestParam(value = "showDataSet",required = false, defaultValue = "1") Integer showDataSet) {
//        //简单限制一下线上下载
//        String key = "gaea:report:export:limit:" + reportCode;
//        if (cacheHelper.exist(key)) {
//            throw BusinessExceptionBuilder.build("当前下载人数过多，请稍后重试...");
//        }
//        cacheHelper.stringSetExpire(key, "0", 300);
//        try {
//            return reportDashboardService.exportDashboard(request, response, reportCode, showDataSet);
//        } finally {
//            cacheHelper.delete(key);
//        }
        //异步统计下载次数
        CompletableFuture.runAsync(() -> {
            log.info("=======>ip:{} 下载模板：{}", RequestUtil.getIpAddr(request), reportCode);
            reportService.downloadStatistics(reportCode);
        });
        return ResponseBean.builder().build();
    }

    /**
     * 导入大屏
     * @param file  导入的zip文件
     * @param reportCode
     * @return
     */
    @PostMapping("/import/{reportCode}")
    public ResponseBean importDashboard(@RequestParam("file") MultipartFile file, @PathVariable("reportCode") String reportCode) {
        reportDashboardService.importDashboard(file, reportCode);
        return ResponseBean.builder().build();
    }

}
