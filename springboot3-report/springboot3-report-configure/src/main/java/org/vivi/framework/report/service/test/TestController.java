package org.vivi.framework.report.service.test;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestMapping(value = "/handleExportExcel",method = RequestMethod.POST)
    public void handleExportExcel() throws Exception {
        this.testReportService.handleExportExcel(httpServletResponse);
    }

    @RequestMapping(value = "/uploadExcel",method = RequestMethod.POST)
    public void uploadExcel(MultipartFile file) throws Exception {
        this.testReportService.uploadExcel(file);
    }
}
