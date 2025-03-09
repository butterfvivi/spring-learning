package org.vivi.framework.report.service.test;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report/test")
public class TestController {

    @Autowired
    protected HttpServletRequest httpServletRequest;

    @Autowired
    protected HttpServletResponse httpServletResponse;

    @Autowired
    private TestReportService testReportService;

    /**
     * @Title: exportExcel
     * @Description: 导出excel数据
     */
    @RequestMapping(value = "/luckySheetExportExcel",method = RequestMethod.POST)
    public void luckySheetExportExcel() throws Exception {
        this.testReportService.handleExportExcel(httpServletResponse);
    }
}
