package org.vivi.framework.ireport.demo.service.report;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.vivi.framework.ireport.demo.model.report.Report;
import org.vivi.framework.ireport.demo.web.dto.GenerateReportDto;
import org.vivi.framework.ireport.demo.web.dto.ReportDto;

import java.util.List;

public interface ReportService extends IService<Report> {

    /**
     * 获取所有报表
     */
    List<Report> getAllReport(ReportDto reportDto);

    /**
     * 生成报告
     */
    void export(HttpServletResponse response, GenerateReportDto reportDto) throws Exception;

    /**
     * 预览报告
     */
    Object getAllPreviewData(GenerateReportDto reportDto);

}
